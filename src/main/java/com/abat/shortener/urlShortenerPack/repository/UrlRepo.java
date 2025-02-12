package com.abat.shortener.urlShortenerPack.repository;

import com.abat.shortener.urlShortenerPack.entity.Url;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UrlRepo extends JpaRepository<Url, Long> {

    Url findByShortLink(String shortLink);
}
