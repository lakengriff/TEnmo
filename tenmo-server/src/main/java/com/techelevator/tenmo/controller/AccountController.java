package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.services.AccountService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;

@RequestMapping("/account/")
@RestController
@PreAuthorize("isAuthenticated()")

public class AccountController {
    private AccountDao dao;
    private AccountService accountService;


    public AccountController(AccountDao dao, AccountService accountService){
        this.dao = dao;
        this.accountService = accountService;
    }

    @RequestMapping(path = "transfer-history", method = RequestMethod.GET)
    public List<Transfer> transferHistoryControl(Principal principal){
        return accountService.viewTransferHistoryService(principal.getName());
    }

    @RequestMapping(path = "balance", method = RequestMethod.GET)
    public BigDecimal viewBalanceControl(Principal principal){
        return accountService.viewBalanceService(principal.getName());
    }

    @RequestMapping(path = "pending-requests", method = RequestMethod.GET)
    public List<Transfer> pendingRequestsControl(Principal principal){
        return accountService.viewPendingTransferService(principal.getName());
    }

}
