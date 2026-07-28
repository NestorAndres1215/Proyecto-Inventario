package com.example.backend.service.pdf;

import com.example.backend.entity.DetalleEntrada;
import com.example.backend.repository.Detalle_EntradaRepository;
import com.itextpdf.text.DocumentException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class EntradaServicePDF {

	private final Detalle_EntradaRepository entradaRepository;

	private static final List<String> HEADERS =
			Arrays.asList("ID", "Nombre", "Cantidad", "Descripcion", "Fecha Entrega", "Usuario");

	private static final float[] COLUMN_WIDTHS = {10f, 15f, 10f, 10f, 10f, 10f};

	private static final List<Function<DetalleEntrada, Object>> EXTRACTORES = Arrays.asList(
			DetalleEntrada::getDetalleEntradaId,
			detalle -> detalle.getProducto().getNombre(),
			DetalleEntrada::getCantidad,
			DetalleEntrada::getDescripcion,
			detalle -> detalle.getEntrada().getFechaEntrada(),
			detalle -> detalle.getUsuario().getNombre()
	);

	public byte[] generarInformePdf() throws DocumentException {
		List<DetalleEntrada> entradas = entradaRepository.findAll();
		return PdfReportGenerator.generar("Reporte Entradas Productos", HEADERS, COLUMN_WIDTHS, entradas, EXTRACTORES);
	}
}