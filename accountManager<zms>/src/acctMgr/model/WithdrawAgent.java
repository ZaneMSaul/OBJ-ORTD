package acctMgr.model;

import javax.swing.SwingUtilities;
import java.math.BigDecimal;
import java.math.RoundingMode;
/**
 * 
 * @author Zane
 *
 */
public class WithdrawAgent extends AbstractModel implements Runnable, Agent {
	private Object pauseLock;
	private volatile boolean paused;
	public volatile boolean active;
	private Account account;
	private int iters;
	private BigDecimal amount;
	private BigDecimal transferred;
	private String name = new String("Default");
	private AgentStatus status;
	private volatile boolean wasBlocked;
	/**
	 * sets parameters to this invocation
	 * @param account owner of the money
	 * @param amount is the current amount of money
	 */
	public WithdrawAgent(Account account, BigDecimal amount){
		this.account = account;
		this.amount = amount;
		this.amount.setScale(2, RoundingMode.HALF_UP);
		this.transferred = BigDecimal.ZERO;
		this.transferred.setScale(2, RoundingMode.HALF_UP);
		this.status = AgentStatus.Running;
		this.wasBlocked = false;
		this.active = true;
		this.paused = false;
		this.pauseLock = new Object();
	}
	/**
	 * 
	 * @param account owner of the money
	 * @param amount of money
	 * @param iters iterator used to increment or decrement
	 */
	public WithdrawAgent(Account account, BigDecimal amount, int iters){
		this(account, amount);
		this.iters = iters;
		active = false;
	}
	/**
	 * run through until stopped or exception thrown
	 */
	public void run() {
		while(active || iters > 0) {
			synchronized (pauseLock) {
				
                while (paused) {
                    try {
                        pauseLock.wait();
                    } catch (InterruptedException e) {
                    	System.out.println("Thread " + Thread.currentThread().getName() + " interrupted");
                    }
                }
                
            }
			try {
				account.autoWithdraw(amount, this);
			}
			catch(InterruptedException e) {
            	System.out.println("Thread " + Thread.currentThread().getName() + " interrupted");
            }
			transferred = transferred.add(amount);
			iters--;
			final ModelEvent me = new ModelEvent(ModelEvent.EventKind.AmountTransferredUpdate, transferred, AgentStatus.NA);
			SwingUtilities.invokeLater(
					new Runnable() {
					    public void run() {
					    	notifyChanged(me);
					    }
					});
			try {
				Thread.sleep(300);
			}
			catch(InterruptedException ex){
				System.out.println("Thread " + Thread.currentThread().getName() + " interrupted");
			}
		}
	}
	/**
	 * gets and returns transferred amount of money
	 * 
	 */
	public BigDecimal getTransferred(){return transferred;}
	public void onPause() {
        synchronized (pauseLock) {
            paused = true;
            setStatus(AgentStatus.Paused);
        }
    }
	/**
	 * notifies when processed is resumed
	 */
    public void onResume() {
        synchronized (pauseLock) {
        	if(wasBlocked) setStatus(AgentStatus.Blocked);
        	else setStatus(AgentStatus.Running);
            paused = false;
            pauseLock.notify();
        }
    }
    /**
     * notifies when agent status is changes
     */
    public void setStatus(AgentStatus agSt) {
    	status = agSt;
    	if(status == AgentStatus.Blocked) wasBlocked = true;
    	if(status == AgentStatus.Running) wasBlocked = false;
    	final ModelEvent me = new ModelEvent(ModelEvent.EventKind.AgentStatusUpdate, BigDecimal.ZERO, agSt);
    	SwingUtilities.invokeLater(
				new Runnable() {
				    public void run() {
				    	notifyChanged(me);
				    }
				});
    }
    /**
     * @return status
     */
    public AgentStatus getStatus(){return status;}
    /**
     * sets name to this invocation
     */
    public void setName(String name) {this.name = name;}
    public String getName(){return name;}
    /**
     * gets and returns account
     * @return account
     */
    public Account getAccount(){return account;}
    public void finish(){
    	active = false;
    }
}
