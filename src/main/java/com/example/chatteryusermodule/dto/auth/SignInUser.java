package com.example.chatteryusermodule.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.io.Serializable;

@Data
public class SignInUser implements Serializable {
    @Size(min = 5, max = 256, message = "Адрес эл. почты должен содержать от 5 до 256 символов")
    @Email(message = "Переданный эл. адрес должен быть в формате client@client.com")
    @NotBlank(message = "Эл. почта не может быть пустой")
    private String email;

    @Size(min = 10, max = 30, message = "Пароль должен быть длиной не менее 10-ти символов и не более 30-ти")
    @NotBlank(message = "Пароль не может быть пустым")
    private String password;
}
