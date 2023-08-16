package uz.pdp.citybookingservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.pdp.citybookingservice.domain.entity.BookingEntity;
import uz.pdp.citybookingservice.service.BookingService;


import java.security.Principal;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/booking")
public class BookingController {
    private final BookingService bookingService;

    @PutMapping("/removeOrder{id}")
    public ResponseEntity<HttpStatus> bookFlat(
            @PathVariable UUID id
    ) {
        bookingService.cancelBooking(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/book/flat/{flatId}")
    public ResponseEntity<BookingEntity> bookFlat(
            Principal principal,
            @RequestParam String cardNumber,
            @PathVariable UUID flatId) {

        return ResponseEntity.ok(bookingService.bookSingleFlat(flatId,cardNumber,principal));
    }

}
