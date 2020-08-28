package ru.mustaev.restapp.service;

import org.springframework.stereotype.Service;
import ru.mustaev.restapp.domain.Client;
import ru.mustaev.restapp.repo.ClientRepository;

import java.util.List;
import java.util.Optional;

@Service
public class ClientService {
    private final ClientRepository clientRepository;

    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public List<Client> findAll(){
        return clientRepository.findAll();
    }
    public Client save(Client client){
        return clientRepository.save(client);
    }
    public Optional<Client> findById(Long id){
        return clientRepository.findById(id);
    }
    public void deleteById(Long id){
        clientRepository.deleteById(id);
    }
    public List<String> findAllByName(){
        return clientRepository.findAllByName();
    }
    public List<String> findAllContainingNameAsc(String filter){
        return clientRepository.findAllContainingNameAsc(filter);
    }
}
