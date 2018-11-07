package acctMgr.controller;

import acctMgr.model.AccountList;
import acctMgr.model.Account;
import acctMgr.view.AccountListView;
import acctMgr.view.AccountView;

import java.io.File;
import java.util.ArrayList;

/**
 * 
 * @author Zane
 *
 */

public class AccountListController extends AbstractController{
/**
 * seperates string read with commas and places them into a respective file
 * @param file
 * @return
 */
    public static ArrayList createAcctList(File file) {
        ArrayList<Account> accounts = AccountList.start(file);
        return accounts;
    }
    public void buttonOperation(String opt, int index){
        ArrayList<Account> accounts = AccountList.getAccountList();
        ArrayList<Account> accountsEuro = AccountList.accountsEuro();
        ArrayList<Account> accountsYen = AccountList.accountsYen();

        if(opt == AccountView.Save) {
            AccountList.end( );
        } else if(opt == AccountListView.SaveAndExit) {
            AccountList.end( );
            AccountListView acView = (AccountListView) getView();
            acView.dispose();
        } else if (opt == AccountListView.EditInDollars) {
            accounts.get(index).setCurrencyType("Dollars");
            AccountView.accountView(accounts.get(index));
        }else if (opt == AccountListView.EditInEuros) {
            accountsEuro.get(index).setCurrencyType("Euros");
            AccountView.accountView(accountsEuro.get(index));
        }else if (opt == AccountListView.EditInYen) {
            accountsYen.get(index).setCurrencyType("Yen");
            AccountView.accountView(accountsYen.get(index));
        }
    }
}
