package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;

import java.math.BigDecimal;
import java.util.List;

public interface TransferDao {

    int createRequest (Transfer transfer);

    boolean changeRequestStatus (Transfer transfer, String newStatus);

//    boolean rejectRequest(Transfer transfer);

    boolean transferMoney(Transfer transfer);

}
