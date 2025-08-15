
package com.gestionnegocios.api_gestionnegocios.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Tipo de cuenta (ej: BANCARIA, EFECTIVO, DIGITAL).
 */
@Entity
@Table(name = "tipos_cuenta")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(exclude = { "cuentas" })
public class TipoCuenta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tipo_cuenta")
    @EqualsAndHashCode.Include
    private Integer id;

    @NotBlank
    @Size(max = 50)
    @Column(name = "nombre_tipo", nullable = false, unique = true, length = 50)
    private String nombre;

    @Builder.Default
    @Column(name = "estado", nullable = false)
    private boolean estado = true;

    @Builder.Default
    @JsonIgnore
    @OneToMany(mappedBy = "tipoCuenta")
    private Set<Cuenta> cuentas = new HashSet<>();
}
