package com.ankamagames.wakfu.common.game.ai.antlrcriteria;

import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import java.util.*;
import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.wakfu.common.game.nation.*;
import com.ankamagames.wakfu.common.game.nation.crime.data.*;

public final class CanBecomeSoldierOrMilitiaman extends FunctionCriterion
{
    private static ArrayList<ParserType[]> signatures;
    private boolean m_target;
    
    public CanBecomeSoldierOrMilitiaman(final ArrayList<ParserObject> args) {
        super();
        final byte type = this.checkType(args);
        switch (type) {
            case 0: {
                this.m_target = false;
                break;
            }
            case 1: {
                final String isTarget = args.get(0).getValue();
                if (isTarget.equalsIgnoreCase("target")) {
                    this.m_target = true;
                    break;
                }
                break;
            }
        }
    }
    
    @Override
    protected List<ParserType[]> getSignatures() {
        return CanBecomeSoldierOrMilitiaman.signatures;
    }
    
    @Override
    public int getValidity(final Object criterionUser, final Object criterionTarget, final Object criterionContent, final Object criterionContext) {
        final BasicCharacterInfo targetCharacter = CriteriaUtils.getTargetCharacterInfoFromParameters(this.m_target, criterionUser, criterionTarget, criterionContext, criterionContent);
        if (targetCharacter == null) {
            return -1;
        }
        final CitizenComportment citizenComportment = targetCharacter.getCitizenComportment();
        final int citizenScore = citizenComportment.getCitizenScoreForNation(citizenComportment.getNationId());
        final CitizenRank rank = CitizenRankManager.getInstance().getRankFromCitizenScore(citizenScore);
        if (rank.hasRule(CitizenRankRule.CAN_BECOME_SOLDIER_OR_MILITIAMAN)) {
            return 0;
        }
        return -1;
    }
    
    @Override
    public Enum getEnum() {
        return WakfuCriterionIds.CAN_BECOME_SOLDIER_OR_MILITIAMAN;
    }
    
    static {
        CanBecomeSoldierOrMilitiaman.signatures = new ArrayList<ParserType[]>();
        ParserType[] sig = new ParserType[0];
        CanBecomeSoldierOrMilitiaman.signatures.add(sig);
        sig = new ParserType[] { ParserType.STRING };
        CanBecomeSoldierOrMilitiaman.signatures.add(sig);
    }
}
