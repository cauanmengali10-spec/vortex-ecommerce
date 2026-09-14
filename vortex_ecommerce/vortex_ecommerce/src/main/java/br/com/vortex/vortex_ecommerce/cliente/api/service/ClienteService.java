package br.com.vortex.vortex_ecommerce.cliente.api.service;

import br.com.vortex.vortex_ecommerce.cliente.api.ClienteRequest;
import br.com.vortex.vortex_ecommerce.cliente.api.ClienteResponse;

public interface ClienteService {

  ClienteResponse criaCliente(ClienteRequest clienteRequest);
}
