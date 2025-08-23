package com.gestionnegocios.api_gestionnegocios.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * Entidad que representa un negocio de un usuario.
 */
@Entity
@Table(name = "negocios")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(exclude = { "cuentas" })
public class Negocio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_negocio")
    @EqualsAndHashCode.Include
    private Integer id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    @NotBlank
    @Size(max = 150)
    @Column(nullable = false, length = 150)
    private String nombre;

    @Size(max = 255)
    @Column(length = 255)
    private String descripcion;

    @Size(max = 200)
    @Column(length = 200)
    private String direccion;

    @Size(max = 20)
    @Column(length = 20)
    private String telefono;

    @Column(name = "fecha_creacion", updatable = false)
    private LocalDateTime fechaCreacion;

    @Builder.Default
    @Column(name = "estado", nullable = false)
    private boolean estado = true;

    @Builder.Default
    @JsonIgnore
    @OneToMany(mappedBy = "negocio", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Cuenta> cuentas = new HashSet<>();

    @Builder.Default
    @JsonIgnore
    @OneToMany(mappedBy = "negocio", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Concepto> conceptos = new HashSet<>();

    @Builder.Default
    @JsonIgnore
    @ManyToMany
    @JoinTable(name = "negocio_empleado", joinColumns = @JoinColumn(name = "id_negocio"), inverseJoinColumns = @JoinColumn(name = "id_usuario"))
    private Set<Usuario> empleados = new HashSet<>();

    @PrePersist
    public void prePersist() {
        if (fechaCreacion == null) {
            fechaCreacion = LocalDateTime.now();
        }
    }
}
