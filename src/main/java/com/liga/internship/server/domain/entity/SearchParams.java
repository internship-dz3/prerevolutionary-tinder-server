package com.liga.internship.server.domain.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "search-params", schema = "tinder")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class SearchParams {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "min_age")
    private Integer minAge;

    @Column(name="max_age")
    private Integer maxAge;

    @Column(name = "gender")
    private String gender;
}
