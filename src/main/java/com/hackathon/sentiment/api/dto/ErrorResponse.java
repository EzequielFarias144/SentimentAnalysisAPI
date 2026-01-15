package com.hackathon.sentiment.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record ErrorResponse(
        String comentario,
        Double probabilidade,
        @JsonProperty("palavras_chave")
        List<String> palavrasChave,
        String mensagem
) {}
