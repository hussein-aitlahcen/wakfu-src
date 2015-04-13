package com.ankamagames.wakfu.common.game.ai.antlrcriteria;

import java.util.*;
import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.wakfu.common.datas.Breed.*;

public class IsMonsterBreed extends FunctionCriterion
{
    private NumericalValue m_breedId;
    private static ArrayList<ParserType[]> signatures;
    
    @Override
    protected List<ParserType[]> getSignatures() {
        return IsMonsterBreed.signatures;
    }
    
    public IsMonsterBreed(final ArrayList<ParserObject> args) {
        super();
        this.checkType(args);
        this.m_breedId = args.get(0);
    }
    
    public int getBreedId() {
        if (this.m_breedId.isConstant() && this.m_breedId.isInteger()) {
            return (int)this.m_breedId.getLongValue(null, null, null, null);
        }
        return -1;
    }
    
    @Override
    public int getValidity(final Object criterionUser, final Object criterionTarget, final Object criterionContent, final Object criterionContext) {
        if (!(criterionUser instanceof BasicCharacterInfo)) {
            throw new CriteriaExecutionException("Le user d'un crit\u00e8re IsMonsterBreed doit \u00eatre un player.");
        }
        final BasicCharacterInfo character = (BasicCharacterInfo)criterionUser;
        final Breed breed = character.getBreed();
        final int breedId = this.getBreedId();
        if (breedId == breed.getBreedId()) {
            return 0;
        }
        return -1;
    }
    
    @Override
    public Enum getEnum() {
        return WakfuCriterionIds.ISMONSTERBREED;
    }
    
    static {
        IsMonsterBreed.signatures = new ArrayList<ParserType[]>();
        final ParserType[] sig = { ParserType.NUMBER };
        IsMonsterBreed.signatures.add(sig);
    }
}
