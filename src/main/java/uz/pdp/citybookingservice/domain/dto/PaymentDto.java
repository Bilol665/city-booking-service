package uz.pdp.citybookingservice.domain.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class PaymentDto {
    private String sender;
    private String receiver;
    private Double cash;
}
