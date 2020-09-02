package ru.mustaev.restapp.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.mustaev.restapp.domain.Client;
import ru.mustaev.restapp.service.ClientService;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ClientController.class)
public class ClientControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ClientService clientService;
    @Autowired
    private ObjectMapper objectMapper;

    private List<Client> clients;

    @BeforeEach
    void setUp(){
        this.clients = new ArrayList<>();
        this.clients.add(new Client(1L, "Robin", new HashSet<>()));
        this.clients.add(new Client(2L, "Mike", new HashSet<>()));
        this.clients.add(new Client(3L, "Bob", new HashSet<>()));
    }

    @Test
    void shouldCreateClient() throws Exception {
        Client client = new Client(1L, "Tom", new HashSet<>());
        given(clientService.save(any(Client.class))).willAnswer((invocation) -> invocation.getArgument(0));

        this.mockMvc.perform(post("/client")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(client)))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.name", is(client.getName())))
                .andExpect(jsonPath("$.id", is(1)));
    }

    @Test
    void shouldReturnAllClientsNames() throws Exception{
        given(clientService.findAllByName()).willReturn(clients
                .stream()
                .map(Client::getName)
                .collect(Collectors.toList()));

        this.mockMvc.perform(get("/client"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(clients.size())))
                .andExpect(jsonPath("$[0]", is(clients.get(0).getName())));
    }

    @Test
    void shouldReturnAllClientsNamesWithFilterAsc() throws Exception{
        String filter = "ob";
        List<String> sortedClients = clients.stream().filter(e->e.getName().toLowerCase().contains(filter))
                .map(Client::getName)
                .sorted()
                .collect(Collectors.toList());

        given(clientService.findAllContainingNameAsc(filter)).willReturn(sortedClients);

        this.mockMvc.perform(get("/client").param("filter", filter))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(sortedClients.size())))
                .andExpect(jsonPath("$[0]", is(clients.get(2).getName())));
    }

    @Test
    void shouldReturnClientById() throws Exception {
        Long clientId = 2L;
        given(clientService.findById(clientId)).willReturn(clients.stream()
                .filter(e -> e.getId().equals(clientId))
                .findFirst());

        this.mockMvc.perform(get("/client/{id}", clientId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(clients.get(1).getName())))
                .andExpect(jsonPath("$.id", is(2)));
    }

    @Test
    void shouldReturn404WhenClientNotFound() throws Exception {
        Long clientId = 1L;
        given(clientService.findById(clientId)).willReturn(Optional.empty());

        this.mockMvc.perform(get("/client/{id}", clientId))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldDeleteClient() throws Exception{
        Long clientId = 1L;
        Client client = new Client(clientId, "Tom", new HashSet<>());
        given(clientService.findById(clientId)).willReturn(Optional.of(client));

        this.mockMvc.perform(delete("/client/{id}", client.getId()))
                .andExpect(status().isOk());
    }
}
