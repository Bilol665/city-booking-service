package uz.pdp.citybookingservice.service.booking;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import uz.pdp.citybookingservice.domain.dto.FlatDto;
import uz.pdp.citybookingservice.domain.dto.UserDto;
import uz.pdp.citybookingservice.domain.entity.BookingEntity;
import uz.pdp.citybookingservice.domain.entity.BookingStatus;
import uz.pdp.citybookingservice.domain.entity.BookingType;
import uz.pdp.citybookingservice.exception.DataNotFoundException;
import uz.pdp.citybookingservice.exception.NotAcceptable;
import uz.pdp.citybookingservice.repository.BookingRepository;
import uz.pdp.citybookingservice.service.connection.ApartmentService;
import uz.pdp.citybookingservice.service.connection.AuthService;
import uz.pdp.citybookingservice.service.connection.MailService;
import uz.pdp.citybookingservice.service.connection.PaymentService;

import java.security.Principal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class BookingService {
    private final BookingRepository bookingRepository;
    private final ApartmentService apartmentService;
    private final PaymentService paymentService;
    private final AuthService authService;
    private final MailService mailService;
    public void cancelBooking(UUID orderId){
        BookingEntity booking =
                bookingRepository.findById(orderId).orElseThrow(() -> new DataNotFoundException("There is no such orders"));
        booking.setStatus(BookingStatus.CLOSED);
    }
    @Scheduled(cron = "0 0 * * *")
    private void deleteClosedBookings() {
        List<BookingEntity> bookingEntities = bookingRepository.findAllByCreatedDateBefore(
                new Date(System.currentTimeMillis() - 86400000)
                        .toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
        bookingEntities.forEach((bookingEntity -> {if(Objects.equals(bookingEntity.getStatus(),BookingStatus.CLOSED)) bookingRepository.delete(bookingEntity);}));
    }

    public BookingEntity bookSingleFlat(UUID flatId, Principal principal) {
        UUID userId = authService.getUserByUsername(principal.getName(), principal).getId();
        UserDto user = authService.getById(userId, principal);
        FlatDto flat = apartmentService.getFlatByID(flatId, principal.getName());
        BookingEntity build = BookingEntity.builder()
                .fromWhomId(flat.getOwnerId())
                .bookingNumber((long) (getMax() + 1))
                .ownerId(userId)
                .type(BookingType.FLAT)
                .orderId(flatId)
                .endTime(LocalDateTime.now().plusMonths(1))
                .totalPrice(flat.getPricePerMonth())
                .status(BookingStatus.CREATED)
                .build();
        mailService.send1ApprovedMessage(user.getEmail(),flat.getNumber());
        return bookingRepository.save(build);
    }
    private int getMax() {
        try {
            return bookingRepository.getMax();
        }catch (Exception e) {
            return 0;
        }
    }

    public void confirm1(Principal principal, UUID bookingId) {
        UserDto user = authService.getUserByUsername(principal.getName(), principal);
        BookingEntity bookingEntity = bookingRepository.findById(bookingId).orElseThrow(() -> new DataNotFoundException("Booking not found!"));
        if (!Objects.equals(user.getId(), bookingEntity.getFromWhomId())) throw new NotAcceptable("It is not your booking!");
        bookingEntity.setStatus(BookingStatus.IN_PROGRESS);
        bookingRepository.save(bookingEntity);
        FlatDto flat = apartmentService.getFlatByID(bookingEntity.getOrderId(), principal.getName());
        mailService.send2ApprovedMessage(principal.getName(),flat.getNumber());
    }

    public void approve(Principal principal,String senderCardNumber, UUID bookingId) {
        UserDto customer = authService.getUserByUsername(principal.getName(), principal);
        BookingEntity bookingEntity = bookingRepository.findById(bookingId).orElseThrow(() -> new DataNotFoundException("Booking not found!"));
        if (!Objects.equals(customer.getId(), bookingEntity.getFromWhomId())) throw new NotAcceptable("It is not your booking!");
        bookingEntity.setStatus(BookingStatus.FULLY_APPROVED);
        bookingRepository.save(bookingEntity);
        FlatDto flat = apartmentService.getFlatByID(bookingEntity.getOrderId(), principal.getName());
        UserDto renter = authService.getById(bookingEntity.getFromWhomId(), principal);
        mailService.sendFullApproveMessageToCustomer(principal.getName(),flat);
        mailService.sendFullApprovalToRenter(renter.getEmail(),customer.getEmail(), flat.getNumber());
        paymentService.pay(senderCardNumber,flat.getId(), flat.getPricePerMonth(),principal);
        apartmentService.setOwner(flat.getId(),principal.getName());
    }
}


