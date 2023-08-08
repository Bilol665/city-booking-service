package uz.pdp.citybookingservice.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.pdp.citybookingservice.dto.FlatDto;
import uz.pdp.citybookingservice.dto.UserDto;
import uz.pdp.citybookingservice.entity.BookingEntity;
import uz.pdp.citybookingservice.entity.BookingType;
import uz.pdp.citybookingservice.exception.DataNotFoundException;
import uz.pdp.citybookingservice.repository.BookingRepository;
import uz.pdp.citybookingservice.service.user.AuthService;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class BookingService {
    private final BookingRepository bookingRepository;
    private final AuthService authService;

//    public ApiResponse bookFlat(FlatDto flatDto, LocalDateTime startTime) {
//        FlatEntity flat = flatRepository.findById(flatDto.getFlatId()).orElseThrow(() -> new DataNotFoundException("Flat not found"));
//        if (flat.getFlatStatus() != FlatStatus.FREE) {
//           return new ApiResponse(HttpStatus.BAD_REQUEST, false, "Flat is not available for booking");
//        }
//         if (startTime == null) {
//            startTime = LocalDateTime.now();
//        }
//        LocalDateTime endTime = startTime.plusDays(flatDto.getDays());
//        if (endTime.isAfter(LocalDateTime.now().plusDays(flatDto.getDays()))) {
//            return new ApiResponse(HttpStatus.BAD_REQUEST, false, "Booking duration cannot exceed " + flatDto.getDays() + " days");
//        }
//        flat.setOwnerId(flatDto.getOwnerId());
//        flat.setOrderId(flatDto.getFlatId());
//        flat.setFlatStatus(FlatStatus.BOOKED);
//        Double price = flatDto.getPricePerNight() * flatDto.getDays();
//        flat.setPrice(price);
//        flat.setType(BookingType.FLAT);
//        flat.setWhenBooked(startTime);
//        flat.setWhenWillFinish(endTime);
//        flatRepository.save(flat);
//        bookingRepository.save(flat);
//
//        return new ApiResponse(HttpStatus.OK, true, "Success");
//    }

    public void cancelBooking(UUID orderId){
        BookingEntity thereIsNoSuchOrders =
                bookingRepository.findById(orderId).orElseThrow(() -> new DataNotFoundException("There is no such orders"));
        bookingRepository.delete(thereIsNoSuchOrders);
    }

    public BookingEntity bookSingleFlat(UUID flatId, Principal principal) {
        UserDto user = authService.getUserByUsername(principal.getName());
        FlatDto flatByID = authService.getFlatByID(flatId, principal.getName());
        BookingEntity build = BookingEntity.builder()
                .bookingNumber((long) (getMax() + 1))
                .ownerId(user.getId())
                .type(BookingType.FLAT)
                .orderId(flatId)
                .endTime(LocalDateTime.now().plusMonths(1))
                .totalPrice(flatByID.getPricePerMonth())
                .build();
        authService.setOwner(flatId,principal.getName());
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


