package com.techelevator.tenmo;

import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import com.techelevator.tenmo.model.UserCredentials;
import com.techelevator.tenmo.services.AccountService;
import com.techelevator.tenmo.services.AuthenticationService;
import com.techelevator.tenmo.services.ConsoleService;
import com.techelevator.tenmo.services.TransferService;
import jdk.swing.interop.SwingInterOpUtils;
import org.springframework.http.converter.json.GsonBuilderUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class App {

    private static final String API_BASE_URL = "http://localhost:8080/";

    private final ConsoleService consoleService = new ConsoleService();
    private final AuthenticationService authenticationService = new AuthenticationService(API_BASE_URL);
    private final AccountService accountService = new AccountService(API_BASE_URL);
    private final TransferService transferService = new TransferService(API_BASE_URL);
    private final int TRANSFER_TYPE_REQUEST_CODE = 1;
    private final int TRANSFER_TYPE_SEND_CODE = 2;
    private final int TRANSFER_STATUS_PENDING_CODE = 1;
    private final int TRANSFER_STATUS_APPROVED_CODE = 2;
    private final int TRANSFER_STATUS_REJECTED_CODE = 3;

    private AuthenticatedUser currentUser;

    public static void main(String[] args) {
        App app = new App();
        app.run();
    }

    private void run() {
        consoleService.printGreeting();
        loginMenu();
        if (currentUser != null) {
            mainMenu();
        }
    }
    private void loginMenu() {
        int menuSelection = -1;
        while (menuSelection != 0 && currentUser == null) {
            consoleService.printLoginMenu();
            menuSelection = consoleService.promptForMenuSelection("Please choose an option: ");
            if (menuSelection == 1) {
                handleRegister();
            } else if (menuSelection == 2) {
                handleLogin();
            } else if (menuSelection != 0) {
                System.out.println("Invalid Selection");
                consoleService.pause();
            }
        }
    }

    private void handleRegister() {
        System.out.println("Please register a new user account");
        UserCredentials credentials = consoleService.promptForCredentials();
        if (authenticationService.register(credentials)) {
            System.out.println("Registration successful. You can now login.");
        } else {
            consoleService.printErrorMessage();
        }
    }

    private void handleLogin() {
        UserCredentials credentials = consoleService.promptForCredentials();
        currentUser = authenticationService.login(credentials);
        if (currentUser != null) {
            accountService.setAuthToken(currentUser.getToken());
            transferService.setAuthToken(currentUser.getToken());
        } else {
            consoleService.printErrorMessage();
        }
    }

    private void mainMenu() {
        int menuSelection = -1;
        while (menuSelection != 0) {
            consoleService.printMainMenu();
            menuSelection = consoleService.promptForMenuSelection("Please choose an option: ");
            if (menuSelection == 1) {
                viewCurrentBalance();
            } else if (menuSelection == 2) {
                viewTransferHistory();
            } else if (menuSelection == 3) {
                viewPendingRequests();
            } else if (menuSelection == 4) {
                sendBucks();
            } else if (menuSelection == 5) {
                requestBucks();
            } else if (menuSelection == 0) {
                continue;
            } else {
                System.out.println("Invalid Selection");
            }
            consoleService.pause();
        }
    }

	private void viewCurrentBalance() {
        System.out.println("Your current account balance is: $" + accountService.viewBalance());
	}

	private void viewTransferHistory() {
        Transfer[] transfers = accountService.viewTransferHistory();
        List<Integer> transferIds = new ArrayList<>();
        for(Transfer transfer : transfers){
            transferIds.add(transfer.getTransferId());
        }

        int accountId = accountService.getAccountId();
        System.out.println("------------------------------------------- \n Transfers \n ID          From/To                 Amount \n -------------------------------------------");
        for(Transfer transfer : transfers){
            if(accountId == transfer.getAccountToId()) {
                System.out.println(transfer.getTransferId() + "          From: " + transfer.getAccountFromId() + "          " + "$" + transfer.getAmount());
            } else if (accountId == transfer.getAccountFromId()){
                System.out.println(transfer.getTransferId() + "          To: " + transfer.getAccountToId() + "          " + "$" + transfer.getAmount());
            }
        }
        int targetId = -1;
        while (targetId != 0) {
            targetId = consoleService.promptForInt("Please enter transfer ID to view details (0 to cancel): ");
            if (transferIds.contains(targetId)) {
                getTransferDetails(targetId);
                targetId = 0;
            } else {
                System.out.println("Invalid ID. Please try again.");
            }
        }
	}

	private void viewPendingRequests() {
        Transfer[] requests = accountService.viewPendingRequests();
        List<Integer> transferIds = new ArrayList<>();
        for(Transfer request : requests){
            transferIds.add(request.getTransferId());
        }

        System.out.println("-------------------------------------------\n" +
                "Pending Transfers\n" +
                "ID          To                     Amount\n" +
                "-------------------------------------------");
        for(Transfer request: requests){
            System.out.println(request.getTransferId() + "          " + request.getAccountToId() + "          " + "$" + request.getAmount());
        }
        int targetId = -1;
        while (targetId != 0){
            targetId = consoleService.promptForInt("Please enter transfer ID to approve/reject (0 to cancel): ");
            if(transferIds.contains(targetId)){
                Transfer transferToUpdate = transferService.getTransferDetails(targetId);
                int pendingMenuSelection = -1;
                while(pendingMenuSelection != 0){
                    consoleService.printPendingMenu();
                    pendingMenuSelection = consoleService.promptForInt("Please choose an option: ");
                    switch (pendingMenuSelection){
                        case 1:
                            transferToUpdate.setTransferStatusId(TRANSFER_STATUS_APPROVED_CODE);
                            transferService.changeRequestStatus(transferToUpdate);
                            pendingMenuSelection = 0;
                            break;
                        case 2:
                            transferToUpdate.setTransferStatusId(TRANSFER_STATUS_REJECTED_CODE);
                            transferService.changeRequestStatus(transferToUpdate);
                            pendingMenuSelection = 0;
                            break;
                        default:
                            System.out.println("Invalid option. Please try again.");
                            break;
                    }
                }
                targetId = 0;
            } else {
                System.out.println("Invalid ID. Please try again.");
            }
        }
	}

	private void sendBucks() {
        printOtherUsers();
//        Console Service Method to Prompt for Input
	}

	private void requestBucks() {
		printOtherUsers();
//        Console Service Method to Prompt for Input
	}

    private void printOtherUsers(){
        User[] otherUsers = accountService.getOtherUsers();
        System.out.println("-------------------------------------------\n" +
                "Users\n" +
                "ID          Name\n" +
                "-------------------------------------------");
        for(User user : otherUsers){
            System.out.println(user.getId() + "         " + user.getUsername());
        }
        System.out.println("---------");
    }

    private void getTransferDetails(int transferId){
        Transfer transfer = transferService.getTransferDetails(transferId);
        System.out.println("--------------------------------------------\n" +
                "Transfer Details\n" +
                "--------------------------------------------");
        System.out.println("Id: " + transfer.getTransferId());
        System.out.println("From: " + accountService.getUsernameByAccountId(transfer.getAccountFromId()));
        System.out.println("To: " + accountService.getUsernameByAccountId(transfer.getAccountToId()));
        switch(transfer.getTransferTypeId()){
            case TRANSFER_TYPE_SEND_CODE:
                System.out.println("Type: Send");
                break;
            case TRANSFER_TYPE_REQUEST_CODE:
                System.out.println("Type: Request");
                break;
        }
        switch(transfer.getTransferStatusId()){
            case TRANSFER_STATUS_PENDING_CODE:
                System.out.println("Status: Pending");
                break;
            case TRANSFER_STATUS_APPROVED_CODE:
                System.out.println("Status: Approved");
                break;
            case TRANSFER_STATUS_REJECTED_CODE:
                System.out.println("Status: rejected");
                break;
        }
        System.out.println("Amount: $" + transfer.getAmount());
    }

}

// TODO: add additional functionality to views (extra transfer details and accepting/rejecting requests)
