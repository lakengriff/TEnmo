package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;

import java.math.BigDecimal;
import java.util.List;

public interface AccountDao {

    BigDecimal viewCurrentBalance(String userName);

    List<Transfer> viewTransferHistory(int accountId);

    List<Transfer> viewPendingTransfers(int accountId);

    List<String> viewUsersToSendTo (String userName);
}
