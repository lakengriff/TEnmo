package com.techelevator.tenmo.services;

import com.techelevator.tenmo.dao.JdbcAccountDao;
import com.techelevator.tenmo.dao.JdbcTransferDao;
import com.techelevator.tenmo.dao.JdbcUserDao;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class TransferService {
    JdbcTransferDao jdbcTransferDao;
    JdbcAccountDao jdbcAccountDao;

    TransferService(JdbcTransferDao jdbcTransferDao, JdbcAccountDao jdbcAccountDao) {
        this.jdbcTransferDao = jdbcTransferDao;
        this.jdbcAccountDao = jdbcAccountDao;
    }

    public int createRequestService(Transfer transfer, String username){
        Account fromAccount = jdbcAccountDao.getAccountById(transfer.getAccountFromId());
        if((transfer.getTransferTypeId() == 1 && transfer.getAccountToId() == jdbcAccountDao.findAccountIdByUsername(username)) || (transfer.getTransferTypeId() == 2 && transfer.getAccountFromId() == jdbcAccountDao.findAccountIdByUsername(username))) {
            if (transfer.getAccountFromId() != transfer.getAccountToId()) {
                if (transfer.getTransferTypeId() == 1 || (fromAccount.getBalance().compareTo(transfer.getAmount()) > -1)) {
                    return jdbcTransferDao.createRequest(transfer);
                }
                return -1;
            }
            return -1;
        }
        return -1;
    }

    public boolean changeRequestStatusService(Transfer transfer, int newStatus, String username){
        if(transfer.getAccountFromId() == jdbcAccountDao.findAccountIdByUsername(username)) {
            Account fromAccount = jdbcAccountDao.getAccountById(transfer.getAccountFromId());
            if (transfer.getAccountFromId() != transfer.getAccountToId()) {
                if (newStatus == 3 || (fromAccount.getBalance().compareTo(transfer.getAmount()) > -1)) {
                    return jdbcTransferDao.changeRequestStatus(transfer, newStatus);
                }
                return false;
            }
            return false;
        }
        return false;
    }
}
