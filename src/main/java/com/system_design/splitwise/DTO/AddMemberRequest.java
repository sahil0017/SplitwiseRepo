package com.system_design.splitwise.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class AddMemberRequest {

    private UUID userId;
    private UUID groupId;

}
