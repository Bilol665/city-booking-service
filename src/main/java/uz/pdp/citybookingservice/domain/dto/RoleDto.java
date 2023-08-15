package uz.pdp.citybookingservice.domain.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class RoleDto {
    @NotBlank(message = "Role can n ot be omitted")
    private String role;
}
