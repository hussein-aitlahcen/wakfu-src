package com.ankamagames.wakfu.common.game.ai.antlrcriteria;

import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.wakfu.common.game.fight.*;
import java.util.*;

public final class HasSummonWithBreed extends FunctionCriterion
{
    private NumericalValue m_breedId;
    private static ArrayList<ParserType[]> signatures;
    
    @Override
    protected List<ParserType[]> getSignatures() {
        return HasSummonWithBreed.signatures;
    }
    
    public HasSummonWithBreed(final ArrayList<ParserObject> args) {
        super();
        this.checkType(args);
        this.m_breedId = args.get(0);
    }
    
    @Override
    public int getValidity(final Object criterionUser, final Object criterionTarget, final Object criterionContent, final Object criterionContext) {
        if (!(criterionUser instanceof BasicCharacterInfo)) {
            throw new CriteriaExecutionException("Le user d'un crit\u00e8re IsMonsterBreed doit \u00eatre un player.");
        }
        final BasicCharacterInfo character = (BasicCharacterInfo)criterionUser;
        final BasicFight<?> basicFight = (BasicFight<?>)character.getCurrentFight();
        if (basicFight == null) {
            return -1;
        }
        final Collection<? extends BasicCharacterInfo> fighters = (Collection<? extends BasicCharacterInfo>)basicFight.getFightersInPlay();
        for (final BasicCharacterInfo info : fighters) {
            if (info == character) {
                continue;
            }
            if (info.getController() != character) {
                continue;
            }
            if (info.getBreedId() == this.m_breedId.getLongValue(criterionUser, criterionTarget, criterionContent, criterionContext)) {
                return 0;
            }
        }
        return -1;
    }
    
    public short getBreedId() {
        if (this.m_breedId.isConstant() && this.m_breedId.isInteger()) {
            return (short)this.m_breedId.getLongValue(null, null, null, null);
        }
        return -1;
    }
    
    @Override
    public Enum getEnum() {
        return WakfuCriterionIds.HAS_SUMMON_WITH_BREED;
    }
    
    static {
        HasSummonWithBreed.signatures = new ArrayList<ParserType[]>();
        final ParserType[] sig = { ParserType.NUMBER };
        HasSummonWithBreed.signatures.add(sig);
    }
}
