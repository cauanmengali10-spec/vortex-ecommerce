package br.com.vortex.vortex_ecommerce.cliente.api.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.vortex.vortex_ecommerce.cliente.api.ClienteRequest;
import br.com.vortex.vortex_ecommerce.cliente.api.ClienteResponse;
import br.com.vortex.vortex_ecommerce.cliente.domain.Cliente;
import br.com.vortex.vortex_ecommerce.cliente.repository.ClienteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
@RequiredArgsConstructor
public class ClienteApplicationService implements ClienteService {

  private final ClienteRepository clienteRepository;

  @Override
  @Transactional
  public ClienteResponse criaCliente(ClienteRequest request) {
    log.debug("[inicio] ClienteApplicationService - criacao de cliente");
    Cliente clienteCriado = clienteRepository.salva(new Cliente(request));
    log.debug("[finaliza] ClienteApplicationService - criacao de cliente");
    return new ClienteResponse(clienteCriado);
  }
}
