package com.edacamo.msaccounts.services;

import com.edacamo.msaccounts.domain.entities.Movements;
import com.edacamo.msaccounts.interfaces.dto.MovementRequest;
import com.edacamo.msaccounts.interfaces.dto.TransactionReportRequest;
import com.edacamo.msaccounts.interfaces.dto.TransactionReportResponse;

import java.util.List;

public interface MovementsService {

    Movements createMovimiento(MovementRequest movimientoRequest);

    List<Movements> getMovimientosByCuenta(Long movimientoId);

    public void deleteMovimiento(Long movimientoId);

    Movements updateMovimiento(Long id, Movements movimientoDetails);

    public List<TransactionReportResponse> getMovementsReport(TransactionReportRequest request);
}
