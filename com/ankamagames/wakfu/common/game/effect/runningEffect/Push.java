package com.ankamagames.wakfu.common.game.effect.runningEffect;

import com.ankamagames.wakfu.common.game.effect.runningEffect.util.movementEffect.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.baseImpl.common.clientAndServer.utils.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;
import com.ankamagames.framework.external.*;
import com.ankamagames.wakfu.common.game.effect.*;

public class Push extends MovementEffect
{
    private static final ObjectPool m_staticPool;
    private static final ParameterListSet PARAMETERS_LIST_SET;
    public static final byte DIR_NORTH_EAST = 0;
    public static final byte DIR_SOUTH_EAST = 1;
    public static final byte DIR_NORTH_WEST = 2;
    public static final byte DIR_SOUTH_WEST = 3;
    public static final byte DIR_OPPOSED_TO_MOVEMENT = 4;
    public static final byte DIR_OPPOSED_TO_TARGET_CELL = 5;
    public static final byte DIR_RANDOM = 6;
    public static final byte DIR_CASTER_DIRECTION = 7;
    
    @Override
    public ParameterListSet getParametersListSet() {
        return Push.PARAMETERS_LIST_SET;
    }
    
    @Override
    public void setTriggersToExecute() {
        super.setTriggersToExecute();
        this.m_triggers.set(187);
    }
    
    @Override
    public Push newInstance() {
        Push wre;
        try {
            wre = (Push)Push.m_staticPool.borrowObject();
            wre.m_pool = Push.m_staticPool;
        }
        catch (Exception e) {
            wre = new Push();
            wre.m_pool = null;
            wre.m_isStatic = false;
            Push.m_logger.error((Object)("Erreur lors d'un checkOut sur un Push : " + e.getMessage()));
        }
        return wre;
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
    
    @Override
    boolean getCloser() {
        return false;
    }
    
    @Override
    public boolean validatePrecondition() {
        return this.m_target != null && !this.moverIsCarried() && !this.moverIsRooted() && (this.ignoreStabilisation() || !this.moverIsStabilized()) && !this.moverCantBePushOrPull();
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
    public void effectiveComputeValue(final RunningEffect triggerRE) {
        super.effectiveComputeValue(triggerRE);
        if (this.m_genericEffect == null) {
            return;
        }
        if (((WakfuEffect)this.m_genericEffect).getParamsCount() >= 4) {
            final int elementId = ((WakfuEffect)this.m_genericEffect).getParam(3, this.getContainerLevel(), RoundingMethod.LIKE_PREVIOUS_LEVEL);
            final Elements element = Elements.getElementFromId((byte)elementId);
            if (element == null) {
                Push.m_logger.error((Object)("Erreur de parametre, l'element n'existe pas " + elementId));
                return;
            }
            this.m_collisionDamageElement = element;
            this.m_computeCollisionDamage = true;
        }
    }
    
    @Override
    Point3 getReferentialCell() {
        int directionStatus = -1;
        if (((WakfuEffect)this.m_genericEffect).getParamsCount() == 3) {
            final short level = this.getContainerLevel();
            directionStatus = (byte)((WakfuEffect)this.m_genericEffect).getParam(2, level, RoundingMethod.RANDOM);
        }
        Direction8 dir = null;
        Label_0292: {
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
                    dir = this.m_target.getMovementDirection().opposite();
                    break;
                }
                case 5: {
                    return this.m_targetCell;
                }
                case 6: {
                    switch (DiceRoll.roll(1, 4)) {
                        case 1: {
                            dir = Direction8.NORTH_EAST;
                            break Label_0292;
                        }
                        case 2: {
                            dir = Direction8.NORTH_WEST;
                            break Label_0292;
                        }
                        case 3: {
                            dir = Direction8.SOUTH_EAST;
                            break Label_0292;
                        }
                        default: {
                            dir = Direction8.SOUTH_WEST;
                            break Label_0292;
                        }
                    }
                    break;
                }
                case 7: {
                    dir = this.m_caster.getDirection();
                    break;
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
        }
        return new Point3(this.m_target.getWorldCellX() + dir.getOppositeDirection().m_x, this.m_target.getWorldCellY() + dir.getOppositeDirection().m_y);
    }
    
    static {
        m_staticPool = new MonitoredPool(new ObjectFactory<Push>() {
            @Override
            public Push makeObject() {
                return new Push();
            }
        });
        PARAMETERS_LIST_SET = new WakfuRunningEffectParameterListSet(new ParameterList[] { new WakfuRunningEffectParameterList("Poussage standard", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("Nombre de case", WakfuRunningEffectParameterType.VALUE), new WakfuRunningEffectParameter("D\u00e9g\u00e2ts de collision par cellule", WakfuRunningEffectParameterType.VALUE) }), new WakfuRunningEffectParameterList("Poussage dans une direction pr\u00e9cise", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("Nombre de case", WakfuRunningEffectParameterType.VALUE), new WakfuRunningEffectParameter("D\u00e9g\u00e2ts de collision par cellule", WakfuRunningEffectParameterType.VALUE), new WakfuRunningEffectParameter("Direction particuli\u00e8re : -1 (defaut) 0:NE 1:SE 2:NW 3:SW 4:Oppos\u00e9e \u00e0 la direction de la cible 5:Oppos\u00e9 \u00e0 la cellule cible 6:al\u00e9atoire 7: direction du caster", WakfuRunningEffectParameterType.CONFIG) }), new WakfuRunningEffectParameterList("Elements des degats de pouss\u00e9", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("Nombre de case", WakfuRunningEffectParameterType.VALUE), new WakfuRunningEffectParameter("D\u00e9g\u00e2ts de collision par cellule", WakfuRunningEffectParameterType.VALUE), new WakfuRunningEffectParameter("Direction particuli\u00e8re : -1 (defaut) 0:NE 1:SE 2:NW 3:SW 4:Oppos\u00e9e \u00e0 la direction de la cible 5:Oppos\u00e9 \u00e0 la cellule cible 6:al\u00e9atoire 7: direction du caster", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("Elements des d\u00e9g\u00e2ts de collision (default = EARTH(3))", WakfuRunningEffectParameterType.CONFIG) }) });
    }
}
