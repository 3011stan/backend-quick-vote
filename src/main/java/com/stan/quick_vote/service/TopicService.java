package com.stan.quick_vote.service;

import com.stan.quick_vote.dto.CooperativeResponseDTO;
import com.stan.quick_vote.dto.TopicRequestDTO;
import com.stan.quick_vote.dto.TopicResponseDTO;
import com.stan.quick_vote.exceptions.NotFoundException;
import com.stan.quick_vote.model.Topic;
import com.stan.quick_vote.repository.TopicRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class TopicService {
    private final TopicRepository topicRepository;
    private final CooperativeService cooperativeService;

    public TopicResponseDTO findById(String id) {
        Topic topic = topicRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Topic not found"));

        return mapToResponseDTO(topic);
    }

    public TopicResponseDTO createTopic(TopicRequestDTO topicRequestDTO) throws RuntimeException {
        CooperativeResponseDTO cooperative =
                cooperativeService.findById(topicRequestDTO.getCooperativeId());

        Topic topic = Topic.builder()
                .name(topicRequestDTO.getName())
                .description(topicRequestDTO.getDescription())
                .cooperativeId(cooperative.getId())
                .build();

        topic = topicRepository.save(topic);
        return mapToResponseDTO(topic);
    }

    private TopicResponseDTO mapToResponseDTO(Topic topic) {
        return TopicResponseDTO.builder()
                .id(topic.getId())
                .name(topic.getName())
                .description(topic.getDescription())
                .cooperativeId(topic.getCooperativeId())
                .createdAt(topic.getCreatedAt())
                .updatedAt(topic.getUpdatedAt())
                .build();
    }
}
