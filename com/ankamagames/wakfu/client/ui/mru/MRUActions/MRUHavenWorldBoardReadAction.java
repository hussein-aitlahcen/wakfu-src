package com.ankamagames.wakfu.client.ui.mru.MRUActions;

import com.ankamagames.wakfu.common.account.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.framework.text.*;
import com.ankamagames.wakfu.client.core.*;
import java.util.*;

public final class MRUHavenWorldBoardReadAction extends MRUGenericInteractiveAction
{
    public static final int NO_NATION = 1;
    public static final int IS_NOT_SUBSCRIBED = 2;
    private final Collection<Integer> m_disabledReasons;
    
    public MRUHavenWorldBoardReadAction() {
        super();
        this.m_disabledReasons = new ArrayList<Integer>();
    }
    
    @Override
    public MRUGenericInteractiveAction getCopy() {
        return new MRUHavenWorldBoardReadAction();
    }
    
    @Override
    public boolean isEnabled() {
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        if (!WakfuAccountPermissionContext.SUBSCRIBER.hasPermission(localPlayer)) {
            this.addDisabledReason(2);
        }
        if (localPlayer.getNationId() <= 0 && localPlayer.getTravellingNationId() > 0) {
            this.addDisabledReason(1);
        }
        return this.m_disabledReasons.isEmpty() && super.isEnabled();
    }
    
    public void addDisabledReason(final int disabledReason) {
        if (this.m_disabledReasons.contains(disabledReason)) {
            return;
        }
        this.m_disabledReasons.add(disabledReason);
    }
    
    @Override
    public String getLabel() {
        final TextWidgetFormater sb = new TextWidgetFormater().b().addColor(this.isEnabled() ? MRUHavenWorldBoardReadAction.DEFAULT_TOOLTIP_COLOR : MRUHavenWorldBoardReadAction.NOK_TOOLTIP_COLOR);
        sb.append(WakfuTranslator.getInstance().getString("informations"))._b();
        if (!this.isEnabled()) {
            for (final int reason : this.m_disabledReasons) {
                sb.newLine().addColor(MRUHavenWorldBoardReadAction.NOK_TOOLTIP_COLOR);
                switch (reason) {
                    case 1: {
                        sb.append(WakfuTranslator.getInstance().getString("error.playerNoNation"));
                        break;
                    }
                    case 2: {
                        sb.append(WakfuTranslator.getInstance().getString("error.playerNotSubscribed"));
                        break;
                    }
                }
                sb.closeText();
            }
        }
        return sb.finishAndToString();
    }
}
