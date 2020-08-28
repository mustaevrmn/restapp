package ru.mustaev.restapp.service;

import org.springframework.stereotype.Service;
import ru.mustaev.restapp.domain.Address;
import ru.mustaev.restapp.repo.AddressRepository;

import java.util.List;

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
    public void deleteById(Long id){
        addressRepository.deleteById(id);
    }
}
