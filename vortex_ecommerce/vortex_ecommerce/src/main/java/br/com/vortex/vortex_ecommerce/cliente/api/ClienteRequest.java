package br.com.vortex.vortex_ecommerce.cliente.api;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import org.hibernate.validator.constraints.br.CPF;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ClienteRequest {

  @NotBlank(message = "{validacao.nome.obrigatorio}")
  @Size(max = 120, message = "{validacao.nome.tamanho.maximo}")
  private String nome;

  @NotBlank(message = "{validacao.email.obrigatorio}")
  @Email(message = "{validacao.email.formato.invalido}")
  private String email;

  @NotBlank(message = "{validacao.cpf.obrigatorio}")
  @CPF(message = "{validacao.cpf.invalido}")
  private String cpf;

  @Size(max = 20, message = "{validacao.telefone.tamanho.maximo}")
  private String telefone;

  private boolean aceitaNotificacoes;
}
