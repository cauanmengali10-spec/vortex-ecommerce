package br.com.vortex.vortex_ecommerce.auth.otp.infra;

import java.time.OffsetDateTime;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import br.com.vortex.vortex_ecommerce.auth.otp.domain.CodigoOtp;
import br.com.vortex.vortex_ecommerce.auth.otp.domain.FinalidadeOtp;
import br.com.vortex.vortex_ecommerce.auth.otp.repository.CodigoOtpRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Repository
@RequiredArgsConstructor
@Log4j2
public class CodigoOtpInfraRepository implements CodigoOtpRepository {

  private final CodigoOtpJPARepository codigoOtpJPARepository;

  @Override
  public CodigoOtp salva(CodigoOtp codigoOtp) {
    log.debug("[inicio] InfraRepository - salva codigo OTP");
    CodigoOtp codigoOtpSalvo = codigoOtpJPARepository.save(codigoOtp);
    log.debug("[finaliza] InfraRepository - salva codigo OTP");
    return codigoOtpSalvo;
  }

  @Override
  public Optional<CodigoOtp>
      findFirstByEmailAndFinalidadeAndUsadoFalseAndExpiraEmAfterOrderByExpiraEmDesc(
          String email, FinalidadeOtp finalidade, OffsetDateTime agora) {
    return codigoOtpJPARepository
        .findFirstByEmailAndFinalidadeAndUsadoFalseAndExpiraEmAfterOrderByExpiraEmDesc(
            email, finalidade, agora);
  }
}
