
package com.gestionnegocios.api_gestionnegocios.models;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Log de auditoría para los movimientos de cuentas.
 */
@Entity
@Table(name = "movimientos_log")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class MovimientoLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_log")
    private Integer id;

    @Column(name = "id_movimiento")
    private Integer idMovimiento;

    @Column(name = "id_cuenta")
    private Integer idCuenta;

    @Column(name = "fecha_mov")
    private LocalDateTime fechaMov;

    @Column(name = "id_tipo_mov")
    private Integer idTipoMov;

    @Column(precision = 15, scale = 2)
    private BigDecimal monto;

    @Column(length = 255)
    private String descripcion;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private Accion accion;

    @Column(name = "log_timestamp")
    private LocalDateTime logTimestamp;

    @Column(name = "log_usuario_id")
    private Integer logUsuarioId;

    /**
     * Acciones posibles en el log de movimientos.
     */
    public enum Accion {
        INSERT, UPDATE, DELETE
    }
}
