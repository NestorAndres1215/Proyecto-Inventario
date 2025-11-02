package com.example.backend.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


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
@Table(name = "detalle_salida")
public class DetalleSalida {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ds_id")
	private Long detalleSalidaId;

	@Column(name = "ds_cantidad")
	private int cantidad;

	@Column(name = "ds_descripcion")
	private String descripcion;

	@ManyToOne
	@JoinColumn(name = "ds_usuario", referencedColumnName = "lg_codigo", nullable = false)
	private Usuario usuario;

	@ManyToOne
	@JoinColumn(name = "ds_producto", referencedColumnName = "pro_producto", nullable = false)
	private Producto producto;

	@ManyToOne
	@JoinColumn(name = "ds_salida", referencedColumnName = "sa_codigo", nullable = false)
	private Salidas salida;
}
