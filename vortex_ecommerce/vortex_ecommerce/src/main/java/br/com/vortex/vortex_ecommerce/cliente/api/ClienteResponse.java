package br.com.vortex.vortex_ecommerce.cliente.api;

import java.util.UUID;

import br.com.vortex.vortex_ecommerce.cliente.domain.Cliente;
import lombok.Value;

@Value
public class ClienteResponse {

  private UUID id;
  private String nome;
  private String email;

  public ClienteResponse(Cliente cliente) {
    this.id = cliente.getId();
    this.nome = cliente.getNome();
    this.email = cliente.getEmail();
  }
}
