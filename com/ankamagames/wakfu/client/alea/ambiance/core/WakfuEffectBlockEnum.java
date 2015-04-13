package com.ankamagames.wakfu.client.alea.ambiance.core;

import com.ankamagames.baseImpl.graphics.alea.ambiance.blocks.*;
import com.ankamagames.baseImpl.graphics.alea.ambiance.models.*;
import com.ankamagames.baseImpl.graphics.alea.ambiance.blocks.operations.*;
import com.ankamagames.baseImpl.graphics.alea.ambiance.models.operators.*;
import com.ankamagames.baseImpl.graphics.alea.ambiance.blocks.providers.*;
import com.ankamagames.baseImpl.graphics.alea.ambiance.models.effects.*;

public enum WakfuEffectBlockEnum implements EffectBlockFactory.BlockEnum
{
    ABS(1, BlockType.Operator), 
    ROUND(2, BlockType.Operator), 
    ADD(3, BlockType.Operator), 
    SUB(4, BlockType.Operator), 
    MULT(5, BlockType.Operator), 
    DIV(6, BlockType.Operator), 
    MIN(7, BlockType.Operator), 
    MAX(8, BlockType.Operator), 
    FUNCTION(9, BlockType.Operator), 
    SELECTER(10, BlockType.Operator), 
    LOWER(1000, BlockType.Operator), 
    EQUALS(1001, BlockType.Operator), 
    GREATER(1002, BlockType.Operator), 
    AND(1003, BlockType.Operator), 
    OR(1004, BlockType.Operator), 
    NOT(1005, BlockType.Operator), 
    FLOAT_CONSTANT(10001, BlockType.Provider), 
    WAKFU_PLAYER(10002, BlockType.Provider), 
    TICK(10003, BlockType.Provider), 
    DAY_RATIO(10004, BlockType.Provider), 
    HUMIDITY(10005, BlockType.Provider), 
    WIND(10006, BlockType.Provider), 
    TEMPERATURE(10007, BlockType.Provider), 
    WAKFU_ZONE(10008, BlockType.Provider), 
    STRING(10009, BlockType.Provider), 
    RANDOM_FLOAT(10010, BlockType.Provider), 
    ENVELOP(10011, BlockType.Provider), 
    SEASON(10012, BlockType.Provider), 
    GLOBAL_COLOR(20001, BlockType.Effect), 
    RAIN(20003, BlockType.Effect), 
    SNOW(20004, BlockType.Effect), 
    CLOUD(20005, BlockType.Effect), 
    MAP_LIGHTING_MODIFER(20007, BlockType.Effect), 
    CAMERA_SHAKE(20008, BlockType.Effect);
    
    private final int m_typeId;
    private final BlockType m_blockType;
    private static final WakfuBlockEnumFactory m_factory;
    
    public static void test() throws Exception {
        final WakfuEffectBlockEnum[] values = values();
        for (int i = 0; i < values.length - 1; ++i) {
            for (int j = i + 1; j < values.length; ++j) {
                if (values[i].getTypeId() == values[j].getTypeId()) {
                    throw new Exception(values[i].name() + " et " + values[j].name() + "ont le m\u00eame id");
                }
            }
        }
    }
    
    private WakfuEffectBlockEnum(final int typeId, final BlockType type) {
        this.m_typeId = typeId;
        this.m_blockType = type;
    }
    
    @Override
    public final int getTypeId() {
        return this.m_typeId;
    }
    
    @Override
    public final BlockType getBlockType() {
        return this.m_blockType;
    }
    
    public static String getNameFromType(final int typeId) {
        return WakfuEffectBlockEnum.m_factory.getBlockName(typeId);
    }
    
    public static AbstractModel createModel(final WakfuEffectBlockEnum c) {
        return WakfuEffectBlockEnum.m_factory.createModel(c);
    }
    
    public static WakfuBlockEnumFactory getModelFactory() {
        return WakfuEffectBlockEnum.m_factory;
    }
    
    static {
        m_factory = new WakfuBlockEnumFactory();
    }
    
    private static class WakfuBlockEnumFactory implements EffectBlockFactory.ModelFactory<WakfuEffectBlockEnum>
    {
        @Override
        public WakfuEffectBlockEnum getBlockEnum(final String enumName) {
            return WakfuEffectBlockEnum.valueOf(enumName);
        }
        
        @Override
        public WakfuEffectBlockEnum getBlockEnumFromTypeId(final int typeId) {
            for (final WakfuEffectBlockEnum e : WakfuEffectBlockEnum.values()) {
                if (e.getTypeId() == typeId) {
                    return e;
                }
            }
            return null;
        }
        
        @Override
        public String getBlockName(final int typeId) {
            final WakfuEffectBlockEnum e = this.getBlockEnumFromTypeId(typeId);
            return (e == null) ? ("<unknow " + typeId + ">") : e.name();
        }
        
        @Override
        public AbstractModel createModel(final WakfuEffectBlockEnum blockType) {
            final int typeId = blockType.getTypeId();
            switch (blockType) {
                case ABS: {
                    return new OperatorModel1(typeId, Operation1.Abs);
                }
                case ROUND: {
                    return new OperatorModel1(typeId, Operation1.Round);
                }
                case ADD: {
                    return new OperatorModel2(typeId, Operation2.Add);
                }
                case SUB: {
                    return new OperatorModel2(typeId, Operation2.Sub);
                }
                case MULT: {
                    return new OperatorModel2(typeId, Operation2.Mult);
                }
                case DIV: {
                    return new OperatorModel2(typeId, Operation2.Div);
                }
                case MIN: {
                    return new OperatorModel2(typeId, Operation2.Min);
                }
                case MAX: {
                    return new OperatorModel2(typeId, Operation2.Max);
                }
                case FUNCTION: {
                    return new FunctionModel(typeId);
                }
                case LOWER: {
                    return new OperatorModel2(typeId, Operation2.Lower);
                }
                case GREATER: {
                    return new OperatorModel2(typeId, Operation2.Greater);
                }
                case EQUALS: {
                    return new OperatorModel2(typeId, Operation2.Equals);
                }
                case AND: {
                    return new OperatorModel2(typeId, Operation2.And);
                }
                case OR: {
                    return new OperatorModel2(typeId, Operation2.Or);
                }
                case NOT: {
                    return new OperatorModel1(typeId, Operation1.Not);
                }
                case FLOAT_CONSTANT: {
                    return new ConstFloatProvider.Model(typeId);
                }
                case WAKFU_PLAYER: {
                    return null;
                }
                case TICK: {
                    return new TickProvider.Model(typeId);
                }
                case DAY_RATIO: {
                    return null;
                }
                case HUMIDITY: {
                    return null;
                }
                case WIND: {
                    return null;
                }
                case TEMPERATURE: {
                    return null;
                }
                case WAKFU_ZONE: {
                    return null;
                }
                case STRING: {
                    return new FileProvider.Model(typeId);
                }
                case RANDOM_FLOAT: {
                    return new RandomFloatProvider.Model(typeId);
                }
                case ENVELOP: {
                    return new EnvelopProvider.Model(typeId);
                }
                case SEASON: {
                    return null;
                }
                case GLOBAL_COLOR: {
                    return new GlobalColorModel(typeId);
                }
                case RAIN: {
                    return new RainModel(typeId);
                }
                case SNOW: {
                    return new SnowModel(typeId);
                }
                case CLOUD: {
                    return new CloudModel(typeId);
                }
                case MAP_LIGHTING_MODIFER: {
                    return new MapLightingModifierModel(typeId);
                }
                case CAMERA_SHAKE: {
                    return new CameraShakeModel(typeId);
                }
                default: {
                    return null;
                }
            }
        }
    }
}
