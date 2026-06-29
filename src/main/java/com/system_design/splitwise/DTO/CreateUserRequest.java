package com.system_design.splitwise.DTO;

import lombok.Data;

@Data
public class CreateUserRequest {

    private String name;

    private String email;

    private String phone;

}