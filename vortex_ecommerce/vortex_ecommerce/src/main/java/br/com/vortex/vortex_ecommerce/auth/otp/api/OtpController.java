package br.com.vortex.vortex_ecommerce.auth.otp.api;

import org.springframework.web.bind.annotation.RestController;

import br.com.vortex.vortex_ecommerce.auth.otp.api.service.OtpService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@RestController
@Log4j2
@RequiredArgsConstructor
public class OtpController implements OtpAPI {

  private final OtpService otpService;

  @Override
  public void solicitaCodigo(OtpRequest otpRequest) {
    log.debug("[inicio] OtpController - solicita codigo");
    otpService.solicitaCodigo(otpRequest.getEmail(), otpRequest.getFinalidade());
    log.debug("[finaliza] OtpController - solicita codigo");
  }

  @Override
  public OtpTokenResponse validaCodigo(OtpVerifyRequest otpVerifyRequest) {
    log.debug("[inicio] OtpController - valida codigo");
    String token =
        otpService.validaCodigo(
            otpVerifyRequest.getEmail(),
            otpVerifyRequest.getCodigo(),
            otpVerifyRequest.getFinalidade());
    OtpTokenResponse response = new OtpTokenResponse(token);
    log.debug("[finaliza] OtpController - valida codigo");
    return response;
  }
}
