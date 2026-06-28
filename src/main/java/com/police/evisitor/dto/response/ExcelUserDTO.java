package com.police.evisitor.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExcelUserDTO {

    private Integer rowNo;

    private String userName;

    private String login;

    private String password;

    private String mobile;

    private String email;

    private String role;

    private String state;

    private String zone;

    private String range;

    private String district;

    private String sdpo;

    private String policeStation;

    private String hotel;

    private String address;
    
    private String comment;

}