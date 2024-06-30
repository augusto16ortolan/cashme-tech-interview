package com.cashme.tech_project.api.client;

import com.cashme.tech_project.api.client.address.AddressRequest;
import com.cashme.tech_project.api.client.address.AddressResponse;
import com.cashme.tech_project.domain.PageFilter;
import com.cashme.tech_project.domain.address.AddressEntity;
import com.cashme.tech_project.domain.address.AddressType;
import com.cashme.tech_project.domain.client.ClientEntity;
import com.cashme.tech_project.domain.client.ClientService;
import com.cashme.tech_project.domain.client.NationalIdentityType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ClientController.class)
public class ClientControllerTest {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @MockBean
    private ClientService clientService;

    @MockBean
    private ClientMapper clientMapper;

    private UUID clientId;
    private ClientRequest clientRequest;
    private ClientEntity clientEntity;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        ClientController clientController = new ClientController(clientMapper, clientService);
        mockMvc = MockMvcBuilders.standaloneSetup(clientController).build();

        clientId = UUID.randomUUID();

        clientRequest = ClientRequest.builder()
                .name("John Doe")
                .nationalIdentity("12345678901")
                .nationalIdentityType(NationalIdentityType.CPF)
                .address(AddressRequest.builder()
                        .addressType(AddressType.STREET)
                        .address("Test")
                        .number("123")
                        .uf("RS")
                        .city("Test")
                        .neighborhood("Test")
                        .zipCode("123456788")
                        .build())
                .build();

        clientEntity = ClientEntity.builder()
                .id(clientId)
                .name(clientRequest.getName())
                .nationalIdentity(clientRequest.getNationalIdentity())
                .nationalIdentityType(clientRequest.getNationalIdentityType())
                .address(AddressEntity.builder()
                        .address("Test")
                        .addressType(AddressType.STREET)
                        .number("123")
                        .uf("RS")
                        .city("Test")
                        .zipCode("123456788")
                        .neighborhood("Test")
                        .build())
                .build();

        final var clientResponse = ClientResponse.builder()
                .id(clientId)
                .name(clientRequest.getName())
                .nationalIdentity(clientRequest.getNationalIdentity())
                .nationalIdentityType(clientRequest.getNationalIdentityType())
                .address(AddressResponse.builder()
                        .address("Test")
                        .number("123")
                        .addressType(AddressType.STREET)
                        .uf("RS")
                        .city("Test")
                        .zipCode("123456788")
                        .neighborhood("Test")
                        .build())
                .build();

        when(clientMapper.toEntity(any(ClientRequest.class))).thenReturn(clientEntity);
        when(clientMapper.toUpdateEntity(any(UUID.class), any(ClientRequest.class))).thenReturn(clientEntity);
        when(clientMapper.toResponse(any(ClientEntity.class))).thenReturn(clientResponse);
    }

    @Test
    void shouldCreateClientAndReturnsCreated() throws Exception {
        when(clientService.createClient(any(ClientEntity.class))).thenReturn(clientEntity);

        mockMvc.perform(post("/v1/client")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(clientRequest)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(clientId.toString()))
                .andExpect(jsonPath("$.name").value(clientRequest.getName()))
                .andExpect(jsonPath("$.nationalIdentity").value(clientRequest.getNationalIdentity()));
    }

    @Test
    void shouldUpdateClientAndReturnsOk() throws Exception {
        when(clientService.updateClient(any(ClientEntity.class))).thenReturn(clientEntity);

        mockMvc.perform(put("/v1/client/{clientId}", clientId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(clientRequest)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(clientId.toString()))
                .andExpect(jsonPath("$.name").value(clientRequest.getName()))
                .andExpect(jsonPath("$.nationalIdentity").value(clientRequest.getNationalIdentity()));
    }

    @Test
    void shouldDeleteClientAndReturnsNoContent() throws Exception {
        doNothing().when(clientService).deleteClient(clientId);

        mockMvc.perform(delete("/v1/client/{clientId}", clientId))
                .andExpect(status().isNoContent());

        verify(clientService, times(1)).deleteClient(clientId);
    }

    @Test
    void shouldGetClientByIdAndReturnsOk() throws Exception {
        when(clientService.findClientById(clientId)).thenReturn(clientEntity);

        mockMvc.perform(get("/v1/client/{clientId}", clientId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(clientId.toString()))
                .andExpect(jsonPath("$.name").value(clientEntity.getName()));
    }

    @Test
    void shouldGetAllClientsAndReturnsClientPage() throws Exception {
        final var pageFilter = new PageFilter();
        pageFilter.setPage(0);
        pageFilter.setSize(10);
        pageFilter.setSortBy("name desc");

        final var pageable = pageFilter.toPageable();

        when(clientService.findPagedClients(any(Pageable.class)))
                .thenReturn(new PageImpl<>(Collections.singletonList(clientEntity), pageable, 1));

        mockMvc.perform(get("/v1/client")
                        .param("page", "0")
                        .param("size", "10")
                        .param("sortBy", "name desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content[0].id").value(clientId.toString()))
                .andExpect(jsonPath("$.content[0].name").value(clientEntity.getName()));
    }
}
