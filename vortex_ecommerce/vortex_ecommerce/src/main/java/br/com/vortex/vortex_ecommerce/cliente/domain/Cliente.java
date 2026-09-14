package br.com.vortex.vortex_ecommerce.cliente.domain;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.UUID;

import jakarta.persistence.*;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import br.com.vortex.vortex_ecommerce.cliente.api.ClienteRequest;
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
@Table(name = "clientes")
public class Cliente implements UserDetails {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "id")
  private UUID id;

  @Column(nullable = false, length = 120)
  private String nome;

  @Column(nullable = false, unique = true, length = 14)
  private String cpf;

  @Column(nullable = false, unique = true, length = 190)
  private String email;

  @Column(length = 20)
  private String telefone;

  private boolean eAdmin;

  private boolean aceitaNotificacoes;

  @Column(nullable = false)
  private LocalDateTime criadoEm;

  @Column(nullable = false)
  private LocalDateTime atualizadoEm;

  @PrePersist
  public void onCreate() {
    criadoEm = LocalDateTime.now();
    atualizadoEm = LocalDateTime.now();
  }

  @PreUpdate
  public void onUpdate() {
    atualizadoEm = LocalDateTime.now();
  }

  public Cliente(ClienteRequest request) {
    this.nome = request.getNome();
    this.email = request.getEmail();
    this.telefone = request.getTelefone();
    this.cpf = request.getCpf();
    this.aceitaNotificacoes = request.isAceitaNotificacoes();
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return null;
  }

  @Override
  public String getPassword() {
    return null;
  }

  @Override
  public String getUsername() {
    return this.email;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }

  private static final long serialVersionUID = 1L;
}
