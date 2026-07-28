package com.example.backend.entity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;
import java.time.LocalDate;

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

	@Column(name = "prov_nombre", nullable = false)
	private String nombre;

	@Column(name = "prov_ruc", nullable = false, length = 11)
	private String ruc;

	@Column(name = "prov_direccion")
	private String direccion;

	@Column(name = "prov_telefono", length = 15)
	private String telefono;

	@Column(name = "prov_email", length = 100)
	private String email;

	@Column(name = "prov_contacto")
	private String contacto; // persona de contacto

	@Column(name = "prov_estado", nullable = false)
	private boolean estado;

	@Column(name = "prov_fecha_registro", nullable = false)
	private LocalDate fechaRegistro;
}
