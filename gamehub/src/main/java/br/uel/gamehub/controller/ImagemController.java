package br.uel.gamehub.controller;

import java.io.IOException;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/imagens")
public class ImagemController {

    @GetMapping("/{filename:.+}")
    public ResponseEntity<Resource> getImage(@PathVariable String filename) throws IOException {
        // Tenta carregar a imagem solicitada
        Resource resource = new ClassPathResource("static/imagens/" + filename);
        // Se não existir, usa a imagem default
        if (!resource.exists() || !resource.isReadable()) {
            resource = new ClassPathResource("static/imagens/default.jpg");
        }

        String contentType = "image/jpeg";
        if (filename.toLowerCase().endsWith(".jpg")) {
            contentType = "image/jpg";
        }
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(contentType));
        return new ResponseEntity<>(resource, headers, HttpStatus.OK);
    }
}
