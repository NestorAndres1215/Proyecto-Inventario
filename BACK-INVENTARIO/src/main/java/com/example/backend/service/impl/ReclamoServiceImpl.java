package com.example.backend.service.impl;

import com.example.backend.dto.request.ReclamosRequest;
import com.example.backend.entity.Reclamos;
import com.example.backend.entity.Usuario;
import com.example.backend.exception.ResourceNotFoundException;
import com.example.backend.mapper.ReclamoMapper;
import com.example.backend.repository.ReclamoRepository;
import com.example.backend.service.ReclamoService;
import com.example.backend.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ReclamoServiceImpl implements ReclamoService {

    private final JavaMailSender mailSender;
    private final ReclamoRepository reclamoRepository;
    private final UsuarioService usuarioService;
    private final ReclamoMapper reclamoMapper;

    private static final String ASUNTO_DISCULPAS =
            "Respuesta de disculpas para el reclamo #%d";

    private static final String SALUDO =
            "Estimado/a %s %s,\n\n";

    private static final String CUERPO_DISCULPAS =
            "Lamentamos profundamente los inconvenientes ocasionados por su reclamo. "
                    + "Queremos ofrecerle nuestras más sinceras disculpas y asegurarle "
                    + "que estamos trabajando para resolver la situación lo antes posible.\n\n";

    private static final String MENSAJE_DISCULPAS =
            "Mensaje de disculpas: %s\n\n";

    private static final String FIRMA =
            "--------------------------\n"
                    + "Atentamente,\n"
                    + "Equipo de Soporte";

    @Override
    public List<Reclamos> obtenerTodosLosReclamos() {
        return reclamoRepository.findAll();
    }

    @Override
    public Reclamos agregarReclamo(ReclamosRequest request) {

        Usuario usuario = usuarioService.listarPorId(request.getCodigo());

        Reclamos reclamo = reclamoMapper.toEntity(request, usuario);

        return guardarReclamo(reclamo);
    }

    @Override
    public Reclamos obtenerReclamoPorId(Long id) {

        return reclamoRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Reclamo no encontrado")
                );
    }

    @Override
    public Reclamos actualizarReclamo(Reclamos reclamo) {
        return guardarReclamo(reclamo);
    }

    @Override
    public Reclamos enviarDisculpasReclamo(Long id, String mensaje) {

        Reclamos reclamo = obtenerReclamoPorId(id);

        String asunto = construirAsunto(id);
        String contenido = construirContenido(reclamo, mensaje);

        enviarCorreo(reclamo.getUsuario().getEmail(), asunto, contenido);

        reclamo.setEstado(false);

        return guardarReclamo(reclamo);
    }

    @Override
    public Reclamos activarReclamo(Long id) {
        return cambiarEstadoReclamo(id, true);
    }

    @Override
    public Reclamos desactivarReclamo(Long id) {
        return cambiarEstadoReclamo(id, false);
    }

    @Override
    public Reclamos cambiarEstadoReclamo(Long id, boolean estado) {
        Reclamos reclamo = obtenerReclamoPorId(id);
        reclamo.setEstado(estado);
        return guardarReclamo(reclamo);
    }

    @Override
    public List<Reclamos> obtenerReclamosActivados() {
        return reclamoRepository.findByEstadoIsTrue();
    }

    @Override
    public List<Reclamos> obtenerReclamosDesactivados() {
        return reclamoRepository.findByEstadoIsFalse();
    }

    private Reclamos guardarReclamo(Reclamos reclamo) {
        return reclamoRepository.save(reclamo);
    }

    private String construirAsunto(Long id) {
        return String.format(ASUNTO_DISCULPAS, id);
    }

    private String construirContenido(Reclamos reclamo, String mensaje) {

        Usuario usuario = reclamo.getUsuario();

        return String.format(SALUDO, usuario.getNombre(), usuario.getApellido())
                + CUERPO_DISCULPAS
                + String.format(
                MENSAJE_DISCULPAS,
                mensaje
        )
                + FIRMA;
    }

    private void enviarCorreo(
            String destinatario,
            String asunto,
            String contenido
    ) {

        SimpleMailMessage correo = new SimpleMailMessage();

        correo.setTo(destinatario);
        correo.setSubject(asunto);
        correo.setText(contenido);

        mailSender.send(correo);
    }
}