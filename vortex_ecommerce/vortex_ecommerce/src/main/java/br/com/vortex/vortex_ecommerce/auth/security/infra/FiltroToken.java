package br.com.vortex.vortex_ecommerce.auth.security.infra;

import java.io.IOException;
import java.util.Collections;
import java.util.Optional;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import br.com.vortex.vortex_ecommerce.auth.security.api.service.TokenService;
import br.com.vortex.vortex_ecommerce.auth.security.domain.ValidaConteudoAuthorizationHeader;
import br.com.vortex.vortex_ecommerce.cliente.domain.Cliente;
import br.com.vortex.vortex_ecommerce.cliente.repository.ClienteRepository;
import br.com.vortex.vortex_ecommerce.handler.APIException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RequiredArgsConstructor
public class FiltroToken extends OncePerRequestFilter {

  private final TokenService tokenService;
  private final ClienteRepository clienteRepository;

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    log.info("[inicio] Filtro - filtrando requisicao");

    String token = recuperaToken(request);
    autenticaCliente(token);

    log.info("[finaliza] Filtro - filtrando requisicao");
    filterChain.doFilter(request, response);
  }

  private void autenticaCliente(String token) {
    log.info("[inicio] autenticacaoCliente - autenticando usuario pelo token");

    Cliente cliente = recuperaCliente(token);

    var authenticationToken =
        new UsernamePasswordAuthenticationToken(cliente, null, cliente.getAuthorities());

    SecurityContextHolder.getContext().setAuthentication(authenticationToken);

    log.info("[finaliza] autenticacaoCliente - autenticando usuario pelo token");
  }

  private Cliente recuperaCliente(String token) {
    var email =
        tokenService
            .getUsuario(token)
            .orElseThrow(
                () ->
                    APIException.build(
                        HttpStatus.FORBIDDEN, "O Token enviado está inválido. Tente novamente."));

    return clienteRepository
        .findByEmail(email)
        .orElseThrow(() -> APIException.build(HttpStatus.NOT_FOUND, "Cliente não encontrado."));
  }

  private String recuperaToken(HttpServletRequest requestOpt) {
    log.info("[inicio] recuperaToken - extraindo o token dos cabecalhos da requisicao");

    Optional<String> AuthorizationHeaderValueOpt =
        Optional.ofNullable(recuperaValorAuthorizationHeader(requestOpt));

    String AuthorizationHeaderValue =
        AuthorizationHeaderValueOpt.filter(new ValidaConteudoAuthorizationHeader())
            .orElseThrow(() -> APIException.build(HttpStatus.UNAUTHORIZED, "Token inválido!"));

    log.info("[finaliza] recuperaToken - extraindo o token dos cabecalhos da requisicao");
    return AuthorizationHeaderValue.substring(7, AuthorizationHeaderValue.length());
  }

  private String recuperaValorAuthorizationHeader(HttpServletRequest request) {
    log.info("Cabeçalhos recebidos: " + Collections.list(request.getHeaderNames()));
    log.info("Authorization Header: " + request.getHeader("Authorization"));

    String authorization = request.getHeader("Authorization");

    if (authorization == null || authorization.isBlank()) {
      log.error("Nenhum token foi encontrado nos cabeçalhos!");
      throw APIException.build(HttpStatus.FORBIDDEN, "Token não está presente na requisição!");
    }
    log.info("Token encontrado!");
    return authorization;
  }

  @Override
  protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
    String path = request.getRequestURI();

    return path.contains("/public/")
        || path.contains("/swagger-ui/")
        || path.contains("/webhook")
        || path.contains("/actuator");
  }
}
