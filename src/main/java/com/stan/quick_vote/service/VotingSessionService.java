package com.stan.quick_vote.service;

import com.stan.quick_vote.dto.TopicResponseDTO;
import com.stan.quick_vote.dto.VotingSessionRequestDTO;
import com.stan.quick_vote.dto.VotingSessionResponseDTO;
import com.stan.quick_vote.model.VotingSession;
import com.stan.quick_vote.repository.VotingSessionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class VotingSessionService {
    private final VotingSessionRepository votingSessionRepository;
    private final TopicService topicService;

    public VotingSessionResponseDTO createVotingSession(VotingSessionRequestDTO dto) {
        TopicResponseDTO topicResponseDTO = topicService.findById(dto.getTopicId());

        LocalDateTime startAt = Optional.ofNullable(dto.getStartAt()).orElse(LocalDateTime.now());
        LocalDateTime endAt = Optional.ofNullable(dto.getEndAt()).orElse(startAt.plusMinutes(1));

        VotingSession session = VotingSession.builder()
                .topicId(dto.getTopicId())
                .name(dto.getName())
                .startAt(startAt)
                .endAt(endAt)
                .cooperativeId(topicResponseDTO.getCooperativeId())
                .build();

        session = votingSessionRepository.save(session);

        return toResponseDTO(session);
    }

    public VotingSessionResponseDTO toResponseDTO(VotingSession session) {
        return VotingSessionResponseDTO.builder()
                .id(session.getId())
                .topicId(session.getTopicId())
                .cooperativeId(session.getCooperativeId())
                .startAt(session.getStartAt())
                .name(session.getName())
                .endAt(session.getEndAt())
                .createdAt(session.getCreatedAt())
                .updatedAt(session.getUpdatedAt())
                .build();
    }

}
