package com.ankamagames.wakfu.common.game.ai.antlrcriteria;

import java.util.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import org.jetbrains.annotations.*;
import com.ankamagames.wakfu.common.game.fighter.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.characteristic.*;
import com.ankamagames.wakfu.common.datas.*;

public final class GetTotalHpInPercent extends FunctionValue
{
    private boolean m_target;
    private static ArrayList<ParserType[]> signatures;
    
    public GetTotalHpInPercent(final ArrayList<ParserObject> args) {
        super();
        this.checkType(args);
        if (args.get(0).getValue().equalsIgnoreCase("target")) {
            this.m_target = true;
        }
        else {
            this.m_target = false;
        }
    }
    
    @Override
    protected List<ParserType[]> getSignatures() {
        return GetTotalHpInPercent.signatures;
    }
    
    @Override
    public boolean isInteger() {
        return false;
    }
    
    @Override
    public long getLongValue(@Nullable final Object criterionUser, @Nullable final Object criterionTarget, @Nullable final Object criterionContent, @Nullable final Object criterionContext) {
        throw new UnsupportedOperationException("Can't get Long value of a double");
    }
    
    @Override
    public double getDoubleValue(@Nullable final Object criterionUser, @Nullable final Object criterionTarget, @Nullable final Object criterionContent, @Nullable final Object criterionContext) {
        double value = 0.0;
        final CriterionUser targetCharacter = CriteriaUtils.getTargetCriterionUserFromParameters(this.m_target, criterionUser, criterionTarget, criterionContext, criterionContent);
        if (targetCharacter == null) {
            return -1.0;
        }
        final FighterCharacteristicType hp = FighterCharacteristicType.HP;
        final FighterCharacteristicType virtualHp = FighterCharacteristicType.VIRTUAL_HP;
        int characValue = 0;
        int maxValue = 0;
        if (targetCharacter.hasCharacteristic(hp)) {
            characValue = targetCharacter.getCharacteristicValue(hp);
            maxValue = targetCharacter.getCharacteristicMax(hp);
        }
        if (targetCharacter.hasCharacteristic(virtualHp)) {
            characValue += targetCharacter.getCharacteristicValue(virtualHp);
            maxValue += targetCharacter.getCharacteristicMax(virtualHp);
        }
        if (maxValue == 0) {
            return 0.0;
        }
        value = characValue / maxValue * 100.0f;
        return super.getSign() * value;
    }
    
    @Override
    public Enum getEnum() {
        return WakfuCriterionIds.GET_TOTAL_HP_IN_PCT;
    }
    
    static {
        (GetTotalHpInPercent.signatures = new ArrayList<ParserType[]>()).add(CriterionConstants.ONE_STRING_SIGNATURE);
    }
}
