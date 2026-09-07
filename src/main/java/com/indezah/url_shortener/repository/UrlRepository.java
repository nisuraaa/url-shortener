package com.indezah.url_shortener.repository;

import com.indezah.url_shortener.entity.Url;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UrlRepository extends JpaRepository<Url, Long> {
    Optional<Url> findByShortCode(String shortCode);
    Optional<Url> findByOriginalUrl(String originalUrl);

    @Modifying
    @Query("UPDATE Url u SET u.clicks = u.clicks + 1 WHERE u.shortCode = :shortCode")
    void incrementClicks(String shortCode);
}
