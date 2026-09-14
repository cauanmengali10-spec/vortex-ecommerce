package br.com.vortex.vortex_ecommerce.cliente.infra;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import br.com.vortex.vortex_ecommerce.cliente.domain.Cliente;
import br.com.vortex.vortex_ecommerce.cliente.repository.ClienteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Repository
@RequiredArgsConstructor
@Log4j2
public class ClienteInfraRepository implements ClienteRepository {

  private final ClienteJPARepository clienteJPARepository;

  @Override
  public Cliente salva(Cliente cliente) {
    log.debug("[inicio] InfraRepository - salva cliente");
    Cliente clienteSalvo = clienteJPARepository.save(cliente);
    log.debug("[finaliza] InfraRepository - salva cliente");
    return clienteSalvo;
  }

  @Override
  public Optional<Cliente> findByEmail(String email) {
    log.debug("[inicio] InfraRepository - busca cliente por email");
    Optional<Cliente> cliente = clienteJPARepository.findByEmail(email);
    log.debug("[finaliza] InfraRepository - busca cliente por email");
    return cliente;
  }
}
