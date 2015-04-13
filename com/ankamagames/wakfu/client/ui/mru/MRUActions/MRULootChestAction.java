package com.ankamagames.wakfu.client.ui.mru.MRUActions;

import com.ankamagames.framework.text.*;
import java.util.*;

public final class MRULootChestAction extends MRUInteractifMachine
{
    private ArrayList<String> errorsList;
    private String m_costText;
    private boolean m_canPay;
    
    public MRULootChestAction() {
        super();
        this.errorsList = new ArrayList<String>();
    }
    
    @Override
    public MRULootChestAction getCopy() {
        return new MRULootChestAction();
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
                sb.newLine().openText().addColor(MRULootChestAction.NOK_TOOLTIP_COLOR);
                sb.append(error).closeText();
            }
        }
        if (this.m_costText != null && this.m_costText.length() > 0) {
            sb.newLine().append(this.m_costText);
        }
        return sb.finishAndToString();
    }
    
    public void addErrorText(final String error) {
        this.errorsList.add(error);
    }
    
    public void setCostText(final String costText) {
        this.m_costText = costText;
    }
    
    public void setCanPay(final boolean canPay) {
        this.m_canPay = canPay;
    }
}
