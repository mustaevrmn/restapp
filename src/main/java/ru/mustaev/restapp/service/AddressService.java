package ru.mustaev.restapp.service;

import org.springframework.stereotype.Service;
import ru.mustaev.restapp.domain.Address;
import ru.mustaev.restapp.repo.AddressRepository;

import java.util.List;
import java.util.Optional;

@Service
public class AddressService {
    private final AddressRepository addressRepository;

    public AddressService(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }
    public List<Address> findAllByClientId(Long id){
        return addressRepository.findAllByClientId(id);
    }
    public Address save(Address address){
        return addressRepository.save(address);
    }
    public Optional<Address> findById(Long id){
        return addressRepository.findById(id);
    }
    public void deleteById(Long id){
        addressRepository.deleteById(id);
    }
}
