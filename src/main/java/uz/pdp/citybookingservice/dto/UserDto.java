package uz.pdp.citybookingservice.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserDto {
    private UUID id;
    @NotBlank(message = "Enter email please")
    private String email;
    @NotBlank(message = "Enter password please")
    private String password;
    private List<RoleDto> roles;
}
