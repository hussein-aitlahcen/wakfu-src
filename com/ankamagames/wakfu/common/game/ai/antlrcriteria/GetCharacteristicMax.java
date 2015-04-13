package com.ankamagames.wakfu.common.game.ai.antlrcriteria;

import com.ankamagames.wakfu.common.game.fighter.*;
import java.util.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.characteristic.*;
import com.ankamagames.wakfu.common.datas.*;

public class GetCharacteristicMax extends FunctionValue implements Targetable
{
    private FighterCharacteristicType type;
    private boolean target;
    private static ArrayList<ParserType[]> signatures;
    
    public FighterCharacteristicType getCharacType() {
        return this.type;
    }
    
    @Override
    protected List<ParserType[]> getSignatures() {
        return GetCharacteristicMax.signatures;
    }
    
    @Override
    public boolean isInteger() {
        return true;
    }
    
    public GetCharacteristicMax(final ArrayList<ParserObject> args) {
        super();
        final short paramType = this.checkType(args);
        if (paramType == 0) {
            this.type = GetCharacteristic.getCharacteristicTypeFromString(args.get(0).getValue());
        }
        if (paramType == 1) {
            this.type = GetCharacteristic.getCharacteristicTypeFromString(args.get(0).getValue());
            if (args.get(1).getValue().equalsIgnoreCase("target")) {
                this.target = true;
            }
            else {
                this.target = false;
            }
        }
    }
    
    @Override
    public long getLongValue(final Object criterionUser, final Object criterionTarget, final Object criterionContent, final Object criterionContext) {
        long value = 0L;
        final CriterionUser targetCharacter = CriteriaUtils.getTargetCriterionUserFromParameters(this.target, criterionUser, criterionTarget, criterionContext, criterionContent);
        if (targetCharacter == null) {
            return -1L;
        }
        if (targetCharacter.hasCharacteristic(this.type)) {
            value = targetCharacter.getCharacteristicMax(this.type);
            return super.getSign() * value;
        }
        GetCharacteristicMax.m_logger.error((Object)("Trying to get GetCharacteristicMax for a character without this charac. Charac=" + this.type + " criterionUser=" + criterionUser + " criterionTarget=" + criterionTarget + " criterionContent=" + criterionContent));
        return -1L;
    }
    
    @Override
    public Enum getEnum() {
        return WakfuCriterionIds.GETCHARACTERISTICMAX;
    }
    
    @Override
    public boolean isTarget() {
        return this.target;
    }
    
    static {
        GetCharacteristicMax.signatures = new ArrayList<ParserType[]>();
        ParserType[] sig = { ParserType.STRING };
        GetCharacteristicMax.signatures.add(sig);
        sig = new ParserType[] { ParserType.STRING, ParserType.STRING };
        GetCharacteristicMax.signatures.add(sig);
    }
}
