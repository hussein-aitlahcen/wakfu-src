package com.ankamagames.wakfu.common.game.ai.antlrcriteria;

import com.ankamagames.wakfu.common.game.fighter.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.wakfu.common.game.fight.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.characteristic.*;
import com.ankamagames.wakfu.common.datas.*;
import java.util.*;

public class GetCharacteristic extends FunctionValue implements Targetable
{
    private FighterCharacteristicType type;
    private boolean target;
    private static ArrayList<ParserType[]> signatures;
    
    public static FighterCharacteristicType getCharacteristicTypeFromString(final String type) {
        if (type.equalsIgnoreCase("hp")) {
            return FighterCharacteristicType.HP;
        }
        if (type.equalsIgnoreCase("trap") || type.equalsIgnoreCase("nbtrap") || type.equalsIgnoreCase("maxtrap") || type.equalsIgnoreCase("contr\u00f4le") || type.equalsIgnoreCase("control")) {
            return FighterCharacteristicType.LEADERSHIP;
        }
        if (type.equalsIgnoreCase("mp") || type.equalsIgnoreCase("pm")) {
            return FighterCharacteristicType.MP;
        }
        if (type.equalsIgnoreCase("wp") || type.equalsIgnoreCase("pw")) {
            return FighterCharacteristicType.WP;
        }
        if (type.equalsIgnoreCase("ap") || type.equalsIgnoreCase("pa")) {
            return FighterCharacteristicType.AP;
        }
        if (type.equalsIgnoreCase("summons") || type.equalsIgnoreCase("nbsummons") || type.equalsIgnoreCase("commandement") || type.equalsIgnoreCase("leadership")) {
            return FighterCharacteristicType.LEADERSHIP;
        }
        if (type.equalsIgnoreCase("for") || type.equalsIgnoreCase("strength") || type.equalsIgnoreCase("force")) {
            return FighterCharacteristicType.EARTH_MASTERY;
        }
        if (type.equalsIgnoreCase("agi")) {
            return FighterCharacteristicType.AIR_MASTERY;
        }
        if (type.equalsIgnoreCase("int")) {
            return FighterCharacteristicType.FIRE_MASTERY;
        }
        if (type.equalsIgnoreCase("chan")) {
            return FighterCharacteristicType.WATER_MASTERY;
        }
        if (type.equalsIgnoreCase("sag")) {
            return FighterCharacteristicType.WISDOM;
        }
        if (type.equalsIgnoreCase("dmg_earth")) {
            return FighterCharacteristicType.DMG_EARTH_PERCENT;
        }
        if (type.equalsIgnoreCase("dmg_air")) {
            return FighterCharacteristicType.DMG_AIR_PERCENT;
        }
        if (type.equalsIgnoreCase("dmg_fire")) {
            return FighterCharacteristicType.DMG_FIRE_PERCENT;
        }
        if (type.equalsIgnoreCase("dmg_water")) {
            return FighterCharacteristicType.DMG_WATER_PERCENT;
        }
        if (type.equalsIgnoreCase("res_earth")) {
            return FighterCharacteristicType.RES_EARTH_PERCENT;
        }
        if (type.equalsIgnoreCase("res_air")) {
            return FighterCharacteristicType.RES_AIR_PERCENT;
        }
        if (type.equalsIgnoreCase("res_fire")) {
            return FighterCharacteristicType.RES_FIRE_PERCENT;
        }
        if (type.equalsIgnoreCase("res_water")) {
            return FighterCharacteristicType.RES_WATER_PERCENT;
        }
        if (type.equalsIgnoreCase("res_stasis")) {
            return FighterCharacteristicType.RES_STASIS_PERCENT;
        }
        if (type.equalsIgnoreCase("sag") && type.equalsIgnoreCase("vit")) {
            return FighterCharacteristicType.VITALITY;
        }
        if (type.equalsIgnoreCase("init")) {
            return FighterCharacteristicType.INIT;
        }
        if (type.equalsIgnoreCase("prosp")) {
            return FighterCharacteristicType.PROSPECTION;
        }
        if (type.equalsIgnoreCase("chrage")) {
            return FighterCharacteristicType.CHRAGE;
        }
        if (type.equalsIgnoreCase("dmg_in_percent")) {
            return FighterCharacteristicType.DMG_IN_PERCENT;
        }
        if (type.equalsIgnoreCase("plate")) {
            return FighterCharacteristicType.ARMOR_PLATE;
        }
        if (type.equalsIgnoreCase("mechanics")) {
            return FighterCharacteristicType.LEADERSHIP;
        }
        if (type.equalsIgnoreCase("virtual_hp")) {
            return FighterCharacteristicType.VIRTUAL_HP;
        }
        if (type.equalsIgnoreCase("ferocity")) {
            return FighterCharacteristicType.FEROCITY;
        }
        if (type.equalsIgnoreCase("fumble_rate")) {
            return FighterCharacteristicType.FUMBLE_RATE;
        }
        if (type.equalsIgnoreCase("critical_bonus")) {
            return FighterCharacteristicType.CRITICAL_BONUS;
        }
        if (type.equalsIgnoreCase("osa_invocation_knowledge") || type.equalsIgnoreCase("invocation_knowledge") || type.equalsIgnoreCase("ik") || type.equalsIgnoreCase("InvocationKnowledge")) {
            return FighterCharacteristicType.OSA_INVOCATION_KNOWLEDGE;
        }
        try {
            return FighterCharacteristicType.valueOf(type.toUpperCase());
        }
        catch (IllegalArgumentException e) {
            GetCharacteristic.m_logger.error((Object)("GetCharacteristic criteria with invalid parameter : " + type + " : unknown characteristic"));
            return null;
        }
    }
    
    public FighterCharacteristicType getCharacType() {
        return this.type;
    }
    
    @Override
    protected List<ParserType[]> getSignatures() {
        return GetCharacteristic.signatures;
    }
    
    @Override
    public boolean isInteger() {
        return true;
    }
    
    public GetCharacteristic(final ArrayList<ParserObject> args) {
        super();
        final short paramType = this.checkType(args);
        if (paramType == 0) {
            this.type = getCharacteristicTypeFromString(args.get(0).getValue());
        }
        if (paramType == 1) {
            this.type = getCharacteristicTypeFromString(args.get(0).getValue());
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
        Object character = this.target ? criterionTarget : criterionUser;
        if (!(character instanceof CriterionUser)) {
            if (!this.target || !(criterionTarget instanceof Point3) || !(criterionContext instanceof AbstractFight)) {
                return -1L;
            }
            final AbstractFight<BasicCharacterInfo> fight = (AbstractFight<BasicCharacterInfo>)criterionContext;
            final Point3 position = (Point3)criterionTarget;
            for (final EffectUser effectUser : fight.getPossibleTargetsAtPosition(position)) {
                if (effectUser.hasCharacteristic(this.type)) {
                    character = effectUser;
                    break;
                }
            }
        }
        if (!(character instanceof CriterionUser)) {
            return -1L;
        }
        final CriterionUser effectUser2 = (CriterionUser)character;
        if (!effectUser2.hasCharacteristic(this.type)) {
            return -1L;
        }
        value = effectUser2.getCharacteristicValue(this.type);
        return super.getSign() * value;
    }
    
    @Override
    public Enum getEnum() {
        return WakfuCriterionIds.GETCHARACTERISTIC;
    }
    
    @Override
    public boolean isTarget() {
        return this.target;
    }
    
    static {
        GetCharacteristic.signatures = new ArrayList<ParserType[]>();
        ParserType[] sig = { ParserType.STRING };
        GetCharacteristic.signatures.add(sig);
        sig = new ParserType[] { ParserType.STRING, ParserType.STRING };
        GetCharacteristic.signatures.add(sig);
    }
}
