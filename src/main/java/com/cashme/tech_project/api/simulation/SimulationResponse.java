package com.cashme.tech_project.api.simulation;

import com.cashme.tech_project.api.client.ClientResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
public class SimulationResponse {

    @Schema(description = "Simulation ID")
    private UUID id;

    @Schema(description = "Simulation Date")
    private LocalDateTime simulationDate;

    @Schema(description = "Requested value")
    private BigDecimal requestedValue;

    @Schema(description = "Warranty value")
    private BigDecimal warrantyValue;

    @Schema(description = "Financing Months")
    private Integer financingMonths;

    @Schema(description = "Monthly Interest Rate")
    private BigDecimal monthlyInterestRate;

    @Schema(description = "Client of simulation")
    private ClientResponse client;
}
