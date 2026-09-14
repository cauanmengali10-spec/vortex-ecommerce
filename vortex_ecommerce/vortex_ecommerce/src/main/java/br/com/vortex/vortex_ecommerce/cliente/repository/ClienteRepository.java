package br.com.vortex.vortex_ecommerce.cliente.repository;

import java.util.Optional;

import br.com.vortex.vortex_ecommerce.cliente.domain.Cliente;

public interface ClienteRepository {

  Cliente salva(Cliente cliente);

  Optional<Cliente> findByEmail(String email);
}
