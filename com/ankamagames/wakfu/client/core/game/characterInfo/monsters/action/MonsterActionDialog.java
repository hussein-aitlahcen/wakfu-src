package com.ankamagames.wakfu.client.core.game.characterInfo.monsters.action;

import org.apache.log4j.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.wakfu.common.game.skill.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.common.account.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import java.nio.*;

class MonsterActionDialog extends AbstractClientMonsterAction
{
    private static final Logger m_logger;
    
    MonsterActionDialog(final int actionId, final byte actionTypeId, final SimpleCriterion criterion, final boolean criteriaOnNpc, final ActionVisual visual, final int scriptId, final long duration, final boolean needProgressBar, final boolean movePlayer, final boolean canBeTriggerWhenBusy) {
        super(actionId, actionTypeId, criterion, criteriaOnNpc, visual, scriptId, duration, needProgressBar, movePlayer, canBeTriggerWhenBusy);
    }
    
    @Override
    public void run(final NonPlayerCharacter npc) {
        final LocalPlayerCharacter player = WakfuGameEntity.getInstance().getLocalPlayer();
        if (!WakfuAccountPermissionContext.SUBSCRIBER_ZONE.hasPermission(player)) {
            MonsterActionDialog.m_logger.error((Object)"Impossible pour un joueur non abonn\u00e9 de d\u00e9marrer une action de dialogue");
            return;
        }
        super.run(npc);
        this.sendRequest(npc.getId());
    }
    
    @Override
    public boolean serialize(final ByteBuffer buffer) {
        return true;
    }
    
    @Override
    public int serializedSize() {
        return 0;
    }
    
    @Override
    public void clear() {
    }
    
    static {
        m_logger = Logger.getLogger((Class)MonsterActionDialog.class);
    }
}
