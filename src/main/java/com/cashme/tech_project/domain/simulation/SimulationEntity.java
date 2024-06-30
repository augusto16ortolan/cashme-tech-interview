package com.cashme.tech_project.domain.simulation;

import com.cashme.tech_project.domain.client.ClientEntity;
import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "simulation")
public class SimulationEntity {

    @Id
    @With
    @GeneratedValue
    @Column(name = "simulation_id")
    private UUID id;

    @Column(name = "simulation_date")
    private LocalDateTime simulationDate;

    @Column(name = "requested_value")
    private BigDecimal requestedValue;

    @Column(name = "warranty_value")
    private BigDecimal warrantyValue;

    @Column(name = "financing_months")
    private Integer financingMonths;

    @Column(name = "monthly_interest_rate")
    private BigDecimal monthlyInterestRate;

    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    private ClientEntity client;

    public void updateSimulation(final SimulationEntity newSimulation) {
        this.simulationDate = newSimulation.getSimulationDate();
        this.requestedValue = newSimulation.getRequestedValue();
        this.warrantyValue = newSimulation.getWarrantyValue();
        this.financingMonths = newSimulation.getFinancingMonths();
        this.monthlyInterestRate = newSimulation.getMonthlyInterestRate();
        this.client = newSimulation.getClient();
    }
}
