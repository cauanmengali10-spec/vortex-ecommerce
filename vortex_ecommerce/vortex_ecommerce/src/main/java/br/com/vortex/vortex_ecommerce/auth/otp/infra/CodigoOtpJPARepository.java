package br.com.vortex.vortex_ecommerce.auth.otp.infra;

import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.vortex.vortex_ecommerce.auth.otp.domain.CodigoOtp;
import br.com.vortex.vortex_ecommerce.auth.otp.domain.FinalidadeOtp;

public interface CodigoOtpJPARepository extends JpaRepository<CodigoOtp, UUID> {

  Optional<CodigoOtp> findFirstByEmailAndFinalidadeAndUsadoFalseAndExpiraEmAfterOrderByExpiraEmDesc(
      String email, FinalidadeOtp finalidade, OffsetDateTime agora);
}
