package com.cashme.tech_project.api.simulation;

import com.cashme.tech_project.domain.PageFilter;
import com.cashme.tech_project.domain.simulation.SimulationEntity;
import com.cashme.tech_project.domain.simulation.SimulationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SimulationController.class)
public class SimulationControllerTest {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @MockBean
    private SimulationService simulationService;

    @MockBean
    private SimulationMapper simulationMapper;

    private UUID simulationId;
    private SimulationRequest simulationRequest;
    private SimulationEntity simulationEntity;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        SimulationController simulationController = new SimulationController(simulationMapper, simulationService);
        mockMvc = MockMvcBuilders.standaloneSetup(simulationController).build();

        simulationId = UUID.randomUUID();

        simulationRequest = SimulationRequest.builder()
                .simulationDate(LocalDateTime.now())
                .requestedValue(BigDecimal.valueOf(1000))
                .warrantyValue(BigDecimal.valueOf(500))
                .financingMonths(12)
                .monthlyInterestRate(BigDecimal.valueOf(0.01))
                .clientId(UUID.randomUUID())
                .build();

        simulationEntity = SimulationEntity.builder()
                .id(simulationId)
                .simulationDate(simulationRequest.getSimulationDate())
                .requestedValue(simulationRequest.getRequestedValue())
                .warrantyValue(simulationRequest.getWarrantyValue())
                .financingMonths(simulationRequest.getFinancingMonths())
                .monthlyInterestRate(simulationRequest.getMonthlyInterestRate())
                .build();

        final var simulationResponse = SimulationResponse.builder()
                .id(simulationId)
                .simulationDate(simulationRequest.getSimulationDate())
                .requestedValue(simulationRequest.getRequestedValue())
                .warrantyValue(simulationRequest.getWarrantyValue())
                .financingMonths(simulationRequest.getFinancingMonths())
                .monthlyInterestRate(simulationRequest.getMonthlyInterestRate())
                .build();

        when(simulationMapper.toEntity(any(SimulationRequest.class))).thenReturn(simulationEntity);
        when(simulationMapper.toUpdateEntity(any(UUID.class), any(SimulationRequest.class))).thenReturn(simulationEntity);
        when(simulationMapper.toResponse(any(SimulationEntity.class))).thenReturn(simulationResponse);
    }

    @Test
    void shouldCreateSimulationAndReturnsCreated() throws Exception {
        when(simulationService.createSimulation(any(SimulationEntity.class))).thenReturn(simulationEntity);

        mockMvc.perform(post("/v1/simulation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(simulationRequest)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(simulationId.toString()))
                .andExpect(jsonPath("$.requestedValue").value(simulationRequest.getRequestedValue().intValue()));
    }

    @Test
    void shouldUpdateSimulationAndReturnsOk() throws Exception {
        when(simulationService.updateSimulation(any(SimulationEntity.class))).thenReturn(simulationEntity);

        mockMvc.perform(put("/v1/simulation/{simulationId}", simulationId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(simulationRequest)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(simulationId.toString()))
                .andExpect(jsonPath("$.requestedValue").value(simulationRequest.getRequestedValue().intValue()));
    }

    @Test
    void shouldDeleteSimulationAndReturnsNoContent() throws Exception {
        doNothing().when(simulationService).deleteSimulation(simulationId);

        mockMvc.perform(delete("/v1/simulation/{simulationId}", simulationId))
                .andExpect(status().isNoContent());

        verify(simulationService, times(1)).deleteSimulation(simulationId);
    }

    @Test
    void shouldGetSimulationByIdAndReturnsOk() throws Exception {
        when(simulationService.findSimulationById(simulationId)).thenReturn(simulationEntity);

        mockMvc.perform(get("/v1/simulation/{simulationId}", simulationId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(simulationId.toString()))
                .andExpect(jsonPath("$.requestedValue").value(simulationRequest.getRequestedValue().intValue()));
    }

    @Test
    void shouldGetAllSimulationsAndReturnsSimulationPage() throws Exception {
        final var pageFilter = new PageFilter();
        pageFilter.setPage(0);
        pageFilter.setSize(10);

        final var pageable = pageFilter.toPageable();

        when(simulationService.findPagedSimulations(any(Pageable.class)))
                .thenReturn(new PageImpl<>(Collections.singletonList(simulationEntity), pageable, 1));

        mockMvc.perform(get("/v1/simulation")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content[0].id").value(simulationId.toString()))
                .andExpect(jsonPath("$.content[0].requestedValue").value(simulationRequest.getRequestedValue().intValue()));
    }
}
