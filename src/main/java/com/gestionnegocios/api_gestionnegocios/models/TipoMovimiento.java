
package com.gestionnegocios.api_gestionnegocios.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Tipo de movimiento (ej: CRÉDITO, DÉBITO).
 */
@Entity
@Table(name = "tipos_movimiento")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(exclude = { "movimientos" })
public class TipoMovimiento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tipo_mov")
    @EqualsAndHashCode.Include
    private Integer id;

    @NotBlank
    @Size(max = 50)
    @Column(name = "nombre_mov", nullable = false, unique = true, length = 50)
    private String nombre;

    @Builder.Default
    @JsonIgnore
    @OneToMany(mappedBy = "tipoMovimiento")
    private Set<Movimiento> movimientos = new HashSet<>();

    @Builder.Default
    @JsonIgnore
    @OneToMany(mappedBy = "tipoMovimiento", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Concepto> conceptos = new HashSet<>();
}
