package com.ankamagames.wakfu.common.game.effect.runningEffect;

import com.ankamagames.wakfu.common.game.effect.runningEffect.util.movementEffect.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;
import com.ankamagames.framework.external.*;
import com.ankamagames.wakfu.common.game.effect.*;

public class Pull extends MovementEffect
{
    private static final ObjectPool m_staticPool;
    private static final ParameterListSet PARAMETERS_LIST_SET;
    public static final byte DIR_NORTH_EAST = 0;
    public static final byte DIR_SOUTH_EAST = 1;
    public static final byte DIR_NORTH_WEST = 2;
    public static final byte DIR_SOUTH_WEST = 3;
    public static final byte DIR_TOWARD_MOVEMENT = 4;
    public static final byte DIR_TOWARD_TARGET_CELL = 5;
    
    @Override
    public ParameterListSet getParametersListSet() {
        return Pull.PARAMETERS_LIST_SET;
    }
    
    @Override
    public Pull newInstance() {
        Pull wre;
        try {
            wre = (Pull)Pull.m_staticPool.borrowObject();
            wre.m_pool = Pull.m_staticPool;
        }
        catch (Exception e) {
            wre = new Pull();
            wre.m_pool = null;
            wre.m_isStatic = false;
            Pull.m_logger.error((Object)("Erreur lors d'un checkOut sur un Pull : " + e.getMessage()));
        }
        return wre;
    }
    
    @Override
    public void setTriggersToExecute() {
        super.setTriggersToExecute();
        this.m_triggers.set(188);
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
        return false;
    }
    
    public boolean hasSerializedAdditionnalInfoForExecution() {
        return true;
    }
    
    @Override
    boolean getCloser() {
        return true;
    }
    
    @Override
    public boolean validatePrecondition() {
        return !this.moverIsCarried() && !this.moverIsRooted() && (this.ignoreStabilisation() || !this.moverIsStabilized()) && !this.moverCantBePushOrPull();
    }
    
    @Override
    boolean doesCollide() {
        return true;
    }
    
    @Override
    public MovementEffectUser getMover() {
        if (this.m_target instanceof MovementEffectUser) {
            return (MovementEffectUser)this.m_target;
        }
        return null;
    }
    
    @Override
    Point3 getReferentialCell() {
        if (((WakfuEffect)this.m_genericEffect).getParamsCount() < 3) {
            return this.getCellJustInFrontOfCaster();
        }
        int directionStatus = -1;
        if (((WakfuEffect)this.m_genericEffect).getParamsCount() == 3) {
            final short level = this.getContainerLevel();
            directionStatus = (byte)((WakfuEffect)this.m_genericEffect).getParam(2, level, RoundingMethod.RANDOM);
        }
        Direction8 dir = null;
        switch (directionStatus) {
            case 0: {
                dir = Direction8.NORTH_EAST;
                break;
            }
            case 1: {
                dir = Direction8.SOUTH_EAST;
                break;
            }
            case 2: {
                dir = Direction8.NORTH_WEST;
                break;
            }
            case 3: {
                dir = Direction8.SOUTH_WEST;
                break;
            }
            case 4: {
                dir = this.m_target.getMovementDirection();
                break;
            }
            case 5: {
                return this.m_targetCell;
            }
            default: {
                if (this.targetAndCasterOnSameCell()) {
                    dir = Direction8.NONE;
                    break;
                }
                dir = new Vector3i(this.m_caster.getWorldCellX(), this.m_caster.getWorldCellY(), this.m_caster.getWorldCellAltitude(), this.m_target.getWorldCellX(), this.m_target.getWorldCellY(), this.m_target.getWorldCellAltitude()).toDirection4();
                break;
            }
        }
        return new Point3(this.m_target.getWorldCellX() + dir.m_x, this.m_target.getWorldCellY() + dir.m_y);
    }
    
    private Point3 getCellJustInFrontOfCaster() {
        final Direction8 dir = new Vector3i(this.m_caster.getWorldCellX(), this.m_caster.getWorldCellY(), this.m_caster.getWorldCellAltitude(), this.getMover().getWorldCellX(), this.getMover().getWorldCellY(), this.getMover().getWorldCellAltitude()).toDirection4();
        return new Point3(this.m_caster.getWorldCellX() + dir.m_x, this.m_caster.getWorldCellY() + dir.m_y, this.m_caster.getWorldCellAltitude());
    }
    
    static {
        m_staticPool = new MonitoredPool(new ObjectFactory<Pull>() {
            @Override
            public Pull makeObject() {
                return new Pull();
            }
        });
        PARAMETERS_LIST_SET = new WakfuRunningEffectParameterListSet(new ParameterList[] { new WakfuRunningEffectParameterList("Attraction standard", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("Nombre de case (tire vers le caster)", WakfuRunningEffectParameterType.VALUE), new WakfuRunningEffectParameter("D\u00e9g\u00e2ts de collision par cellule", WakfuRunningEffectParameterType.VALUE) }), new WakfuRunningEffectParameterList("Tirage dans une direction pr\u00e9cise", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("Nombre de case", WakfuRunningEffectParameterType.VALUE), new WakfuRunningEffectParameter("D\u00e9g\u00e2ts de collision par cellule", WakfuRunningEffectParameterType.VALUE), new WakfuRunningEffectParameter("Direction particuli\u00e8re : 0:NE 1:SE 2:NW 3:SW 4:Dans la direction de la cible 5:Vers la cellule cible", WakfuRunningEffectParameterType.CONFIG) }) });
    }
}
