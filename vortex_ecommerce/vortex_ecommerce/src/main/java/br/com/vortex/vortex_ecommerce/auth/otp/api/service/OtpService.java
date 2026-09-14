package br.com.vortex.vortex_ecommerce.auth.otp.api.service;

import br.com.vortex.vortex_ecommerce.auth.otp.domain.FinalidadeOtp;

public interface OtpService {

  void solicitaCodigo(String email, FinalidadeOtp finalidade);

  String validaCodigo(String email, String codigo, FinalidadeOtp finalidade);
}
