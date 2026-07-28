package com.example.backend.controller;

import java.io.IOException;

import com.example.backend.service.pdf.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.itextpdf.text.DocumentException;

@RestController
@RequestMapping("/pdf")
@Tag(name = "Pdf")
@RequiredArgsConstructor
public class PdfController {

    private final ProductoServicePDF pdfService;
    private final SalidaServicePDF salidasPDF;
    private final EntradaServicePDF entradasPDF;
    private final UsuarioAdministradorServicePDF administradorPDF;
    private final UsuarioOperadorServicePDF operadorPDF;
    private final ProveedorServicePDF proveedorPDF;

    @GetMapping("/generar-productos")
    public ResponseEntity<byte[]> generarInformePdfProdcutos() throws Exception {
        byte[] pdfBytes = pdfService.generarInformePdf();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("inline", "informe_productos_activos.pdf");
        return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
    }

    @GetMapping("/generar-salidas")
    public ResponseEntity<byte[]> generarInformePdfSalidas() throws Exception {
        byte[] pdfBytes = salidasPDF.generarInformePdf();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("inline", "informe_detalle_salidas_productos.pdf");
        return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
    }

    @GetMapping("/generar-entradas")
    public ResponseEntity<byte[]> generarInformePdfEntradas() throws Exception {
        byte[] pdfBytes = entradasPDF.generarInformePdf();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("inline", "informe_detalle_entradas_productos.pdf");
        return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
    }

    @GetMapping("/generar-proveedor")
    public ResponseEntity<byte[]> generarInformePdfProveedor() throws Exception {
        byte[] pdfBytes = proveedorPDF.generarInformePdf();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("inline", "informe_detalle_proveedores.pdf");
        return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
    }

    @GetMapping("/generar-administrador")
    public ResponseEntity<byte[]> generarInformePdfAdmin() throws IOException, DocumentException {
        byte[] pdfBytes = administradorPDF.generarInformePdf();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("inline", "informe_administradores.pdf");
        return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
    }

    @GetMapping("/generar-operador")
    public ResponseEntity<byte[]> generarInformePdfOperador() throws DocumentException {
        byte[] pdfBytes = operadorPDF.generarInformePdf();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("inline", "informe_operador.pdf");
        return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
    }
}

