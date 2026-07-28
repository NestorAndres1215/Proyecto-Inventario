package com.example.backend.service.pdf;

import com.example.backend.entity.Proveedor;
import com.example.backend.repository.ProveedorRepository;
import com.itextpdf.text.DocumentException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class ProveedorServicePDF {

	private final ProveedorRepository proveedorRepository;

	private static final List<String> HEADERS =
			Arrays.asList("ID", "Nombre", "Correo", "Direccion", "Ruc", "Telefono");

	private static final float[] COLUMN_WIDTHS = {10f, 15f, 10f, 10f, 10f, 10f};

	private static final List<Function<Proveedor, Object>> EXTRACTORES = Arrays.asList(
			Proveedor::getProveedorId,
			Proveedor::getNombre,
			Proveedor::getEmail,
			Proveedor::getDireccion,
			Proveedor::getRuc,
			Proveedor::getTelefono
	);

	public byte[] generarInformePdf() throws DocumentException {
		List<Proveedor> proveedoresActivos = proveedorRepository.findByEstadoIsTrue();
		return PdfReportGenerator.generar("Informe de Proveedores Activos", HEADERS, COLUMN_WIDTHS, proveedoresActivos, EXTRACTORES);
	}
}