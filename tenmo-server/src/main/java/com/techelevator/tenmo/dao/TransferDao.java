package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;

import java.math.BigDecimal;
import java.util.List;

public interface TransferDao {

    BigDecimal viewCurrentBalance(String userName);

    List<Transfer> viewTransferHistory(String userName);

    List<Transfer> viewPendingTransfers(String userName);

    int createRequest (Transfer transfer);

    boolean acceptRequest (Transfer transfer);

    boolean rejectRequest(Transfer transfer);

    boolean transferMoney(Transfer transfer);

    List<Account> viewUsersToSendTo (String userName);
}
