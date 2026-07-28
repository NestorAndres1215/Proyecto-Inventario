package com.example.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

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

	@Column(name = "ds_cantidad", nullable = false)
	private int cantidad;

	@Column(name = "ds_descripcion")
	private String descripcion;

	@Column(name = "ds_precio_unitario", precision = 10, scale = 2, nullable = false)
	private BigDecimal precioUnitario;

	@Column(name = "ds_subtotal", precision = 10, scale = 2)
	private BigDecimal subtotal;

	@Column(name = "ds_stock_anterior")
	private int stockAnterior;

	@Column(name = "ds_stock_actual")
	private int stockActual;

	@ManyToOne
	@JoinColumn(name = "ds_usuario", referencedColumnName = "us_codigo", nullable = false)
	private Usuario usuario;

	@ManyToOne
	@JoinColumn(name = "ds_producto", referencedColumnName = "pro_codigo", nullable = false)
	private Producto producto;

	@ManyToOne
	@JoinColumn(name = "ds_salida", referencedColumnName = "sa_codigo", nullable = false)
	private Salidas salida;
}
