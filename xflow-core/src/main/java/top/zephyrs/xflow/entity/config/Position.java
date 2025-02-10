package top.zephyrs.xflow.entity.config;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class Position {
    private BigDecimal x;
    private BigDecimal y;
}
