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
public class JdbcAccountDao implements AccountDao {
    private final JdbcTemplate jdbcTemplate;

    public JdbcAccountDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public BigDecimal viewCurrentBalance(String username){
        BigDecimal resultDecimal = null;
        String sql = "SELECT balance FROM account AS a JOIN tenmo_user AS u ON a.user_id = u.user_id WHERE u.username = ?";
        SqlRowSet result = jdbcTemplate.queryForRowSet(sql, username);
        if(result.next()){
            resultDecimal = result.getBigDecimal("balance");
        }
        return resultDecimal;
    }

    @Override
    public List<Transfer> viewTransferHistory(int accountId){
        List<Transfer> transferList = new ArrayList<>();
        String sql = "SELECT transfer_id, transfer_type_id, transfer_status_id, account_from, account_to, amount, transfer_id FROM transfer AS t JOIN transfer_status AS ts ON t.transfer_status_id = ts.transfer_status_id WHERE (account_from = ? OR account_to = ?) AND transfer_status = 'Approved'";
        SqlRowSet result = jdbcTemplate.queryForRowSet(sql, accountId);
        while(result.next()){
            transferList.add(mapRowToTransfer(result));
        }
        return transferList;
    }

    @Override
    public List<Transfer> viewPendingTransfers(int accountId){
        List<Transfer> transferList = new ArrayList<>();
        String sql = "SELECT transfer_id, transfer_type_id, transfer_status_id, account_from, account_to, amount, transfer_id FROM transfer AS t JOIN transfer_status AS ts ON t.transfer_status_id = ts.transfer_status_id WHERE account_from = ? AND transfer_status = 'Pending'";
        SqlRowSet result = jdbcTemplate.queryForRowSet(sql, accountId);
        while(result.next()){
            transferList.add(mapRowToTransfer(result));
        }
        return transferList;
    }

    @Override
    public List<String> viewUsersToSendTo (String userName){
        List<String> usersList = new ArrayList<>();
        String sql = "SELECT username FROM tenmo_user WHERE username != ?";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userName);
        while(results.next()){
            usersList.add(results.getString("username"));
        }
        return usersList;
    }

    private Transfer mapRowToTransfer(SqlRowSet rowSet) {
        Transfer transfer = new Transfer();
        transfer.setTransferId(rowSet.getInt("transfer_id"));
        transfer.setAccountFromId(rowSet.getInt("account_from"));
        transfer.setAccountToId(rowSet.getInt("account_to"));
        transfer.setAmount(rowSet.getBigDecimal("amount"));
        transfer.setTransferStatusId(rowSet.getInt("transfer_status_id"));
        transfer.setTransferTypeId(rowSet.getInt("transfer_type_id"));
        transfer.setTransferStatus(rowSet.getString("transfer_status_desc"));
        transfer.setTransferType(rowSet.getString("transfer_type_desc"));

        return transfer;
    }
}