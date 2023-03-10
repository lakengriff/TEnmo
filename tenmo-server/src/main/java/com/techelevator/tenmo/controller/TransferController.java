package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.JdbcTransferDao;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.services.TransferService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.security.Principal;

@RequestMapping("/account/transfer/")
@RestController
@PreAuthorize("isAuthenticated()")

public class TransferController {
    JdbcTransferDao jdbcTransferDao;
    TransferService transferService;

   public  TransferController(JdbcTransferDao jdbcTransferDao,  TransferService transferService){
        this.jdbcTransferDao = jdbcTransferDao;
        this.transferService = transferService;
    }

    @RequestMapping(path = "", method = RequestMethod.POST)
    public int createRequestControl(@Valid @RequestBody Transfer transfer, Principal principal){
       return transferService.createRequestService(transfer, principal.getName());
    }

    @RequestMapping(path = "", method = RequestMethod.PUT)
    public boolean changeRequestStatusControl(@Valid @RequestBody Transfer transfer, int newStatus, Principal principal){
       return transferService.changeRequestStatusService(transfer, principal.getName());
    }
}
