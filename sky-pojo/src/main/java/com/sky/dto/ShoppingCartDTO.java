package com.sky.dto;

import lombok.Data;
import org.springframework.lang.Nullable;

import java.io.Serializable;

@Data
public class ShoppingCartDTO implements Serializable {
    @Nullable
    private Long dishId;

    @Nullable
    private Long setmealId;

    @Nullable
    private String dishFlavor;

}
