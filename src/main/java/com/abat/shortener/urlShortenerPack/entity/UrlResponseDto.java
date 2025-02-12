package com.abat.shortener.urlShortenerPack.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UrlResponseDto {

    private String originalUrl;
    private String shortLink;
    private LocalDateTime expirationDate;
}
