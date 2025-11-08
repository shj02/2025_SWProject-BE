// src/main/java/com/mongletrip/mongletrip_backend/user/dto/UserDetailResponse.java

package com.mongletrip.mongletrip_backend.user.dto;

import lombok.Builder;
import lombok.Getter;
import java.time.LocalDate;
import java.util.List;

@Getter
@Builder
public class UserDetailResponse {
    private Long userId;
    private String email;
    private String name;
    private String phoneNumber;
    private String gender;
    private LocalDate birthdate;
    private String nationality;
    private List<String> travelStyles;
}