package uz.pdp.citybookingservice.dto;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;


@Entity(name = "tokens")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class JwtToken {
    @Id
    private String username;
    private String token;
}
