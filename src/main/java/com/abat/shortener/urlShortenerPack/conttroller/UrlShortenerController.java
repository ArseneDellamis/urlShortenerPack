package com.abat.shortener.urlShortenerPack.conttroller;

import com.abat.shortener.urlShortenerPack.entity.Url;
import com.abat.shortener.urlShortenerPack.entity.UrlDto;
import com.abat.shortener.urlShortenerPack.entity.UrlErrorResponseDto;
import com.abat.shortener.urlShortenerPack.entity.UrlResponseDto;
import com.abat.shortener.urlShortenerPack.service.UrlService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
public class UrlShortenerController {

    private  final UrlService service;

    @PostMapping("/generate")

    public ResponseEntity<?> generateShortLink(@RequestBody UrlDto urlDto) {
        Url urlToRet =  service.generateShortLink(urlDto);

        if (urlToRet != null) {
            UrlResponseDto responseDto = new UrlResponseDto();
            responseDto.setOriginalUrl(urlToRet.getOriginalUrl());
            responseDto.setExpirationDate(urlToRet.getExpirationDate());
            responseDto.setShortLink(urlToRet.getShortLink());
            return new ResponseEntity<UrlResponseDto>(responseDto, HttpStatus.OK);
        }

        UrlErrorResponseDto errorResponseDto = new UrlErrorResponseDto("404", "There was an error processing your request. please try again");
        return new ResponseEntity<UrlErrorResponseDto>(errorResponseDto, HttpStatus.OK);
    }


    @GetMapping("arsene.com/{shortLink}")
    public ResponseEntity<?> redirectToOriginalUrl(@PathVariable String shortLink, HttpServletResponse response) throws IOException {
        if (StringUtils.isEmpty(shortLink)) {
            UrlErrorResponseDto urlErrorResponseDto = new UrlErrorResponseDto("Invalid url", "400");

            return new ResponseEntity<UrlErrorResponseDto>(urlErrorResponseDto, HttpStatus.OK);
        }

        Url urlToRet = service.getEncodedUrl(shortLink);


        if (urlToRet == null) {
            UrlErrorResponseDto urlErrorResponseDto = new UrlErrorResponseDto("url does not exist or might have expired", "400");

            return ResponseEntity.status(HttpStatus.OK).body(urlErrorResponseDto);
        }


        if (urlToRet.getExpirationDate().isBefore(LocalDateTime.now())) {

            service.deleteShortLink(urlToRet);
            UrlErrorResponseDto urlErrorResponseDto = new UrlErrorResponseDto("url expired. please try generate a new one ", "200");

            return ResponseEntity.status(HttpStatus.OK).body(urlErrorResponseDto);
        }

        response.sendRedirect(urlToRet.getOriginalUrl());
        return null;
    }

    @DeleteMapping("/delete/{shortLink}")
    public ResponseEntity<?> deleteShortLink(@PathVariable String shortLink) {
        try {
            Url urlToRet = service.getEncodedUrl(shortLink);
            service.deleteShortLink(urlToRet);
            return ResponseEntity.ok("Short link deleted successfully!");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Short link not found.");
        }
    }


}
