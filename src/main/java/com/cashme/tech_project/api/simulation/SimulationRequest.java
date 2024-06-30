package com.cashme.tech_project.api.simulation;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SimulationRequest {

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @NotNull(message = "Simulation Date is required")
    @Schema(description = "Date of the simulation", example = "2024-07-01T10:15:30")
    private LocalDateTime simulationDate;

    @Builder.Default
    @Schema(description = "Requested value for the simulation", example = "1000.00")
    private BigDecimal requestedValue = BigDecimal.ZERO;

    @Builder.Default
    @Schema(description = "Value of the warranty for the simulation", example = "500.00")
    private BigDecimal warrantyValue = BigDecimal.ZERO;

    @Builder.Default
    @Schema(description = "Number of financing months", example = "12")
    private Integer financingMonths = 1;

    @Builder.Default
    @Schema(description = "Monthly interest rate for financing", example = "0.01")
    private BigDecimal monthlyInterestRate = BigDecimal.ZERO;

    @NotNull(message = "Client id is required")
    @Schema(description = "Client's UUID for whom the simulation is being done", example = "123e4567-e89b-12d3-a456-556642440000")
    private UUID clientId;
}
