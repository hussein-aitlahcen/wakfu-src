package com.ankamagames.wakfu.common.game.effect.runningEffect;

import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.framework.kernel.core.common.serialization.*;
import java.nio.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effectArea.*;
import com.ankamagames.wakfu.common.datas.specific.*;
import com.ankamagames.wakfu.common.game.fight.time.*;
import com.ankamagames.wakfu.common.game.fighter.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.characteristic.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.fight.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;
import com.ankamagames.framework.external.*;
import com.ankamagames.wakfu.common.game.effect.*;

public class PropertyApply extends WakfuRunningEffect
{
    private static final ObjectPool m_staticPool;
    private static final ParameterListSet PARAMETERS_LIST_SET;
    private PropertyType m_property;
    private boolean m_worldPropertyAlreadyApplied;
    private boolean m_executed;
    private byte m_nbProperties;
    private Point3 m_serializedInvisiblePosition;
    private Direction8 m_serializedDirection;
    public final BinarSerialPart ADDITIONNAL_DATAS;
    
    @Override
    public ParameterListSet getParametersListSet() {
        return PropertyApply.PARAMETERS_LIST_SET;
    }
    
    public PropertyApply() {
        super();
        this.m_executed = false;
        this.m_nbProperties = -1;
        this.ADDITIONNAL_DATAS = new BinarSerialPart(12) {
            @Override
            public void serialize(final ByteBuffer buffer) {
                if (PropertyApply.this.m_property == FightPropertyType.INVISIBLE || PropertyApply.this.m_property == FightPropertyType.INVISIBLE_SUPERIOR) {
                    buffer.put((byte)1);
                    buffer.putInt(PropertyApply.this.m_target.getWorldCellX());
                    buffer.putInt(PropertyApply.this.m_target.getWorldCellY());
                    buffer.putShort(PropertyApply.this.m_target.getWorldCellAltitude());
                    buffer.put((byte)PropertyApply.this.m_target.getDirection().m_index);
                }
                else {
                    buffer.put((byte)0);
                }
            }
            
            @Override
            public void unserialize(final ByteBuffer buffer, final int version) {
                if (buffer.get() == 1) {
                    PropertyApply.this.m_serializedInvisiblePosition = new Point3(buffer.getInt(), buffer.getInt(), buffer.getShort());
                    PropertyApply.this.m_serializedDirection = Direction8.getDirectionFromIndex(buffer.get());
                }
                else {
                    PropertyApply.this.m_serializedInvisiblePosition = null;
                }
            }
            
            @Override
            public int expectedSize() {
                if (PropertyApply.this.m_property == FightPropertyType.INVISIBLE || PropertyApply.this.m_property == FightPropertyType.INVISIBLE_SUPERIOR) {
                    return 12;
                }
                return 1;
            }
        };
        this.setTriggersToExecute();
    }
    
    public PropertyApply(final PropertyType charac) {
        super();
        this.m_executed = false;
        this.m_nbProperties = -1;
        this.ADDITIONNAL_DATAS = new BinarSerialPart(12) {
            @Override
            public void serialize(final ByteBuffer buffer) {
                if (PropertyApply.this.m_property == FightPropertyType.INVISIBLE || PropertyApply.this.m_property == FightPropertyType.INVISIBLE_SUPERIOR) {
                    buffer.put((byte)1);
                    buffer.putInt(PropertyApply.this.m_target.getWorldCellX());
                    buffer.putInt(PropertyApply.this.m_target.getWorldCellY());
                    buffer.putShort(PropertyApply.this.m_target.getWorldCellAltitude());
                    buffer.put((byte)PropertyApply.this.m_target.getDirection().m_index);
                }
                else {
                    buffer.put((byte)0);
                }
            }
            
            @Override
            public void unserialize(final ByteBuffer buffer, final int version) {
                if (buffer.get() == 1) {
                    PropertyApply.this.m_serializedInvisiblePosition = new Point3(buffer.getInt(), buffer.getInt(), buffer.getShort());
                    PropertyApply.this.m_serializedDirection = Direction8.getDirectionFromIndex(buffer.get());
                }
                else {
                    PropertyApply.this.m_serializedInvisiblePosition = null;
                }
            }
            
            @Override
            public int expectedSize() {
                if (PropertyApply.this.m_property == FightPropertyType.INVISIBLE || PropertyApply.this.m_property == FightPropertyType.INVISIBLE_SUPERIOR) {
                    return 12;
                }
                return 1;
            }
        };
        this.m_property = charac;
        this.setTriggersToExecute();
    }
    
    @Override
    public void onCheckIn() {
        super.onCheckIn();
    }
    
    public PropertyType getProperty() {
        return this.m_property;
    }
    
    @Override
    public PropertyApply newInstance() {
        PropertyApply re;
        try {
            re = (PropertyApply)PropertyApply.m_staticPool.borrowObject();
            re.m_pool = PropertyApply.m_staticPool;
        }
        catch (Exception e) {
            re = new PropertyApply();
            re.m_pool = null;
            re.m_isStatic = false;
            PropertyApply.m_logger.error((Object)("Erreur lors d'un checkOut sur un CharacBuff : " + e.getMessage()));
        }
        re.m_property = this.m_property;
        re.m_worldPropertyAlreadyApplied = this.m_worldPropertyAlreadyApplied;
        re.m_nbProperties = this.m_nbProperties;
        return re;
    }
    
    @Override
    public void setTriggersToExecute() {
        super.setTriggersToExecute();
    }
    
    @Override
    protected void executeOverride(final RunningEffect linkedRE, final boolean trigger) {
        if (this.m_target == null || this.m_property == null) {
            this.setNotified(true);
            return;
        }
        switch (this.m_property.getPropertyTypeId()) {
            case 4: {
                if (this.m_target instanceof BasicEffectArea) {
                    if (this.m_nbProperties > 0) {
                        final byte value = this.m_target.getPropertyValue(this.m_property);
                        if (value + this.m_nbProperties > 127) {
                            this.m_nbProperties = (byte)(127 - value);
                        }
                        this.m_target.setPropertyValue(this.m_property, this.m_nbProperties);
                    }
                    else {
                        this.m_target.addProperty(this.m_property);
                    }
                    this.m_executed = true;
                    break;
                }
                break;
            }
            case 0: {
                if (this.getContext().getContextType() != 1) {
                    if (this.m_nbProperties > 0) {
                        final byte value = this.m_target.getPropertyValue(this.m_property);
                        if (value + this.m_nbProperties > 127) {
                            this.m_nbProperties = (byte)(127 - value);
                        }
                        this.m_target.setPropertyValue(this.m_property, this.m_nbProperties);
                    }
                    else {
                        this.m_target.addProperty(this.m_property);
                    }
                    this.m_executed = true;
                    break;
                }
                final FightPropertyType fightPropertyType = ((WorldPropertyType)this.m_property).getEquivalentFightProperty();
                if (fightPropertyType != null) {
                    if (this.m_nbProperties > 0) {
                        final byte value2 = this.m_target.getPropertyValue(this.m_property);
                        if (value2 + this.m_nbProperties > 127) {
                            this.m_nbProperties = (byte)(127 - value2);
                        }
                        this.m_target.setPropertyValue(fightPropertyType, this.m_nbProperties);
                    }
                    else {
                        this.m_target.addProperty(fightPropertyType);
                    }
                    this.m_executed = true;
                }
                break;
            }
            default: {
                if (this.m_nbProperties > 0) {
                    final byte value = this.m_target.getPropertyValue(this.m_property);
                    if (value + this.m_nbProperties > 127) {
                        this.m_nbProperties = (byte)(127 - value);
                    }
                    this.m_target.setPropertyValue(this.m_property, (byte)(value + this.m_nbProperties));
                }
                else {
                    this.m_target.addProperty(this.m_property);
                }
                this.m_executed = true;
                break;
            }
        }
        final boolean hasTimeline = this.m_context.getTimeline() != null;
        if (this.m_property == FightPropertyType.EXTRA_TURN && hasTimeline) {
            ((AbstractTimeline)this.m_context.getTimeline()).addFighterThisTurnOnly(this.m_target.getId());
        }
        if (this.m_property == FightPropertyType.HASTE && hasTimeline) {
            this.m_context.getTimeline().updateDynamicOrder();
        }
        if (this.m_property == FightPropertyType.GROGGY_3 && this.m_target.hasCharacteristic(FighterCharacteristicType.AP)) {
            this.m_target.getCharacteristic(FighterCharacteristicType.AP).toMin();
        }
        if (this.m_property == FightPropertyType.CRIPPLED_3 && this.m_target.hasCharacteristic(FighterCharacteristicType.MP)) {
            this.m_target.getCharacteristic(FighterCharacteristicType.MP).toMin();
        }
        if (this.m_property == FightPropertyType.STASIS_3 && this.m_target.hasCharacteristic(FighterCharacteristicType.WP)) {
            final AbstractCharacteristic wp = this.m_target.getCharacteristic(FighterCharacteristicType.WP);
            if (wp == null) {
                PropertyApply.m_logger.error((Object)("charac nulle alors hasCharac renvoie true " + this.m_target + ", " + this.m_target.getClass().getSimpleName()));
            }
            else {
                wp.dispatchUpdate();
            }
        }
        if (this.m_property == FightPropertyType.INVERT_DMG_AND_RES && this.m_target.hasCharacteristic(FighterCharacteristicType.RES_IN_PERCENT) && this.m_target.hasCharacteristic(FighterCharacteristicType.DMG_IN_PERCENT)) {
            this.m_target.getCharacteristic(FighterCharacteristicType.RES_IN_PERCENT).dispatchUpdate();
            this.m_target.getCharacteristic(FighterCharacteristicType.DMG_IN_PERCENT).dispatchUpdate();
        }
        if ((this.m_property == FightPropertyType.INVISIBLE || this.m_property == FightPropertyType.INVISIBLE_SUPERIOR) && this.m_target instanceof FightObstacle) {
            final FightMap fightMap = this.m_context.getFightMap();
            if (fightMap != null) {
                fightMap.addIgnoredSightObstacle((FightObstacle)this.m_target);
            }
        }
    }
    
    public byte getNbProperties() {
        if (this.m_genericEffect == null) {
            return -1;
        }
        final short level = this.getContainerLevel();
        if (((WakfuEffect)this.m_genericEffect).getParamsCount() > 1) {
            return (byte)((WakfuEffect)this.m_genericEffect).getParam(1, level, RoundingMethod.RANDOM);
        }
        if (((WakfuEffect)this.m_genericEffect).getParamsCount() == 1) {
            return (byte)((WakfuEffect)this.m_genericEffect).getParam(0, level, RoundingMethod.RANDOM);
        }
        return -1;
    }
    
    @Override
    public void effectiveComputeValue(final RunningEffect triggerRE) {
        this.m_executed = false;
        final short level = this.getContainerLevel();
        if (this.m_genericEffect == null) {
            this.m_nbProperties = -1;
        }
        else if (((WakfuEffect)this.m_genericEffect).getParamsCount() == 1) {
            this.m_nbProperties = (byte)((WakfuEffect)this.m_genericEffect).getParam(0, level, RoundingMethod.RANDOM);
        }
        else {
            this.m_nbProperties = -1;
        }
        this.m_value = this.m_property.getId();
    }
    
    @Override
    public void unapplyOverride() {
        if (this.m_executed && this.m_target != null && this.m_property != null) {
            if (this.m_nbProperties > 1) {
                final byte value = (byte)(this.m_target.getPropertyValue(this.m_property) - this.m_nbProperties);
                this.m_target.setPropertyValue(this.m_property, value);
            }
            else {
                this.m_target.substractProperty(this.m_property);
            }
            this.m_worldPropertyAlreadyApplied = false;
            if (this.m_property == FightPropertyType.HASTE && this.m_context.getTimeline() != null) {
                this.m_context.getTimeline().updateDynamicOrder();
            }
            final boolean invisibleProperty = this.m_property == FightPropertyType.INVISIBLE || this.m_property == FightPropertyType.INVISIBLE_SUPERIOR;
            if (invisibleProperty) {
                if (this.m_serializedInvisiblePosition != null) {
                    this.m_target.teleport(this.m_serializedInvisiblePosition.getX(), this.m_serializedInvisiblePosition.getY(), this.m_serializedInvisiblePosition.getZ());
                    this.m_target.setDirection(this.m_serializedDirection);
                }
                final FightMap fightMap = this.m_context.getFightMap();
                if (fightMap != null) {
                    if (this.m_target instanceof FightObstacle) {
                        fightMap.removeIgnoredMovementObstacle((FightObstacle)this.m_target);
                    }
                    fightMap.removeIgnoredSightObstacle((FightObstacle)this.m_target);
                }
                else {
                    PropertyApply.m_logger.error((Object)("pas de combat associ\u00e9 \u00e0 la target " + this.m_target.toString()));
                }
            }
        }
        super.unapplyOverride();
    }
    
    @Override
    public boolean useCaster() {
        return false;
    }
    
    @Override
    public boolean useTarget() {
        return true;
    }
    
    @Override
    public boolean useTargetCell() {
        return false;
    }
    
    public void setSerializedInvisiblePosition(final Point3 serializedInvisiblePosition) {
        this.m_serializedInvisiblePosition = serializedInvisiblePosition;
    }
    
    @Override
    public BinarSerialPart getAdditionalDatasBinarSerialPart() {
        return this.ADDITIONNAL_DATAS;
    }
    
    @Override
    public boolean unapplicationMustBeNotified() {
        return this.m_property == FightPropertyType.INVISIBLE || this.m_property == FightPropertyType.INVISIBLE_SUPERIOR || super.unapplicationMustBeNotified();
    }
    
    static {
        m_staticPool = new MonitoredPool(new ObjectFactory<PropertyApply>() {
            @Override
            public PropertyApply makeObject() {
                return new PropertyApply();
            }
        });
        PARAMETERS_LIST_SET = new WakfuRunningEffectParameterListSet(new ParameterList[] { new WakfuRunningEffectParameterList("Pas de params", new WakfuRunningEffectParameter[0]), new WakfuRunningEffectParameterList("Set (certaines propri\u00e9t\u00e9s seulement)", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("valeur", WakfuRunningEffectParameterType.VALUE) }) });
    }
}
