package com.stan.quick_vote.controller;

import com.stan.quick_vote.dto.VotingSessionRequestDTO;
import com.stan.quick_vote.dto.VotingSessionResponseDTO;
import com.stan.quick_vote.service.VotingSessionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/voting-sessions")
public class VotingSessionController {
    private final VotingSessionService votingSessionService;

    @PostMapping
    public VotingSessionResponseDTO createVotingSession(@RequestBody @Valid VotingSessionRequestDTO dto) {
        return votingSessionService.createVotingSession(dto);
    }
}
