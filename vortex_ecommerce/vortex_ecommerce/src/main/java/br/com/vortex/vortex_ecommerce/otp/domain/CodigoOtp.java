package br.com.vortex.vortex_ecommerce.otp.domain;


import jakarta.persistence.*;
import lombok.*;

import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Table(name = "codigo_otp")
public class CodigoOtp {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private UUID id;

    @Column(nullable = false, length = 190)
    private String email;

    @Column(nullable = false, length = 6)
    private String codigo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private FinalidadeOtp finalidade;

    @Column(nullable = false)
    private OffsetDateTime expiraEm;

    private int tentativas;

    private boolean usado;

    @Column(nullable = false)
    private OffsetDateTime criadoEm;

    public boolean estaExpirado() {
        return OffsetDateTime.now().isAfter(expiraEm);
    }

    public void registraTentativasErradas() {
        this.tentativas++;
    }

    public void marcaComoUsado() {
        this.usado = true;

    }
}




