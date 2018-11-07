package acctMgr.view;

import acctMgr.controller.AccountController;
import acctMgr.controller.AccountListController;
import acctMgr.controller.Controller;
import acctMgr.model.Account;
import acctMgr.model.AccountList;
import acctMgr.model.Model;
import acctMgr.model.ModelEvent;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.math.BigDecimal;
import java.nio.file.FileSystems;
import java.util.ArrayList;
import java.util.Vector;
/**
 * 
 * @author Zane
 *
 */
public class AccountListView extends JFrameView {
    public final static String Save = "Save";
    public final static String SaveAndExit = "Save and Exit";
    public final static String EditInDollars = "Edit In Dollars";
    public final static String EditInEuros = "Edit In Euros";
    public final static String EditInYen = "Edit In Yen";

    /*public AccountView createEditView(Account ac, AccountController acContr, String type) {

    }*/
    ////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * sets up window for the program
     */
    static Vector<String> accountListArray = new Vector<String>();
    private JPanel topPanel;
    private JComboBox accountListBox;
    private JComboBox getBox() {
        if (accountListBox == null){
            accountListBox = new JComboBox(accountListArray);
            accountListBox.addActionListener(handler);
        }
        return accountListBox;
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * gets content for the JPanel to show
     * @return topPanel
     */
    private JPanel getContent() {
        if(topPanel == null){
            topPanel = new JPanel();
            topPanel.add(getBox());
            topPanel.add(getSaveExitButton());
            topPanel.add(getButtonPanel(), null);
        }
        return topPanel;
    }
    private JPanel buttonPanel;
    private JPanel getButtonPanel()
    {
        if(buttonPanel == null){
            GridBagConstraints depButtonCtr = new GridBagConstraints();
            depButtonCtr.gridx = 0;
            depButtonCtr.gridy = 0;

            GridBagConstraints wButtonCtr = new GridBagConstraints();
            wButtonCtr.gridx = 1;
            wButtonCtr.gridy = 0;

            GridBagConstraints depAgButtonCtr = new GridBagConstraints();
            depAgButtonCtr.gridx = 2;
            depAgButtonCtr.gridy = 0;

            GridBagConstraints wAgButtonCtr = new GridBagConstraints();
            wAgButtonCtr.gridx = 3;
            wAgButtonCtr.gridy = 0;

            GridBagConstraints saveButtonCtr = new GridBagConstraints();
            saveButtonCtr.gridx = 2;
            saveButtonCtr.gridy = 1;

            GridBagConstraints saveAndExitButtonCtr = new GridBagConstraints();
            saveAndExitButtonCtr.gridx = 3;
            saveAndExitButtonCtr.gridy = 1;

            GridBagConstraints dolButtonCtr = new GridBagConstraints();
            dolButtonCtr.gridx = 4;
            dolButtonCtr.gridy = 1;

            GridBagConstraints eurButtonCtr = new GridBagConstraints();
            eurButtonCtr.gridx = 5;
            eurButtonCtr.gridy = 1;

            GridBagConstraints yenButtonCtr = new GridBagConstraints();
            yenButtonCtr.gridx = 6;
            yenButtonCtr.gridy = 1;

            buttonPanel = new JPanel();
            buttonPanel.setLayout(new GridBagLayout());
            buttonPanel.add(getSaveButton(), saveButtonCtr);
            buttonPanel.add(getSaveExitButton(), saveAndExitButtonCtr);
            buttonPanel.add(getEditDollarsButton(), dolButtonCtr);
            buttonPanel.add(getEditEurosButton(), eurButtonCtr);
            buttonPanel.add(getEditYenButton(), yenButtonCtr);
        }

        return buttonPanel;
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////

    private JButton saveExitButton;
    private JButton getSaveExitButton(){
        if(saveExitButton == null){
            saveExitButton = new JButton(SaveAndExit);
            saveExitButton.addActionListener(ButtonHandler);
        }
        return saveExitButton;
    }
    private JButton saveButton;
    private JButton getSaveButton(){
        if(saveButton == null){
            saveButton = new JButton(Save);
            saveButton.addActionListener(ButtonHandler);
        }
        return saveButton;
    }
    private JButton EditDollars;
    private JButton getEditDollarsButton() {
        if (EditDollars == null) {
            EditDollars = new JButton(EditInDollars);
            EditDollars.addActionListener(ButtonHandler);
        }
        return EditDollars;
    }
    private JButton EditYen;
    private JButton getEditYenButton() {
        if (EditYen == null) {
            EditYen = new JButton(EditInYen);
            EditYen.addActionListener(ButtonHandler);
        }
        return EditYen;
    }
    private JButton EditEuros;
    private JButton getEditEurosButton() {
        if (EditEuros == null) {
            EditEuros = new JButton(EditInEuros);
            EditEuros.addActionListener(ButtonHandler);
        }
        return EditEuros;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////
    private Handler handler = new Handler();
    private class Handler implements ActionListener {
        public void actionPerformed(ActionEvent evt) {
        }
    }
    private ButtonHandler ButtonHandler = new ButtonHandler();
    private class ButtonHandler implements ActionListener {
        public void actionPerformed(ActionEvent evt) {
            ((AccountListController)getController()).buttonOperation(evt.getActionCommand(), accountListBox.getSelectedIndex());
        }
    }
    public AccountListView(Model model, Controller controller) {
        super(model, controller);
        this.getContentPane().add(getContent());
        addWindowListener(new java.awt.event.WindowAdapter(){
            public void windowClosing(java.awt.event.WindowEvent evt){
                setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            }
        });
        pack();
    }
    @Override
    public void modelChanged(ModelEvent me) { }

    ////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * runs the program frame from beginning
     * @param args
     */
    public static void main(String[] args) {
        File file = new File ("./src/acctMgr/controller/data.txt");
        //final Account account = new Account("Laurie Clarke", "489032", new BigDecimal("300.00"));
        AccountList accts = new AccountList(file);
        for( int i = 0; i < AccountList.getAccountList().size(); i++){
            ArrayList anAccount = AccountList.getAccountList();
            Account acct = (Account) anAccount.get(i);
            accountListArray.addElement(acct.getName() + " " + acct.getID());
        }
        AccountListController acctsController = new AccountListController();
        acctsController.setModel(accts);
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                AccountListView acctsView = new AccountListView(accts, acctsController);
                acctsController.setView(acctsView);
                acctsView.setVisible(true);
            }
        });
    }
}
