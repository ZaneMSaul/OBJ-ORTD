package acctMgr.model;
import java.util.List;
import java.util.ArrayList;

/**
 * 
 * @author Zane
 *
 */
public abstract class AbstractModel implements Model {
	
	private List<ModelListener> listeners = new ArrayList<ModelListener>(5);
	
	/**
	 * Notifies of an event change
	 * @param event takes in event to be notified of
	 */
	public void notifyChanged(ModelEvent event) {
		for(ModelListener ml : listeners){
			ml.modelChanged(event);
		}
	}
	/**
	 * add model listener
	 * @param l takes in listener to be added
	 */
	
	public void addModelListener(ModelListener l){
		listeners.add(l);
	}
	/**
	 * removed listener
	 * @param l takes in listener to be removed
	 */
	public void removeModelListener(ModelListener l){
		listeners.remove(l);
	}

}
