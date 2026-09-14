package br.com.vortex.vortex_ecommerce.auth.otp.api;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import br.com.vortex.vortex_ecommerce.auth.otp.domain.FinalidadeOtp;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OtpVerifyRequest {

  @NotBlank(message = "{validacao.email.obrigatorio}")
  @Email(message = "{validacao.email.formato.invalido}")
  private String email;

  @NotBlank(message = "{validacao.codigo.obrigatorio}")
  @Pattern(regexp = "\\d{6}", message = "{validacao.codigo.seis.digitos}")
  private String codigo;

  @NotNull(message = "{validacao.finalidade.obrigatoria}")
  private FinalidadeOtp finalidade;
}
