package com.ankamagames.wakfu.common.game.ai.antlrcriteria;

import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.wakfu.common.game.fight.*;
import com.ankamagames.wakfu.common.datas.*;
import java.util.*;

public final class GetFightersWithBreedInRange extends GetFightersCountInRange
{
    private static ArrayList<ParserType[]> signatures;
    private NumericalValue m_breedIdParameter;
    private long m_breedId;
    
    @Override
    protected List<ParserType[]> getSignatures() {
        return GetFightersWithBreedInRange.signatures;
    }
    
    public GetFightersWithBreedInRange(final ArrayList<ParserObject> args) {
        super();
        this.m_maxRange = args.get(0);
        this.m_target = args.get(1).getValue().equalsIgnoreCase("target");
        this.m_breedIdParameter = args.get(2);
    }
    
    @Override
    protected Collection<? extends BasicCharacterInfo> getFighters(final AbstractFight<?> fight, final CriterionUser user) {
        if (fight == null) {
            return (Collection<? extends BasicCharacterInfo>)Collections.emptyList();
        }
        final Collection<? extends BasicCharacterInfo> inPlay = fight.getFightersInPlay();
        final Collection<BasicCharacterInfo> res = new ArrayList<BasicCharacterInfo>();
        for (final BasicCharacterInfo fighter : inPlay) {
            if (fighter.getBreedId() == this.m_breedId) {
                res.add(fighter);
            }
        }
        return res;
    }
    
    @Override
    protected void hook(final Object criterionUser, final Object criterionTarget, final Object criterionContext, final Object criterionContent) {
        this.m_breedId = this.m_breedIdParameter.getLongValue(criterionUser, criterionTarget, criterionContent, criterionContext);
    }
    
    @Override
    public Enum getEnum() {
        return WakfuCriterionIds.GET_FIGHTERS_WITH_BREED_IN_RANGE;
    }
    
    static {
        GetFightersWithBreedInRange.signatures = new ArrayList<ParserType[]>();
        final ParserType[] sig = { ParserType.NUMBER, ParserType.STRING, ParserType.NUMBER };
        GetFightersWithBreedInRange.signatures.add(sig);
    }
}
