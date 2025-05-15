package com.edacamo.msaccounts.interfaces.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class TransactionReportRequest {

    private String clientId;
    private Integer accountId;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
}
