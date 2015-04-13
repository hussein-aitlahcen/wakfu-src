package com.ankamagames.wakfu.common.game.ai.antlrcriteria;

import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.wakfu.common.game.fighter.*;
import java.util.*;

public class AIHasMoved extends FunctionCriterion
{
    private static final List<ParserType[]> SIGNATURES;
    
    public AIHasMoved(final ArrayList<ParserObject> args) {
        super();
        this.checkType(args);
    }
    
    @Override
    protected List<ParserType[]> getSignatures() {
        return AIHasMoved.SIGNATURES;
    }
    
    @Override
    public int getValidity(final Object criterionUser, final Object criterionTarget, final Object criterionContent, final Object criterionContext) {
        if (!(criterionUser instanceof FightActionHistoryProvider)) {
            throw new CriteriaExecutionException("On essaye d'acc\u00e9der \u00e0 l'historique de combat d'un objet qui n'en poss\u00e8de pas");
        }
        final FightActionHistoryProvider character = (FightActionHistoryProvider)criterionUser;
        final FightActionHistory fightActionHistory = character.getFightActionHistory();
        return fightActionHistory.hasMoved() ? 0 : -1;
    }
    
    @Override
    public Enum getEnum() {
        return WakfuCriterionIds.AI_HAS_MOVED;
    }
    
    static {
        SIGNATURES = Collections.singletonList(ParserType.EMPTY_ARRAY);
    }
}
