package com.template.demo.repository;

import com.template.demo.model.Client;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepository extends JpaRepository<Client, Integer> {
    Optional<Client> findByCpfOrEmail(String cpf, String email);

    Optional<Client> findByCpf(String cpf);

    Optional<Client> findByEmail(String email);

    Optional<Client> findById(Long id);

    List<Client> findAllByOrderByIdAsc();
}
