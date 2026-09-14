package br.com.vortex.vortex_ecommerce.cliente.api;

import org.springframework.web.bind.annotation.RestController;

import br.com.vortex.vortex_ecommerce.cliente.api.service.ClienteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@RestController
@Log4j2
@RequiredArgsConstructor
public class ClienteController implements ClienteAPI {

  private final ClienteService clienteService;

  @Override
  public ClienteResponse cadastrarCliente(ClienteRequest clienteRequest) {
    log.debug("[inicio] ClienteController - cadastrar");
    ClienteResponse response = clienteService.criaCliente(clienteRequest);
    log.debug("[finaliza] ClienteController - cadastrar");
    return response;
  }
}
