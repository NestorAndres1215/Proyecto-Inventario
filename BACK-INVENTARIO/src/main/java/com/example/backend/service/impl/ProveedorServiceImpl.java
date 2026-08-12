package com.example.backend.service.impl;

import com.example.backend.dto.request.ProveedorRequest;
import com.example.backend.entity.Proveedor;
import com.example.backend.exception.ResourceNotFoundException;
import com.example.backend.repository.ProveedorRepository;
import com.example.backend.service.ProveedorService;

import com.example.backend.mapper.ProveedorMapper;
import com.example.backend.validators.ProveedorValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProveedorServiceImpl implements ProveedorService {

    private final ProveedorRepository proveedorRepository;
    private final ProveedorValidator proveedorValidator;
    private final ProveedorMapper proveedorMapper;

    @Override
    public Proveedor crearProveedor(ProveedorRequest dto) {

        proveedorValidator.validarProveedorNuevo(dto);

        Proveedor proveedor = proveedorMapper.toEntity(dto);

        return proveedorRepository.save(proveedor);
    }

    @Override
    public Proveedor actualizarProveedor(ProveedorRequest dto) {

        Proveedor proveedor = obtenerPorId(dto.getId());

        proveedorValidator.validarProveedorActualizado(proveedor, dto);

        proveedorMapper.updateEntity(proveedor, dto);

        return proveedorRepository.save(proveedor);
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
                        new ResourceNotFoundException("Proveedor no encontrado")
                );
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
}