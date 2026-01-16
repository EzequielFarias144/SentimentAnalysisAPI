package com.hackathon.sentiment.api.controller;

import com.hackathon.sentiment.api.domain.service.CommentService;
import com.hackathon.sentiment.api.dto.CommentHistoryResponse;

import com.hackathon.sentiment.api.dto.CommentItemResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/comments")
@CrossOrigin(origins = "*")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @GetMapping
    public ResponseEntity<CommentHistoryResponse> getHistory(
            @RequestParam(required = false) String language,
            @RequestParam(required = false) String sentiment,
            @RequestParam(required = false, defaultValue = "10") int limit
    ) {

        String langFilter = (language == null || "all".equalsIgnoreCase(language) || language.isBlank()) ? null : language;
        String sentimentFilter = (sentiment == null || "all".equalsIgnoreCase(sentiment) || sentiment.isBlank()) ? null : sentiment;

        var domainHistory = commentService.findCommentsFiltered(langFilter, sentimentFilter, limit);

        List<CommentItemResponse> responseList = domainHistory.stream()
                .map(item -> new CommentItemResponse(
                        item.getText(),
                        item.getSentiment(),
                        item.getScore(),
                        item.getLanguage()
                ))
                .collect(Collectors.toList());

        return ResponseEntity.ok(new CommentHistoryResponse(responseList));
    }
}
