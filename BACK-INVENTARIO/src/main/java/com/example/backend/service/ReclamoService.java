package com.example.backend.service;

import com.example.backend.entity.Reclamos;
import com.example.backend.repository.ReclamoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReclamoService {

    private final JavaMailSender javaMailSender;
    private final ReclamoRepository reclamoRepository;

    // Constantes para mensajes de correo
    private static final String ASUNTO_DISCULPAS = "Respuesta de disculpas para el reclamo #%d";
    private static final String SALUDO = "Estimado/a %s %s,\n\n";
    private static final String CUERPO_DISCULPAS = "Lamentamos profundamente los inconvenientes ocasionados por su reclamo. "
            + "Queremos ofrecerle nuestras más sinceras disculpas y asegurarle que estamos trabajando para resolver la situación lo antes posible.\n\n";
    private static final String MENSAJE_DISCULPAS = "Mensaje de disculpas: %s\n\n";
    private static final String FIRMA = "--------------------------\nAtentamente,\nEquipo de Soporte";

    // ------------------ MÉTODOS PRINCIPALES ------------------

    public List<Reclamos> obtenerTodosLosReclamos() {
        return reclamoRepository.findAll();
    }

    public Reclamos agregarReclamo(Reclamos reclamo) {
        reclamo.setEstado(true); // por defecto activo
        return reclamoRepository.save(reclamo);
    }

    public Reclamos obtenerReclamoPorId(Long id) {
        return reclamoRepository.findById(id).orElse(null);
    }

    public Reclamos actualizarReclamo(Reclamos reclamo) {
        return reclamoRepository.save(reclamo);
    }

    // ------------------ ENVIAR DISCULPAS ------------------

    public Reclamos enviarDisculpasReclamo(Long id, String mensaje) {
        Reclamos reclamo = obtenerReclamoPorId(id);
        if (reclamo == null) return null;

        String destinatario = reclamo.getUsuario().getEmail();
        String asunto = String.format(ASUNTO_DISCULPAS, id);
        String contenido = String.format(SALUDO, reclamo.getUsuario().getNombre(), reclamo.getUsuario().getApellido())
                + CUERPO_DISCULPAS
                + String.format(MENSAJE_DISCULPAS, mensaje)
                + FIRMA;

        enviarCorreo(destinatario, asunto, contenido);

        reclamo.setEstado(false);
        return actualizarReclamo(reclamo);
    }

    private void enviarCorreo(String destinatario, String asunto, String contenido) {
        SimpleMailMessage correo = new SimpleMailMessage();
        correo.setTo(destinatario);
        correo.setSubject(asunto);
        correo.setText(contenido);
        javaMailSender.send(correo);
    }

    // ------------------ ACTIVAR / DESACTIVAR ------------------

    public boolean desactivarReclamo(Long id) {
        return cambiarEstadoReclamo(id, false);
    }

    public boolean activarReclamo(Long id) {
        return cambiarEstadoReclamo(id, true);
    }

    private boolean cambiarEstadoReclamo(Long id, boolean estado) {
        Reclamos reclamo = obtenerReclamoPorId(id);
        if (reclamo != null) {
            reclamo.setEstado(estado);
            actualizarReclamo(reclamo);
            return true;
        }
        return false;
    }

    // ------------------ OBTENER POR ESTADO ------------------

    public List<Reclamos> obtenerReclamosDesactivados() {
        return reclamoRepository.findByEstadoIsFalse();
    }

    public List<Reclamos> obtenerReclamosActivados() {
        return reclamoRepository.findByEstadoIsTrue();
    }
}
