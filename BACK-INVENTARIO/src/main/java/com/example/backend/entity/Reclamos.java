package com.example.backend.entity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "reclamos")
public class Reclamos {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "re_reclamo") // columna primaria personalizada
	private Long reclamoId;

	@Column(name = "re_asunto") // cambiado a snake_case
	private String asunto;

	@ManyToOne
	@JoinColumn(name = "re_usuario", referencedColumnName = "us_codigo")
	private Usuario usuario;

	@Column(name = "re_estado") // cambiado a snake_case
	private boolean estado;
}
