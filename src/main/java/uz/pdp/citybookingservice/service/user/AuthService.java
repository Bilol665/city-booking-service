package uz.pdp.citybookingservice.service.user;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import uz.pdp.citybookingservice.dto.FlatDto;
import uz.pdp.citybookingservice.dto.JwtToken;
import uz.pdp.citybookingservice.dto.UserDto;
import uz.pdp.citybookingservice.exception.DataNotFoundException;
import uz.pdp.citybookingservice.repository.JwtTokenRepository;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService implements UserDetailsService {
    private final RestTemplate restTemplate;
    private final JwtTokenRepository jwtTokenRepository;
    @Value("${services.apartment-url}")
    private String url;
    @Value("${services.user-service-url}")
    private String userUrl;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(userUrl + "/api/v1/auth/get/user")
                .queryParam("username",username);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(httpHeaders);
        return restTemplate.exchange(builder.toUriString(), HttpMethod.GET, entity, UserDetails.class).getBody();
    }
    public UserDto getUserByUsername(String username) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(userUrl + "/api/v1/auth/get/user")
                .queryParam("username",username);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(httpHeaders);
        return restTemplate.exchange(builder.toUriString(), HttpMethod.GET, entity, UserDto.class).getBody();
    }
    public FlatDto getFlatByID(UUID id,String username) {
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
}
