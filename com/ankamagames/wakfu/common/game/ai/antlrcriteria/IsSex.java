package com.ankamagames.wakfu.common.game.ai.antlrcriteria;

import java.util.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.wakfu.common.datas.*;

public class IsSex extends FunctionCriterion
{
    private byte m_sex;
    private boolean m_target;
    private static ArrayList<ParserType[]> signatures;
    
    @Override
    protected List<ParserType[]> getSignatures() {
        return IsSex.signatures;
    }
    
    public boolean isTarget() {
        return this.m_target;
    }
    
    public IsSex(final ArrayList<ParserObject> args) {
        super();
        this.m_target = false;
        final byte type = this.checkType(args);
        if (type == 1) {
            final String isTarget = args.get(1).getValue();
            if (isTarget.equalsIgnoreCase("target")) {
                this.m_target = true;
            }
        }
        final String sex = args.get(0).getValue();
        if (sex.equalsIgnoreCase("Male")) {
            this.m_sex = 0;
        }
        else {
            this.m_sex = 1;
        }
    }
    
    @Override
    public int getValidity(final Object criterionUser, final Object criterionTarget, final Object criterionContent, final Object criterionContext) {
        final BasicCharacterInfo targetCharacter = CriteriaUtils.getTargetCharacterInfoFromParameters(this.m_target, criterionUser, criterionTarget, criterionContext, criterionContent);
        if (targetCharacter == null) {
            return -1;
        }
        if (targetCharacter.getSex() == this.m_sex) {
            return 0;
        }
        return 1;
    }
    
    @Override
    public Enum getEnum() {
        return WakfuCriterionIds.ISSEX;
    }
    
    public byte getSex() {
        return this.m_sex;
    }
    
    static {
        IsSex.signatures = new ArrayList<ParserType[]>();
        ParserType[] sig = { ParserType.STRING };
        IsSex.signatures.add(sig);
        sig = new ParserType[] { ParserType.STRING, ParserType.STRING };
        IsSex.signatures.add(sig);
    }
}
