package com.example.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "salidas")
public class Salidas {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sa_codigo")
    private Long salidaId;

    @Column(name = "sa_fecha_salida")
    private LocalDate fechaSalida;

    @Column(name = "sa_observacion")
    private String observacion;

    @Column(name = "sa_estado", nullable = false)
    private String estado  = "Activo";

    @Column(name = "sa_total", precision = 10, scale = 2)
    private BigDecimal total;

    @ManyToOne
    @JoinColumn(name = "sa_usuario", referencedColumnName = "us_codigo", nullable = false)
    private Usuario usuario;
}
