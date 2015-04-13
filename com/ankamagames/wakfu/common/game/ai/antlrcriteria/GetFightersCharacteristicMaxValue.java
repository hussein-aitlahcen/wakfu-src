package com.ankamagames.wakfu.common.game.ai.antlrcriteria;

import org.jetbrains.annotations.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.wakfu.common.game.fight.protagonists.*;
import com.ankamagames.wakfu.common.datas.specific.*;
import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.characteristic.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.wakfu.common.game.fight.*;
import com.ankamagames.wakfu.common.game.fighter.*;
import java.util.*;

public final class GetFightersCharacteristicMaxValue extends FunctionValue
{
    private static ArrayList<ParserType[]> signatures;
    public static final String MONSTER = "monster";
    private String m_characType;
    private boolean m_monsters;
    
    public GetFightersCharacteristicMaxValue(final ArrayList<ParserObject> args) {
        super();
        this.m_characType = null;
        if (args.isEmpty()) {
            return;
        }
        this.m_characType = args.get(0).getValue();
        this.m_monsters = "monster".equalsIgnoreCase(args.get(1).getValue());
    }
    
    @Override
    protected List<ParserType[]> getSignatures() {
        return GetFightersCharacteristicMaxValue.signatures;
    }
    
    @Override
    public boolean isInteger() {
        return true;
    }
    
    @Override
    public long getLongValue(@Nullable final Object criterionUser, @Nullable final Object criterionTarget, @Nullable final Object criterionContent, @Nullable final Object criterionContext) {
        if (criterionContext == null) {
            throw new CriteriaExecutionException("Pas de contexte...");
        }
        final AbstractFight<?> fight = CriteriaUtils.getFight(criterionUser, criterionContext);
        if (fight == null) {
            throw new CriteriaExecutionException("Impossible de r\u00e9cup\u00e9rer le combat");
        }
        final FighterCharacteristicType characType = GetCharacteristic.getCharacteristicTypeFromString(this.m_characType);
        final byte type = (byte)(this.m_monsters ? 1 : 0);
        final Collection<? extends BasicCharacterInfo> fighters = fight.getFighters(ProtagonistFilter.inPlay(), ProtagonistFilter.ofType(type), ProtagonistFilter.not(ProtagonistFilter.hasProperty(WorldPropertyType.NOT_PRESENT_IN_TIMELINE)));
        int maxValue = 0;
        for (final BasicCharacterInfo f : fighters) {
            if (f.hasCharacteristic(characType)) {
                maxValue = MathHelper.max(maxValue, f.getCharacteristicValue(characType), new int[0]);
            }
        }
        return maxValue;
    }
    
    @Override
    public Enum getEnum() {
        return WakfuCriterionIds.GET_FIGHTERS_CHARACTERISTIC_MAX_VALUE;
    }
    
    static {
        (GetFightersCharacteristicMaxValue.signatures = new ArrayList<ParserType[]>()).add(CriterionConstants.ONE_STRING_SIGNATURE);
        GetFightersCharacteristicMaxValue.signatures.add(new ParserType[] { ParserType.STRING, ParserType.STRING });
    }
}
