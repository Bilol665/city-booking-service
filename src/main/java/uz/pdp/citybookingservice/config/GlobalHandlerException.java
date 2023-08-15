package uz.pdp.citybookingservice.config;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import uz.pdp.citybookingservice.exception.DataNotFoundException;
import uz.pdp.citybookingservice.exception.NotAcceptable;

@ControllerAdvice
public class GlobalHandlerException {
    @ExceptionHandler(value = {DataNotFoundException.class})
    public ResponseEntity<String> dataNotFoundExp(DataNotFoundException e) {
        return ResponseEntity.status(404).body(e.getMessage());
    }
    @ExceptionHandler(value = {NotAcceptable.class})
    public ResponseEntity<String> notAcceptable(NotAcceptable e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_ACCEPTABLE);
    }
}
