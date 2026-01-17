package com.hackathon.sentiment.api.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/health")
@CrossOrigin(origins = "*")
public class HealthController {

    @Value("${python.service.url}")
    private String pythonServiceUrl;

    @GetMapping("/warmup")
    public ResponseEntity<Map<String, String>> warmup() {
        Map<String, String> response = new HashMap<>();
        
        try {
            // Chama o Python ML para acordá-lo
            RestTemplate restTemplate = new RestTemplate();
            Map<String, String> request = new HashMap<>();
            request.put("text", "teste de conexao para warmup");
            request.put("language", "pt");
            
            // Timeout de 60 segundos para dar tempo do serviço acordar
            restTemplate.postForObject(pythonServiceUrl, request, Object.class);
            
            response.put("status", "success");
            response.put("message", "Backend e ML aquecidos com sucesso");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("status", "partial");
            response.put("message", "Backend OK, ML pode estar acordando: " + e.getMessage());
            // Retorna 200 mesmo com erro para não bloquear o frontend
            return ResponseEntity.ok(response);
        }
    }
}
