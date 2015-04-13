package com.ankamagames.wakfu.client.core.game.characterInfo.monsters.action;

import com.ankamagames.wakfu.common.game.characterInfo.action.*;
import com.ankamagames.wakfu.common.game.skill.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer.character.action.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import java.nio.*;

public abstract class AbstractClientMonsterAction extends AbstractMonsterAction
{
    protected final ActionVisual m_visual;
    protected final int m_scriptId;
    private boolean m_needProgressBar;
    
    AbstractClientMonsterAction(final int actionId, final byte typeId, final SimpleCriterion criterion, final boolean criteriaOnNpc, final ActionVisual visual, final int scriptId, final long duration, final boolean needProgressBar, final boolean movePlayer, final boolean canBeTriggeredWhenBusy) {
        super(actionId, typeId, criterion, criteriaOnNpc, movePlayer, duration, canBeTriggeredWhenBusy);
        this.m_visual = visual;
        this.m_scriptId = scriptId;
        this.m_needProgressBar = needProgressBar;
    }
    
    public void run(final NonPlayerCharacter npc) {
        if (this.m_needProgressBar) {
            WakfuGameEntity.getInstance().getLocalPlayer().getActionInProgress().startMonsterAction(this.m_duration);
        }
    }
    
    public boolean isRunnable(final NonPlayerCharacter npc) {
        final LocalPlayerCharacter player = WakfuGameEntity.getInstance().getLocalPlayer();
        final CharacterInfo user = (CharacterInfo)(this.m_criteriaOnNpc ? npc : player);
        final CharacterInfo target = (CharacterInfo)(this.m_criteriaOnNpc ? player : npc);
        return this.m_criterion == null || this.m_criterion.isValid(user, target, this, user.getAppropriateContext());
    }
    
    public ActionVisual getVisual() {
        return this.m_visual;
    }
    
    public int getScriptId() {
        return this.m_scriptId;
    }
    
    protected void sendRequest(final long npcId) {
        final MonsterActionRequestMessage msg = new MonsterActionRequestMessage(npcId, this);
        WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(msg);
        this.clear();
    }
    
    @Override
    public boolean unserialize(final ByteBuffer buffer) {
        throw new UnsupportedOperationException("Pas de d\u00e9s\u00e9rialization dans le Client");
    }
}
