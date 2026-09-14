package br.com.vortex.vortex_ecommerce.cliente.infra;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.vortex.vortex_ecommerce.cliente.domain.Cliente;

public interface ClienteJPARepository extends JpaRepository<Cliente, UUID> {

  Optional<Cliente> findByEmail(String email);
}
