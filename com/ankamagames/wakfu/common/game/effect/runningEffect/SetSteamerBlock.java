package com.ankamagames.wakfu.common.game.effect.runningEffect;

import com.ankamagames.framework.kernel.core.common.serialization.*;
import java.nio.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.wakfu.common.game.fighter.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.characteristic.*;
import java.util.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;
import com.ankamagames.framework.external.*;
import com.ankamagames.wakfu.common.game.effect.*;

public final class SetSteamerBlock extends SetEffectArea
{
    private static final ObjectPool m_staticPool;
    private static final ParameterListSet PARAMETERS_LIST_SET;
    private int m_blockHp;
    private BinarSerialPart ADDITIONAL_DATA;
    
    @Override
    public ParameterListSet getParametersListSet() {
        return SetSteamerBlock.PARAMETERS_LIST_SET;
    }
    
    public SetSteamerBlock() {
        super();
        this.ADDITIONAL_DATA = new BinarSerialPart(19) {
            @Override
            public void serialize(final ByteBuffer buffer) {
                buffer.putLong(SetSteamerBlock.this.m_newTargetId);
                buffer.putShort(SetSteamerBlock.this.m_zoneLevel);
                buffer.put((byte)(SetSteamerBlock.this.m_shouldBeInfinite ? 1 : 0));
                if (SetSteamerBlock.this.m_areaDirection != null) {
                    buffer.putInt(SetSteamerBlock.this.m_areaDirection.getIndex());
                }
                else {
                    buffer.putInt(-1);
                }
                buffer.putInt(SetSteamerBlock.this.m_blockHp);
            }
            
            @Override
            public void unserialize(final ByteBuffer buffer, final int version) {
                SetSteamerBlock.this.m_newTargetId = buffer.getLong();
                SetSteamerBlock.this.m_zoneLevel = buffer.getShort();
                SetSteamerBlock.this.m_shouldBeInfinite = (buffer.get() == 1);
                final int directionIdx = buffer.getInt();
                if (directionIdx == -1) {
                    SetSteamerBlock.this.m_areaDirection = null;
                }
                else {
                    SetSteamerBlock.this.m_areaDirection = Direction8.getDirectionFromIndex(directionIdx);
                }
                SetSteamerBlock.this.m_blockHp = buffer.getInt();
            }
        };
        this.setTriggersToExecute();
    }
    
    @Override
    public SetSteamerBlock newInstance() {
        SetSteamerBlock re;
        try {
            re = (SetSteamerBlock)SetSteamerBlock.m_staticPool.borrowObject();
            re.m_pool = SetSteamerBlock.m_staticPool;
        }
        catch (Exception e) {
            re = new SetSteamerBlock();
            re.m_pool = null;
            re.m_isStatic = false;
            SetSteamerBlock.m_logger.error((Object)("Erreur lors d'un checkOut sur un SetSteamerBlock : " + e.getMessage()));
        }
        return re;
    }
    
    @Override
    protected void executeOverride(final RunningEffect linkedRE, final boolean trigger) {
        super.executeOverride(linkedRE, trigger);
        this.initializeBlock();
    }
    
    @Override
    public void effectiveComputeValue(final RunningEffect triggerRE) {
        final short level = this.getContainerLevel();
        if (this.m_genericEffect != null) {
            this.m_value = ((WakfuEffect)this.m_genericEffect).getParam(0, level, RoundingMethod.LIKE_PREVIOUS_LEVEL);
        }
        this.m_newTargetId = this.m_context.getEffectUserInformationProvider().getNextFreeEffectUserId((byte)2);
        this.m_zoneLevel = this.getContainerLevel();
        this.m_shouldBeInfinite = false;
        this.m_areaDirection = Direction8.NORTH_EAST;
        this.m_blockHp = this.computeHp();
    }
    
    private int computeHp() {
        final int baseHp = ((WakfuEffect)this.m_genericEffect).getParam(1, this.getContainerLevel(), RoundingMethod.LIKE_PREVIOUS_LEVEL);
        if (this.m_caster == null) {
            return baseHp;
        }
        final int earthDmg = this.m_caster.getCharacteristicValue(FighterCharacteristicType.DMG_EARTH_PERCENT);
        final int percentDmg = this.m_caster.getCharacteristicValue(FighterCharacteristicType.DMG_IN_PERCENT);
        return baseHp + baseHp * (earthDmg + percentDmg) / 100;
    }
    
    private void initializeBlock() {
        if (this.m_caster == null) {
            return;
        }
        if (this.m_area == null) {
            return;
        }
        final AbstractCharacteristic hp = this.m_area.getCharacteristic(FighterCharacteristicType.HP);
        hp.setMax(this.m_blockHp);
        hp.toMax();
        this.copyCharac(FighterCharacteristicType.RES_AIR_PERCENT);
        this.copyCharac(FighterCharacteristicType.RES_EARTH_PERCENT);
        this.copyCharac(FighterCharacteristicType.RES_FIRE_PERCENT);
        this.copyCharac(FighterCharacteristicType.RES_WATER_PERCENT);
        this.copyCharac(FighterCharacteristicType.RES_IN_PERCENT);
        final Iterable<RunningEffect> passiveResist = this.m_caster.getRunningEffectManager().getEffectsWithActionId(RunningEffectConstants.STEAMER_BLOCK_PASSIVE_RESIST.getId());
        final AbstractCharacteristic res = this.m_area.getCharacteristic(FighterCharacteristicType.RES_IN_PERCENT);
        for (final RunningEffect effect : passiveResist) {
            res.add(effect.getValue());
        }
    }
    
    private void copyCharac(final FighterCharacteristicType charac) {
        this.m_area.getCharacteristic(charac).set(this.m_caster.getCharacteristicValue(charac));
    }
    
    @Override
    public BinarSerialPart getAdditionalDatasBinarSerialPart() {
        return this.ADDITIONAL_DATA;
    }
    
    static {
        m_staticPool = new MonitoredPool(new ObjectFactory<SetSteamerBlock>() {
            @Override
            public SetSteamerBlock makeObject() {
                return new SetSteamerBlock();
            }
        });
        PARAMETERS_LIST_SET = new WakfuRunningEffectParameterListSet(new ParameterList[] { new WakfuRunningEffectParameterList("Base HP", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("Id du bloc", WakfuRunningEffectParameterType.VALUE), new WakfuRunningEffectParameter("Base HP", WakfuRunningEffectParameterType.CONFIG) }) });
    }
}
