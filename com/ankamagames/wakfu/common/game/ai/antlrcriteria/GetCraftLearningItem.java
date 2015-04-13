package com.ankamagames.wakfu.common.game.ai.antlrcriteria;

import java.util.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.wakfu.common.game.craft.reference.*;

public final class GetCraftLearningItem extends FunctionValue
{
    private static ArrayList<ParserType[]> signatures;
    private NumericalValue m_craftId;
    
    @Override
    protected List<ParserType[]> getSignatures() {
        return GetCraftLearningItem.signatures;
    }
    
    public GetCraftLearningItem() {
        super();
    }
    
    public GetCraftLearningItem(final ArrayList<ParserObject> args) {
        super();
        this.checkType(args);
        if (args.size() == 1) {
            this.m_craftId = args.get(0);
        }
    }
    
    @Override
    public boolean isInteger() {
        return true;
    }
    
    @Override
    public long getLongValue(final Object criterionUser, final Object criterionTarget, final Object criterionContent, final Object criterionContext) {
        final int craftId = (int)this.m_craftId.getLongValue(criterionUser, criterionTarget, criterionContent, criterionContext);
        final ReferenceCraft craft = CraftManager.INSTANCE.getCraft(craftId);
        if (craft == null) {
            throw new CriteriaExecutionException("Impossible de r\u00e9cup\u00e9rer la nation d'ID " + craftId);
        }
        return super.getSign() * craft.getLearningBookId();
    }
    
    @Override
    public Enum getEnum() {
        return WakfuCriterionIds.GET_CRAFT_LEARNING_ITEM;
    }
    
    static {
        GetCraftLearningItem.signatures = new ArrayList<ParserType[]>();
        final ParserType[] sig = { null };
        sig[0] = (sig[0] = ParserType.NUMBER);
        GetCraftLearningItem.signatures.add(sig);
    }
}
