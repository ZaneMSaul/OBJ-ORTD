package acctMgr.view;

import acctMgr.model.Model;
import acctMgr.model.ModelEvent;
import acctMgr.controller.Controller;
public class AppFrame extends JFrameView {
    private JFramePanel basePanel;

    public AppFrame(Model model, Controller controller) {
        super(model, controller);
    }

    @Override
    public void modelChanged(ModelEvent me) {

    }
}
