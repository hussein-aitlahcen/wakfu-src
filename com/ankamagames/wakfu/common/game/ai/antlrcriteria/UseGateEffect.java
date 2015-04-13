package com.ankamagames.wakfu.common.game.ai.antlrcriteria;

import com.ankamagames.wakfu.common.game.effect.runningEffect.*;
import com.ankamagames.wakfu.common.game.effect.*;
import com.ankamagames.wakfu.common.game.fight.*;
import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.wakfu.common.game.spell.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import java.util.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;

public final class UseGateEffect extends FunctionCriterion
{
    private static final ArrayList<ParserType[]> SIGNATURES;
    
    public UseGateEffect(final ArrayList<ParserObject> args) {
        super();
        this.checkType(args);
    }
    
    @Override
    protected ArrayList<ParserType[]> getSignatures() {
        return UseGateEffect.SIGNATURES;
    }
    
    @Override
    public int getValidity(final Object criterionUser, final Object criterionTarget, final Object criterionContent, final Object criterionContext) {
        final CriterionUser caster = CriteriaUtils.getTargetCriterionUserFromParameters(false, criterionUser, criterionTarget, criterionContext, criterionContent);
        if (!(criterionContent instanceof WakfuRunningEffect)) {
            return -1;
        }
        final WakfuRunningEffect effect = (WakfuRunningEffect)criterionContent;
        final WakfuEffectContainer effectContainer = ((RunningEffect<FX, WakfuEffectContainer>)effect).getEffectContainer();
        if (!(effectContainer instanceof AbstractSpellLevel)) {
            return -1;
        }
        final AbstractFight<?> fight = CriteriaUtils.getFight(criterionUser, criterionContext);
        if (fight == null) {
            return -1;
        }
        if (!(caster instanceof BasicCharacterInfo)) {
            return -1;
        }
        final Point3 targetPosition = CriteriaUtils.getTargetPosition(true, criterionUser, criterionTarget, criterionContext, criterionContent);
        if (targetPosition == null) {
            return -1;
        }
        final BasicCharacterInfo user = (BasicCharacterInfo)caster;
        final AbstractSpellLevel spellLevel = (AbstractSpellLevel)effectContainer;
        final CommonCastValidator commonCastValidator = new CommonCastValidator(fight);
        final AbstractSpell spell = spellLevel.getSpell();
        final boolean spellCastRangeDynamic = spell.isSpellCastRangeDynamic(spellLevel, user, criterionTarget, criterionContext);
        final int rangeMax = spell.getRangeMax(spellLevel, user, criterionTarget, criterionContext);
        final int rangeMin = spell.getRangeMin(spellLevel, user, criterionTarget, criterionContext);
        final boolean testLos = spell.isTestLineOfSight(spellLevel, user, criterionTarget, criterionContext);
        final boolean hasToTestFreeCell = spell.hasToTestFreeCell();
        final boolean hasToTestNotBorderCell = spell.hasToTestNotBorderCell();
        final boolean canCastOnCasterCell = spell.isCanCastOnCasterCell();
        final SimpleCriterion castCriterions = spell.getCastCriterions();
        final CastValidity validity = commonCastValidator.getCastValidity(user, spellLevel, targetPosition, spellCastRangeDynamic, rangeMin, rangeMax, testLos, hasToTestFreeCell, hasToTestNotBorderCell, canCastOnCasterCell, castCriterions);
        final BasicCharacterInfo target = CriteriaUtils.getTargetCharacterInfoFromParameters(TargetType.TARGET, criterionUser, criterionTarget, criterionContext, criterionContent);
        final boolean cellAlignement = CommonCastValidator.checkCellAlignement(user, targetPosition, spellLevel, target, fight);
        if (validity == CastValidity.OK && cellAlignement) {
            return -1;
        }
        return 0;
    }
    
    @Override
    public Enum getEnum() {
        return WakfuCriterionIds.USE_GATE_EFFECT;
    }
    
    static {
        (SIGNATURES = new ArrayList<ParserType[]>()).add(CriterionConstants.EMPTY_SIGNATURE);
    }
}
