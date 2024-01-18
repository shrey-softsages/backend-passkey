package com.webauthn.app.web;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Controller
public class RestController {
    @GetMapping("/.well-known/assetlinks.json")
    public ResponseEntity<String> getAssetLinks() {
        try {
            // Load the assetlinks.json file from the classpath
            Resource resource = new ClassPathResource("assetlinks.json");
            Path assetLinksPath = resource.getFile().toPath();

            // Read the content of the file
            String assetLinksContent = new String(Files.readAllBytes(assetLinksPath));

            // Set the appropriate content type in the response
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(assetLinksContent);
        } catch (IOException e) {
            e.printStackTrace();
            // Handle the exception appropriately (e.g., return a 404 Not Found response)
            return ResponseEntity.notFound().build();
        }
    }

    @RequestMapping("/hello")
    public String sayHello()
    {
        return "login";
    }



}

