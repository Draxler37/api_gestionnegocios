
package com.gestionnegocios.api_gestionnegocios.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Moneda utilizada en las cuentas (ej: USD, CUP).
 */
@Entity
@Table(name = "monedas")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(exclude = { "cuentas" })
public class Moneda {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_moneda")
    @EqualsAndHashCode.Include
    private Integer id;

    @NotBlank
    @Size(max = 50)
    @Column(name = "nombre_moneda", nullable = false, unique = true, length = 50)
    private String nombre;

    @Builder.Default
    @JsonIgnore
    @OneToMany(mappedBy = "moneda")
    private Set<Cuenta> cuentas = new HashSet<>();
}
