package com.ankamagames.wakfu.common.game.ai.antlrcriteria;

import java.util.*;
import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.wakfu.common.game.protector.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;

public class GetNativeNationId extends FunctionValue
{
    private static ArrayList<ParserType[]> signatures;
    private boolean target;
    
    @Override
    protected List<ParserType[]> getSignatures() {
        return GetNativeNationId.signatures;
    }
    
    @Override
    public boolean isInteger() {
        return true;
    }
    
    public GetNativeNationId(final ArrayList<ParserObject> args) {
        super();
        this.target = (this.checkType(args) == 0 && args.get(0).getValue().equalsIgnoreCase("target"));
    }
    
    @Override
    public long getLongValue(final Object criterionUser, final Object criterionTarget, final Object criterionContent, final Object criterionContext) {
        final Object character = this.target ? criterionTarget : criterionUser;
        if (character != null) {
            int nationId = 0;
            if (character instanceof BasicCharacterInfo) {
                final ProtectorBase protector = ((BasicCharacterInfo)character).getProtector();
                if (protector != null) {
                    nationId = protector.getNativeNationId();
                }
            }
            else if (character instanceof ProtectorBase) {
                nationId = ((ProtectorBase)character).getNativeNationId();
            }
            return super.getSign() * nationId;
        }
        throw new CriteriaExecutionException("On essaie de r\u00e9cup\u00e9rer l'id de nation d'une cible invalide");
    }
    
    @Override
    public Enum getEnum() {
        return WakfuCriterionIds.GETNATIVENATIONID;
    }
    
    static {
        GetNativeNationId.signatures = new ArrayList<ParserType[]>();
        ParserType[] sig = { ParserType.STRING };
        GetNativeNationId.signatures.add(sig);
        sig = new ParserType[0];
        GetNativeNationId.signatures.add(sig);
    }
}
