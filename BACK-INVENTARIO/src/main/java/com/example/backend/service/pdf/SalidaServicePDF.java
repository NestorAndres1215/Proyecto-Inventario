package com.example.backend.service.pdf;

import com.example.backend.entity.DetalleSalida;
import com.example.backend.repository.Detalle_SalidaRepository;
import com.itextpdf.text.DocumentException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class SalidaServicePDF {

	private final Detalle_SalidaRepository salidaRepository;

	private static final List<String> HEADERS =
			Arrays.asList("ID", "Nombre", "Cantidad", "Descripcion", "Fecha Salida", "Usuario");

	private static final float[] COLUMN_WIDTHS = {10f, 15f, 10f, 10f, 10f, 10f};

	private static final List<Function<DetalleSalida, Object>> EXTRACTORES = Arrays.asList(
			DetalleSalida::getDetalleSalidaId,
			detalle -> detalle.getProducto().getNombre(),
			DetalleSalida::getCantidad,
			DetalleSalida::getDescripcion,
			detalle -> detalle.getSalida().getFechaSalida(),
			detalle -> detalle.getUsuario().getNombre()
	);

	public byte[] generarInformePdf() throws DocumentException {
		List<DetalleSalida> salidas = salidaRepository.findAll();
		return PdfReportGenerator.generar("Reporte Salidas Productos", HEADERS, COLUMN_WIDTHS, salidas, EXTRACTORES);
	}
}