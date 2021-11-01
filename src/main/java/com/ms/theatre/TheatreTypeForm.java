package com.ms.theatre;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TheatreTypeForm {
    private String typeId;
    private String description;
    private Integer seatSize;
    private double price;
}
