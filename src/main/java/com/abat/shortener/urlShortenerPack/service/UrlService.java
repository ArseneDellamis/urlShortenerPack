package com.abat.shortener.urlShortenerPack.service;

import com.abat.shortener.urlShortenerPack.entity.Url;
import com.abat.shortener.urlShortenerPack.entity.UrlDto;
import org.springframework.stereotype.Service;

@Service
public interface UrlService {

     Url generateShortLink(UrlDto urlDto);
     Url persistShorLink(Url url);
     Url getEncodedUrl(String url);
     void deleteShortLink(Url url);
     void deleteAllExpiredShortLinks();


}
