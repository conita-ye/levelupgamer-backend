package com.levelupgamer.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class RutValidator implements ConstraintValidator<Rut, String> {

    @Override
    public boolean isValid(String rut, ConstraintValidatorContext context) {
        if (rut == null || rut.isEmpty()) {
            return true; 
        }

        try {
            rut = rut.toUpperCase().replace(".", "").replace("-", "");
            
            // Validar longitud: debe tener entre 8 y 9 caracteres (7-8 dígitos + 1 dígito verificador)
            if (rut.length() < 8 || rut.length() > 9) {
                return false;
            }
            
            // Validar que el último carácter sea un dígito o K
            char dv = rut.charAt(rut.length() - 1);
            if (dv != 'K' && (dv < '0' || dv > '9')) {
                return false;
            }
            
            // Extraer el número (todos los caracteres excepto el último)
            String rutNumberStr = rut.substring(0, rut.length() - 1);
            
            // Validar que todos los caracteres del número sean dígitos
            if (!rutNumberStr.matches("\\d+")) {
                return false;
            }
            
            int rutAux = Integer.parseInt(rutNumberStr);
            
            // Validar que el número no sea cero
            if (rutAux <= 0) {
                return false;
            }

            // Calcular el dígito verificador usando el algoritmo estándar chileno (Modulo 11)
            // Multiplicadores: 2, 3, 4, 5, 6, 7 (se repiten)
            // Se trabaja con el número revertido (de derecha a izquierda)
            String rutReversed = new StringBuilder(rutNumberStr).reverse().toString();
            int[] multiplicadores = {2, 3, 4, 5, 6, 7};
            int suma = 0;
            
            for (int i = 0; i < rutReversed.length(); i++) {
                int digito = Character.getNumericValue(rutReversed.charAt(i));
                int multiplicador = multiplicadores[i % multiplicadores.length];
                suma += digito * multiplicador;
            }
            
            // Calcular el resto de la división por 11
            int resto = suma % 11;
            
            // Calcular el dígito verificador esperado
            char expectedDv;
            if (resto == 0) {
                expectedDv = '0';
            } else if (resto == 1) {
                expectedDv = 'K';
            } else {
                expectedDv = (char) ('0' + (11 - resto));
            }
            
            // Si el dígito verificador no coincide, personalizar el mensaje de error
            if (dv != expectedDv) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate(
                    "RUT inválido: el dígito verificador debe ser '" + expectedDv + "'"
                ).addConstraintViolation();
                return false;
            }
            
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
