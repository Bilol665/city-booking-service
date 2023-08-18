package uz.pdp.citybookingservice.service.connection;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import uz.pdp.citybookingservice.domain.dto.FlatDto;

import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ApartmentService {
    private final RestTemplate restTemplate;
    private final BaseService baseService;
    @Value("${services.apartment-url}")
    private String url;
    public FlatDto getFlatByID(UUID id, String username) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url + "api/v1/flat/get/" + id);
        HttpHeaders httpHeaders = baseService.configureHttpHeaders(username);
        HttpEntity<UUID> entity = new HttpEntity<>(httpHeaders);
        return restTemplate.exchange(builder.toUriString(), HttpMethod.GET, entity, FlatDto.class).getBody();
    }
    public FlatDto setOwner(UUID flatId,String username) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url + "/api/v1/flat/update/setOwner")
                .queryParam("flatId",flatId);
        HttpHeaders httpHeaders = baseService.configureHttpHeaders(username);
        HttpEntity<UUID> entity = new HttpEntity<>(httpHeaders);
        return restTemplate.exchange(builder.toUriString(),HttpMethod.PUT,entity,FlatDto.class).getBody();
    }
    public UUID getCardIdByFlatId(UUID id, String username) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url + "/api/v1/flat/get/" + id);
        HttpHeaders headers = baseService.configureHttpHeaders(username);
        HttpEntity<UUID> entity = new HttpEntity<>(headers);
        return Objects.requireNonNull(restTemplate.exchange(builder.toUriString(), HttpMethod.GET, entity, FlatDto.class).getBody()).getCardId();
    }

}
