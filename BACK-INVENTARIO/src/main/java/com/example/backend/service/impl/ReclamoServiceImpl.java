package com.example.backend.service.impl;

import com.example.backend.entity.Reclamos;
import com.example.backend.repository.ReclamoRepository;
import com.example.backend.service.ReclamoService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@RequiredArgsConstructor
public class ReclamoServiceImpl implements ReclamoService {

    private final JavaMailSender javaMailSender;
    private final ReclamoRepository reclamoRepository;



    @Override
    public List<Reclamos> obtenerTodosLosReclamos() {
        return reclamoRepository.findAll();
    }

    @Override
    public Reclamos enviarDisculpasReclamo(Long id, String mensaje) {
        Reclamos reclamo = obtenerReclamoPorId(id);

        String destinatario = reclamo.getUsuario().getEmail();
        String asunto = "Respuesta de disculpas para el reclamo #" + id;
        String contenido = "Estimado/a " + reclamo.getUsuario().getNombre() + reclamo.getUsuario().getApellido() + ",\n\n"
                + "Lamentamos profundamente los inconvenientes ocasionados por su reclamo. Queremos ofrecerle nuestras más sinceras disculpas y asegurarle que estamos trabajando para resolver la situación lo antes posible.\n\n"
                + "Mensaje de disculpas: " + mensaje + "\n\n" +
                "--------------------------,\n" +
                "Atentamente,\n" + "Equipo de Soporte";
        enviarCorreoDisculpas(destinatario, asunto, contenido);
        reclamo.setEstado(false);

        return actualizarReclamo(reclamo);
    }

    @Override
    public Reclamos obtenerReclamoPorId(Long id) {
        return reclamoRepository.findById(id).orElse(null);
    }

    @Override
    public Reclamos actualizarReclamo(Reclamos reclamo) {
        return reclamoRepository.save(reclamo);
    }

    public void enviarCorreoDisculpas(String destinatario, String asunto, String contenido) {
        SimpleMailMessage correo = new SimpleMailMessage();
        correo.setTo(destinatario);
        correo.setSubject(asunto);
        correo.setText(contenido);

        javaMailSender.send(correo);
    }

    @Override
    public boolean desactivarReclamo(Long id) {
        Reclamos reclamo = obtenerReclamoPorId(id);
        if (reclamo != null) {
            reclamo.setEstado(false);
            actualizarReclamo(reclamo);
            return true; // se desactivó correctamente
        }
        return false; // no existe el reclamo
    }
    @Override
    public boolean activarReclamo(Long id) {
        Reclamos reclamo = obtenerReclamoPorId(id);
        if (reclamo != null) {
            reclamo.setEstado(true);
            actualizarReclamo(reclamo);
            return true; // se activó correctamente
        }
        return false; // no existe el reclamo
    }

    @Override
    public List<Reclamos> obtenerReclamosDesactivados() {
        return reclamoRepository.findByEstadoIsFalse();
    }

    @Override
    public List<Reclamos> obtenerReclamosActivados() {
        return reclamoRepository.findByEstadoIsTrue();
    }

    @Override
    public Reclamos agregarReclamo(Reclamos reclamo) {
        reclamo.setEstado(true); // por defecto activo
        return reclamoRepository.save(reclamo);
    }


}
