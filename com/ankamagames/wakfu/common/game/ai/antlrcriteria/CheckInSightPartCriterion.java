package com.ankamagames.wakfu.common.game.ai.antlrcriteria;

import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import java.util.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;
import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.part.*;

abstract class CheckInSightPartCriterion extends FunctionCriterion
{
    protected static ArrayList<ParserType[]> signatures;
    protected boolean m_target;
    
    CheckInSightPartCriterion() {
        super();
        this.m_target = false;
    }
    
    @Override
    protected List<ParserType[]> getSignatures() {
        return CheckInSightPartCriterion.signatures;
    }
    
    @Override
    public int getValidity(final Object criterionUser, final Object criterionTarget, final Object criterionContent, final Object criterionContext) {
        final BasicCharacterInfo targetCharacter = CriteriaUtils.getTargetCharacterInfoFromParameters(this.m_target, criterionUser, criterionTarget, criterionContext, criterionContent);
        if (targetCharacter == null) {
            return -1;
        }
        Object observer;
        if (this.m_target) {
            observer = criterionUser;
        }
        else {
            observer = criterionTarget;
        }
        if (observer != null && observer instanceof EffectUser) {
            final EffectUser caster = (EffectUser)observer;
            if (targetCharacter.getPartLocalisator() != null) {
                final Part part = targetCharacter.getPartLocalisator().getMainPartInSightFromPosition(caster.getWorldCellX(), caster.getWorldCellY(), caster.getWorldCellAltitude());
                if (this.isInSightPartConcerned(part)) {
                    return 0;
                }
            }
        }
        return -1;
    }
    
    protected abstract boolean isInSightPartConcerned(final Part p0);
    
    static {
        CheckInSightPartCriterion.signatures = new ArrayList<ParserType[]>();
        ParserType[] sig = new ParserType[0];
        CheckInSightPartCriterion.signatures.add(sig);
        sig = new ParserType[] { ParserType.STRING };
        CheckInSightPartCriterion.signatures.add(sig);
    }
}
