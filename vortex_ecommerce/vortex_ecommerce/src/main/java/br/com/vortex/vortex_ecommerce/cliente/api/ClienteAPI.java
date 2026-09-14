package br.com.vortex.vortex_ecommerce.cliente.api;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

@RequestMapping("/public/clientes")
public interface ClienteAPI {

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  ClienteResponse cadastrarCliente(@RequestBody @Valid ClienteRequest clienteRequest);
}
