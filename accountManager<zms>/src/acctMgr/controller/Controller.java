package acctMgr.controller;
import acctMgr.view.View;
import acctMgr.model.Model;
/**
 * 
 * @author Zane
 *
 */
public interface Controller {
	void setModel(Model model);
	Model getModel();
	View getView();
	void setView(View view);
}
