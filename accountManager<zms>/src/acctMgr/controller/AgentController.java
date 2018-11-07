package acctMgr.controller;

import acctMgr.model.Agent;


import acctMgr.model.Account;
import acctMgr.model.DepositAgent;
import acctMgr.model.WithdrawAgent;
import acctMgr.view.AgentView;

import javax.swing.SwingUtilities;
/**
 * @author Zane
 * 
 * AgentControler extends AbstractController
 */
public class AgentController extends AbstractController {
/**
 * registers users choice	
 * @param opt takes choice of the user
 */
	public void operation(String opt) {
		if(opt == AgentView.Pause) {
			((Agent)getModel()).onPause();
			//((AgentView)getView()).setPaused(true);
		} else if(opt == AgentView.Resume) {
			((Agent)getModel()).onResume();
			//((AgentView)getView()).setPaused(false);
		} else if(opt == AgentView.Dismiss) {
			Agent ag = (Agent)getModel();
			AgentView agView = (AgentView)getView();
			if(ag instanceof WithdrawAgent) {
				Account account = ag.getAccount();
				account.removeModelListener(agView);
			}
			ag.finish();
			agView.dispose();
		}
	}
}
