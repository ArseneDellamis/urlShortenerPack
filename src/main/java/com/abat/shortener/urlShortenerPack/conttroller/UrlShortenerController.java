package com.abat.shortener.urlShortenerPack.conttroller;

import com.abat.shortener.urlShortenerPack.entity.Url;
import com.abat.shortener.urlShortenerPack.entity.UrlDto;
import com.abat.shortener.urlShortenerPack.entity.UrlErrorResponseDto;
import com.abat.shortener.urlShortenerPack.entity.UrlResponseDto;
import com.abat.shortener.urlShortenerPack.service.UrlService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDateTime;

@Slf4j
@RestController
@RequiredArgsConstructor
public class UrlShortenerController {

    private  final UrlService service;

    @PostMapping("/generate")

    public ResponseEntity<?> generateShortLink(@RequestBody UrlDto urlDto) {
        log.info("Received request to generate short link for URL: {}", urlDto.getUrl());

        Url urlToRet =  service.generateShortLink(urlDto);

        if (urlToRet != null) {
            log.info("Short link generated successfully: {}", urlToRet.getShortLink());

            UrlResponseDto responseDto = new UrlResponseDto();
            responseDto.setOriginalUrl(urlToRet.getOriginalUrl());
            responseDto.setExpirationDate(urlToRet.getExpirationDate());
            responseDto.setShortLink(urlToRet.getShortLink());
            return new ResponseEntity<UrlResponseDto>(responseDto, HttpStatus.OK);
        }

        log.error("Error processing request for URL: {}", urlDto.getUrl());
        UrlErrorResponseDto errorResponseDto = new UrlErrorResponseDto("404", "There was an error processing your request. please try again");
        return new ResponseEntity<UrlErrorResponseDto>(errorResponseDto, HttpStatus.OK);
    }


    @GetMapping("arsene.com/{shortLink}")
    public ResponseEntity<?> redirectToOriginalUrl(@PathVariable String shortLink, HttpServletResponse response) throws IOException {

        log.info("Redirect request received for short link: {}", shortLink);
        if (StringUtils.isEmpty(shortLink)) {
            log.warn("Short link provided is empty");
            UrlErrorResponseDto urlErrorResponseDto = new UrlErrorResponseDto("Invalid url", "400");

            return new ResponseEntity<UrlErrorResponseDto>(urlErrorResponseDto, HttpStatus.OK);
        }

        Url urlToRet = service.getEncodedUrl(shortLink);


        if (urlToRet == null) {
            log.warn("Short URL not found or might have expired: {}", shortLink);
            UrlErrorResponseDto urlErrorResponseDto = new UrlErrorResponseDto("url does not exist or might have expired", "400");

            return ResponseEntity.status(HttpStatus.OK).body(urlErrorResponseDto);
        }


        if (urlToRet.getExpirationDate() != null && urlToRet.getExpirationDate().isBefore(LocalDateTime.now())) {
            log.info("Short URL expired: {}", shortLink);
            service.deleteShortLink(urlToRet);
            UrlErrorResponseDto urlErrorResponseDto = new UrlErrorResponseDto("URL expired. Please try generating a new one.", "200");
            return ResponseEntity.status(HttpStatus.OK).body(urlErrorResponseDto);
        }

        log.info("Redirecting to original URL: {}", urlToRet.getOriginalUrl());
        response.sendRedirect(urlToRet.getOriginalUrl());
        return null;
    }

    @DeleteMapping("/delete/{shortLink}")
    public ResponseEntity<?> deleteShortLink(@PathVariable String shortLink) {
        log.info("Delete request received for short link: {}", shortLink);
        try {
            Url urlToRet = service.getEncodedUrl(shortLink);
            service.deleteShortLink(urlToRet);
            log.info("Short link deleted successfully: {}", shortLink);
            return ResponseEntity.ok("Short link deleted successfully!");
        } catch (RuntimeException e) {
            log.error("Error deleting short link: {}", shortLink, e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Short link not found.");
        }

    }
}
