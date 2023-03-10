package com.techelevator.tenmo;

import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import com.techelevator.tenmo.model.UserCredentials;
import com.techelevator.tenmo.services.AccountService;
import com.techelevator.tenmo.services.AuthenticationService;
import com.techelevator.tenmo.services.ConsoleService;
import com.techelevator.tenmo.services.TransferService;

import java.util.Map;

public class App {

    private static final String API_BASE_URL = "http://localhost:8080/";

    private final ConsoleService consoleService = new ConsoleService();
    private final AuthenticationService authenticationService = new AuthenticationService(API_BASE_URL);
    private final AccountService accountService = new AccountService(API_BASE_URL);
    private final TransferService transferService = new TransferService(API_BASE_URL);

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
        int accountId = accountService.getAccountId();
        System.out.println("------------------------------------------- \n Transfers \n ID          From/To                 Amount \n -------------------------------------------");
        for(Transfer transfer : transfers){
            if(accountId == transfer.getAccountToId()) {
                System.out.println(transfer.getTransferId() + "          From: " + transfer.getAccountFromId() + "          " + "$" + transfer.getAmount());
            } else if (accountId == transfer.getAccountFromId()){
                System.out.println(transfer.getTransferId() + "          To: " + transfer.getAccountToId() + "          " + "$" + transfer.getAmount());
            }
        }int targetId = consoleService.promptForInt("Please enter transfer ID to view details (0 to cancel): ");
	}

	private void viewPendingRequests() {
        Transfer[] requests = accountService.viewPendingRequests();
        System.out.println("-------------------------------------------\n" +
                "Pending Transfers\n" +
                "ID          To                     Amount\n" +
                "-------------------------------------------");
        for(Transfer request: requests){
            System.out.println(request.getTransferId() + "          " + request.getAccountToId() + "          " + "$" + request.getAmount());
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

    }

}

// TODO: add additional functionality to views (extra transfer details and accepting/rejecting requests)
