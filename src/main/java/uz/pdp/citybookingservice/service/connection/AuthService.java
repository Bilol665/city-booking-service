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
import uz.pdp.citybookingservice.dto.UserDto;

@Service
@RequiredArgsConstructor
public class AuthService implements UserDetailsService {
    private final RestTemplate restTemplate;
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

}
