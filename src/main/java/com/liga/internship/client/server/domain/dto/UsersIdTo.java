package com.liga.internship.client.server.domain.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class UsersIdTo {
    private Long currentUserId;
    private Long favoriteUserId;
}
