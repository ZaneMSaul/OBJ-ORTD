package acctMgr.model;
import javax.swing.SwingUtilities;
import java.math.BigDecimal;
import java.math.RoundingMode;
/**
 * 
 * @author Zane
 *
 */
public class Account extends AbstractModel {
	private final static BigDecimal EuroExchange = BigDecimal.valueOf(0.79);
	private final static BigDecimal YenExchange = BigDecimal.valueOf(94.1);
	private BigDecimal balance;
	private String name;
	private String ID;
	/**
	 * holder of name, ID, and balance
	 * @param name
	 * @param ID
	 * @param balance
	 */
	private String currencyType;
	private boolean currentState;
	public Account(String name, String ID, BigDecimal balance, String currency, boolean changeState){
		this.name = name;
		this.ID = ID;
		this.balance = balance;
		this.balance.setScale(2, RoundingMode.HALF_UP);
		this.currencyType = currency;
		this.currentState = changeState;
	}
	public String getCurrencyType( ){
		return currencyType;
	}
	public void setCurrencyType( String newCurrency){
		currencyType = newCurrency;
	}
	/**
	 * gets the name read in
	 * @returns name
	 */
	public String getName(){
		return name;
	}
	/**
	 * gets ID read in
	 * @return ID
	 */
	public String getID(){
		return ID;
	}
	/**
	 * Getter for balance
	 */
	public BigDecimal getBalance(){return balance;}
	public synchronized void deposit(BigDecimal amount) {
		balance = balance.add(amount);
		
		final ModelEvent me = new ModelEvent(ModelEvent.EventKind.BalanceUpdate, balance, AgentStatus.NA);

		SwingUtilities.invokeLater(
				new Runnable() {
				    public void run() {
				    	notifyChanged(me);
				    }
				});
		notifyAll();
	}
	/**
	 * Used to withdraw
	 * @param amount
	 * @throws OverdrawException
	 */
	public void updateAllWtihdraw( BigDecimal amount) throws OverdrawException{
			if (currencyType == "Yen") {

					BigDecimal newUSB = AccountList.getAccountList().get(Integer.parseInt(ID) - 1).balance.add(BigDecimal.ZERO);
					newUSB = newUSB.subtract(amount.divide(YenExchange));
					if (newUSB.signum() < 0) throw new OverdrawException(newUSB);
					AccountList.getAccountList().get(Integer.parseInt(ID) - 1).withdraw(amount.divide(YenExchange));

					BigDecimal newEuroB = AccountList.accountListEuro.get(Integer.parseInt(ID) - 1).balance.add(BigDecimal.ZERO);
					newEuroB = newEuroB.subtract(amount.divide(YenExchange).multiply(EuroExchange));
					if (newEuroB.signum() < 0) throw new OverdrawException(newEuroB);
					AccountList.getAccountList().get(Integer.parseInt(ID) - 1).withdraw(amount.divide(YenExchange).multiply(EuroExchange));

			} else if (currencyType == "Euro") {
				BigDecimal newB = AccountList.getAccountList().get(Integer.parseInt(ID) - 1).balance.add(BigDecimal.ZERO);
				newB = newB.subtract(amount);
				if(newB.signum() < 0) throw new OverdrawException(newB);
				AccountList.getAccountList().get(Integer.parseInt(ID) - 1).withdraw(amount.divide(EuroExchange));
			}
	}
	
	public synchronized void withdraw(BigDecimal amount) throws OverdrawException {
		BigDecimal newB = balance.add(BigDecimal.ZERO);
		newB = newB.subtract(amount);
		if(newB.signum() < 0) throw new OverdrawException(newB);
		balance = newB;
		final ModelEvent me = new ModelEvent(ModelEvent.EventKind.BalanceUpdate, balance, AgentStatus.NA);
		//updateAllWtihdraw( amount );
		SwingUtilities.invokeLater(
				new Runnable() {
				    public void run() {
				    	notifyChanged(me);
				    }
				});
	}
	
	public synchronized void autoWithdraw(BigDecimal amount, Agent ag) throws InterruptedException {
			while(balance.subtract(amount).signum() < 0) {
				ag.setStatus(AgentStatus.Blocked);
				//System.out.println("autoWithdraw blocking");
				wait();
			}
			if(ag.getStatus() == AgentStatus.Paused) return;
			ag.setStatus(AgentStatus.Running);
					
			balance = balance.subtract(amount);
			final ModelEvent me = new ModelEvent(ModelEvent.EventKind.BalanceUpdate, this.balance, AgentStatus.Running);
			SwingUtilities.invokeLater(
				new Runnable() {
				    public void run() {
				    	notifyChanged(me);
				    }
				});
	}
	////////////////////////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * Functions for working with Euro Currency
	 * @return
	 */
	public BigDecimal getEuroBalance() {
		BigDecimal bal = balance.add(BigDecimal.ZERO);
		bal.setScale(2, RoundingMode.HALF_UP);
		bal = bal.multiply(EuroExchange);

		return bal;
	}
	public void withdrawEuro(BigDecimal amount) throws OverdrawException{

		amount = amount.divide(EuroExchange, 2, RoundingMode.CEILING);

		BigDecimal newB = balance.add(BigDecimal.ZERO);
		newB = newB.subtract(amount);
		if(newB.signum() < 0) throw new OverdrawException(newB);
		balance = newB;

		final ModelEvent me = new ModelEvent(ModelEvent.EventKind.BalanceUpdate, balance, AgentStatus.NA);

		SwingUtilities.invokeLater(
				new Runnable() {
					public void run() {
						notifyChanged(me);
					}
				});
	}

	public void depositEuro(BigDecimal amount) {
		amount = amount.divide(EuroExchange, 2, RoundingMode.CEILING);
		balance = balance.add(amount);

		final ModelEvent me = new ModelEvent(ModelEvent.EventKind.BalanceUpdate, balance, AgentStatus.NA);
		SwingUtilities.invokeLater(
				new Runnable() {
					public void run() {
						notifyChanged(me);
					}
				});
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * functions for working with Yen Currency
	 * @return
	 */
	public BigDecimal getYenBalance() {
		BigDecimal bal = balance.add(BigDecimal.ZERO);
		bal.setScale(2, RoundingMode.HALF_UP);
		bal = bal.multiply(YenExchange);
		return bal;

	}
	public void withdrawYen(BigDecimal amount) throws OverdrawException {
		amount = amount.divide(YenExchange, 2, RoundingMode.CEILING);
		BigDecimal newB = balance.add(BigDecimal.ZERO);
		newB = newB.subtract(amount);
		if(newB.signum() < 0) throw new OverdrawException(newB);
		balance = newB;
		final ModelEvent me = new ModelEvent(ModelEvent.EventKind.BalanceUpdate, balance, AgentStatus.NA);

		SwingUtilities.invokeLater(
				new Runnable() {
					public void run() {
						notifyChanged(me);
					}
				});
	}

	public void depositYen(BigDecimal amount) {
		amount = amount.divide(YenExchange, 2, RoundingMode.CEILING);
		balance = balance.add(amount);

		final ModelEvent me = new ModelEvent(ModelEvent.EventKind.BalanceUpdate, balance, AgentStatus.NA);
		SwingUtilities.invokeLater(
				new Runnable() {
					public void run() {
						notifyChanged(me);
					}
				});
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public String toString() {
		return name + " " + ID + " " + balance + "\n";
	}
}

