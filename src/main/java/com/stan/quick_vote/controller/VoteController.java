package com.stan.quick_vote.controller;

import com.stan.quick_vote.dto.VoteRequestDTO;
import com.stan.quick_vote.service.VoteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/voting-sessions/{votingSessionId}/votes")
@RequiredArgsConstructor
public class VoteController {
    private final VoteService voteService;

    @PostMapping
    public ResponseEntity<Void> registerVote(
            @PathVariable String votingSessionId,
            @RequestBody @Valid VoteRequestDTO voteRequestDTO) {
        
        voteService.registerVote(voteRequestDTO, votingSessionId);

        return ResponseEntity.ok().build();
    }
}
