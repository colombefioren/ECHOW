package com.echow.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class SignUpRequest {

  @NotBlank
  @Size(min = 3, max = 50)
  @Pattern(
      regexp = "^[a-zA-Z0-9_-]+$",
      message = "Username can only contain letters, numbers, underscore and hyphen")
  private String username;

  @NotBlank @Email private String email;

  @NotBlank
  @Size(min = 6)
  @Pattern(
      regexp = "^(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]+$",
      message =
          "Password must contain at least one uppercase letter, one number, and one special character")
  private String password;

  @NotBlank
  @Size(min = 2, max = 50)
  @Pattern(
      regexp = "^[a-zA-ZÀ-ÿ]+([ '][a-zA-ZÀ-ÿ]+)*$",
      message = "First name can only contain letters, spaces, and apostrophes")
  private String firstName;

  @Size(min = 2, max = 50)
  @Pattern(
      regexp = "^[a-zA-ZÀ-ÿ]+([ '][a-zA-ZÀ-ÿ]+)*$",
      message = "Last name can only contain letters, spaces, and apostrophes")
  private String lastName;
}
