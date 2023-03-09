package com.techelevator.dao;

import com.techelevator.tenmo.dao.JdbcAccountDao;
import com.techelevator.tenmo.dao.JdbcUserDao;
import com.techelevator.tenmo.model.Account;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import java.math.BigDecimal;

public class JdbcAccountDaoTests extends BaseDaoTests {
    protected static final Account ACCOUNT_1 = new Account(1, 1001, new BigDecimal("1500"));
    protected static final Account ACCOUNT_2 = new Account(2, 1002, new BigDecimal("2000"));
    protected static final Account ACCOUNT_3 = new Account(3, 1003, new BigDecimal("2500"));

    private JdbcUserDao sut;

    @Before
    public void setup() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        sut = new JdbcUserDao(jdbcTemplate);
    }

    @Test
    public void viewCurrentBalance_should_return_balance(){
        
    }
}
