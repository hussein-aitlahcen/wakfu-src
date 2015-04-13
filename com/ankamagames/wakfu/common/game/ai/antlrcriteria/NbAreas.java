package com.ankamagames.wakfu.common.game.ai.antlrcriteria;

import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effectArea.*;
import com.ankamagames.wakfu.common.game.fight.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;
import java.util.*;

abstract class NbAreas extends FunctionValue
{
    private static ArrayList<ParserType[]> signatures;
    protected boolean m_isForAll;
    protected String m_targetType;
    
    @Override
    protected List<ParserType[]> getSignatures() {
        return NbAreas.signatures;
    }
    
    @Override
    public boolean isInteger() {
        return true;
    }
    
    NbAreas() {
        super();
        this.m_isForAll = true;
    }
    
    NbAreas(final ArrayList<ParserObject> args) {
        super();
        this.m_isForAll = true;
        final short paramType = this.checkType(args);
        if (paramType == 0) {
            this.m_isForAll = true;
        }
        if (paramType == 1) {
            this.m_isForAll = false;
            this.m_targetType = args.get(0).getValue();
        }
    }
    
    @Override
    public long getLongValue(final Object criterionUser, final Object criterionTarget, final Object criterionContent, final Object criterionContext) {
        BasicFight fight = CriteriaUtils.getFightFromContext(criterionContext);
        if (fight == null) {
            fight = CriteriaUtils.getFight(criterionUser, criterionContext);
        }
        if (fight == null) {
            throw new CriteriaExecutionException("On essaie de compter les pi\u00e8ges en dehors d'un combat...");
        }
        CriterionUser character = null;
        if (!this.m_isForAll) {
            if (this.m_targetType == null || this.m_targetType.equalsIgnoreCase("caster")) {
                character = CriteriaUtils.getTargetCriterionUserFromParameters(false, criterionUser, criterionTarget, criterionContext, criterionContent);
            }
            else if (this.m_targetType.equalsIgnoreCase("target")) {
                character = CriteriaUtils.getTargetCriterionUserFromParameters(true, criterionUser, criterionTarget, criterionContext, criterionContent);
            }
            else if (this.m_targetType.equalsIgnoreCase("triggeringCaster")) {
                final EffectUser triggeringCaster = CriteriaUtils.getTriggeringEffectCaster(criterionContent);
                if (triggeringCaster instanceof CriterionUser) {
                    character = (CriterionUser)triggeringCaster;
                }
            }
            else if (this.m_targetType.equalsIgnoreCase("triggeringTarget")) {
                final EffectUser triggeringTarget = CriteriaUtils.getTriggeringEffectTarget(criterionContent);
                if (triggeringTarget instanceof CriterionUser) {
                    character = (CriterionUser)triggeringTarget;
                }
            }
            else if (this.m_targetType.equalsIgnoreCase("casterController")) {
                final CriterionUser caster = CriteriaUtils.getTargetCriterionUserFromParameters(false, criterionUser, criterionTarget, criterionContext, criterionContent);
                character = fight.getFighterFromId(caster.getOriginalControllerId());
            }
        }
        long value = 0L;
        for (final BasicEffectArea area : fight.getEffectAreaManager().getActiveEffectAreas()) {
            if ((this.m_isForAll || this.userIsAreaOwner(character, area)) && this.isConcernedArea(area)) {
                ++value;
            }
        }
        return super.getSign() * value;
    }
    
    private boolean userIsAreaOwner(final CriterionUser character, final BasicEffectArea area) {
        return character != null && area.getOwner() != null && area.getOwner().getId() == character.getId();
    }
    
    protected boolean isConcernedArea(final BasicEffectArea area) {
        return area.getType() == this.getAreaType();
    }
    
    protected abstract int getAreaType();
    
    static {
        (NbAreas.signatures = new ArrayList<ParserType[]>()).add(CriterionConstants.EMPTY_SIGNATURE);
        NbAreas.signatures.add(CriterionConstants.ONE_STRING_SIGNATURE);
    }
}
