package com.ms.seat;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class SeatSelected {
    private String id;
    private String seatNo;
    private String transactionId;
    private String referenceTicket;
}
