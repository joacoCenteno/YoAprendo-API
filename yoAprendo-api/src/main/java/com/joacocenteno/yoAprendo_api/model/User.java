package com.joacocenteno.yoAprendo_api.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String user_name;

    @Column(nullable = false, length = 100)
    private String user_surname;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    private UserRol user_rol;

    @Column(nullable = false)
    private Boolean user_active;

    @ManyToOne
    @JoinColumn(name = "cecoe_id")
    private Cecoe cecoe;
}
