package com.ankamagames.wakfu.client.ui.mru.MRUActions;

import com.ankamagames.wakfu.client.core.game.interactiveElement.genericAction.*;
import com.ankamagames.framework.text.*;
import java.util.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.interactiveElement.*;

public final class MRUGenericActivableAction extends MRUInteractifMachine
{
    private ArrayList<String> errorsList;
    private String m_costText;
    private boolean m_canPay;
    private short m_actionIndex;
    private ArrayList<AbstractClientGenericAction> m_actions;
    
    public MRUGenericActivableAction() {
        super();
        this.errorsList = new ArrayList<String>();
    }
    
    @Override
    public MRUGenericActivableAction getCopy() {
        return new MRUGenericActivableAction();
    }
    
    @Override
    public boolean isEnabled() {
        return this.m_canPay && super.isEnabled();
    }
    
    @Override
    public String getLabel() {
        final String label = super.getLabel();
        final TextWidgetFormater sb = new TextWidgetFormater().append(label);
        if (this.errorsList.size() > 0) {
            for (final String error : this.errorsList) {
                sb.newLine().addColor(MRUGenericActivableAction.NOK_TOOLTIP_COLOR);
                sb.append(error);
            }
        }
        if (this.m_costText != null && this.m_costText.length() > 0) {
            sb.newLine().append(this.m_costText);
        }
        return sb.finishAndToString();
    }
    
    public void setActions(final ArrayList<AbstractClientGenericAction> actions) {
        this.m_actions = actions;
    }
    
    public void setActionIndex(final short actionIndex) {
        this.m_actionIndex = actionIndex;
    }
    
    public void setCostText(final String costText) {
        this.m_costText = costText;
    }
    
    public void setCanPay(final boolean canPay) {
        this.m_canPay = canPay;
    }
    
    @Override
    public void run() {
        if (!this.isRunnable()) {
            return;
        }
        if (this.m_actions != null) {
            for (int i = 0; i < this.m_actions.size(); ++i) {
                final AbstractClientGenericAction action = this.m_actions.get(i);
                action.execute();
            }
        }
        super.run();
    }
    
    @Override
    protected InteractiveElementAction getUsableAction() {
        return InteractiveElementAction.valueOf(this.m_actionIndex);
    }
}
