package com.ankamagames.wakfu.common.game.effect.runningEffect;

import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.baseImpl.common.clientAndServer.utils.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.wakfu.common.game.fighter.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.characteristic.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;
import com.ankamagames.framework.external.*;
import com.ankamagames.wakfu.common.game.effect.*;

public class ChangeDirection extends WakfuRunningEffect
{
    private static final ObjectPool m_staticPool;
    public static final int CELL = 0;
    public static final int NORTHWEST = 1;
    public static final int NORTHEAST = 2;
    public static final int SOUTHEAST = 3;
    public static final int SOUTHWEST = 4;
    public static final int RAND = 5;
    public static final int SAME_CASTER = 6;
    public static final int INV_CASTER = 7;
    public static final int CASTER_TOWARD_TARGET = 8;
    public static final int TARGET_TOWARD_CASTER = 9;
    public static final int CASTER_PERPENDICULAR = 10;
    public static final int INV_TARGET = 11;
    private static final ParameterListSet PARAMETERS_LIST_SET;
    
    @Override
    public ParameterListSet getParametersListSet() {
        return ChangeDirection.PARAMETERS_LIST_SET;
    }
    
    @Override
    public ChangeDirection newInstance() {
        ChangeDirection re;
        try {
            re = (ChangeDirection)ChangeDirection.m_staticPool.borrowObject();
            re.m_pool = ChangeDirection.m_staticPool;
        }
        catch (Exception e) {
            re = new ChangeDirection();
            re.m_pool = null;
            re.m_isStatic = false;
            ChangeDirection.m_logger.error((Object)("Erreur lors d'un newInstance sur ChangeDirection : " + e.getMessage()));
        }
        return re;
    }
    
    @Override
    protected void executeOverride(final RunningEffect linkedRE, final boolean trigger) {
        if (this.m_target == null) {
            this.setNotified(true);
            return;
        }
        final Direction8 dir = Direction8.getDirectionFromIndex(this.m_value);
        if (dir == Direction8.NONE) {
            this.setNotified(true);
            return;
        }
        final Direction8 oldDirection = this.m_target.getDirection();
        this.m_target.setDirection(dir);
        if (oldDirection == dir) {
            this.setNotified(true);
        }
    }
    
    @Override
    public void effectiveComputeValue(final RunningEffect triggerRE) {
        final short level = this.getContainerLevel();
        if (this.m_genericEffect != null) {
            this.m_value = ((WakfuEffect)this.m_genericEffect).getParam(0, level, RoundingMethod.RANDOM);
        }
        else {
            this.m_value = 5;
        }
        if (this.m_value == 5) {
            this.m_value = DiceRoll.roll(1, 4);
        }
        if (this.m_target == null) {
            this.m_value = Direction8.NONE.getIndex();
            return;
        }
        Direction8 newDirection = null;
        switch (this.m_value) {
            case 0: {
                final Vector3i vDir = new Vector3i(this.m_target.getWorldCellX(), this.m_target.getWorldCellY(), this.m_target.getWorldCellAltitude(), this.m_targetCell.getX(), this.m_targetCell.getY(), this.m_targetCell.getZ());
                newDirection = vDir.toDirection4();
                break;
            }
            case 2: {
                newDirection = Direction8.NORTH_EAST;
                break;
            }
            case 1: {
                newDirection = Direction8.NORTH_WEST;
                break;
            }
            case 3: {
                newDirection = Direction8.SOUTH_EAST;
                break;
            }
            case 4: {
                newDirection = Direction8.SOUTH_WEST;
                break;
            }
            case 6: {
                newDirection = this.m_caster.getDirection();
                break;
            }
            case 7: {
                newDirection = this.m_caster.getDirection().getOppositeDirection();
                break;
            }
            case 8: {
                final Vector3i vDir = new Vector3i(this.m_caster.getWorldCellX(), this.m_caster.getWorldCellY(), this.m_caster.getWorldCellAltitude(), this.m_target.getWorldCellX(), this.m_target.getWorldCellY(), this.m_target.getWorldCellAltitude());
                newDirection = vDir.toDirection4();
                this.m_target = this.m_caster;
                break;
            }
            case 9: {
                final Vector3i vDir = new Vector3i(this.m_target.getWorldCellX(), this.m_target.getWorldCellY(), this.m_target.getWorldCellAltitude(), this.m_caster.getWorldCellX(), this.m_caster.getWorldCellY(), this.m_caster.getWorldCellAltitude());
                newDirection = vDir.toDirection4();
                break;
            }
            case 10: {
                final Direction8 casterDirection = this.m_caster.getDirection();
                newDirection = Direction8.getDirectionFromVector(casterDirection.m_y, casterDirection.m_x);
                break;
            }
            case 11: {
                newDirection = this.m_target.getDirection().getOppositeDirection();
                break;
            }
            default: {
                newDirection = Direction8.NONE;
                break;
            }
        }
        if (this.m_target == null || this.m_target.isActiveProperty(FightPropertyType.DO_NOT_ORIENTATE_FIGHTER_DURING_FIGHT)) {
            newDirection = Direction8.NONE;
        }
        this.m_value = newDirection.getIndex();
    }
    
    @Override
    public boolean useCaster() {
        return true;
    }
    
    @Override
    public boolean useTarget() {
        return true;
    }
    
    @Override
    public boolean useTargetCell() {
        return true;
    }
    
    static {
        m_staticPool = new MonitoredPool(new ObjectFactory<ChangeDirection>() {
            @Override
            public ChangeDirection makeObject() {
                return new ChangeDirection();
            }
        });
        PARAMETERS_LIST_SET = new WakfuRunningEffectParameterListSet(new ParameterList[] { new WakfuRunningEffectParameterList("Direction/Type", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter(" 0:cellule, 1-2-3-4:NW-NE-SE-SW, 5:rand, ,6:sameCaster, 7:invCaster, 8:turnCasterTowardTarget, 9:turnTargetTowardCaster10:perpendiculaire caster, 11:inverse target", WakfuRunningEffectParameterType.CONFIG) }) });
    }
}
