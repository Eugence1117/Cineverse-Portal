package com.ms.ticket;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Setter
@Getter
public class TicketSeatUpdateForm {
    private String ticketId;
    private String seatNo;
}
