package com.example.backend.service.pdf;

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

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.function.Function;

public final class PdfReportGenerator {

    private static final Font TITLE_FONT = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD, BaseColor.BLACK);
    private static final Font HEADER_FONT = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD, BaseColor.WHITE);
    private static final Font CELL_FONT = new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.BLACK);

    private PdfReportGenerator() {
    }

    public static <T> byte[] generar(String titulo,
                                     List<String> headers,
                                     float[] columnWidths,
                                     List<T> datos,
                                     List<Function<T, Object>> extractores) throws DocumentException {

        if (headers.size() != columnWidths.length || headers.size() != extractores.size()) {
            throw new IllegalArgumentException("headers, columnWidths y extractores deben tener el mismo tamaño");
        }

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        Document document = new Document();
        PdfWriter.getInstance(document, byteArrayOutputStream);
        document.open();

        document.add(crearTitulo(titulo));
        document.add(new Paragraph(" "));

        PdfPTable table = new PdfPTable(headers.size());
        table.setWidthPercentage(100);
        table.setWidths(columnWidths);

        agregarEncabezados(table, headers);
        agregarFilas(table, datos, extractores);

        document.add(table);
        document.close();

        return byteArrayOutputStream.toByteArray();
    }

    private static Paragraph crearTitulo(String texto) {
        Paragraph title = new Paragraph(texto, TITLE_FONT);
        title.setAlignment(Element.ALIGN_CENTER);
        return title;
    }

    private static void agregarEncabezados(PdfPTable table, List<String> headers) {
        for (String header : headers) {
            PdfPCell headerCell = new PdfPCell(new Phrase(header, HEADER_FONT));
            headerCell.setBackgroundColor(BaseColor.DARK_GRAY);
            headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(headerCell);
        }
    }

    private static <T> void agregarFilas(PdfPTable table, List<T> datos, List<Function<T, Object>> extractores) {
        for (T dato : datos) {
            for (Function<T, Object> extractor : extractores) {
                Object valor = extractor.apply(dato);
                PdfPCell cell = new PdfPCell(new Phrase(String.valueOf(valor), CELL_FONT));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);
            }
        }
    }
}