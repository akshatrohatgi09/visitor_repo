package com.police.evisitor.dto.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BulkUploadResponse {

	private Integer totalRecords;

    private Integer successCount;

    private Integer failedCount;

    private List<FailedUserDTO> failedUsers;

}
