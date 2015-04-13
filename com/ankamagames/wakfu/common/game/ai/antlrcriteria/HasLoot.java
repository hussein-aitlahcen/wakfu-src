package com.ankamagames.wakfu.common.game.ai.antlrcriteria;

import java.util.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.wakfu.common.game.item.loot.*;
import com.ankamagames.wakfu.common.datas.*;

public final class HasLoot extends FunctionCriterion
{
    private static ArrayList<ParserType[]> signatures;
    private boolean m_target;
    
    @Override
    protected List<ParserType[]> getSignatures() {
        return HasLoot.signatures;
    }
    
    public HasLoot(final ArrayList<ParserObject> args) {
        super();
        this.m_target = false;
        this.checkType(args);
        this.m_target = args.get(0).getValue().equalsIgnoreCase("target");
    }
    
    @Override
    public Enum getEnum() {
        return WakfuCriterionIds.HAS_LOOT;
    }
    
    @Override
    public int getValidity(final Object criterionUser, final Object criterionTarget, final Object criterionContent, final Object criterionContext) {
        final BasicCharacterInfo user = CriteriaUtils.getTargetCharacterInfoFromParameters(this.m_target, criterionUser, criterionTarget, criterionContext, criterionContent);
        if (!(user instanceof Dropper)) {
            return -1;
        }
        return (((Dropper)user).getFullLootList().size() > 0) ? 0 : -1;
    }
    
    static {
        (HasLoot.signatures = new ArrayList<ParserType[]>()).add(new ParserType[] { ParserType.STRING });
    }
}
