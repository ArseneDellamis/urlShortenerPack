package com.abat.shortener.urlShortenerPack.service;

import com.abat.shortener.urlShortenerPack.entity.Url;
import com.abat.shortener.urlShortenerPack.entity.UrlDto;
import org.springframework.stereotype.Service;

@Service
public interface UrlService {

    public Url generateShortLink(UrlDto urlDto);
    public Url persistShorLink(Url url);
    public Url getEncodedUrl(String url);
    public void deleteShortLink(Url url);

}
