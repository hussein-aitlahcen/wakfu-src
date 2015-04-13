package com.ankamagames.wakfu.common.game.ai.antlrcriteria;

import com.ankamagames.wakfu.common.game.fighter.*;
import java.util.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.characteristic.*;
import com.ankamagames.wakfu.common.datas.*;

public final class IsCharacteristicFull extends FunctionCriterion implements Targetable
{
    private boolean m_target;
    private FighterCharacteristicType m_type;
    private static ArrayList<ParserType[]> signatures;
    
    @Override
    protected List<ParserType[]> getSignatures() {
        return IsCharacteristicFull.signatures;
    }
    
    public IsCharacteristicFull(final ArrayList<ParserObject> args) {
        super();
        this.m_target = false;
        final short paramType = this.checkType(args);
        if (paramType == 0) {
            this.m_type = GetCharacteristic.getCharacteristicTypeFromString(args.get(0).getValue());
        }
        if (paramType == 1) {
            this.m_type = GetCharacteristic.getCharacteristicTypeFromString(args.get(0).getValue());
            if (args.get(1).getValue().equalsIgnoreCase("target")) {
                this.m_target = true;
            }
            else {
                this.m_target = false;
            }
        }
    }
    
    @Override
    public int getValidity(final Object criterionUser, final Object criterionTarget, final Object criterionContent, final Object criterionContext) {
        final CriterionUser targetCharacter = CriteriaUtils.getTargetCriterionUserFromParameters(this.m_target, criterionUser, criterionTarget, criterionContext, criterionContent);
        if (targetCharacter == null) {
            return -1;
        }
        if (!targetCharacter.hasCharacteristic(this.m_type)) {
            return -1;
        }
        final int characValue = targetCharacter.getCharacteristicValue(this.m_type);
        final int maxValue = targetCharacter.getCharacteristicMax(this.m_type);
        if (characValue == maxValue) {
            return 0;
        }
        return -1;
    }
    
    @Override
    public Enum getEnum() {
        return WakfuCriterionIds.IS_CHARACTERISTIC_FULL;
    }
    
    @Override
    public boolean isTarget() {
        return this.m_target;
    }
    
    static {
        IsCharacteristicFull.signatures = new ArrayList<ParserType[]>();
        ParserType[] sig = { ParserType.STRING };
        IsCharacteristicFull.signatures.add(sig);
        sig = new ParserType[] { ParserType.STRING, ParserType.STRING };
        IsCharacteristicFull.signatures.add(sig);
    }
}
