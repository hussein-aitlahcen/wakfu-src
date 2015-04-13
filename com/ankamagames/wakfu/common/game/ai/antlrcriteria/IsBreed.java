package com.ankamagames.wakfu.common.game.ai.antlrcriteria;

import gnu.trove.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.wakfu.common.datas.Breed.*;
import java.util.*;
import com.ankamagames.wakfu.common.datas.*;

public class IsBreed extends FunctionCriterion
{
    private TShortArrayList m_breedId;
    private boolean m_target;
    private static ArrayList<ParserType[]> signatures;
    
    @Override
    protected List<ParserType[]> getSignatures() {
        return IsBreed.signatures;
    }
    
    public TShortArrayList getBreedId() {
        return this.m_breedId;
    }
    
    public boolean isTarget() {
        return this.m_target;
    }
    
    public IsBreed(final ArrayList<ParserObject> args) {
        super();
        this.m_breedId = new TShortArrayList();
        this.m_target = false;
        this.checkType(args);
        for (final ParserObject arg : args) {
            final String breed = ((StringObject)arg).getValue();
            if (breed.equalsIgnoreCase("target")) {
                this.m_target = true;
            }
            else if (breed.equalsIgnoreCase("caster")) {
                this.m_target = false;
            }
            else {
                final AvatarBreed avatarBreed = AvatarBreed.getBreedFromName(breed);
                if (avatarBreed == null) {
                    continue;
                }
                this.m_breedId.add(avatarBreed.getBreedId());
            }
        }
    }
    
    @Override
    public int getValidity(final Object criterionUser, final Object criterionTarget, final Object criterionContent, final Object criterionContext) {
        final CriterionUser targetCharacter = CriteriaUtils.getTargetCharacterInfoFromParameters(this.m_target, criterionUser, criterionTarget, criterionContext, criterionContent);
        if (targetCharacter == null) {
            return -1;
        }
        if (this.m_breedId.contains(targetCharacter.getBreed().getBreedId())) {
            return 0;
        }
        return -1;
    }
    
    @Override
    public Enum getEnum() {
        return WakfuCriterionIds.ISBREED;
    }
    
    static {
        IsBreed.signatures = new ArrayList<ParserType[]>();
        final ParserType[] sig = { ParserType.STRINGLIST };
        IsBreed.signatures.add(sig);
    }
}
