package uz.pdp.citybookingservice.service.connection;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import uz.pdp.citybookingservice.domain.dto.FlatDto;
import uz.pdp.citybookingservice.domain.dto.MailDto;

import java.net.URI;

@Service
@RequiredArgsConstructor
public class MailService {
    private final RestTemplate restTemplate;
    @Value("${services.notification-url}")
    private String notificationUrl;

    public void send1ApprovedMessage(String email,Integer flatNumber) {
        String message = "Hey there is a book request to your flat № " + flatNumber+ '\n'
                + "You can approve it visiting our site!";
        sendMessage(email, message);
    }
    public void send2ApprovedMessage(String email,Integer flatNumber) {
        String message = "Hey your book request to flat № " + flatNumber+ " was approved by its owner!\n"
                + "You can check it out and confirm your booking!";
        sendMessage(email, message);
    }
    public void sendFullApproveMessageToCustomer(String email, FlatDto flat) {
        String message = "Hey your booking is almost done!\nYou just booked a flat№ " + flat.getNumber() +
                " to " + flat.getPricePerMonth() + " a month! One you should do is to move there!";
        sendMessage(email,message);
    }
    public void sendFullApprovalToRenter(String email,String customerEmail,Integer flatNumber) {
        String message = "Hey, user: " + customerEmail + " just finished booking your flat№" +
                flatNumber + " so credits are being sent to your card and the customer is in the way to the flat!!\n" +
                "One more thing, please do not forget to cancel your post!";
        sendMessage(email,message);
    }
    private void sendMessage(String email, String message) {
        MailDto mailDto = new MailDto(message,email);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<MailDto> entity = new HttpEntity<>(mailDto,httpHeaders);
        restTemplate.exchange(
                URI.create(notificationUrl + "/send-single"),
                HttpMethod.POST,
                entity,
                String.class
        );
    }
}
