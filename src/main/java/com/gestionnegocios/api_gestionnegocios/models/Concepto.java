
package com.gestionnegocios.api_gestionnegocios.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

/**
 * Concepto de un movimiento (ej: ALIMENTOS, INTERNET).
 */
@Entity
@Table(name = "conceptos")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Concepto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_concepto")
    @EqualsAndHashCode.Include
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_negocio", nullable = false)
    private Negocio negocio;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_tipo_mov", nullable = false)
    private TipoMovimiento tipoMovimiento;

    @NotBlank
    @Size(max = 50)
    @Column(name = "nombre_concepto", nullable = false, length = 50)
    private String nombre;

    @Size(max = 255)
    @Column(length = 255)
    private String descripcion;
}
