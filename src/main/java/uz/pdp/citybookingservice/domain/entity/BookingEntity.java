package uz.pdp.citybookingservice.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity(name = "bookings")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookingEntity extends BaseEntity {
    private UUID ownerId;
    private UUID orderId;
    @Enumerated(EnumType.STRING)
    private BookingType type;
    private Double totalPrice;
    private Long bookingNumber;
    private LocalDateTime endTime;
    @Enumerated(EnumType.STRING)
    private BookingStatus status;
}
