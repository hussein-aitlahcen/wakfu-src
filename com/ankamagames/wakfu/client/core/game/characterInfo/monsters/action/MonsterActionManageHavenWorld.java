package com.ankamagames.wakfu.client.core.game.characterInfo.monsters.action;

import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.wakfu.common.game.skill.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.client.network.protocol.frame.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.occupation.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;
import java.nio.*;

class MonsterActionManageHavenWorld extends AbstractClientMonsterAction
{
    MonsterActionManageHavenWorld(final int actionId, final byte actionTypeId, final SimpleCriterion criterion, final boolean criteriaOnNpc, final ActionVisual visual, final int scriptId, final long duration, final boolean needProgressBar, final boolean movePlayer, final boolean canBeTriggerWhenBusy) {
        super(actionId, actionTypeId, criterion, criteriaOnNpc, visual, scriptId, duration, needProgressBar, movePlayer, canBeTriggerWhenBusy);
    }
    
    @Override
    public void run(final NonPlayerCharacter npc) {
        super.run(npc);
        this.sendRequest(npc.getId());
        NetHavenWorldFrame.INSTANCE.setCacheOccupation(new ManageHavenWorldOccupation(UIWorldEditorFrame.getInstance()));
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
}
