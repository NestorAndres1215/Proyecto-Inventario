package com.example.backend.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

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

	@Column(name = "pro_nombre", nullable = false)
	private String nombre;

	@Column(name = "pro_precio", precision = 10, scale = 2, nullable = false)
	private BigDecimal precio;

	@Column(name = "pro_descripcion")
	private String descripcion;

	@Column(name = "pro_ubicacion")
	private String ubicacion;

	@Column(name = "pro_stock", nullable = false)
	private int stock;

	@Column(name = "pro_stock_minimo")
	private int stockMinimo;

	@Column(name = "pro_estado", nullable = false)
	private boolean estado;

	@ManyToOne
	@JoinColumn(name = "pro_proveedor", referencedColumnName = "prov_codigo")
	private Proveedor proveedor;

	@Column(name = "pro_fecha_registro", nullable = false)
	private LocalDate fechaRegistro;

}

