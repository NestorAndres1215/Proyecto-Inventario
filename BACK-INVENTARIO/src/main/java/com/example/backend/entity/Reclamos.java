package com.example.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "reclamos")
public class Reclamos {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "re_reclamo")
	private Long reclamoId;

	@Column(name = "re_asunto")
	private String asunto;

	@ManyToOne
	@JoinColumn(name = "re_usuario", referencedColumnName = "us_codigo")
	private Usuario usuario;

	@Column(name = "re_estado")
	private boolean estado;

	@Column(name = "re_fecha_registro", nullable = false)
	private LocalDate fechaRegistro;

	@Column(name = "re_fecha_resolucion")
	private LocalDate fechaResolucion;
}
