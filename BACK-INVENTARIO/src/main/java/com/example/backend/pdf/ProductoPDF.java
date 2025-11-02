package com.example.backend.pdf;

import java.io.ByteArrayOutputStream;
import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.backend.entity.Producto;
import com.example.backend.repository.ProductoRepository;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

@Service
@RequiredArgsConstructor
public class ProductoPDF {


	private final  ProductoRepository productoRepository;

    public byte[] generarInformePdf() throws DocumentException {
		List<Producto> productosActivos = productoRepository.findByEstadoIsTrue();

		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		Document document = new Document();
		PdfWriter.getInstance(document, byteArrayOutputStream);

		document.open();

		// Crear un Paragraph para el título con estilo y alineación centrados
		Font titleFont = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD, BaseColor.BLACK);
		Paragraph title = new Paragraph("Informe de Productos Activos", titleFont);
		title.setAlignment(Element.ALIGN_CENTER);
		document.add(title);

		// Agregar espacio (párrafo vacío) debajo del título
		Paragraph emptySpace = new Paragraph(" "); // Puede personalizar el espacio aquí
		document.add(emptySpace);

		// Crear una tabla
		PdfPTable table = new PdfPTable(6); // 3 columnas para ID, Nombre y Precio
		table.setWidthPercentage(100);

		// Configurar el ancho de las columnas (en porcentaje)
		float[] columnWidths = { 10f, 15f, 10f,10f,10f,10f }; // Por ejemplo, 20% para la columna ID, 40% para Nombre, 20% para
													// Precio
		table.setWidths(columnWidths);

		// Encabezados de la tabla con estilo
		Font headerFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD, BaseColor.WHITE);
		PdfPCell headerCell;

		headerCell = new PdfPCell(new Phrase("ID", headerFont));
		headerCell.setBackgroundColor(BaseColor.DARK_GRAY);
		headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
		table.addCell(headerCell);

		headerCell = new PdfPCell(new Phrase("Nombre", headerFont));
		headerCell.setBackgroundColor(BaseColor.DARK_GRAY);
		headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
		table.addCell(headerCell);

		headerCell = new PdfPCell(new Phrase("Precio", headerFont));
		headerCell.setBackgroundColor(BaseColor.DARK_GRAY);
		headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
		table.addCell(headerCell);
		
		headerCell = new PdfPCell(new Phrase("Stock", headerFont));
		headerCell.setBackgroundColor(BaseColor.DARK_GRAY);
		headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
		table.addCell(headerCell);
		
		headerCell = new PdfPCell(new Phrase("Ubicacion", headerFont));
		headerCell.setBackgroundColor(BaseColor.DARK_GRAY);
		headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
		table.addCell(headerCell);
		
		headerCell = new PdfPCell(new Phrase("Proveedor", headerFont));
		headerCell.setBackgroundColor(BaseColor.DARK_GRAY);
		headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
		table.addCell(headerCell);
		
		// Agregar datos de productos a la tabla con estilo
		Font cellFont = new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.BLACK);
		PdfPCell cell;

		for (Producto producto : productosActivos) {
			cell = new PdfPCell(new Phrase(String.valueOf(producto.getProductoId()), cellFont));
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(cell);

			cell = new PdfPCell(new Phrase(producto.getNombre(), cellFont));
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(cell);

			cell = new PdfPCell(new Phrase(String.valueOf(producto.getPrecio()), cellFont));
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(cell);
			
			cell = new PdfPCell(new Phrase(String.valueOf(producto.getStock()), cellFont));
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(cell);
			
			cell = new PdfPCell(new Phrase(String.valueOf(producto.getUbicacion()), cellFont));
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(cell);
			cell = new PdfPCell(new Phrase(String.valueOf(producto.getProveedor().getNombre()), cellFont));
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(cell);

		}

		// Agregar la tabla al documento
		document.add(table);

		document.close();

		return byteArrayOutputStream.toByteArray();
	}
}
