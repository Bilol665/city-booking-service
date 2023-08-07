package uz.pdp.citybookingservice.dto;

import lombok.*;

import java.util.List;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccommodationDto {
    private String name;
    private List<FlatDto> flats;

}
