package com.liga.internship.server.domain.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class UserTo {
    private Long id;
    private String username;
    private Integer age;
    private String description;
    private String gender;
}
