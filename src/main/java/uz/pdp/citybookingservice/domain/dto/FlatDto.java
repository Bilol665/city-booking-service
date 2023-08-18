package uz.pdp.citybookingservice.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class FlatDto {
    private Integer number;
    private UUID id;
    private UUID ownerId;
    private Double pricePerMonth;
    private LocalDateTime createdTime;
    private UUID cardId;

}
