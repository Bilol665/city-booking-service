package uz.pdp.citybookingservice.service.connection;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import uz.pdp.citybookingservice.dto.CompanyDto;
import uz.pdp.citybookingservice.dto.FlatDto;
import uz.pdp.citybookingservice.dto.JwtToken;
import uz.pdp.citybookingservice.exception.DataNotFoundException;
import uz.pdp.citybookingservice.repository.JwtTokenRepository;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ApartmentService {
    private final RestTemplate restTemplate;
    private final JwtTokenRepository jwtTokenRepository;
    @Value("${services.apartment-url}")
    private String url;
    public FlatDto getFlatByID(UUID id, String username) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url + "api/v1/flat/get/" + id);
        HttpHeaders httpHeaders = new HttpHeaders();
        JwtToken jwtToken = jwtTokenRepository.findById(username).orElseThrow(() -> new DataNotFoundException("Jwt not found!"));
        httpHeaders.add("Authorization","Bearer " + jwtToken.getToken());
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<UUID> entity = new HttpEntity<>(httpHeaders);
        return restTemplate.exchange(builder.toUriString(), HttpMethod.GET, entity, FlatDto.class).getBody();
    }
    public FlatDto setOwner(UUID flatId,String username) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url + "api/v1/flat/update/setOwner")
                .queryParam("flatId",flatId);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        JwtToken jwtToken = jwtTokenRepository.findById(username).orElseThrow(() -> new DataNotFoundException("Jwt not found!"));
        httpHeaders.add("Authorization","Bearer " + jwtToken.getToken());
        HttpEntity<UUID> entity = new HttpEntity<>(httpHeaders);
        return restTemplate.exchange(builder.toUriString(),HttpMethod.PUT,entity,FlatDto.class).getBody();
    }
    public CompanyDto getCompany(UUID companyId, String username) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url + "api/v1/company/get/" + companyId);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        JwtToken jwtToken = jwtTokenRepository.findById(username).orElseThrow(() -> new DataNotFoundException("Jwt not found!"));
        httpHeaders.add("Authorization","Bearer " + jwtToken.getToken());
        HttpEntity<UUID> entity = new HttpEntity<>(httpHeaders);
        return restTemplate.exchange(builder.toUriString(),HttpMethod.GET,entity,CompanyDto.class).getBody();
    }
}
