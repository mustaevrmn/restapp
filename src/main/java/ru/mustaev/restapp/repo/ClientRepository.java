package ru.mustaev.restapp.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.mustaev.restapp.domain.Client;

import java.util.List;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {

    @Query("select c.name from Client c where lower(c.name) " +
            "like lower(concat('%', :filter,'%')) " +
            "order by c.name ASC")
    List<String> findAllContainingNameAsc(String filter);

    @Query("select c.name from Client c")
    List<String> findAllByName();
}
