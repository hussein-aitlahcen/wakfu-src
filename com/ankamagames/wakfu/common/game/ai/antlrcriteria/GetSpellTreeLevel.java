package com.ankamagames.wakfu.common.game.ai.antlrcriteria;

import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.wakfu.common.game.spell.*;
import java.util.*;

public class GetSpellTreeLevel extends FunctionValue
{
    private NumericalValue m_elementId;
    private static ArrayList<ParserType[]> signatures;
    
    public int getElementId() {
        if (this.m_elementId.isConstant() && this.m_elementId.isInteger()) {
            return (int)this.m_elementId.getLongValue(null, null, null, null);
        }
        return -1;
    }
    
    @Override
    protected List<ParserType[]> getSignatures() {
        return GetSpellTreeLevel.signatures;
    }
    
    public GetSpellTreeLevel(final ArrayList<ParserObject> args) {
        super();
        this.checkType(args);
        this.m_elementId = args.get(0);
    }
    
    @Override
    public boolean isInteger() {
        return true;
    }
    
    @Override
    public long getLongValue(final Object criterionUser, final Object criterionTarget, final Object criterionContent, final Object criterionContext) {
        if (!this.m_elementId.isInteger()) {
            throw new CriteriaExecutionException("L'id n'est pas un entier");
        }
        final int elementId = this.getElementId();
        Iterable spellInventory;
        if (criterionUser instanceof BasicCharacterInfo) {
            spellInventory = ((BasicCharacterInfo)criterionUser).getSpellInventory();
        }
        else {
            if (!(criterionUser instanceof Iterable)) {
                throw new CriteriaExecutionException("CriterionUser is neither a BasicCharacterInfo nor a SpellInventory");
            }
            spellInventory = (Iterable)criterionUser;
        }
        int treeLevel = 0;
        for (final Object content : spellInventory) {
            if (!(content instanceof AbstractSpellLevel)) {
                throw new CriteriaExecutionException("Inventory is not a spell inventory");
            }
            final AbstractSpellLevel spellLevel = (AbstractSpellLevel)content;
            if (spellLevel.getSpell().getElementId() != elementId) {
                continue;
            }
            treeLevel += spellLevel.getLevel();
        }
        return super.getSign() * treeLevel;
    }
    
    @Override
    public Enum getEnum() {
        return WakfuCriterionIds.GETSPELLTREELEVEL;
    }
    
    static {
        GetSpellTreeLevel.signatures = new ArrayList<ParserType[]>();
        final ParserType[] sig = { ParserType.NUMBER };
        GetSpellTreeLevel.signatures.add(sig);
    }
}
