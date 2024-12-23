package com.stan.quick_vote.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CooperativeRequestDTO {
    private String name;
    private String registrationNumber;
    private String address;
    private String contactEmail;
    private String contactPhone;
}
