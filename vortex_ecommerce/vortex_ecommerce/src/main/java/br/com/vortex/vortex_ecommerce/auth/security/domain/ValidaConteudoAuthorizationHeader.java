package br.com.vortex.vortex_ecommerce.auth.security.domain;

import java.util.function.Predicate;

public class ValidaConteudoAuthorizationHeader implements Predicate<String> {

  @Override
  public boolean test(String conteudoAuthorizationHeander) {
    return !conteudoAuthorizationHeander.isEmpty()
        && conteudoAuthorizationHeander.startsWith("Bearer ");
  }
}
