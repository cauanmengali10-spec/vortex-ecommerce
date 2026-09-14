package br.com.vortex.vortex_ecommerce.auth.otp.api.service;

import java.security.SecureRandom;
import java.time.OffsetDateTime;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.vortex.vortex_ecommerce.auth.otp.domain.CodigoOtp;
import br.com.vortex.vortex_ecommerce.auth.otp.domain.FinalidadeOtp;
import br.com.vortex.vortex_ecommerce.auth.otp.repository.CodigoOtpRepository;
import br.com.vortex.vortex_ecommerce.auth.security.api.service.TokenService;
import br.com.vortex.vortex_ecommerce.handler.APIException;
import br.com.vortex.vortex_ecommerce.handler.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
@RequiredArgsConstructor
public class OtpApplicationService implements OtpService {

  private static final int MAXIMO_TENTATIVAS = 5;

  private final CodigoOtpRepository codigoOtpRepository;
  private final JavaMailSender javaMailSender;
  private final TokenService tokenService;
  private final MessageSource messageSource;

  @Value("${vortex.otp.expiracao}")
  private Long minutosExpiracao;

  private final SecureRandom secureRandom = new SecureRandom();

  @Override
  @Transactional
  public void solicitaCodigo(String email, FinalidadeOtp finalidade) {
    log.info("[inicio] OtpService - solicitacao de codigo");

    String codigo = String.format("%06d", secureRandom.nextInt(1_000_000));

    CodigoOtp otp =
        CodigoOtp.builder()
            .email(email)
            .codigo(codigo)
            .finalidade(finalidade)
            .expiraEm(OffsetDateTime.now().plusMinutes(minutosExpiracao))
            .criadoEm(OffsetDateTime.now())
            .tentativas(0)
            .usado(false)
            .build();

    codigoOtpRepository.salva(otp);
    enviaEmail(email, codigo, finalidade);
    log.info("[finaliza] OtpService - solicitacao de codigo");
  }

  @Override
  public String validaCodigo(String email, String codigo, FinalidadeOtp finalidade) {
    log.info("[inicio] OtpService - validacao de codigo");

    CodigoOtp otp =
        codigoOtpRepository
            .findFirstByEmailAndFinalidadeAndUsadoFalseAndExpiraEmAfterOrderByExpiraEmDesc(
                email, finalidade, OffsetDateTime.now())
            .orElseThrow(
                () ->
                    new APIException(
                        HttpStatus.BAD_REQUEST, ErrorCode.CODIGO_OTP_INVALIDO_OU_EXPIRADO));

    String token;
    if (otp.getTentativas() >= MAXIMO_TENTATIVAS) {
      throw new APIException(HttpStatus.BAD_REQUEST, ErrorCode.TENTATIVA_MAXIMA_EXCEDIDA);
    }

    if (!otp.getCodigo().equals(codigo)) {
      otp.registraTentativasErradas();
      codigoOtpRepository.salva(otp);
      throw new APIException(HttpStatus.BAD_REQUEST, ErrorCode.CODIGO_OTP_INVALIDO_OU_EXPIRADO);
    }

    otp.marcaComoUsado();
    token = tokenService.gerarToken(email);
    codigoOtpRepository.salva(otp);

    log.info("[finaliza] OtpService - validacao de codigo");
    return token;
  }

  private void enviaEmail(String email, String codigo, FinalidadeOtp finalidade) {
    SimpleMailMessage message = new SimpleMailMessage();
    message.setTo(email);
    message.setSubject(messageSource.getMessage("otp.email.assunto", null, Locale.getDefault()));
    message.setText(
        messageSource.getMessage(
            "otp.email.corpo",
            new Object[] {codigo, minutosExpiracao, finalidade},
            Locale.getDefault()));
    javaMailSender.send(message);
  }
}
