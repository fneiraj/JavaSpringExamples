package dev.fneira.interfaceprocessor.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class RutValidator implements ConstraintValidator<Rut, String> {

  public boolean isValid(final String rutValue, final ConstraintValidatorContext context) {
    // if rut is not in the correct format, return false
    if (!isValidFormat(rutValue)) {
      return false;
    }

    // disable existing violation message
    context.disableDefaultConstraintViolation();

    // add rut invalid message
    context.buildConstraintViolationWithTemplate("Rut is invalid").addConstraintViolation();

    // return true if rut and dv is valid
    // upper case is used to avoid case sensitive validation in dv with 'k' or 'K'
    return isValidDv(rutValue.toUpperCase());
  }

  private boolean isValidFormat(final String rut) {
    return rut.matches("^[0-9]+-[0-9kK]$");
  }

  private boolean isValidDv(final String rutWithDv) {
    final String rut = rutWithDv.substring(0, rutWithDv.length() - 2);
    final char dv = rutWithDv.charAt(rutWithDv.length() - 1);
    final char calculatedDv = calculateDv(rut);
    return dv == calculatedDv;
  }

  private char calculateDv(final String rut) {
    final char[] characters = rut.toCharArray();
    final int[] multipliers = new int[] {2, 3, 4, 5, 6, 7};
    int sum = 0;
    int multiplierIndex = 0;
    for (int i = characters.length - 1; i >= 0; i--) {
      final int digit = characters[i] - '0';
      final int multiplier = multipliers[multiplierIndex % multipliers.length];
      sum += digit * multiplier;
      multiplierIndex++;
    }
    final int calculatedDv = (11 - (sum % 11)) % 11;
    return calculatedDv == 10 ? 'K' : (char) (calculatedDv + '0');
  }
}
