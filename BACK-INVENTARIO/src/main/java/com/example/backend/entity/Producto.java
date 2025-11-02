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
@Table(name = "producto")
public class Producto {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "pro_codigo")
	private Long productoId;

	@Column(name = "pro_nombre")
	private String nombre;

	@Column(name = "pro_precio")
	private String precio;

	@Column(name = "pro_descripcion")
	private String descripcion;

	@Column(name = "pro_ubicacion")
	private String ubicacion;

	@Column(name = "pro_stock")
	private int stock;

	@Column(name = "pro_estado")
	private boolean estado;

	@ManyToOne
	@JoinColumn(name = "pro_proveedor", referencedColumnName = "prov_codigo")
	private Proveedor proveedor;

}

