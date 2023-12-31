package kr.com.devlog.common;

import kr.com.devlog.response.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class ExceptionController {
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)

    public ErrorResponse invalidRequestHandler(MethodArgumentNotValidException e) {
        //Me
        ErrorResponse errorResponse = ErrorResponse.builder().code("400").message("잘못된 요청입니다.").build();
        for (FieldError fieldError : e.getFieldErrors()) {
            errorResponse.addValidation(fieldError.getField(),fieldError.getDefaultMessage());
        }
        return errorResponse;


    }

}
