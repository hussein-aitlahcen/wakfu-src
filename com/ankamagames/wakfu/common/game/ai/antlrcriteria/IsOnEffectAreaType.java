package com.ankamagames.wakfu.common.game.ai.antlrcriteria;

import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.wakfu.common.game.effectArea.*;
import com.ankamagames.wakfu.common.game.fight.*;
import com.ankamagames.wakfu.common.game.effect.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effectArea.*;
import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;
import java.util.*;

public class IsOnEffectAreaType extends FunctionCriterion
{
    private static ArrayList<ParserType[]> signatures;
    private final boolean m_target;
    private final NumericalValue m_effectAreaTypeId;
    private final ConstantBooleanCriterion m_ownAreaNeeded;
    
    @Override
    protected List<ParserType[]> getSignatures() {
        return IsOnEffectAreaType.signatures;
    }
    
    public IsOnEffectAreaType(final ArrayList<ParserObject> args) {
        super();
        this.checkType(args);
        this.m_target = args.get(0).getValue().equalsIgnoreCase("target");
        this.m_effectAreaTypeId = args.get(1);
        if (args.size() >= 3) {
            this.m_ownAreaNeeded = args.get(2);
        }
        else {
            this.m_ownAreaNeeded = null;
        }
    }
    
    public IsOnEffectAreaType(final String target, final int effectAreaTypeId, final boolean ownAreaNeeded) {
        super();
        this.m_target = target.equalsIgnoreCase("target");
        this.m_effectAreaTypeId = new ConstantIntegerValue(effectAreaTypeId);
        this.m_ownAreaNeeded = new ConstantBooleanCriterion(ownAreaNeeded);
    }
    
    @Override
    public int getValidity(final Object criterionUser, final Object criterionTarget, final Object criterionContent, final Object criterionContext) {
        final BasicCharacterInfo character = CriteriaUtils.getTargetCharacterInfoFromParameters(this.m_target, criterionUser, criterionTarget, criterionContext, criterionContent);
        if (character == null) {
            return -1;
        }
        final int effectAreaTypeId = (int)this.m_effectAreaTypeId.getLongValue(criterionUser, criterionTarget, criterionContent, criterionContext);
        final EffectAreaType effectAreaType = EffectAreaType.getTypeFromId(effectAreaTypeId);
        if (effectAreaType == null) {
            IsOnEffectAreaType.m_logger.error((Object)("Error while checking IsOnEffectAreaType : area type " + effectAreaTypeId + " doesn't exist"));
            return -1;
        }
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
        final boolean onlyOwnAreas = this.m_ownAreaNeeded != null && this.m_ownAreaNeeded.isValid(criterionUser, criterionContent, criterionContent, criterionContext);
        EffectUser areaOwnerToCheck;
        if (onlyOwnAreas) {
            areaOwnerToCheck = CriteriaUtils.getOwner(character);
        }
        else {
            areaOwnerToCheck = null;
        }
        final Collection<BasicEffectArea> areas = fight.getEffectAreaManager().getActiveEffectAreas();
        for (final BasicEffectArea area : areas) {
            if (area.getType() != effectAreaTypeId) {
                continue;
            }
            if (areaOwnerToCheck != null && CriteriaUtils.getOwner(area) != areaOwnerToCheck) {
                continue;
            }
            if (area.contains(character.getPosition())) {
                return 0;
            }
        }
        return -1;
    }
    
    @Override
    public Enum getEnum() {
        return WakfuCriterionIds.IS_ON_EFFECT_AREA_TYPE;
    }
    
    static {
        IsOnEffectAreaType.signatures = new ArrayList<ParserType[]>();
        ParserType[] sig = { ParserType.STRING, ParserType.NUMBER };
        IsOnEffectAreaType.signatures.add(sig);
        sig = new ParserType[] { ParserType.STRING, ParserType.NUMBER, ParserType.BOOLEAN };
        IsOnEffectAreaType.signatures.add(sig);
    }
}
