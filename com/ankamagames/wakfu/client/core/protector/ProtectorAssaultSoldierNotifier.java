package com.ankamagames.wakfu.client.core.protector;

import com.ankamagames.wakfu.common.game.protector.event.*;
import com.ankamagames.wakfu.client.core.game.protector.event.*;
import com.ankamagames.wakfu.client.core.game.protector.*;
import com.ankamagames.wakfu.common.game.nation.government.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.wakfu.client.ui.*;
import com.ankamagames.wakfu.client.*;
import com.ankamagames.wakfu.client.core.preferences.*;
import com.ankamagames.baseImpl.graphics.core.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer.fight.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.xulor2.core.messagebox.*;

public final class ProtectorAssaultSoldierNotifier implements ProtectorEventListener<ProtectorEvent>
{
    public static ProtectorAssaultSoldierNotifier INSTANCE;
    
    @Override
    public void onProtectorEvent(final ProtectorEvent event) {
        final ProtectorEvents eventType = ProtectorEvents.getFromId(event.getId());
        switch (eventType) {
            case PROTECTOR_ATTACKED: {
                this.popupJoinFightIfNecessary(event);
                break;
            }
        }
    }
    
    private void popupJoinFightIfNecessary(final ProtectorEvent event) {
        final Protector protector = event.getProtector();
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        if (localPlayer.getCurrentFight() != null) {
            return;
        }
        if (localPlayer.getCitizenComportment().getNationId() != protector.getCurrentNationId()) {
            return;
        }
        if (!localPlayer.getCitizenComportment().hasJob(NationJob.SOLDIER)) {
            return;
        }
        final String inviteToJoin = WakfuTranslator.getInstance().getString("attacked.protector.joinInFight", String.format("<b>%s</b>", protector.getName()));
        final MessageBoxControler messageBoxControler = Xulor.getInstance().msgBox(inviteToJoin, WakfuMessageBoxConstants.getMessageBoxIconUrl(5), 2073L, 102, 1);
        messageBoxControler.addEventListener(new MessageBoxEventListener() {
            @Override
            public void messageBoxClosed(final int type, final String userEntry) {
                if ((type & 0x8) != 0x8) {
                    return;
                }
                final boolean locked = WakfuClientInstance.getInstance().getGamePreferences().getBooleanValue(WakfuKeyPreferenceStoreEnum.AUTO_LOCK_FIGHTS_KEY);
                final FightJoinProtectorRequestMessage message = new FightJoinProtectorRequestMessage(protector.getId(), locked);
                WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(message);
            }
        });
    }
    
    static {
        ProtectorAssaultSoldierNotifier.INSTANCE = new ProtectorAssaultSoldierNotifier();
    }
}
