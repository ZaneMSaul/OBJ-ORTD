package acctMgr.model;

import java.io.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.io.File;
/**
 * 
 * @author Zane
 *
 */
public class AccountList extends AbstractModel {
    private final static BigDecimal EuroExchange = BigDecimal.valueOf(0.79);
    private final static BigDecimal YenExchange = BigDecimal.valueOf(94.1);

    public static ArrayList<Account> accountList = new ArrayList<>();
    public static ArrayList<Account> accountListYen = new ArrayList<>();
    public static ArrayList<Account> accountListEuro = new ArrayList<>();
    public static File fileLocation;

    public AccountList(File fileLoc) {
        start(fileLoc);
    }
    /**
     * 
     * @param fileLoc
     * @return
     */
    public static ArrayList start(File fileLoc) {
        fileLocation = fileLoc;
        try {
            BufferedReader br = new BufferedReader(new FileReader(fileLoc));
            String line = null;
            while ((line = br.readLine()) != null) {
                String[] tokens = line.split(" ");

                Account newAccount = new Account(tokens[0], tokens[1], new BigDecimal(tokens[2]), "Dollars", false);
                accountList.add(newAccount);
            }
            for (Account acct : accountList) {
                System.out.println(acct.getBalance());
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return accountList;
    }

    public static void end( ) {
        //ArrayList<Account> outAccounts = accountsDollars( accounts );
        FileWriter fw = null;
        try {
            fw = new FileWriter(fileLocation);
        } catch (IOException e) {
            e.printStackTrace();
        }
        BufferedWriter bw = new BufferedWriter(fw);
        PrintWriter outFile = new PrintWriter(bw);


        for (int i = 0; i < AccountList.getAccountList().size(); i++) {

            outFile.print(accountList.get(i));
        }
        outFile.println();


        outFile.close();
        System.out.println("Output file has been created: " + fileLocation);
    }

    public static ArrayList<Account> getAccountList() {
        return accountList;
    }
    /**
     * 
     * @return accountListYen
     */
    public static ArrayList<Account> accountsYen() {
        for (int i = 0; i < AccountList.getAccountList().size(); i++)
        {
            BigDecimal newValue = accountList.get(i).getBalance();
            newValue = newValue.multiply(YenExchange);
            Account newAccount = new Account(accountList.get(i).getName(), accountList.get(i).getID(), newValue, "Yen", false);
            accountListYen.add(newAccount);
        }
        return accountListYen;
    }
    /**
     * 
     * @return accountListEuro
     */
    public static ArrayList<Account> accountsEuro() {
        for (int i = 0; i < AccountList.getAccountList().size(); i++)
        {
            BigDecimal newValue = accountList.get(i).getBalance();
            newValue = newValue.multiply(EuroExchange);
            Account newAccount = new Account(accountList.get(i).getName(), accountList.get(i).getID(), newValue, "Euro" , false);
            accountListEuro.add(newAccount);
        }
        return accountListEuro;
    }
    /**
     * 
     * @param convertingAccount
     * @return
     */
    public static ArrayList<Account> accountsDollars( ArrayList<Account> convertingAccount ){
        ArrayList<Account> dollarsArray = new ArrayList<>();
        if( convertingAccount.get(0).getCurrencyType() == "Yen"){
            for (int i = 0; i < AccountList.getAccountList().size(); i++)
            {
                BigDecimal newValue = accountList.get(i).getBalance();
                newValue = newValue.divide(YenExchange);
                Account newAccount = new Account(accountList.get(i).getName(), accountList.get(i).getID(), newValue, "Dollars", false);
                dollarsArray.add(newAccount);
            }
        }
        else if( convertingAccount.get(0).getCurrencyType() == "Euro"){
            for (int i = 0; i < AccountList.getAccountList().size(); i++)
            {
                BigDecimal newValue = accountList.get(i).getBalance();
                newValue = newValue.divide(EuroExchange);
                Account newAccount = new Account(accountList.get(i).getName(), accountList.get(i).getID(), newValue, "Dollars", false);
                dollarsArray.add(newAccount);
            }
        }
        else{
            for (int i = 0; i < AccountList.getAccountList().size(); i++)
            {
                BigDecimal newValue = accountList.get(i).getBalance();
                Account newAccount = new Account(accountList.get(i).getName(), accountList.get(i).getID(), newValue, "Dollars", false);
                dollarsArray.add(newAccount);
            }
        }
        return dollarsArray;
    }
}
