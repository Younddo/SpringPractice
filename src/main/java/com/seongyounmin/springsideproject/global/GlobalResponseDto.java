package com.seongyounmin.springsideproject.global;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GlobalResponseDto {

    private String message;
    private int statusCode;


    public GlobalResponseDto(String message, int statusCode) {
        this.message = message;
        this.statusCode = statusCode;
    }


}

