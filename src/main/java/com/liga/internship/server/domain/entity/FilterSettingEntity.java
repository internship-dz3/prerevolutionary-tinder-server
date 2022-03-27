package com.liga.internship.server.domain.entity;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Entity
@Table(name = "params", schema = "tinder")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class FilterSettingEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "min_age")
    @Min(value = 14)
    private Integer minAge;

    @Column(name="max_age")
    @Max(value = 100)
    private Integer maxAge;

    @Column(name = "gender")
    private String gender;

    @OneToOne(mappedBy = "filterSettingEntity")
    private UserEntity userEntity;
}
