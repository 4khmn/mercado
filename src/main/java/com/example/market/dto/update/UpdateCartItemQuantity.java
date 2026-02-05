package com.example.market.dto.update;

import com.example.market.enums.QuantityDirection;
import lombok.Data;

@Data
public class UpdateCartItemQuantity {
    private Long cartItemId;
    private QuantityDirection direction;
}
