package com.ankamagames.wakfu.client.network.protocol.frame.fight.messageHandler.inFight;

import com.ankamagames.wakfu.client.core.game.fight.actionsOperations.*;

final class FightCreationData
{
    public boolean m_fightIsGonnaFinish;
    public boolean m_fightCreation;
    public final CreationActionSequenceOperations m_creationActionSequenceOperations;
    public static FightCreationData INSTANCE;
    
    private FightCreationData() {
        super();
        this.m_fightCreation = false;
        this.m_creationActionSequenceOperations = new CreationActionSequenceOperations();
    }
    
    static {
        FightCreationData.INSTANCE = new FightCreationData();
    }
}
