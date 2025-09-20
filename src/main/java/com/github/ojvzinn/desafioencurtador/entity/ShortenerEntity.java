package com.github.ojvzinn.desafioencurtador.entity;

import com.github.ojvzinn.sqlannotation.annotations.Column;
import com.github.ojvzinn.sqlannotation.annotations.Entity;
import com.github.ojvzinn.sqlannotation.annotations.PrimaryKey;
import lombok.Getter;
import lombok.Setter;

@Entity(name = "Shorteners")
@Getter
@Setter
public class ShortenerEntity {

    @PrimaryKey(autoIncrement = true)
    @Column
    private Long id;

    @Column
    private String originalLink;

    @Column
    private String shortLink;

    @Column
    private Long totalLife;

    public boolean isAlive() {
        return totalLife - System.currentTimeMillis() > 0;
    }
}
