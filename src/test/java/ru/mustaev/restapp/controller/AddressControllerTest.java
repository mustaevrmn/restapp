package ru.mustaev.restapp.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.mustaev.restapp.domain.Address;
import ru.mustaev.restapp.domain.Client;
import ru.mustaev.restapp.service.AddressService;
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

@WebMvcTest(controllers = AddressController.class)
public class AddressControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;
    @MockBean
    private AddressService addressService;
    @MockBean
    private ClientService clientService;

    private List<Address> addresses;
    private List<Client> clients;

    @BeforeEach
    void setUp(){
        this.clients = new ArrayList<>();
        this.clients.add(new Client(1L, "Robin", new HashSet<>()));
        this.clients.add(new Client(2L, "Mike", new HashSet<>()));
        this.clients.add(new Client(3L, "Bob", new HashSet<>()));

        addresses = new ArrayList<>();
        addresses.add(new Address(1L, "London", clients.get(0)));
        addresses.add(new Address(2L, "Tokyo", clients.get(0)));
        addresses.add(new Address(3L, "Moscow", clients.get(1)));
        addresses.add(new Address(4L, "Frankfurt", clients.get(2)));
    }

    @Test
    void shouldReturnAllAddressesByClientId() throws Exception{
        Long clientId = 2L;
        given(addressService.findAllByClientId(clientId)).willReturn(addresses
                .stream()
                .filter(e-> e.getClient().getId().equals(clientId))
                .collect(Collectors.toList()));

        this.mockMvc.perform(get("/client/{id}/address", clientId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(1)))
                .andExpect(jsonPath("$[0].name", is(addresses.get(2).getName())));
    }

    @Test
    void shouldAddNewAddressToExistClient() throws Exception{
        Long clientId = 1L;
        Address address = new Address(5L, "Madrid", clients.get(0));

        given(clientService.findById(clientId)).willReturn(clients.stream()
                .filter(e -> e.getId().equals(clientId))
                .findFirst());
        given(addressService.save(any(Address.class))).willAnswer(invocation -> invocation.getArgument(0));

        this.mockMvc.perform(put("/client/{id}/address", clientId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(address)))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.name", is(address.getName())))
                .andExpect(jsonPath("$.id", is(5)));
    }

    @Test
    void shouldReturn404WhenClientNotPresentOnUpdate() throws Exception {
        Long clientId = 5L;
        Address address = new Address(5L, "Madrid", clients.get(0));
        given(clientService.findById(clientId)).willReturn(Optional.empty());
        given(addressService.save(any(Address.class))).willAnswer(invocation -> invocation.getArgument(0));

        this.mockMvc.perform(put("/client/{id}/address", clientId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(address)))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldDeleteAddressById() throws Exception {
        Long addressId = 1L;
        Address address = new Address(addressId, "Bern", clients.get(0));
        given(addressService.findById(addressId)).willReturn(Optional.of(address));

        this.mockMvc.perform(delete("/address/{id}", address.getId()))
                .andExpect(status().isOk());
    }

    @Test
    void shouldReturn404WhenAddressNotFound() throws Exception {
        Long addressId = 1L;
        given(addressService.findById(addressId)).willReturn(Optional.empty());

        this.mockMvc.perform(delete("/address/{id}", addressId))
                .andExpect(status().isNotFound());
    }
}
