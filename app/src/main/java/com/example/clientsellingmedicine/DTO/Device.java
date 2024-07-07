package com.example.clientsellingmedicine.DTO;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Device {
    private Integer idUser;
    private String token;
    private Integer status;
}

