package app.matheus.motta.jbank.controller.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.br.CPF;

public record CreateWalletDto(
        @CPF(message = "Invalid CPF provided")
        @NotBlank(message = "The 'cpf' is a required field")
        String cpf,

        @Email(message = "Invalid Email provided")
        @NotBlank(message = "The 'email' is a required field")
        String email,

        @NotBlank(message = "The 'name' is a required field")
        String name
){
}
