package com.example.clientsellingmedicine.DTO;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentDto {
    private List<CartItemDTO> cartDetailDtoList;
    private Integer couponDetailId;
    private Order order;
}
