package com.ankamagames.wakfu.client.ui.mru.MRUActions;

import com.ankamagames.wakfu.common.account.*;
import com.ankamagames.wakfu.client.core.game.interactiveElement.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.common.game.respawn.*;
import com.ankamagames.framework.text.*;
import com.ankamagames.wakfu.client.core.*;

public class MRURespawnPointAction extends MRUInteractifMachine
{
    public static final int ENABLED = 0;
    public static final int IS_ALREADY_SET = 1;
    public static final int IS_ON_ENEMY_TERRITORY = 2;
    public static final int IS_NOT_SUBSCRIBED = 3;
    public static final int IS_NOT_IN_HIS_HAVEN_WORLD = 4;
    private int m_disabledReason;
    
    @Override
    public MRURespawnPointAction getCopy() {
        return new MRURespawnPointAction();
    }
    
    @Override
    public boolean isEnabled() {
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        if (!WakfuAccountPermissionContext.SUBSCRIBER_ZONE.hasPermission(localPlayer)) {
            this.m_disabledReason = 3;
            return false;
        }
        final RespawnPointPhoenix machine = (RespawnPointPhoenix)this.m_source;
        final RespawnPointHandler respawnPointHandler = localPlayer.getRespawnPointHandler();
        if (respawnPointHandler.isSelected((int)machine.getId())) {
            this.m_disabledReason = 1;
            return false;
        }
        if (localPlayer.getCitizenComportment().isNationEnemy()) {
            this.m_disabledReason = 2;
            return false;
        }
        if (localPlayer.isInHavenWorld() && !localPlayer.isInOwnHavenWorld()) {
            this.m_disabledReason = 4;
            return false;
        }
        this.m_disabledReason = 0;
        return true;
    }
    
    @Override
    public String getLabel() {
        final String label = super.getLabel();
        if (this.isEnabled()) {
            return label;
        }
        final TextWidgetFormater sb = new TextWidgetFormater().append(label).newLine();
        sb.addColor(MRURespawnPointAction.NOK_TOOLTIP_COLOR);
        switch (this.m_disabledReason) {
            case 3: {
                sb.append(WakfuTranslator.getInstance().getString("error.playerNotSubscribed"));
                break;
            }
            case 1: {
                sb.append(WakfuTranslator.getInstance().getString("desc.mru.phoenixAlreadyBound"));
                break;
            }
            case 2: {
                sb.append(WakfuTranslator.getInstance().getString("phoenix.forbidden.on.enemy.territory"));
                break;
            }
            case 4: {
                sb.append(WakfuTranslator.getInstance().getString("havenWorldForbidden"));
                break;
            }
        }
        return sb.finishAndToString();
    }
}
