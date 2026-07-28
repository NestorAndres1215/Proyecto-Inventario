package com.example.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "rol")
public class Rol {

    @Id
    @Column(name = "tr_codigo", nullable = false, unique = true, length = 4)
    private Long codigo;

    @Column(name = "tr_nombre", nullable = false, length = 100)
    private String nombre;
}
