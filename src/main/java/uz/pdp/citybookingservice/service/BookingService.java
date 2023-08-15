package uz.pdp.citybookingservice.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.pdp.citybookingservice.domain.dto.FlatDto;
import uz.pdp.citybookingservice.domain.entity.BookingEntity;
import uz.pdp.citybookingservice.domain.entity.BookingType;
import uz.pdp.citybookingservice.exception.DataNotFoundException;
import uz.pdp.citybookingservice.repository.BookingRepository;
import uz.pdp.citybookingservice.service.connection.ApartmentService;
import uz.pdp.citybookingservice.service.connection.AuthService;
import uz.pdp.citybookingservice.service.connection.PaymentService;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class BookingService {
    private final BookingRepository bookingRepository;
    private final ApartmentService apartmentService;
    private final PaymentService paymentService;
    public void cancelBooking(UUID orderId){
        BookingEntity thereIsNoSuchOrders =
                bookingRepository.findById(orderId).orElseThrow(() -> new DataNotFoundException("There is no such orders"));
        bookingRepository.delete(thereIsNoSuchOrders);
    }

    public BookingEntity bookSingleFlat(UUID flatId, String senderCardNumber, Principal principal) {
        UUID userId = paymentService.getByCard(senderCardNumber, principal);
        FlatDto flat = apartmentService.getFlatByID(flatId, principal.getName());
        BookingEntity build = BookingEntity.builder()
                .bookingNumber((long) (getMax() + 1))
                .ownerId(userId)
                .type(BookingType.FLAT)
                .orderId(flatId)
                .endTime(LocalDateTime.now().plusMonths(1))
                .totalPrice(flat.getPricePerMonth())
                .build();
        paymentService.pay(senderCardNumber,flat.getId(), flat.getPricePerMonth(),principal);
        apartmentService.setOwner(flatId,principal.getName());
        return bookingRepository.save(build);
    }
    private int getMax() {
        try {
            return bookingRepository.getMax();
        }catch (Exception e) {
            return 0;
        }
    }
}


