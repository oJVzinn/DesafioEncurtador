package com.github.ojvzinn.desafioencurtador.repository;

import com.github.ojvzinn.desafioencurtador.entity.ShortenerEntity;
import com.github.ojvzinn.sqlannotation.interfaces.Repository;

public interface ShortenerRepository extends Repository<ShortenerEntity> {

    ShortenerEntity findByShortLink(String shortLink);
    void deleteByID(Long id);

}
