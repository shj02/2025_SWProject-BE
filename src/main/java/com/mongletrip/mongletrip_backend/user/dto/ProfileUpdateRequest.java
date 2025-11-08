// src/main/java/com/mongletrip/mongletrip_backend/user/dto/ProfileUpdateRequest.java

package com.mongletrip.mongletrip_backend.user.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class ProfileUpdateRequest {
    private String name;
    private String phoneNumber;
    private String gender;
    private LocalDate birthdate;
    private String nationality;
}