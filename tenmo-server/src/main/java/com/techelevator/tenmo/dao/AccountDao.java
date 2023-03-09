package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;

import java.math.BigDecimal;
import java.util.List;

public interface AccountDao {

    BigDecimal viewCurrentBalance(int acountId);

    List<Transfer> viewTransferHistory(int accountId);

    List<Transfer> viewPendingTransfers(int accountId);

    public int findAccountIdByUsername(String username);

}
