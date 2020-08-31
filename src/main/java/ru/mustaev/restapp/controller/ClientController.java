package ru.mustaev.restapp.controller;

import org.springframework.web.bind.annotation.*;
import ru.mustaev.restapp.domain.Client;
import ru.mustaev.restapp.exception.ResourceNotFoundException;
import ru.mustaev.restapp.service.ClientService;

import java.util.List;
import java.util.Optional;

@RestController
public class ClientController {

    private final ClientService clientService;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping("/")
    public String index(){
        return "Welcome to RESTful Web Service";
    }

    @GetMapping("/client")
    public List<String> findSortedAll(@RequestParam(required = false) String filter){
        if (filter != null && !filter.isEmpty()){
            return clientService.findAllContainingNameAsc(filter);
        } else {
            return clientService.findAllByName();
        }
    }

    @PostMapping("/client")
    public Client addClient(@RequestBody Client client){
        client.getAddresses().forEach(address -> address.setClient(client));
        return clientService.save(client);
    }

    @GetMapping("/client/{id}")
    public Optional<Client> findClient(@PathVariable Long id) throws ResourceNotFoundException {
        return Optional.ofNullable(clientService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Client not found for this id: " + id)));
    }

    @DeleteMapping("/client/{id}")
    public void deleteClient(@PathVariable Long id) throws ResourceNotFoundException {
        clientService.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Client not found for this id: " + id));
        clientService.deleteById(id);
    }
}
