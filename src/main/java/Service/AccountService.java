package Service;

import Model.Account;
import DAO.AccountDAO;

import java.util.List;

public class AccountService {
    private AccountDAO accountDAO;

    public AccountService() { //need because making another constructor
        this.accountDAO = new AccountDAO();
    }

    public AccountService(AccountDAO accountDAO) { //for testing with mocks
        this.accountDAO = accountDAO;
    }

    public Account addAccount(Account account) {
        // verification
        if (account.getUsername() == "") {
            System.out.println("Username cannot be empty.");
            return null;
        } else if (account.getPassword().length() < 4) {
            System.out.println("Password must be at least 4 characters.");
            return null;
        } else if (accountDAO.selectAccountByUsername(account.getUsername()) != null) {
            System.out.println("Username already exists.");
            return null;
        }

        return accountDAO.insertAccount(account);
    }

    public Account login(Account account) {
        Account foundAccount = accountDAO.selectAccountByUsername(account.getUsername());
        if (foundAccount == null) {
            System.out.println("Username does not exist");
            return null;
        } else if (foundAccount.getPassword().equals(account.getPassword())) {
            return foundAccount;
        }
        System.out.println("Username and password don't match.");
        return null;
    }

    // Can leave unimplemented since only used inside addAccount
    // public Account findAccountByUsername(String username) {
    //     return accountDAO.selectAccountByUsername(username);
    // }

    public Account findAccountByID(int id) {
        return accountDAO.selectAccountByID(id);
    }

}
