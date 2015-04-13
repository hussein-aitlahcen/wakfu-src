package com.ankamagames.wakfu.common.game.ai.antlrcriteria;

import java.util.*;
import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.wakfu.common.game.spell.*;

public class GetSpellLevel extends FunctionValue
{
    private NumericalValue m_spellId;
    private static ArrayList<ParserType[]> signatures;
    
    public int getSpellId() {
        if (this.m_spellId != null && this.m_spellId.isConstant() && this.m_spellId.isInteger()) {
            return (int)this.m_spellId.getLongValue(null, null, null, null);
        }
        return -1;
    }
    
    @Override
    protected List<ParserType[]> getSignatures() {
        return GetSpellLevel.signatures;
    }
    
    public GetSpellLevel(final ArrayList<ParserObject> args) {
        super();
        this.checkType(args);
        if (args.size() == 1) {
            this.m_spellId = args.get(0);
        }
        else {
            this.m_spellId = null;
        }
    }
    
    @Override
    public boolean isInteger() {
        return true;
    }
    
    @Override
    public long getLongValue(final Object criterionUser, final Object criterionTarget, final Object criterionContent, final Object criterionContext) {
        AbstractSpellLevel spellLevel = null;
        if (this.m_spellId == null && criterionContent instanceof AbstractSpellLevel) {
            spellLevel = (AbstractSpellLevel)criterionContent;
        }
        else {
            if (!(criterionUser instanceof BasicCharacterInfo)) {
                throw new CriteriaExecutionException("Le user du crit\u00e8re n'est pas un personnage ou l'id n'est pas un entier");
            }
            final SpellInventory<? extends AbstractSpellLevel> spellInventory = ((BasicCharacterInfo)criterionUser).getSpellInventory();
            if (spellInventory == null) {
                return 0L;
            }
            spellLevel = spellInventory.getFirstWithReferenceId((int)this.m_spellId.getLongValue(criterionUser, criterionTarget, criterionContent, criterionContext));
        }
        if (spellLevel != null) {
            return super.getSign() * spellLevel.getLevel();
        }
        return 0L;
    }
    
    @Override
    public Enum getEnum() {
        return WakfuCriterionIds.GETSPELLLEVEL;
    }
    
    static {
        GetSpellLevel.signatures = new ArrayList<ParserType[]>();
        ParserType[] sig = { ParserType.NUMBER };
        GetSpellLevel.signatures.add(sig);
        sig = new ParserType[0];
        GetSpellLevel.signatures.add(sig);
    }
}
