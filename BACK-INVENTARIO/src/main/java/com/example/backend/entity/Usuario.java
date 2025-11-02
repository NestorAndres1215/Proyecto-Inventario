package com.example.backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Collections;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "login")
@Builder
public class Usuario implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "us_codigo",nullable = false)
    private Long id;

    @Column(name = "us_usuario",nullable = false)
    private String username;

    @Column(name = "us_contrasena")
    private String password;

    @Column(name = "us_nombre",nullable = false)
    private String nombre;

    @Column(name = "us_apellido",nullable = false)
    private String apellido;

    @Column(name = "us_correo",nullable = false)
    private String email;

    @Column(name = "us_telefono",nullable = false)
    private String telefono;

    @Column(name = "us_direccion")
    private String direccion;

    @Column(name = "us_dni",nullable = false)
    private String dni;

    @Column(name = "us_edad",nullable = false)
    private Integer edad;

    @Column(name = "us_fechaNacimiento")
    private LocalDate fechaNacimiento;

    @Column(name = "us_fechaRegistro")
    private LocalDate fechaRegistro;

    @Column(name = "us_estado")
    private boolean estado;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "us_rol", referencedColumnName = "tr_codigo")
    @JsonIgnore
    private Rol rol;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(new Authority(rol.getNombre()));
    }

    @Override
    public boolean isEnabled() {
        return estado;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
}
