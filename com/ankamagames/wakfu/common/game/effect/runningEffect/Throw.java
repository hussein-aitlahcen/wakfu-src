package com.ankamagames.wakfu.common.game.effect.runningEffect;

import com.ankamagames.framework.kernel.core.common.serialization.*;
import java.nio.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;
import com.ankamagames.wakfu.common.datas.specific.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.fight.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.framework.ai.targetfinder.aoe.*;
import java.util.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;
import com.ankamagames.framework.external.*;
import com.ankamagames.wakfu.common.game.effect.*;

public class Throw extends WakfuRunningEffect
{
    private static final ObjectPool m_staticPool;
    private static final ParameterListSet PARAMETERS_LIST_SET;
    private boolean m_ignoreObstacles;
    private Point3 m_arrivalCell;
    public BinarSerialPart ADDITIONNAL_DATAS;
    
    public Throw() {
        super();
        this.m_arrivalCell = null;
        this.ADDITIONNAL_DATAS = new BinarSerialPart(11) {
            @Override
            public void serialize(final ByteBuffer buffer) {
                buffer.putInt(Throw.this.m_arrivalCell.getX());
                buffer.putInt(Throw.this.m_arrivalCell.getY());
                buffer.putShort(Throw.this.m_arrivalCell.getZ());
                buffer.put((byte)(Throw.this.m_ignoreObstacles ? 1 : 0));
            }
            
            @Override
            public void unserialize(final ByteBuffer buffer, final int version) {
                Throw.this.m_arrivalCell = new Point3(buffer.getInt(), buffer.getInt(), buffer.getShort());
                Throw.this.m_ignoreObstacles = (buffer.get() > 0);
            }
        };
    }
    
    @Override
    public ParameterListSet getParametersListSet() {
        return Throw.PARAMETERS_LIST_SET;
    }
    
    @Override
    public void setTriggersToExecute() {
        super.setTriggersToExecute();
    }
    
    @Override
    public Throw newInstance() {
        Throw wre;
        try {
            wre = (Throw)Throw.m_staticPool.borrowObject();
            wre.m_pool = Throw.m_staticPool;
        }
        catch (Exception e) {
            wre = new Throw();
            wre.m_pool = null;
            wre.m_isStatic = false;
            Throw.m_logger.error((Object)("Erreur lors d'un checkOut sur un Throw : " + e.getMessage()));
        }
        return wre;
    }
    
    @Override
    protected void executeOverride(final RunningEffect linkedRE, final boolean trigger) {
        if (!(this.m_caster instanceof Carrier)) {
            this.setNotified();
            return;
        }
        final Carrier carrier = (Carrier)this.m_caster;
        if (!carrier.isCarrying()) {
            this.setNotified();
            return;
        }
        final CarryTarget carried = carrier.getCurrentCarryTarget();
        final int startx = carried.getWorldCellX();
        final int starty = carried.getWorldCellY();
        final short startz = carried.getWorldCellAltitude();
        boolean executed;
        if (!this.isValueComputationEnabled()) {
            executed = carrier.uncarryTo_effect(this.m_arrivalCell);
            if (carried instanceof EffectUser && this.m_context.getEffectAreaManager() != null) {
                this.m_context.getEffectAreaManager().checkInAndOut(startx, starty, startz, carried.getWorldCellX(), carried.getWorldCellY(), carried.getWorldCellAltitude(), (EffectUser)carried);
            }
        }
        else {
            final FightMap fightMap = this.m_context.getFightMap();
            if (fightMap == null) {
                Throw.m_logger.warn((Object)("pas de fightmap sur le context " + this.m_context));
                this.setNotified();
                return;
            }
            if (!carried.isBlockingMovement() || !fightMap.isMovementBlocked(this.m_targetCell.getX(), this.m_targetCell.getY(), this.m_targetCell.getZ()) || this.m_ignoreObstacles) {
                executed = this.unCarryToTargetCell(linkedRE, trigger, carrier, carried);
            }
            else {
                executed = this.unCarryToEmptyCellInEffectArea(linkedRE, trigger, carrier, carried, fightMap);
            }
        }
        if (!executed) {
            this.setNotified(true);
        }
    }
    
    private boolean unCarryToTargetCell(final RunningEffect linkedRE, final boolean trigger, final Carrier carrier, final CarryTarget carried) {
        final int startx = carried.getWorldCellX();
        final int starty = carried.getWorldCellY();
        final short startz = carried.getWorldCellAltitude();
        if (!carrier.uncarryTo_effect(this.m_targetCell)) {
            return false;
        }
        this.m_arrivalCell = this.m_targetCell;
        this.notifyExecution(linkedRE, trigger);
        if (carried instanceof EffectUser && this.m_context.getEffectAreaManager() != null) {
            ((EffectUser)carried).setSpecialMovementDirection(new Point3(startx, starty, startz).getDirection4To(this.m_arrivalCell));
            this.m_context.getEffectAreaManager().checkInAndOut(startx, starty, startz, carried.getWorldCellX(), carried.getWorldCellY(), carried.getWorldCellAltitude(), (EffectUser)carried);
            ((EffectUser)carried).setSpecialMovementDirection(null);
        }
        return true;
    }
    
    private boolean unCarryToEmptyCellInEffectArea(final RunningEffect linkedRE, final boolean trigger, final Carrier carrier, final CarryTarget carried, final FightMap fightMap) {
        final int startx = carried.getWorldCellX();
        final int starty = carried.getWorldCellY();
        final short startz = carried.getWorldCellAltitude();
        if (this.m_genericEffect == null) {
            return false;
        }
        final AreaOfEffect areaOfEffect = ((WakfuEffect)this.m_genericEffect).getAreaOfEffect();
        if (areaOfEffect == null) {
            return false;
        }
        final Iterable<int[]> iterable = areaOfEffect.getCells(this.m_targetCell.getX(), this.m_targetCell.getY(), this.m_targetCell.getZ(), this.m_caster.getWorldCellX(), this.m_caster.getWorldCellY(), this.m_caster.getWorldCellAltitude(), this.m_caster.getDirection());
        if (iterable == null) {
            return false;
        }
        final Iterator<int[]> iterator = iterable.iterator();
        Point3 emptyCell = null;
        while (iterator.hasNext()) {
            final int[] ints = iterator.next();
            if (!fightMap.isInMap(ints[0], ints[1])) {
                continue;
            }
            final short arrivalAltitude = fightMap.getCellHeight(ints[0], ints[1]);
            if (arrivalAltitude == -32768) {
                continue;
            }
            if (!fightMap.isMovementBlocked(ints[0], ints[1], arrivalAltitude)) {
                emptyCell = new Point3(ints[0], ints[1], arrivalAltitude);
                break;
            }
        }
        if (emptyCell == null) {
            return false;
        }
        if (!carrier.uncarryTo_effect(emptyCell)) {
            return false;
        }
        this.m_arrivalCell = emptyCell;
        this.notifyExecution(linkedRE, trigger);
        if (carried instanceof EffectUser && this.m_context.getEffectAreaManager() != null) {
            this.m_context.getEffectAreaManager().checkInAndOut(startx, starty, startz, carried.getWorldCellX(), carried.getWorldCellY(), carried.getWorldCellAltitude(), (EffectUser)carried);
        }
        return true;
    }
    
    @Override
    public void effectiveComputeValue(final RunningEffect triggerRE) {
        if (this.m_genericEffect == null) {
            return;
        }
        if (((WakfuEffect)this.m_genericEffect).getParamsCount() <= 0) {
            return;
        }
        this.m_ignoreObstacles = (((WakfuEffect)this.m_genericEffect).getParam(0, this.getContainerLevel(), RoundingMethod.LIKE_PREVIOUS_LEVEL) == 1);
    }
    
    @Override
    public boolean useCaster() {
        return true;
    }
    
    @Override
    public boolean useTarget() {
        return false;
    }
    
    @Override
    public boolean useTargetCell() {
        return true;
    }
    
    @Override
    public BinarSerialPart getAdditionalDatasBinarSerialPart() {
        return this.ADDITIONNAL_DATAS;
    }
    
    public Point3 getArrivalCell() {
        return this.m_arrivalCell;
    }
    
    @Override
    public void onCheckIn() {
        this.m_arrivalCell = null;
    }
    
    static {
        m_staticPool = new MonitoredPool(new ObjectFactory<Throw>() {
            @Override
            public Throw makeObject() {
                return new Throw();
            }
        });
        PARAMETERS_LIST_SET = new WakfuRunningEffectParameterListSet(new ParameterList[] { new WakfuRunningEffectParameterList("Jette la cible ", new WakfuRunningEffectParameter[0]), new WakfuRunningEffectParameterList("Ignore les obstacles", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("Ignore les obstacles (1 = oui, 0 = non [d\u00e9faut])", WakfuRunningEffectParameterType.CONFIG) }) });
    }
}
