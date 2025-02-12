package com.abat.shortener.urlShortenerPack.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UrlDto {

    private  String url;
    private String expirationDate;
}
