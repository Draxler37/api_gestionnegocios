package com.gestionnegocios.api_gestionnegocios.repository;

import com.gestionnegocios.api_gestionnegocios.models.Movimiento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Repositorio para la entidad Movimiento.
 */
@Repository
public interface MovimientoRepository extends JpaRepository<Movimiento, Integer> {
    List<Movimiento> findByCuentaId(Integer cuentaId);

    boolean existsByConceptoId(Integer conceptoId);

    @Query("SELECT m FROM Movimiento m WHERE " +
            "m.cuenta.id = :idCuenta AND " +
            "m.concepto.id = :idConcepto AND " +
            "(:fechaInicio IS NULL OR m.fechaMovimiento >= :fechaInicio) AND " +
            "(:fechaFin IS NULL OR m.fechaMovimiento <= :fechaFin) AND " +
            "(:montoMaximo IS NULL OR m.monto <= :montoMaximo)")
    List<Movimiento> findByFilters(
            @Param("idCuenta") Integer idCuenta,
            @Param("idConcepto") Integer idConcepto,
            @Param("fechaInicio") LocalDateTime fechaInicio,
            @Param("fechaFin") LocalDateTime fechaFin,
            @Param("montoMaximo") BigDecimal montoMaximo);
}
