package com.example.backend.service.pdf;

import com.example.backend.entity.Producto;
import com.example.backend.repository.ProductoRepository;
import com.itextpdf.text.DocumentException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class ProductoServicePDF {

	private final ProductoRepository productoRepository;

	private static final List<String> HEADERS =
			Arrays.asList("ID", "Nombre", "Precio", "Stock", "Ubicacion", "Proveedor");

	private static final float[] COLUMN_WIDTHS = {10f, 15f, 10f, 10f, 10f, 10f};

	private static final List<Function<Producto, Object>> EXTRACTORES = Arrays.asList(
			Producto::getProductoId,
			Producto::getNombre,
			Producto::getPrecio,
			Producto::getStock,
			Producto::getUbicacion,
			producto -> producto.getProveedor().getNombre()
	);

	public byte[] generarInformePdf() throws DocumentException {
		List<Producto> productosActivos = productoRepository.findByEstadoIsTrue();
		return PdfReportGenerator.generar("Informe de Productos Activos", HEADERS, COLUMN_WIDTHS, productosActivos, EXTRACTORES);
	}
}