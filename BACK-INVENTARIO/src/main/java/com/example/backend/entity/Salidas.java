package com.example.backend.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "salidas")
public class Salidas {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "sa_codigo")
	private Long salidaId;

	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "UTC")
	@Column(name = "sa_fecha_salida")
	private Date fechaSalida;
}
