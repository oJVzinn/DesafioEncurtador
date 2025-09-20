package com.github.ojvzinn.desafioencurtador.service;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.ojvzinn.desafioencurtador.dto.ShortenerDTO;
import com.github.ojvzinn.desafioencurtador.entity.ShortenerEntity;
import com.github.ojvzinn.desafioencurtador.repository.ShortenerRepository;
import com.github.ojvzinn.desafioencurtador.utils.RandomUtils;
import com.github.ojvzinn.sqlannotation.SQLAnnotation;
import org.json.JSONArray;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class ShortenerService {

    private final ShortenerRepository repository = SQLAnnotation.loadRepository(ShortenerRepository.class);
    private final Cache<String, ShortenerEntity> cache = Caffeine.newBuilder().expireAfterWrite(1, TimeUnit.HOURS).build();

    public void createShortener(ShortenerDTO shortenerDTO) {
        ShortenerEntity shortenerEntity = new ShortenerEntity();
        shortenerEntity.setShortLink(getRandomCode());
        shortenerEntity.setOriginalLink(shortenerDTO.getOriginalLink());
        shortenerEntity.setTotalLife(System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(shortenerDTO.getSecondsLife()));
        repository.save(shortenerEntity);
    }

    public ShortenerEntity findByShortLink(String shortLink) {
        ShortenerEntity shortenerEntity = cache.getIfPresent(shortLink);
        if (shortenerEntity == null) {
            shortenerEntity = repository.findByShortLink(shortLink);
            if (shortenerEntity != null) {
                cache.put(shortLink, shortenerEntity);
            }
        }

        if (shortenerEntity != null && !shortenerEntity.isAlive()) {
            repository.deleteByID(shortenerEntity.getId());
            cache.invalidate(shortLink);
            return null;
        }


        return shortenerEntity;
    }

    public JSONArray listAll() {
        return repository.findAll();
    }

    private String getRandomCode() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            sb.append(RandomUtils.generateRandom());
        }

        return sb.toString();
    }

}
