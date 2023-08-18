package uz.pdp.citybookingservice.service.connection;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import uz.pdp.citybookingservice.domain.dto.CardReadDto;
import uz.pdp.citybookingservice.domain.dto.PaymentDto;

import java.security.Principal;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final RestTemplate restTemplate;
    private final BaseService baseService;
    private final ApartmentService apartmentService;
    @Value("${services.payment-url}")
    private String paymentUrl;

    public void pay(String senderCardNumber,UUID receiverFlatId,Double amount,Principal principal) {
        UUID receiverCardId = apartmentService.getCardIdByFlatId(receiverFlatId, principal.getName());
        String receiverCard = getByCardId(receiverCardId,principal);
        PaymentDto paymentDto = PaymentDto.builder()
                .sender(senderCardNumber)
                .receiver(receiverCard)
                .cash(amount)
                .build();

        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(paymentUrl + "/api/v1/p2p");
        HttpHeaders httpHeaders = baseService.configureHttpHeaders(principal.getName());
        HttpEntity<PaymentDto> entity = new HttpEntity<>(paymentDto,httpHeaders);
        restTemplate.exchange(builder.toUriString(), HttpMethod.POST,entity, CardReadDto.class);
    }
    public String getByCardId(UUID cardId,Principal principal) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(paymentUrl + "/api/v1/card/get/" + cardId);
        HttpHeaders httpHeaders = baseService.configureHttpHeaders(principal.getName());
        HttpEntity<UUID> entity = new HttpEntity<>(httpHeaders);
        return restTemplate.exchange(builder.toUriString(), HttpMethod.GET,entity,String.class).getBody();
    }

}
