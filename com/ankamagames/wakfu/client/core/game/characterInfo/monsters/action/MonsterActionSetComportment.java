package com.ankamagames.wakfu.client.core.game.characterInfo.monsters.action;

import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.wakfu.common.game.skill.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import java.nio.*;

class MonsterActionSetComportment extends AbstractClientMonsterAction
{
    MonsterActionSetComportment(final int actionId, final byte actionTypeId, final SimpleCriterion criterion, final boolean criteriaOnNpc, final ActionVisual visual, final int scriptId, final long duration, final boolean needProgressBar, final boolean movePlayer, final boolean canBeTriggerWhenBusy) {
        super(actionId, actionTypeId, criterion, criteriaOnNpc, visual, scriptId, duration, needProgressBar, movePlayer, canBeTriggerWhenBusy);
    }
    
    @Override
    public void run(final NonPlayerCharacter npc) {
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
}
