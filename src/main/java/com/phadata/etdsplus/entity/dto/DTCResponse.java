package com.phadata.etdsplus.entity.dto;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @author tanwei
 * @desc
 * @time 4/6/21 10:30 AM
 * @since 1.0.0
 */
@Data
public class DTCResponse {


    private String issuer;
    private String issuerName;
    private Integer totalCount;
    private List<ClaimResInfo> claims;

    @Data
    public static class ClaimResInfo {
        private String unionId;
        private String dtid;
        private String cid;
        private String status;
        private Map<String, Object> claim;
        private String claimSign;
    }
}
