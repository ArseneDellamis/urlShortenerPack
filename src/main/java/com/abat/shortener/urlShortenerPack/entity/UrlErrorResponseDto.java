package com.abat.shortener.urlShortenerPack.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UrlErrorResponseDto {

    private String status;
    private String error;
}
