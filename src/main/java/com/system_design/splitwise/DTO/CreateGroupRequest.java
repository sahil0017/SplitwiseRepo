package com.system_design.splitwise.DTO;

import lombok.Data;

import java.util.UUID;

@Data
public class CreateGroupRequest {

    private String name;

    private UUID createdBy;

}
