package com.ankamagames.wakfu.client.network.protocol.frame.fight.messageHandler.outFight;

import gnu.trove.*;
import com.ankamagames.wakfu.client.core.action.FightBeginning.*;

final class ExternalFightCreationActions
{
    public static ExternalFightCreationActions INSTANCE;
    public final TIntArrayList m_fightIsInFightCreation;
    public final TIntObjectHashMap<LookAtTheFoeAction> m_lookAtTheFoeActions1;
    public final TIntObjectHashMap<LookAtTheFoeAction> m_lookAtTheFoeActions2;
    public final TIntObjectHashMap<LookAtTheFoeAction> m_lookAtTheFoeActions3;
    public final TIntObjectHashMap<LookAtTheFoeAction> m_lookAtTheFoeActions4;
    public final TIntObjectHashMap<PlayAnimationAction> m_playAnimationAction1;
    public final TIntObjectHashMap<PlayAnimationAction> m_playAnimationAction2;
    public final TIntObjectHashMap<TauntAction> m_tauntAction1;
    public final TIntObjectHashMap<TauntAction> m_tauntAction2;
    public final TIntObjectHashMap<DisplayCharacterCircleAction> m_displayCharacterCircleAction;
    public final TIntObjectHashMap<StartPlacementAction> m_startPlacementAction;
    public final TIntObjectHashMap<CharacterMoveWithEndedListenerAction> m_characterMoveWithEndedListenerAction;
    public final TIntObjectHashMap<CharacterTeleportAction> m_characterTeleportAction;
    
    private ExternalFightCreationActions() {
        super();
        this.m_fightIsInFightCreation = new TIntArrayList();
        this.m_lookAtTheFoeActions1 = new TIntObjectHashMap<LookAtTheFoeAction>();
        this.m_lookAtTheFoeActions2 = new TIntObjectHashMap<LookAtTheFoeAction>();
        this.m_lookAtTheFoeActions3 = new TIntObjectHashMap<LookAtTheFoeAction>();
        this.m_lookAtTheFoeActions4 = new TIntObjectHashMap<LookAtTheFoeAction>();
        this.m_playAnimationAction1 = new TIntObjectHashMap<PlayAnimationAction>();
        this.m_playAnimationAction2 = new TIntObjectHashMap<PlayAnimationAction>();
        this.m_tauntAction1 = new TIntObjectHashMap<TauntAction>();
        this.m_tauntAction2 = new TIntObjectHashMap<TauntAction>();
        this.m_displayCharacterCircleAction = new TIntObjectHashMap<DisplayCharacterCircleAction>();
        this.m_startPlacementAction = new TIntObjectHashMap<StartPlacementAction>();
        this.m_characterMoveWithEndedListenerAction = new TIntObjectHashMap<CharacterMoveWithEndedListenerAction>();
        this.m_characterTeleportAction = new TIntObjectHashMap<CharacterTeleportAction>();
    }
    
    static {
        ExternalFightCreationActions.INSTANCE = new ExternalFightCreationActions();
    }
}
