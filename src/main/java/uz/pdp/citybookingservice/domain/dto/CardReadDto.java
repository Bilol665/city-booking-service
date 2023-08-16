package uz.pdp.citybookingservice.domain.dto;

import lombok.*;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class CardReadDto {
    private UUID id;
    private String number;
    private Double balance;

}
