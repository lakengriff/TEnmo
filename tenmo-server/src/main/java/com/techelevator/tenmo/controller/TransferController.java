package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.JdbcTransferDao;
import com.techelevator.tenmo.services.TransferService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/account/{username}/")
@RestController
@PreAuthorize("isAuthenticated()")

public class TransferController {
    JdbcTransferDao jdbcTransferDao;
    TransferService transferService;

   public  TransferController(JdbcTransferDao jdbcTransferDao,  TransferService transferService){
        this.jdbcTransferDao = jdbcTransferDao;
        this.transferService = transferService;
    }
}
