package uz.pdp.citybookingservice.service.connection;


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
import uz.pdp.citybookingservice.domain.dto.JwtToken;
import uz.pdp.citybookingservice.domain.dto.UserDto;
import uz.pdp.citybookingservice.exception.DataNotFoundException;
import uz.pdp.citybookingservice.repository.JwtTokenRepository;

import java.security.Principal;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService implements UserDetailsService {
    private final RestTemplate restTemplate;
    private final JwtTokenRepository jwtTokenRepository;
    @Value("${services.user-service-url}")
    private String userUrl;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(userUrl + "/api/v1/get/user")
                .queryParam("username",username);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(httpHeaders);
        return restTemplate.exchange(builder.toUriString(), HttpMethod.GET, entity, UserDetails.class).getBody();
    }
    public UserDto getUserByUsername(String username, Principal principal) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(userUrl + "/api/v1/get/user")
                .queryParam("username",username);
        return getUserDto(principal, builder);
    }
    private UserDto getUserDto(Principal principal, UriComponentsBuilder builder) {
        JwtToken jwtTokenEntity = jwtTokenRepository.findById(principal.getName()).orElseThrow(() -> new DataNotFoundException("Jwt token not found or expired!"));
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("authorization","Bearer " + jwtTokenEntity.getToken());
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(httpHeaders);
        return restTemplate.exchange(builder.toUriString(), HttpMethod.GET, entity, UserDto.class).getBody();
    }
    public UserDto getById(UUID userId,Principal principal) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(userUrl + "/api/v1/get/id")
                .queryParam("id",userId);
        JwtToken jwtTokenEntity = jwtTokenRepository.findById(principal.getName()).orElseThrow(() -> new DataNotFoundException("Jwt token not found or expired!"));
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("authorization","Bearer " + jwtTokenEntity.getToken());
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<UUID> entity = new HttpEntity<>(httpHeaders);
        return restTemplate.exchange(builder.toUriString(), HttpMethod.GET, entity, UserDto.class).getBody();
    }

}
