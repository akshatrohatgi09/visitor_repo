package com.police.evisitor.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HotelTypeResponseDTO {

	private Long hotelTypeId;

	private String hotelTypeName;

}