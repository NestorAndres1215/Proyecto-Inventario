package com.example.backend.service.impl;

import com.example.backend.constants.AlreadyExistsMessages;
import com.example.backend.constants.NotFoundMessages;
import com.example.backend.dto.request.ProveedorRequest;
import com.example.backend.entity.Proveedor;
import com.example.backend.exception.ResourceAlreadyExistsException;
import com.example.backend.exception.ResourceNotFoundException;
import com.example.backend.repository.ProveedorRepository;
import com.example.backend.service.ProveedorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

@Service
@RequiredArgsConstructor
public class ProveedorServiceImpl implements ProveedorService {

    private final ProveedorRepository proveedorRepository;

    @Override
    public Proveedor crearProveedor(ProveedorRequest dto) {

        validarProveedorNuevo(dto);

        Proveedor proveedor = Proveedor.builder()
                .nombre(dto.getNombre())
                .ruc(dto.getRuc())
                .direccion(dto.getDireccion())
                .telefono(dto.getTelefono())
                .email(dto.getEmail())
                .contacto("")
                .fechaRegistro(LocalDate.now())
                .estado(true)
                .build();

        return proveedorRepository.save(proveedor);
    }

    @Override
    public Proveedor actualizarProveedor(ProveedorRequest dto) {

        Proveedor proveedor = obtenerPorId(dto.getId());

        validarProveedorActualizado(proveedor, dto);

        actualizarDatos(proveedor, dto);

        return proveedorRepository.save(proveedor);
    }

    private void actualizarDatos(Proveedor proveedor, ProveedorRequest dto) {
        proveedor.setNombre(dto.getNombre());
        proveedor.setRuc(dto.getRuc());
        proveedor.setDireccion(dto.getDireccion());
        proveedor.setTelefono(dto.getTelefono());
        proveedor.setEmail(dto.getEmail());
    }

    @Override
    public List<Proveedor> listarTodos() {
        return proveedorRepository.findAll();
    }

    @Override
    public List<Proveedor> listarPorRuc(String ruc) {
        return proveedorRepository.findByRuc(ruc);
    }

    @Override
    public List<Proveedor> listarPorNombre(String nombre) {
        return proveedorRepository.findByNombre(nombre);
    }

    @Override
    public List<Proveedor> listarPorTelefono(String telefono) {
        return proveedorRepository.findByTelefono(telefono);
    }

    @Override
    public List<Proveedor> listarPorEmail(String email) {
        return proveedorRepository.findByEmail(email);
    }

    @Override
    public List<Proveedor> findByEstadoIsTrue() {
        return proveedorRepository.findByEstadoIsTrue();
    }

    @Override
    public List<Proveedor> findByEstadoIsFalse() {
        return proveedorRepository.findByEstadoIsFalse();
    }

    @Override
    public Proveedor obtenerPorId(Long id) {
        return proveedorRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(NotFoundMessages.PROVEEDOR_NO_ENCONTRADO));
    }

    @Override
    public Proveedor activarProveedor(Long id) {
        return cambiarEstadoProveedor(id, true);
    }

    @Override
    public Proveedor desactivarProveedor(Long id) {
        return cambiarEstadoProveedor(id, false);
    }

    private Proveedor cambiarEstadoProveedor(Long id, boolean estado) {
        Proveedor proveedor = obtenerPorId(id);
        proveedor.setEstado(estado);
        return proveedorRepository.save(proveedor);
    }

    private void validarProveedorNuevo(ProveedorRequest dto) {

        validarUnico(proveedorRepository.existsByRuc(dto.getRuc()), AlreadyExistsMessages.RUC_YA_EXISTE);

        validarUnico(proveedorRepository.existsByEmail(dto.getEmail()), AlreadyExistsMessages.CORREO_YA_EXISTE);

        validarUnico(proveedorRepository.existsByTelefono(dto.getTelefono()), AlreadyExistsMessages.TELEFONO_YA_EXISTE);
    }

    private void validarProveedorActualizado(Proveedor proveedor, ProveedorRequest dto) {

        validarCambio(proveedor.getRuc(), dto.getRuc(), proveedorRepository::existsByRuc, AlreadyExistsMessages.RUC_YA_EXISTE);

        validarCambio(proveedor.getEmail(), dto.getEmail(), proveedorRepository::existsByEmail, AlreadyExistsMessages.CORREO_YA_EXISTE);

        validarCambio(proveedor.getTelefono(), dto.getTelefono(), proveedorRepository::existsByTelefono, AlreadyExistsMessages.TELEFONO_YA_EXISTE);
    }

    private void validarCambio(String valorActual, String nuevoValor, Predicate<String> existe, String mensaje) {

        if (nuevoValor != null && !Objects.equals(valorActual, nuevoValor) && existe.test(nuevoValor)) {
            throw new ResourceAlreadyExistsException(mensaje);
        }
    }

    private void validarUnico(boolean existe, String mensaje) {
        if (existe) {
            throw new ResourceAlreadyExistsException(mensaje);
        }
    }
}