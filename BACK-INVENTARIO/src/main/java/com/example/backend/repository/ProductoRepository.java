package com.example.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.backend.entity.Producto;
import org.springframework.data.jpa.repository.Query;


public interface ProductoRepository extends JpaRepository<Producto, Long>{
	List<Producto> findByEstadoIsTrue();
	
	List<Producto> findByEstadoIsFalse();


	// ------------------ STOCK ------------------

	// Producto con mayor stock
	@Query("SELECT p FROM Producto p ORDER BY p.stock DESC")
	List<Producto> findTopByStockDesc();

	// Producto con menor stock
	@Query("SELECT p FROM Producto p ORDER BY p.stock ASC")
	List<Producto> findTopByStockAsc();

	// ------------------ POR PROVEEDOR ------------------
	List<Producto> findByProveedorProveedorId(Long proveedorId);

	// ------------------ TOP 10 MÁS BARATOS ------------------
	List<Producto> findTop10ByOrderByPrecioAsc();

	// Opcional: solo productos activos
	List<Producto> findTop10ByEstadoIsTrueOrderByPrecioAsc();

}