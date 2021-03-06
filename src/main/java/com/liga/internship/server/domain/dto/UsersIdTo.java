package com.liga.internship.server.domain.dto;

import lombok.*;

/**
 * Transfer object
 * используется для запросов лайк/дизлайк
 */
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class UsersIdTo {
    private Long currentUserId;
    private Long processUserId;
}
