package com.joacocenteno.yoAprendo_api.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
@Builder
public class Cecoe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cecoe_id;

    @Column(nullable = false, length = 150)
    private String cecoe_name;

    @Builder.Default
    @Column(nullable  = false)
    private Boolean is_active = true;
}
