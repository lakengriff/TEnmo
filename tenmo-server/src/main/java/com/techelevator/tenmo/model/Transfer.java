package com.techelevator.tenmo.model;

import java.math.BigDecimal;

public class Transfer {
    int transferTypeId;
    int transferStatusId;
    int accountFromId;
    int accountToId;
    BigDecimal amount = new BigDecimal("0.00");
    String transferStatus;
    String transferType;

    

}
