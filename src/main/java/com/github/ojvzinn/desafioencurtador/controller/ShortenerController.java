package com.github.ojvzinn.desafioencurtador.controller;

import com.github.ojvzinn.desafioencurtador.dto.ShortenerDTO;
import com.github.ojvzinn.desafioencurtador.entity.ShortenerEntity;
import com.github.ojvzinn.desafioencurtador.service.ShortenerService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class ShortenerController {

    @Autowired
    private ShortenerService shortenerService;

    @GetMapping("/{shortenerLink}")
    public ResponseEntity<Void> getLinkByShortener(@PathVariable String shortenerLink) {
        ShortenerEntity shortener = shortenerService.findByShortLink(shortenerLink);
        if (shortener == null) {
            return ResponseEntity.notFound().build();
        }

        shortenerService.addClick(shortener);
        return ResponseEntity.status(HttpStatus.FOUND).header("Location", shortener.getOriginalLink()).build();
    }

    @GetMapping("/links")
    public ResponseEntity<String> getLinks() {
        JSONObject response = new JSONObject();
        response.put("shorteners", shortenerService.listAll());
        return ResponseEntity.status(HttpStatus.OK).body(response.toString());
    }

    @PostMapping("/create")
    public ResponseEntity<String> createShortener(@RequestBody @Valid ShortenerDTO shortenerDTO) {
        if (!shortenerDTO.getOriginalLink().startsWith("http://") && !shortenerDTO.getOriginalLink().startsWith("https://")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid link format");
        }

        shortenerService.createShortener(shortenerDTO);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

}
