package com.ankamagames.wakfu.client.ui.mru.MRUActions;

import gnu.trove.*;
import com.ankamagames.wakfu.common.account.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.framework.text.*;
import com.ankamagames.wakfu.client.core.*;

public class MRUActionWithCost extends MRUInteractifMachine
{
    public static final int ENABLED = 0;
    public static final int IS_NOT_SUBSCRIBED = 1;
    public static final int NO_NATION = 2;
    protected TIntHashSet m_disabledReason;
    private String m_costText;
    private boolean m_canPay;
    private String m_lockText;
    private boolean m_isLocked;
    
    public MRUActionWithCost() {
        super();
        this.m_disabledReason = new TIntHashSet();
    }
    
    @Override
    public MRUActionWithCost getCopy() {
        return new MRUActionWithCost();
    }
    
    @Override
    public boolean isEnabled() {
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        if (!WakfuAccountPermissionContext.SUBSCRIBER_ZONE.hasPermission(localPlayer)) {
            this.m_disabledReason.add(1);
            return false;
        }
        return this.m_canPay && !this.m_isLocked && super.isEnabled();
    }
    
    @Override
    public String getLabel() {
        final String label = super.getLabel();
        final TextWidgetFormater sb = new TextWidgetFormater().append(label);
        if (this.m_disabledReason.contains(2)) {
            sb.newLine().addColor(MRUActionWithCost.NOK_TOOLTIP_COLOR).append(WakfuTranslator.getInstance().getString("error.playerNoNation")).closeText();
        }
        if (this.m_costText != null && !this.m_costText.isEmpty()) {
            sb.newLine().append(this.m_costText);
        }
        if (this.m_lockText != null && !this.m_lockText.isEmpty()) {
            sb.newLine().append(this.m_lockText);
        }
        return sb.finishAndToString();
    }
    
    public void setCostText(final String costText) {
        this.m_costText = costText;
    }
    
    public void setCanPay(final boolean canPay) {
        this.m_canPay = canPay;
    }
    
    public void setLocked(final boolean locked) {
        this.m_isLocked = locked;
    }
    
    public void setLockText(final String lockText) {
        this.m_lockText = lockText;
    }
}
