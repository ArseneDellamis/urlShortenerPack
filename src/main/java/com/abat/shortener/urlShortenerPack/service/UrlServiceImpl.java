package com.abat.shortener.urlShortenerPack.service;

import com.abat.shortener.urlShortenerPack.entity.Url;
import com.abat.shortener.urlShortenerPack.entity.UrlDto;
import com.abat.shortener.urlShortenerPack.repository.UrlRepo;
import com.google.common.hash.Hashing;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class UrlServiceImpl implements UrlService {

    private final UrlRepo urlRepo;

    @Override
    public Url generateShortLink(UrlDto urlDto) {

        if (StringUtils.isNotEmpty(urlDto.getUrl())) {
            String encodeUrl = encodeUrl(urlDto.getUrl());
            Url urlPersist = new Url();
            urlPersist.setCreationDate(LocalDateTime.now());
            urlPersist.setOriginalUrl(urlDto.getUrl());
            urlPersist.setShortLink(encodeUrl);
            urlPersist.setExpirationDate(getExprirationDate(urlDto.getExpirationDate(), urlPersist.getCreationDate()));
            Url urlToRet = persistShorLink(urlPersist);

            if (urlToRet != null)
                return urlToRet;
        }
        return null;
    }

    private LocalDateTime getExprirationDate(String expirationDate, LocalDateTime creationDate) {
        if (StringUtils.isBlank(expirationDate)) {
            return creationDate.plusSeconds(60);
        }

        LocalDateTime expirationDateToRet = LocalDateTime.parse(expirationDate);
        return expirationDateToRet;
    }

    private String encodeUrl(String url) {
        String encodeUrl = "";
        LocalDateTime time = LocalDateTime.now();
        encodeUrl = Hashing.murmur3_32()
                .hashString(url.concat(time.toString()), StandardCharsets.UTF_8)
                .toString();
        return encodeUrl;
    }

    @Transactional
    @Override
    public Url persistShorLink(Url url) {

        Url urlToRet = urlRepo.save(url);
        return urlToRet;
    }

    @Override
    public Url getEncodedUrl(String url) {

        Url urlToRet = urlRepo.findByShortLink(url);
        return urlToRet;
    }

    @Override

    public void deleteShortLink(Url url) {

        urlRepo.delete(url);
    }

    @Override
    @Scheduled(cron = "0 50 16 * * ?")
    public void deleteAllExpiredShortLinks() {

        LocalDateTime currentTime = LocalDateTime.now();
        List<Url> getAllExpiredUrl = urlRepo.findByExpirationDateBefore(currentTime);
        if (getAllExpiredUrl !=null && !getAllExpiredUrl.isEmpty()) {
            for (Url url : getAllExpiredUrl) {
                log.info("Deleting expired URL: {}", url.getShortLink());
                deleteShortLink(url);
            }
        }else {
            log.info("No expired URLs found to delete.");
        }

    }
}


