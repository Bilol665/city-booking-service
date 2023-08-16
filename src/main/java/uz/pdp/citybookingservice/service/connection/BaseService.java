package uz.pdp.citybookingservice.service.connection;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import uz.pdp.citybookingservice.domain.dto.JwtToken;
import uz.pdp.citybookingservice.exception.DataNotFoundException;
import uz.pdp.citybookingservice.repository.JwtTokenRepository;

@Service
@RequiredArgsConstructor
public class BaseService {
    private final JwtTokenRepository jwtTokenRepository;

    public HttpHeaders configureHttpHeaders(String username) {
        HttpHeaders headers = new HttpHeaders();
        JwtToken jwtToken = jwtTokenRepository.findById(username).orElseThrow(() -> new DataNotFoundException("Jwt not found!"));
        headers.add("Authorization","Bearer " + jwtToken.getToken());
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }
}
