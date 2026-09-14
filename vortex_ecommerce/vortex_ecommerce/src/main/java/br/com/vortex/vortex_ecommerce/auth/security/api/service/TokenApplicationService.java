package br.com.vortex.vortex_ecommerce.auth.security.api.service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class TokenApplicationService implements TokenService {

  @Value("${vortex.jwt.expiracao}")
  private String expiracao;

  @Value("${vortex.jwt.chave}")
  private String chave;

  @Override
  public String gerarToken(String email) {
    log.info("[inicio] TokenService - criacao de token");

    String token =
        Jwts.builder()
            .setIssuer("API Vortex")
            .setSubject(email)
            .setIssuedAt(new Date())
            .setExpiration(
                Date.from(
                    LocalDateTime.now()
                        .plusMinutes(Long.valueOf(expiracao))
                        .atZone(ZoneId.systemDefault())
                        .toInstant()))
            .signWith(SignatureAlgorithm.HS256, chave)
            .compact();

    log.info("[finaliza] TokenService - criacao de token");
    return token;
  }

  @Override
  public Optional<String> getUsuario(String token) {
    try {
      log.info("[inicio] TokenService - extracao do usuario do token");

      var claims = Jwts.parser().setSigningKey(chave).parseClaimsJws(token).getBody();

      log.info("[finaliza] TokenService - extracao do usuario do token");
      return Optional.of(claims.getSubject());
    } catch (ExpiredJwtException ex) {
      log.info("[finaliza] TokenService - extracao do usuario do token");
      return Optional.of(ex.getClaims().getSubject());
    }
  }
}
