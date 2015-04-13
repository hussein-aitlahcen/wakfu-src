package com.ankamagames.wakfu.common.game.ai.antlrcriteria;

import java.util.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.wakfu.common.game.fighter.*;

public final class AIGetSpellCastCount extends FunctionValue
{
    private static ArrayList<ParserType[]> signatures;
    private NumericalValue m_spellId;
    
    public AIGetSpellCastCount() {
        super();
    }
    
    public AIGetSpellCastCount(final ArrayList<ParserObject> args) {
        super();
        this.checkType(args);
        this.m_spellId = args.get(0);
    }
    
    @Override
    protected List<ParserType[]> getSignatures() {
        return AIGetSpellCastCount.signatures;
    }
    
    @Override
    public Enum getEnum() {
        return WakfuCriterionIds.AI_GET_SPELL_CAST_COUNT;
    }
    
    @Override
    public boolean isInteger() {
        return true;
    }
    
    @Override
    public long getLongValue(final Object criterionUser, final Object criterionTarget, final Object criterionContent, final Object criterionContext) {
        if (!(criterionUser instanceof FightActionHistoryProvider)) {
            throw new CriteriaExecutionException("On essaye d'acc\u00e9der \u00e0 l'historique de combat d'un objet qui n'en poss\u00e8de pas");
        }
        final int spellId = (int)this.m_spellId.getLongValue(criterionUser, criterionTarget, criterionContent, criterionContext);
        final FightActionHistoryProvider character = (FightActionHistoryProvider)criterionUser;
        final FightActionHistory fightActionHistory = character.getFightActionHistory();
        return fightActionHistory.getSpellCastCount(spellId);
    }
    
    static {
        (AIGetSpellCastCount.signatures = new ArrayList<ParserType[]>()).add(new ParserType[] { ParserType.NUMBER });
    }
}
