package com.ankamagames.wakfu.common.game.ai.antlrcriteria;

import java.util.*;
import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.wakfu.common.game.skill.*;

public class GetSkillLevel extends FunctionValue
{
    private NumericalValue skillId;
    private static ArrayList<ParserType[]> signatures;
    
    @Override
    protected List<ParserType[]> getSignatures() {
        return GetSkillLevel.signatures;
    }
    
    public GetSkillLevel(final ArrayList<ParserObject> args) {
        super();
        this.checkType(args);
        this.skillId = args.get(0);
    }
    
    public int getSkillId() {
        if (this.skillId.isConstant() && this.skillId.isInteger()) {
            return (int)this.skillId.getLongValue(null, null, null, null);
        }
        return -1;
    }
    
    @Override
    public boolean isInteger() {
        return true;
    }
    
    @Override
    public long getLongValue(final Object criterionUser, final Object criterionTarget, final Object criterionContent, final Object criterionContext) {
        if (!(criterionUser instanceof BasicCharacterInfo) && this.skillId.isInteger()) {
            throw new CriteriaExecutionException("Le user du crit\u00e8re n'est pas un personnage ou l'id n'est pas un entier");
        }
        return super.getSign() * ((BasicCharacterInfo)criterionUser).getSkillInventory().getFirstWithReferenceId((int)this.skillId.getLongValue(criterionUser, criterionTarget, criterionContent, criterionContext)).getLevel();
    }
    
    @Override
    public Enum getEnum() {
        return WakfuCriterionIds.GETSKILLLEVEL;
    }
    
    static {
        GetSkillLevel.signatures = new ArrayList<ParserType[]>();
        final ParserType[] sig = { ParserType.NUMBER };
        GetSkillLevel.signatures.add(sig);
    }
}
