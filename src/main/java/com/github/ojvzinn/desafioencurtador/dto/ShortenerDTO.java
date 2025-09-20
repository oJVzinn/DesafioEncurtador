package com.github.ojvzinn.desafioencurtador.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ShortenerDTO {

    @NotBlank
    private String originalLink;

    @Min(1)
    private Long secondsLife;

}
