package com.liga.internship.server.domain.entity;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "user", schema = "tinder")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username")
    private String username;

    @Column(name = "age")
    private Integer age;

    @Column(name = "speciality")
    private String speciality;

    @Column(name = "description")
    private String description;

    @OneToOne
    @JoinColumn(name = "params_id", referencedColumnName = "id")
    private FilterSettingEntity filterSettingEntity;

    @Column(name = "gender")
    private String gender;

    @ManyToMany
    @JoinTable(
            name = "favorite",
            joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "favorite_id")}
    )
    private Set<UserEntity> favorites = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "favorite",
            joinColumns = {@JoinColumn(name = "favorite_id")},
            inverseJoinColumns = {@JoinColumn(name = "user_id")}
    )
    private Set<UserEntity> admirers = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "dislike",
            joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "disliked_user_id")}
    )
    private Set<UserEntity> dislikes = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "dislike",
            joinColumns = {@JoinColumn(name = "disliked_user_id")},
            inverseJoinColumns = {@JoinColumn(name = "user_id")}
    )
    private Set<UserEntity> haters = new HashSet<>();

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        UserEntity that = (UserEntity) o;
        return id != null && Objects.equals(id, that.id);
    }
}
