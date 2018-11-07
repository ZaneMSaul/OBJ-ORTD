package acctMgr.model;
import java.math.BigDecimal;
/**
 * 
 * @author Zane
 *
 */
public class ModelEvent {
	public enum EventKind {
		BalanceUpdate, AgentStatusUpdate, AmountTransferredUpdate
	}
	private EventKind kind;
	private BigDecimal balance;
	private AgentStatus agSt;
	/**
	 * transfers types to this invocation
	 * @param kind event that is happening
	 * @param balance amount of currency being passed
	 * @param agSt status of agent being utilized
	 */
	public ModelEvent(EventKind kind, BigDecimal balance, AgentStatus agSt){
		this.balance = balance;
		this.kind = kind;
		this.agSt = agSt;
	}
	/**
	 * getter for Eventkind
	 * @return kind
	 */
	public EventKind getKind(){return kind;}
	/**
	 * gets and returns the balance
	 * @return balance
	 */
	public BigDecimal getBalance(){
		return balance;
	}
	/**
	 * gets and returns the agent status
	 * @return agSt
	 */
	public AgentStatus getAgStatus(){return agSt;}
}
