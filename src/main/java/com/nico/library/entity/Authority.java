package com.nico.library.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;

@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Authority
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int authorityId;

    @Column(length = 30, nullable = false, unique = true)
    private String authorityName;

    private boolean visible = true;

    private boolean defaultAuthority = false;

    public Authority(String authorityName)
    {
        this.authorityName = authorityName;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Authority authority = (Authority) o;
        return authorityId == authority.authorityId;
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(authorityId);
    }
}
