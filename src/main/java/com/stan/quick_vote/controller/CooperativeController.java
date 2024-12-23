package com.stan.quick_vote.controller;

import com.stan.quick_vote.dto.CooperativeRequestDTO;
import com.stan.quick_vote.dto.CooperativeResponseDTO;
import com.stan.quick_vote.service.CooperativeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/cooperatives")
@Tag(name = "Cooperatives", description = "Operations related to cooperatives")
public class CooperativeController {
    private final CooperativeService cooperativeService;

    @PostMapping
    @Operation(summary = "Create a Cooperative", description = "Creates a new cooperative based on the provided details.")
    public CooperativeResponseDTO createCooperative(@RequestBody CooperativeRequestDTO cooperativeRequestDTO) {
        return cooperativeService.createCooperative(cooperativeRequestDTO);
    }
}
