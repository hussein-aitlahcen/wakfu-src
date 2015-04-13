package com.ankamagames.wakfu.common.game.ai.antlrcriteria;

import gnu.trove.*;
import java.util.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.wakfu.common.datas.Breed.*;

public class IsBreedFamily extends FunctionCriterion
{
    private TIntArrayList m_breedId;
    private TargetType m_targetType;
    private static ArrayList<ParserType[]> signatures;
    
    @Override
    protected List<ParserType[]> getSignatures() {
        return IsBreedFamily.signatures;
    }
    
    public TIntArrayList getBreedId() {
        return this.m_breedId;
    }
    
    public boolean isTarget() {
        return this.m_targetType == TargetType.TARGET;
    }
    
    public IsBreedFamily(final ArrayList<ParserObject> args) {
        super();
        this.m_breedId = new TIntArrayList();
        this.m_targetType = TargetType.CASTER;
        final byte type = this.checkType(args);
        if (type == 0) {
            final String targetTypeName = args.get(0).getValue();
            final TargetType targetType = TargetType.getFromName(targetTypeName);
            if (targetType != TargetType.NONE) {
                this.m_targetType = targetType;
            }
        }
        for (int i = (type == 0) ? 1 : 0; i < args.size(); ++i) {
            final int id = (int)args.get(i).getLongValue(null, null, null, null);
            this.m_breedId.add(id);
        }
    }
    
    @Override
    public int getValidity(final Object criterionUser, final Object criterionTarget, final Object criterionContent, final Object criterionContext) {
        final BasicCharacterInfo targetCharacter = CriteriaUtils.getTargetCharacterInfoFromParameters(this.m_targetType, criterionUser, criterionTarget, criterionContext, criterionContent);
        if (targetCharacter == null) {
            return -1;
        }
        final Breed breed = targetCharacter.getBreed();
        if (breed instanceof AbstractMonsterBreed && this.m_breedId.contains(breed.getFamilyId())) {
            return 0;
        }
        return -1;
    }
    
    @Override
    public Enum getEnum() {
        return WakfuCriterionIds.ISBREEDFAMILY;
    }
    
    static {
        IsBreedFamily.signatures = new ArrayList<ParserType[]>();
        ParserType[] sig = { ParserType.STRING, ParserType.NUMBERLIST };
        IsBreedFamily.signatures.add(sig);
        sig = new ParserType[] { ParserType.NUMBERLIST };
        IsBreedFamily.signatures.add(sig);
    }
}
