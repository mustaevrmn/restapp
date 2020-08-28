package ru.mustaev.restapp.controller;

import org.springframework.web.bind.annotation.*;
import ru.mustaev.restapp.domain.Address;
import ru.mustaev.restapp.domain.Client;
import ru.mustaev.restapp.service.AddressService;
import ru.mustaev.restapp.service.ClientService;

import java.util.List;
import java.util.Optional;

@RestController
public class AddressController {
    private final AddressService addressService;
    private final ClientService clientService;

    public AddressController(AddressService addressService, ClientService clientService) {
        this.addressService = addressService;
        this.clientService = clientService;
    }

    @GetMapping("/client/{id}/address")
    public List<Address> findAllByClientId(@PathVariable Long id){
        return addressService.findAllByClientId(id);
    }

    @PutMapping("/client/{id}/address")
    public Address addByClientId(@RequestBody Address address, @PathVariable Long id){
        Optional<Client> client = clientService.findById(id);
        if(client.isPresent()) {
            address.setClient(client.get());
            client.get().getAddresses().add(address);
        }
        return addressService.save(address);
    }

    @DeleteMapping("/address/{id}")
    public void deleteAddress(@PathVariable Long id){
        addressService.deleteById(id);
    }

}
