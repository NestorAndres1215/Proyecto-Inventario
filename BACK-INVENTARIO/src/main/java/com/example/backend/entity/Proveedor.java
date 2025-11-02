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
	@Column(name = "prov_codigo")
	private Long proveedorId;

	@Column(name = "prov_nombre")
	private String nombre;

	@Column(name = "prov_ruc")
	private String ruc;

	@Column(name = "prov_direccion")
	private String direccion;

	@Column(name = "prov_telefono")
	private String telefono;

	@Column(name = "prov_email")
	private String email;

	@Column(name = "prov_estado")
	private boolean estado;
}
