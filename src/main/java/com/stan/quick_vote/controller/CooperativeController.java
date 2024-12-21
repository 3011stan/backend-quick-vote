package com.stan.quick_vote.controller;

import com.stan.quick_vote.dto.CooperativeRequestDTO;
import com.stan.quick_vote.dto.CooperativeResponseDTO;
import com.stan.quick_vote.service.CooperativeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/cooperatives")
public class CooperativeController {
    private final CooperativeService cooperativeService;

    @PostMapping
    public CooperativeResponseDTO createCooperative(@RequestBody CooperativeRequestDTO cooperativeRequestDTO) {
        return cooperativeService.createCooperative(cooperativeRequestDTO);
    }
}
