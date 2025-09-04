package com.gestionnegocios.api_gestionnegocios.mapper.decorator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.gestionnegocios.api_gestionnegocios.dto.Cuenta.CuentaResponseDTO;
import com.gestionnegocios.api_gestionnegocios.mapper.CuentaMapper;
import com.gestionnegocios.api_gestionnegocios.models.Cuenta;
import com.gestionnegocios.api_gestionnegocios.security.EncryptionUtil;

@Component
public abstract class CuentaMapperDecorator implements CuentaMapper {

    @Autowired
    @Qualifier("delegate") // MapStruct genera la implementación original
    private CuentaMapper delegate;

    @Autowired
    private EncryptionUtil encryptionUtil;

    @Override
    public CuentaResponseDTO toResponseDTO(Cuenta cuenta) {
        CuentaResponseDTO dto = delegate.toResponseDTO(cuenta);

        if (cuenta.getNumeroCuenta() != null) {
            String numeroDesencriptado = encryptionUtil.decrypt(cuenta.getNumeroCuenta());
            dto.setNumeroCuentaParseada(maskAccountNumber(numeroDesencriptado));
        }

        return dto;
    }

    private String maskAccountNumber(String numeroCuenta) {
        if (numeroCuenta == null || numeroCuenta.length() < 8) {
            return "Número inválido";
        }
        String[] partes = numeroCuenta.split("-");
        if (partes.length < 4) {
            return "Formato inválido";
        }
        return partes[0] + "-" + partes[1] + "-XXXX-XXXX";
    }
}
