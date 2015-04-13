package com.ankamagames.wakfu.common.game.nation.Laws;

import com.ankamagames.framework.external.*;
import com.ankamagames.wakfu.common.game.nation.Laws.impl.*;

public enum NationLawModelConstant implements ExportableEnum, Parameterized
{
    USE_INTERACTIVE_ELEMENTS(1, (NationLawModel<? extends NationLaw>)UseInteractiveElementsLaw.MODEL, NationLawEventType.USE_INTERACTIVE_ELEMENT), 
    PUT_COLLECTOR_MONEY(2, (NationLawModel<? extends NationLaw>)PutCollectorMoneyLaw.MODEL, NationLawEventType.PUT_COLLECTOR_CONTENT), 
    ACT_AGAINST_PROTECTOR(3, (NationLawModel<? extends NationLaw>)ActAgainstProtectorLaw.MODEL, NationLawEventType.PROTECTOR_WISH_ACTION), 
    ACT_FOLLOWING_PROTECTOR(4, (NationLawModel<? extends NationLaw>)ActFollowingProtectorLaw.MODEL, NationLawEventType.PROTECTOR_WISH_ACTION), 
    PROPOSE_DUEL(5, (NationLawModel<? extends NationLaw>)ProposeDuelLaw.MODEL, NationLawEventType.PROPOSE_DUEL), 
    REFUSE_DUEL(6, (NationLawModel<? extends NationLaw>)RefuseDuelLaw.MODEL, NationLawEventType.ANSWER_DUEL), 
    FIGHT_GOVERNMENT(7, (NationLawModel<? extends NationLaw>)FightGovernmentLaw.MODEL, NationLawEventType.FIGHT), 
    FIGHT_GOOD_CITIZEN(8, (NationLawModel<? extends NationLaw>)FightGoodCitizenLaw.MODEL, NationLawEventType.FIGHT), 
    FIGHT_GOOD_FOREIGNER(9, (NationLawModel<? extends NationLaw>)FightGoodForeignerLaw.MODEL, NationLawEventType.FIGHT), 
    JOIN_FIGHT_OUTLAW(10, (NationLawModel<? extends NationLaw>)JoinFightOutlawLaw.MODEL, NationLawEventType.JOIN_FIGHT), 
    JOIN_FIGHT_NOT_GROUPED(11, (NationLawModel<? extends NationLaw>)JoinFightNotGroupedLaw.MODEL, NationLawEventType.JOIN_FIGHT), 
    WON_FIGHT_PROTECTOR(12, (NationLawModel<? extends NationLaw>)WonFightProtectorLaw.MODEL, NationLawEventType.WON_FIGHT), 
    LOSE_FIGHT_INITIATED(13, (NationLawModel<? extends NationLaw>)LoseFightInitiatedLaw.MODEL, NationLawEventType.LOSE_FIGHT), 
    LOSE_FIGHT_DUEL(14, (NationLawModel<? extends NationLaw>)LoseFightDuelLaw.MODEL, NationLawEventType.LOSE_FIGHT), 
    KILL_OUTLAW(15, (NationLawModel<? extends NationLaw>)KillOutlawLaw.MODEL, NationLawEventType.KILL), 
    KILL_TEAMMATE(16, (NationLawModel<? extends NationLaw>)KillTeammateLaw.MODEL, NationLawEventType.KILL), 
    FIGHT_ECO(17, (NationLawModel<? extends NationLaw>)FightMonsterLaw.MODEL, NationLawEventType.FIGHT), 
    FIGHT_ECO_PROTECTED(18, (NationLawModel<? extends NationLaw>)FightProtectedLaw.MODEL, NationLawEventType.FIGHT), 
    COLLECT_ECO(19, (NationLawModel<? extends NationLaw>)CollectLaw.MODEL, NationLawEventType.COLLECT), 
    COLLECT_ECO_PROTECTED(20, (NationLawModel<? extends NationLaw>)CollectProtectedLaw.MODEL, NationLawEventType.COLLECT), 
    PROPOSE_CITIZEN_EXCHANGE(21, (NationLawModel<? extends NationLaw>)ProposeCitizenExchangeLaw.MODEL, NationLawEventType.PROPOSE_EXCHANGE), 
    PROPOSE_FOREIGNER_EXCHANGE(22, (NationLawModel<? extends NationLaw>)ProposeForeignerExchangeLaw.MODEL, NationLawEventType.PROPOSE_EXCHANGE), 
    PROPOSE_OUTLAW_EXCHANGE(23, (NationLawModel<? extends NationLaw>)ProposeOutlawExchangeLaw.MODEL, NationLawEventType.PROPOSE_EXCHANGE), 
    REGISTER_CANDIDATE_EXCEPT_GOVERNOR(24, (NationLawModel<? extends NationLaw>)RegisterCandidateExceptGovernorLaw.MODEL, NationLawEventType.REGISTER_CANDIDATE), 
    VOTE_AGAINST_GOVERNOR(25, (NationLawModel<? extends NationLaw>)VoteAgainstGovernorLaw.MODEL, NationLawEventType.VOTE), 
    VOTE_FOR_GOVERNOR(26, (NationLawModel<? extends NationLaw>)VoteForGovernorLaw.MODEL, NationLawEventType.VOTE);
    
    public final int id;
    public final NationLawModel<? extends NationLaw> model;
    public final NationLawEventType listenedEventType;
    
    private NationLawModelConstant(final int idx, final NationLawModel<? extends NationLaw> model, final NationLawEventType eventType) {
        this.id = idx;
        this.model = model;
        this.listenedEventType = eventType;
    }
    
    public static NationLawModelConstant fromId(final int id) {
        final NationLawModelConstant[] values = values();
        for (int i = 0; i < values.length; ++i) {
            final NationLawModelConstant value = values[i];
            if (value.id == id) {
                return value;
            }
        }
        return null;
    }
    
    @Override
    public String getEnumId() {
        return String.valueOf(this.id);
    }
    
    @Override
    public String getEnumLabel() {
        return this.toString();
    }
    
    @Override
    public String getEnumComment() {
        return this.toString();
    }
    
    @Override
    public ParameterListSet getParametersListSet() {
        return this.model.getParameters();
    }
}
