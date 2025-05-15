package com.edacamo.msaccounts.domain.repositories;

import com.edacamo.msaccounts.domain.entities.Movements;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface MovementsRepository extends CrudRepository<Movements, Long> {

    @Query("SELECT m FROM Movements m WHERE m.account.client.clienteId = :clientId AND m.fecha BETWEEN :startDate AND :endDate")
    List<Movements> findByAccountAndDateRange(
            @Param("clientId") String clientId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);

    @Query("SELECT m FROM Movements m WHERE m.account.id = :cuentaId ORDER BY m.fecha ASC")
    List<Movements> findByCuentaId(@Param("cuentaId") Long cuentaId);
}
