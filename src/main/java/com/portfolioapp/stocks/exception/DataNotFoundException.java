package com.portfolioapp.stocks.exception;


import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@NoArgsConstructor(access = AccessLevel.PUBLIC)
@ResponseStatus(value = HttpStatus.NOT_FOUND,reason = "Data Not Found")
public class DataNotFoundException extends Exception{

    @Builder
    public DataNotFoundException(String message){
        super(message);
    }

}
