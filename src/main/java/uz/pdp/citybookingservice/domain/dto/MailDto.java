package uz.pdp.citybookingservice.domain.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class MailDto {
    private String message;
    private String  email;
}

