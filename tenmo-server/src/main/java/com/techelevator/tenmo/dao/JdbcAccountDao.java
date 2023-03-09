package com.techelevator.tenmo.dao;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
    public Account getAccountById(int accountId) {
        String sql = "SELECT account_id, user_id, balance FROM account WHERE account_id = ?";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, accountId);
        if (results.next()) {
            return mapRowToAccount(results);
        } else {
            return null;
        }
    }

    @Override
    public BigDecimal viewCurrentBalance(int accountId){
        BigDecimal resultDecimal = new BigDecimal("0.00");
        String sql = "SELECT balance FROM account WHERE account_id = ?";
        SqlRowSet result = jdbcTemplate.queryForRowSet(sql, accountId);
        if(result.next()){
            resultDecimal = result.getBigDecimal("balance");
        }
        return resultDecimal;
    }

    @Override
    public List<Transfer> viewTransferHistory(int accountId){
        List<Transfer> transferList = new ArrayList<>();
        String sql = "SELECT t.transfer_id, t.transfer_type_id, t.transfer_status_id, t.account_from, t.account_to, t.amount FROM transfer AS t JOIN transfer_status AS ts ON t.transfer_status_id = ts.transfer_status_id  WHERE (t.account_from = ? OR t.account_to = ?) AND ts.transfer_status_desc = 'Approved'";
        SqlRowSet result = jdbcTemplate.queryForRowSet(sql, accountId, accountId);
        while(result.next()){
            transferList.add(mapRowToTransfer(result));
        }
        return transferList;
    }

    @Override
    public List<Transfer> viewPendingTransfers(int accountId){
        List<Transfer> transferList = new ArrayList<>();
        String sql = "SELECT t.transfer_id, t.transfer_type_id, t.transfer_status_id, t.account_from, t.account_to, t.amount FROM transfer AS t JOIN transfer_status AS ts ON t.transfer_status_id = ts.transfer_status_id WHERE t.account_from = ? AND ts.transfer_status_desc = 'Pending'";
        SqlRowSet result = jdbcTemplate.queryForRowSet(sql, accountId);
        while(result.next()){
            transferList.add(mapRowToTransfer(result));
        }
        return transferList;
    }
    @Override
    public int findAccountIdByUsername(String username) {
        if (username == null) throw new IllegalArgumentException("Username cannot be null");

        int accountId;
        try {
            accountId = jdbcTemplate.queryForObject("SELECT account_id FROM account JOIN tenmo_user ON account.user_id = tenmo_user.user_id WHERE username = ?", int.class, username);
        } catch (NullPointerException | EmptyResultDataAccessException e) {
            throw new UsernameNotFoundException("User " + username + " was not found.");
        }

        return accountId;
    }


    private Transfer mapRowToTransfer(SqlRowSet rowSet) {
        Transfer transfer = new Transfer();
        transfer.setTransferId(rowSet.getInt("transfer_id"));
        transfer.setAccountFromId(rowSet.getInt("account_from"));
        transfer.setAccountToId(rowSet.getInt("account_to"));
        transfer.setAmount(rowSet.getBigDecimal("amount"));
        transfer.setTransferStatusId(rowSet.getInt("transfer_status_id"));
        transfer.setTransferTypeId(rowSet.getInt("transfer_type_id"));

        return transfer;
    }

    private Account mapRowToAccount(SqlRowSet rowSet){
        Account account = new Account();
        account.setAccountId(rowSet.getInt("account_id"));
        account.setBalance(rowSet.getBigDecimal("balance"));
        account.setUserId(rowSet.getInt("user_id"));

        return account;
    }
}