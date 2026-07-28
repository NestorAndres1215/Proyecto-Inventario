package com.example.backend.service.pdf;

import com.example.backend.entity.Usuario;
import com.example.backend.service.UsuarioService;
import com.itextpdf.text.DocumentException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class UsuarioAdministradorServicePDF {

	private final UsuarioService usuarioService;

	private static final List<String> HEADERS =
			Arrays.asList("ID", "Nombre", "Apellido", "Direccion", "Telefono", "Dni");

	private static final float[] COLUMN_WIDTHS = {10f, 15f, 10f, 10f, 10f, 10f};

	private static final List<Function<Usuario, Object>> EXTRACTORES = Arrays.asList(
			Usuario::getId,
			Usuario::getNombre,
			Usuario::getApellido,
			Usuario::getDireccion,
			Usuario::getTelefono,
			Usuario::getDni
	);

	public byte[] generarInformePdf() throws DocumentException {
		List<Usuario> usuariosActivos = usuarioService.listarUsuarioAdminActivado();
		return PdfReportGenerator.generar("Informe de Usuario Administrador", HEADERS, COLUMN_WIDTHS, usuariosActivos, EXTRACTORES);
	}
}