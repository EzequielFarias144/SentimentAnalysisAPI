package com.hackathon.sentiment.api.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
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
        
        // Configurar RestTemplate com timeout de 90 segundos
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(90000); // 90 segundos
        factory.setReadTimeout(90000);
        RestTemplate restTemplate = new RestTemplate(factory);
        
        // Tenta acordar o Python ML com múltiplas tentativas
        boolean success = false;
        String lastError = "";
        
        for (int attempt = 1; attempt <= 3; attempt++) {
            try {
                Map<String, String> request = new HashMap<>();
                request.put("text", "warmup do sistema - tentativa " + attempt);
                request.put("language", "pt");
                
                Object result = restTemplate.postForObject(pythonServiceUrl, request, Object.class);
                
                success = true;
                response.put("status", "success");
                response.put("message", "Backend e ML aquecidos com sucesso (tentativa " + attempt + ")");
                response.put("attempts", String.valueOf(attempt));
                break;
                
            } catch (Exception e) {
                lastError = e.getMessage();
                // Se não é a última tentativa, aguarda 5 segundos
                if (attempt < 3) {
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                    }
                }
            }
        }
        
        if (!success) {
            response.put("status", "warming");
            response.put("message", "ML ainda está acordando. Última tentativa falhou: " + lastError);
            response.put("attempts", "3");
        }
        
        // Sempre retorna 200 para não bloquear o frontend
        return ResponseEntity.ok(response);
    }
}
