package br.com.vortex.vortex_ecommerce.auth.otp.repository;

import java.time.OffsetDateTime;
import java.util.Optional;

import br.com.vortex.vortex_ecommerce.auth.otp.domain.CodigoOtp;
import br.com.vortex.vortex_ecommerce.auth.otp.domain.FinalidadeOtp;

public interface CodigoOtpRepository {

  CodigoOtp salva(CodigoOtp codigoOtp);

  Optional<CodigoOtp> findFirstByEmailAndFinalidadeAndUsadoFalseAndExpiraEmAfterOrderByExpiraEmDesc(
      String email, FinalidadeOtp finalidade, OffsetDateTime agora);
}
