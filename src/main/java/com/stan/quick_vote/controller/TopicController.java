package com.stan.quick_vote.controller;

import com.stan.quick_vote.dto.TopicRequestDTO;
import com.stan.quick_vote.dto.TopicResponseDTO;
import com.stan.quick_vote.service.TopicService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/topics")
public class TopicController {
    private final TopicService topicService;

    @PostMapping
    public TopicResponseDTO createTopic(@RequestBody @Valid TopicRequestDTO topicRequestDTO) {
        return topicService.createTopic(topicRequestDTO);
    }
}
