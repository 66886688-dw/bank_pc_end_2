package com.bank.dto;

import lombok.Data;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
public class DepositDTO {
    @NotNull(message = "用户ID不能为空")
    private Long userId;

    @NotBlank(message = "交易密码不能为空")
    private String tradePassword;

    @NotNull(message = "存款金额不能为空")
    @DecimalMin(value = "0.01", message = "存款金额必须大于0")
    private BigDecimal amount;
}
