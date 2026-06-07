package com.bank.dto;

import lombok.Data;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
public class LoginDTO {

    @NotBlank(message = "账号不能为空")
    private String account;

    @NotBlank(message = "交易密码不能为空")
    @Pattern(regexp = "^\\d{6}$", message = "交易密码必须是6位数字")
    private String tradePassword;
}
