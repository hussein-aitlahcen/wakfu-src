package com.ankamagames.wakfu.common.game.ai.antlrcriteria;

import java.util.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.characteristic.*;
import com.ankamagames.wakfu.common.datas.specific.*;
import com.ankamagames.wakfu.common.datas.*;

public class HasWorldProperty extends FunctionCriterion
{
    private static ArrayList<ParserType[]> signatures;
    private NumericalValue m_worldPropertyId;
    private NumericalValue m_durationToCheck;
    private boolean m_target;
    
    public HasWorldProperty(final ArrayList<ParserObject> args) {
        super();
        final byte sigIdx = this.checkType(args);
        this.m_worldPropertyId = args.get(0);
        if (sigIdx == 1) {
            this.m_durationToCheck = args.get(1);
        }
        if (sigIdx == 2) {
            this.m_target = args.get(1).getValue().equalsIgnoreCase("target");
        }
    }
    
    public long getDurationToCheck() {
        return (this.m_durationToCheck.isConstant() && this.m_durationToCheck.isInteger()) ? this.m_durationToCheck.getLongValue(null, null, null, null) : -1L;
    }
    
    public int getWorldPropertyId() {
        if (this.m_worldPropertyId.isConstant() && this.m_worldPropertyId.isInteger()) {
            return (int)this.m_worldPropertyId.getLongValue(null, null, null, null);
        }
        return -1;
    }
    
    @Override
    protected List<ParserType[]> getSignatures() {
        return HasWorldProperty.signatures;
    }
    
    @Override
    public int getValidity(final Object criterionUser, final Object criterionTarget, final Object criterionContent, final Object criterionContext) {
        final CriterionUser targetCharacter = CriteriaUtils.getTargetCriterionUserFromParameters(this.m_target, criterionUser, criterionTarget, criterionContext, criterionContent);
        final byte worldPropertyId = (byte)this.getWorldPropertyId();
        final WorldPropertyType property = WorldPropertyType.getPropertyFromId(worldPropertyId);
        if (property == null) {
            throw new CriteriaExecutionException("Le param\u00e8tre du crit\u00e8re HasWorldProperty doit \u00eatre un id de WorldPropertyType valide (id=" + worldPropertyId + " n'existe pas)");
        }
        final boolean hasProperty = targetCharacter.hasProperty(property);
        if (!hasProperty) {
            return -1;
        }
        if (this.m_durationToCheck == null) {
            return 0;
        }
        final long duration = this.getDurationToCheck() * 1000L;
        if (System.currentTimeMillis() - AbstractMonsterTimedPropertyManager.getStateStartTime(targetCharacter.getId(), property) < duration) {
            return -1;
        }
        return 0;
    }
    
    @Override
    public Enum getEnum() {
        return WakfuCriterionIds.HAS_WORLD_PROPERTY;
    }
    
    static {
        (HasWorldProperty.signatures = new ArrayList<ParserType[]>()).add(CriterionConstants.ONE_NUMBER_SIGNATURE);
        HasWorldProperty.signatures.add(new ParserType[] { ParserType.NUMBER, ParserType.NUMBER });
        HasWorldProperty.signatures.add(new ParserType[] { ParserType.NUMBER, ParserType.STRING });
    }
}
