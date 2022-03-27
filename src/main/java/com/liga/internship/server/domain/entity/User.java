package com.liga.internship.server.domain.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "user", schema = "tinder")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username")
    private String username;

    @Column(name = "age")
    private Integer age;

    @Column(name = "description")
    private String description;

    @OneToOne
    @JoinColumn(name = "params_id", referencedColumnName = "id")
    private SearchParams searchParams;

    @Column(name = "gender")
    private String gender;

    @ManyToMany
    @JoinTable(
            name = "user-favorite",
            joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "favorite_id")}
    )
    private Set<User> likes;

    @ManyToMany
    @JoinTable(
            name = "user-favorite",
            joinColumns = {@JoinColumn(name = "favorite_id")},
            inverseJoinColumns = {@JoinColumn(name = "user_id")}
    )
    private Set<User> likers;
}
