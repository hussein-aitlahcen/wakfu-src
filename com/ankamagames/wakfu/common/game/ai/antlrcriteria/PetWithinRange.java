package com.ankamagames.wakfu.common.game.ai.antlrcriteria;

import com.ankamagames.wakfu.common.datas.specific.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.wakfu.common.game.fighter.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.characteristic.*;
import com.ankamagames.framework.ai.*;
import com.ankamagames.wakfu.common.game.fight.*;
import com.ankamagames.wakfu.common.game.spell.*;
import java.util.*;

public class PetWithinRange extends FunctionCriterion
{
    private static final List<ParserType[]> SIGNATURES;
    
    @Override
    protected List<ParserType[]> getSignatures() {
        return PetWithinRange.SIGNATURES;
    }
    
    public PetWithinRange(final ArrayList<ParserObject> args) {
        super();
        this.checkType(args);
    }
    
    @Override
    public int getValidity(final Object criterionUser, final Object criterionTarget, final Object criterionContent, final Object criterionContext) {
        if (!(criterionUser instanceof SymbioticCharacter)) {
            throw new CriteriaExecutionException("On essaie de v\u00e9rifer la distance du pet d'autre chose qu'un joueur");
        }
        if (!(criterionContext instanceof AbstractFight)) {
            throw new CriteriaExecutionException("On essaie de v\u00e9rifer la distance du pet ailleurs qu'en combat");
        }
        if (!(criterionContent instanceof AbstractSpellLevel)) {
            throw new CriteriaExecutionException("On essaie de v\u00e9rifer la distance du pet en utilisant autre chose qu'un sort");
        }
        if (((SymbioticCharacter)criterionUser).getSymbiot().getCurrentCreatureParameters() == null) {
            return -1;
        }
        final long characterId = ((SymbioticCharacter)criterionUser).getSymbiot().getCurrentCreatureParameters().getSummonId();
        if (characterId == -1L) {
            return -1;
        }
        final BasicFighter pet = ((AbstractFight)criterionContext).getFighterFromId(characterId);
        if (pet == null) {
            return -1;
        }
        final BasicCharacterInfo userCharacterInfo = (BasicCharacterInfo)criterionUser;
        int poBoost = 0;
        if (userCharacterInfo.hasCharacteristic(FighterCharacteristicType.RANGE)) {
            poBoost = userCharacterInfo.getCharacteristicValue(FighterCharacteristicType.RANGE);
        }
        final AbstractSpell spell = ((AbstractSpellLevel)criterionContent).getSpell();
        final int baseMaxRange = spell.getRangeMax((AbstractSpellLevel)criterionContent, userCharacterInfo, criterionTarget, criterionContext);
        final boolean spellCastRangeDynamic = spell.isSpellCastRangeDynamic((AbstractSpellLevel)criterionContent, userCharacterInfo, criterionTarget, criterionContext);
        final int range = spellCastRangeDynamic ? (baseMaxRange + poBoost) : baseMaxRange;
        if (DistanceUtils.getIntersectionDistance(userCharacterInfo, pet) <= range) {
            return 0;
        }
        return -1;
    }
    
    @Override
    public Enum getEnum() {
        return WakfuCriterionIds.PETINRANGE;
    }
    
    static {
        SIGNATURES = Collections.singletonList(ParserType.EMPTY_ARRAY);
    }
}
