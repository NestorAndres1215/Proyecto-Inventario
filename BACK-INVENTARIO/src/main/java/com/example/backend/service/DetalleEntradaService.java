package com.example.backend.service;

import com.example.backend.entity.DetalleEntrada;

import java.util.List;

public interface DetalleEntradaService {

    List<DetalleEntrada> crearDetalleEntrada(List<DetalleEntrada> detalles);

    DetalleEntrada obtenerPorId(Long id);

    List<DetalleEntrada> obtenerTodos();

    DetalleEntrada actualizarDetalleEntrada(Long id, DetalleEntrada detalleEntrada);

}