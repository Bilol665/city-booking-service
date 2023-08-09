package uz.pdp.citybookingservice.service.connection;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import uz.pdp.citybookingservice.exception.DataNotFoundException;
import uz.pdp.citybookingservice.repository.JwtTokenRepository;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final RestTemplate restTemplate;
    private final JwtTokenRepository jwtTokenRepository;
    @Value("${services.payment-url}")
    private String paymentUrl;

    public void pay(String sender,UUID receiverId,Double amount) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(paymentUrl + "/api/v1/card/transact")
                .queryParam("receiver",receiverId)
                .queryParam("balance",amount);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        String token = jwtTokenRepository.findById(sender).orElseThrow(() -> new DataNotFoundException("Jwt not found!")).getToken();
        httpHeaders.add("authorization","Bearer " + token);
        HttpEntity<String> entity = new HttpEntity<>(httpHeaders);
        restTemplate.exchange(builder.toUriString(), HttpMethod.PUT,entity, JsonNode.class);

    }
}
