package com.milisong.ucs.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDeliveryAddressRequest extends BaseDto implements Serializable {
    private static final long serialVersionUID = 1855744490728039416L;

    private Long id;

    private Long userId;
    
    private Long deliveryOfficeBuildingId;

}