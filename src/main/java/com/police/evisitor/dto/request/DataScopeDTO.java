package com.police.evisitor.dto.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DataScopeDTO {

    private Integer stateCd;
    private Integer zoneCd;
    private Integer rangeCd;
    private Integer districtCd;
    private Integer sdpoCd;
    private Integer psCd;
    private Long hotelCd;

    private Long loginRoleId;
}