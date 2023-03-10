package com.techelevator.tenmo.dao;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcTransferDao implements TransferDao{
    private final JdbcTemplate jdbcTemplate;

    public JdbcTransferDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public int createRequest (Transfer transfer){
        Integer newId = 0;
        String sql = "INSERT INTO transfer (transfer_type_id, transfer_status_id, account_from, account_to, amount) VALUES (?,?,?,?,?) RETURNING transfer_id";
         newId = jdbcTemplate.queryForObject(sql, Integer.class, transfer.getTransferTypeId(), transfer.getTransferStatusId(), transfer.getAccountFromId(), transfer.getAccountToId(), transfer.getAmount());
         if(transfer.getTransferTypeId() == 2){
             transferMoney(transfer);
         }
        return newId;
    }

    @Override
    public boolean changeRequestStatus (Transfer transfer){
        boolean success = false;
        try{
            String sql = "UPDATE transfer SET transfer_status_id = ? WHERE transfer_id = ?";
            jdbcTemplate.update(sql, transfer.getTransferStatusId(), transfer.getTransferId());
            if(transfer.getTransferStatusId() == 2) {
                transferMoney(transfer);
            }
            success = true;
        } catch (IllegalArgumentException e){
            System.out.println(e.getMessage());
        }
        return success;
    }

//    @Override
//    public boolean rejectRequest(Transfer transfer){
//        boolean success = false;
//        try{
//            String sql = "UPDATE transfer_status SET transfer_status_id = (SELECT transfer_status_id FROM transfer_status WHERE transfer_status_desc = 'Rejected') WHERE transaction_id = ?";
//            jdbcTemplate.update(sql, transfer.getTransferId());
//            success = true;
//        } catch (IllegalArgumentException e){
//            System.out.println(e.getMessage());
//        }
//        return success;
//    }

    @Override
    public boolean transferMoney(Transfer transfer){
        boolean success = false;
        try {
            String sql = "START TRANSACTION; UPDATE account SET balance = (balance - ?) WHERE account_id = ?; UPDATE account SET balance = (balance + ?) WHERE account_id = ?; COMMIT TRANSACTION;";
            jdbcTemplate.update(sql, transfer.getAmount(), transfer.getAccountFromId(), transfer.getAmount(), transfer.getAccountToId());
            success = true;
        } catch (IllegalArgumentException e){
            System.out.println(e.getMessage());
        }
        return success;
    }

}
