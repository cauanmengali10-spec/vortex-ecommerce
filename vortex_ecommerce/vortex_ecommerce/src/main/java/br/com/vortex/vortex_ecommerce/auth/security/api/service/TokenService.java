package br.com.vortex.vortex_ecommerce.auth.security.api.service;

import java.util.Optional;

public interface TokenService {

  String gerarToken(String email);

  Optional<String> getUsuario(String token);
}
