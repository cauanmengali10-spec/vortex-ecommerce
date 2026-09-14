package br.com.vortex.vortex_ecommerce.auth.otp.domain;

import java.time.OffsetDateTime;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(name = "codigo_otp")
public class CodigoOtp {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "id")
  private UUID id;

  @Column(nullable = false, length = 190)
  private String email;

  @Column(nullable = false, length = 6)
  private String codigo;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 30)
  private FinalidadeOtp finalidade;

  @Column(nullable = false)
  private OffsetDateTime expiraEm;

  private int tentativas;

  private boolean usado;

  @Column(nullable = false)
  private OffsetDateTime criadoEm;

  public boolean estaExpirado() {
    return OffsetDateTime.now().isAfter(expiraEm);
  }

  public void registraTentativasErradas() {
    this.tentativas++;
  }

  public void marcaComoUsado() {
    this.usado = true;
  }
}
