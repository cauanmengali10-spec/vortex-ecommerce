package br.com.vortex.vortex_ecommerce.auth.otp.api;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

@RequestMapping("/public/otp")
public interface OtpAPI {

  @PostMapping
  @ResponseStatus(HttpStatus.NO_CONTENT)
  void solicitaCodigo(@RequestBody @Valid OtpRequest otpRequest);

  @PostMapping("/validacao")
  OtpTokenResponse validaCodigo(@RequestBody @Valid OtpVerifyRequest otpVerifyRequest);
}
