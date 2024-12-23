package com.stan.quick_vote.controller;

import com.stan.quick_vote.dto.AssociateRequestDTO;
import com.stan.quick_vote.dto.AssociateResponseDTO;
import com.stan.quick_vote.service.AssociateService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/associates")
public class AssociateController {
    private final AssociateService associateService;

    @PostMapping
    public AssociateResponseDTO createAssociate(@RequestBody @Valid AssociateRequestDTO associateRequestDTO) {
        return associateService.createAssociate(associateRequestDTO);
    }
}
