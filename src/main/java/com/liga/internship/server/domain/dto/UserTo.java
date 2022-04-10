package com.liga.internship.server.domain.dto;

import com.liga.internship.server.domain.Gender;
import lombok.*;

/**
 * UserEntity transfer object
 * используется для связи с клиентом
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class UserTo {
    private Long telegramId;
    private String username;
    private String description;
    private Gender gender;
    private Gender look;
}
