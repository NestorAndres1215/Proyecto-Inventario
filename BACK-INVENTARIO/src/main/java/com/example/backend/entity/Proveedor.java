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
@Table(name = "proveedor")
public class Proveedor {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "pro_codigo")
	private Long proveedorId;

	@Column(name = "pro_nombre")
	private String nombre;

	@Column(name = "pro_ruc")
	private String ruc;

	@Column(name = "pro_direccion")
	private String direccion;

	@Column(name = "pro_telefono")
	private String telefono;

	@Column(name = "pro_email")
	private String email;

	@Column(name = "pro_estado")
	private boolean estado;
}
