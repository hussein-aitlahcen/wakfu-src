package com.ankamagames.wakfu.common.game.ai.antlrcriteria;

import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.wakfu.common.game.fight.*;
import com.ankamagames.wakfu.common.game.effect.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effectArea.*;
import com.ankamagames.wakfu.common.datas.*;
import java.util.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;

public class IsOnSpecificEffectArea extends FunctionCriterion
{
    private static final String TEAM_AREAS_PARAM = "teamAreas";
    private static final String OWN_AREAS_PARAM = "ownAreas";
    private static ArrayList<ParserType[]> signatures;
    public static final byte ALL_AREAS = 1;
    public static final byte OWN_AREAS = 2;
    public static final byte TEAM_AREAS = 3;
    protected final boolean m_target;
    private final NumericalValue m_areaId;
    private final byte m_type;
    
    @Override
    protected List<ParserType[]> getSignatures() {
        return IsOnSpecificEffectArea.signatures;
    }
    
    protected IsOnSpecificEffectArea(final boolean target, final NumericalValue areaId, final ConstantBooleanCriterion ownAreaNeeded) {
        super();
        this.m_target = target;
        this.m_areaId = areaId;
        if (ownAreaNeeded.isValid(null, null, null, null)) {
            this.m_type = 2;
        }
        else {
            this.m_type = 1;
        }
    }
    
    public IsOnSpecificEffectArea(final ArrayList<ParserObject> args) {
        super();
        final byte argsType = this.checkType(args);
        this.m_target = args.get(0).getValue().equalsIgnoreCase("target");
        this.m_areaId = args.get(1);
        if (argsType == 1) {
            final boolean ownAreaType = args.get(2).isValid(null, null, null, null);
            this.m_type = (byte)(ownAreaType ? 2 : 1);
        }
        else if (argsType == 2) {
            final String checkType = args.get(2).getValue();
            if ("ownAreas".equalsIgnoreCase(checkType)) {
                this.m_type = 2;
            }
            else if ("teamAreas".equalsIgnoreCase(checkType)) {
                this.m_type = 3;
            }
            else {
                this.m_type = 1;
            }
        }
        else {
            this.m_type = 1;
        }
    }
    
    @Override
    public int getValidity(final Object criterionUser, final Object criterionTarget, final Object criterionContent, final Object criterionContext) {
        final CriterionUser character = CriteriaUtils.getTargetCriterionUserFromParameters(this.m_target, criterionUser, criterionTarget, criterionContext, criterionContent);
        if (character == null) {
            return -1;
        }
        final long areaId = this.m_areaId.getLongValue(criterionUser, criterionTarget, criterionContent, criterionContext);
        AbstractFight fight = null;
        if (criterionContext instanceof AbstractFight) {
            fight = (AbstractFight)criterionContext;
        }
        else if (criterionContext instanceof WakfuFightEffectContext) {
            fight = ((WakfuFightEffectContext)criterionContext).getFight();
        }
        if (fight == null) {
            return -1;
        }
        final Collection<BasicEffectArea> areas = fight.getEffectAreaManager().getActiveEffectAreas();
        for (final BasicEffectArea area : areas) {
            if (area.getBaseId() != areaId) {
                continue;
            }
            if (!this.isValid(character, area)) {
                continue;
            }
            if (area.contains(character.getPosition()) && this.isValidForSpecificCriteria(area, criterionUser, criterionTarget, criterionContent, criterionContext)) {
                return 0;
            }
        }
        return -1;
    }
    
    private boolean isValid(final CriterionUser character, final BasicEffectArea area) {
        if (this.m_type == 2) {
            final EffectUser areaOwnerToCheck = CriteriaUtils.getOwner(character);
            return areaOwnerToCheck == null || CriteriaUtils.getOwner(area) == areaOwnerToCheck;
        }
        return this.m_type != 3 || (character != null && character.getTeamId() == area.getTeamId());
    }
    
    protected boolean isValidForSpecificCriteria(final BasicEffectArea area, final Object criterionUser, final Object criterionTarget, final Object criterionContent, final Object criterionContext) {
        return true;
    }
    
    public NumericalValue getAreaId() {
        return this.m_areaId;
    }
    
    @Override
    public Enum getEnum() {
        return WakfuCriterionIds.IS_ON_SPECIFIC_EFFECT_AREA;
    }
    
    static {
        (IsOnSpecificEffectArea.signatures = new ArrayList<ParserType[]>()).add(new ParserType[] { ParserType.STRING, ParserType.NUMBER });
        IsOnSpecificEffectArea.signatures.add(new ParserType[] { ParserType.STRING, ParserType.NUMBER, ParserType.BOOLEAN });
        IsOnSpecificEffectArea.signatures.add(new ParserType[] { ParserType.STRING, ParserType.NUMBER, ParserType.STRING });
    }
}
