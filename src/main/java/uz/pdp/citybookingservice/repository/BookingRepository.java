package uz.pdp.citybookingservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import uz.pdp.citybookingservice.domain.entity.BookingEntity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
@Repository
public interface BookingRepository extends JpaRepository<BookingEntity, UUID> {
    @Query("select max(r.bookingNumber) from bookings r")
    int getMax();
    List<BookingEntity> findAllByCreatedDateBefore(LocalDateTime createdDate);
}
