package com.ankamagames.wakfu.common.game.ai.antlrcriteria;

import java.util.*;
import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;

public class GetNationId extends FunctionValue
{
    private static ArrayList<ParserType[]> signatures;
    private boolean m_target;
    
    @Override
    protected List<ParserType[]> getSignatures() {
        return GetNationId.signatures;
    }
    
    @Override
    public boolean isInteger() {
        return true;
    }
    
    public GetNationId(final ArrayList<ParserObject> args) {
        super();
        this.m_target = (this.checkType(args) == 0 && args.get(0).getValue().equalsIgnoreCase("target"));
    }
    
    @Override
    public long getLongValue(final Object criterionUser, final Object criterionTarget, final Object criterionContent, final Object criterionContext) {
        final Object character = this.m_target ? criterionTarget : criterionUser;
        if (character != null) {
            int nationId = 0;
            if (character instanceof BasicCharacterInfo) {
                nationId = ((BasicCharacterInfo)character).getCitizenComportment().getNationId();
            }
            return super.getSign() * nationId;
        }
        throw new CriteriaExecutionException("On essaie de r\u00e9cup\u00e9rer l'id de nation d'une cible invalide");
    }
    
    @Override
    public Enum getEnum() {
        return WakfuCriterionIds.GETNATIONID;
    }
    
    static {
        GetNationId.signatures = new ArrayList<ParserType[]>();
        ParserType[] sig = { ParserType.STRING };
        GetNationId.signatures.add(sig);
        sig = new ParserType[0];
        GetNationId.signatures.add(sig);
    }
}
