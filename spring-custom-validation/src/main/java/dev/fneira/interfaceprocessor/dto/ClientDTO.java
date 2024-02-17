package dev.fneira.interfaceprocessor.dto;

import dev.fneira.interfaceprocessor.validation.Rut;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDate;

public class ClientDTO {

  @NotBlank private String name;
  @NotBlank private String lastName;
  @NotBlank @Email private String email;
  @Rut private String rut;
  private LocalDate birthDate;

  public ClientDTO() {}

  public ClientDTO(
      final String name,
      final String lastName,
      final String email,
      final String rut,
      final LocalDate birthDate) {
    this.name = name;
    this.lastName = lastName;
    this.email = email;
    this.rut = rut;
    this.birthDate = birthDate;
  }

  public String getName() {
    return name;
  }

  public void setName(final String name) {
    this.name = name;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(final String lastName) {
    this.lastName = lastName;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(final String email) {
    this.email = email;
  }

  public String getRut() {
    return rut;
  }

  public void setRut(final String rut) {
    this.rut = rut;
  }

  public LocalDate getBirthDate() {
    return birthDate;
  }

  public void setBirthDate(final LocalDate birthDate) {
    this.birthDate = birthDate;
  }
}
