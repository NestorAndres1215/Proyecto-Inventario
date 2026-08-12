package com.example.backend.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

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
@Table(name = "entradas")
public class Entradas {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ent_codigo")
	private Long entradaId;

	@Column(name = "ent_fecha_entrada", nullable = false)
	private LocalDate fechaEntrada;


	@Builder.Default
	@Column(name = "ent_fecha_registro", nullable = false)
	private LocalDate  fechaRegistro = LocalDate .now();

	@Column(name = "ent_estado", nullable = false)
	private String estado = "Activo";

	@Column(name = "ent_observacion")
	private String observacion;

	@ManyToOne
	@JoinColumn(name = "ent_usuario", referencedColumnName = "us_codigo", nullable = false)
	private Usuario usuario;

	@Column(name = "ent_total", precision = 10, scale = 2)
	private BigDecimal total;
}
