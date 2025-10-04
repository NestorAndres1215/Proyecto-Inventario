package com.example.backend.security.repository;


import com.example.backend.security.model.Rol;
import com.example.backend.security.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, String> {

    Optional<Usuario> findByUsername(String username);

    Optional<Usuario> findById(Long id);

    Optional<Usuario> findByTelefono(String telefono);

    Optional<Usuario> findByEmail(String email);

    Optional<Usuario> findByEstadoTrue();

    Optional<Usuario> findByEstadoFalse();

    boolean existsByUsername(String username);

    boolean existsByEmail(String correo);

    boolean existsByDni(String dni);

    boolean existsByTelefono(String telefono);

    boolean existsByUsernameAndPassword(String username, String password);
    List<Usuario> findByRolAndEstado(Rol rol, Boolean estado);


    @Query(value = "SELECT * FROM login WHERE us_rol = 1 AND us_estado = 1", nativeQuery = true)
    List<Usuario> listarUsuarioAdminActivado();

    @Query(value = "SELECT * FROM login WHERE us_rol = 1 AND us_estado = 0", nativeQuery = true)
    List<Usuario> listarUsuarioAdminDesactivado();

    @Query(value = "SELECT * FROM login WHERE us_rol = 2 AND us_estado = 1", nativeQuery = true)
    List<Usuario> listarUsuarioNormalActivado();

    @Query(value = "SELECT * FROM login WHERE us_rol = 2 AND us_estado = 0", nativeQuery = true)
    List<Usuario> listarUsuarioNormalDesactivado();
}
