package com.ankamagames.wakfu.common.game.ai.antlrcriteria;

import java.util.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.wakfu.common.game.fighter.*;

public class AIHasCastSpell extends FunctionCriterion
{
    private static ArrayList<ParserType[]> signatures;
    private NumericalValue m_spellId;
    
    public AIHasCastSpell(final ArrayList<ParserObject> args) {
        super();
        this.checkType(args);
        this.m_spellId = args.get(0);
    }
    
    public int getSpellId() {
        if (this.m_spellId.isConstant() && this.m_spellId.isInteger()) {
            return (int)this.m_spellId.getLongValue(null, null, null, null);
        }
        return -1;
    }
    
    @Override
    protected List<ParserType[]> getSignatures() {
        return AIHasCastSpell.signatures;
    }
    
    @Override
    public int getValidity(final Object criterionUser, final Object criterionTarget, final Object criterionContent, final Object criterionContext) {
        if (!(criterionUser instanceof FightActionHistoryProvider)) {
            throw new CriteriaExecutionException("On essaye d'acc\u00e9der \u00e0 l'historique de combat d'un objet qui n'en poss\u00e8de pas");
        }
        final FightActionHistoryProvider character = (FightActionHistoryProvider)criterionUser;
        final int spellId = this.getSpellId();
        final FightActionHistory fightActionHistory = character.getFightActionHistory();
        return fightActionHistory.hasCastSpell(spellId) ? 0 : -1;
    }
    
    @Override
    public Enum getEnum() {
        return WakfuCriterionIds.AI_HAS_CAST_SPELL;
    }
    
    static {
        AIHasCastSpell.signatures = new ArrayList<ParserType[]>();
        final ParserType[] sig = { ParserType.NUMBER };
        AIHasCastSpell.signatures.add(sig);
    }
}
