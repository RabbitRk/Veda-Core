package com.veda.model.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SignupRequest {

    @NotBlank(message = "Username should not be blank")
    private String userName;

    @NotBlank(message = "Password should not be blank")
    private String passWord;

    @NotBlank(message = "Type should not be blank")
    private String type;

}
