package com.liga.internship.server.domain.dto;

import com.liga.internship.server.domain.Gender;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class UserTo {
    private Long telegramId;
    private String username;
    private Integer age = 20;
    private String description;
    private Gender gender;
    private Gender look;
}
