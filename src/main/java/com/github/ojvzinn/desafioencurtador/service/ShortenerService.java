package com.github.ojvzinn.desafioencurtador.service;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.ojvzinn.desafioencurtador.dto.ShortenerDTO;
import com.github.ojvzinn.desafioencurtador.entity.ShortenerEntity;
import com.github.ojvzinn.desafioencurtador.repository.ShortenerRepository;
import com.github.ojvzinn.desafioencurtador.utils.RandomUtils;
import com.github.ojvzinn.sqlannotation.SQLAnnotation;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;
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
        shortenerEntity.setTotalClicks(0L);
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

    public void addClick(ShortenerEntity shortenerEntity) {
        shortenerEntity.setTotalClicks(shortenerEntity.getTotalClicks() + 1);
        if (cache.getIfPresent(shortenerEntity.getShortLink()) != null) {
            cache.put(shortenerEntity.getShortLink(), shortenerEntity);
            return;
        }

        repository.save(shortenerEntity);
    }

    public JSONArray listAll() {
        Set<ShortenerEntity> response = new HashSet<>(cache.asMap().values());
        repository.findAll().forEach(shortener -> {
            JSONObject info = (JSONObject) shortener;
            ShortenerEntity shortenerEntity = new ShortenerEntity();
            shortenerEntity.setId(info.getLong("id"));
            shortenerEntity.setShortLink(info.getString("shortLink"));
            shortenerEntity.setOriginalLink(info.getString("originalLink"));
            shortenerEntity.setTotalLife(info.getLong("totalLife"));
            shortenerEntity.setTotalClicks(info.getLong("totalClicks"));
            response.add(shortenerEntity);
        });

        return new JSONArray(response);
    }

    private String getRandomCode() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 10; i++) sb.append(RandomUtils.generateRandom());
        return sb.toString();
    }

}
