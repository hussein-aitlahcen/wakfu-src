package com.ankamagames.wakfu.common.game.effect.runningEffect;

import com.ankamagames.framework.kernel.core.common.serialization.*;
import java.nio.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.wakfu.common.datas.specific.*;
import com.ankamagames.baseImpl.common.clientAndServer.utils.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;
import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.pathfind.*;
import com.ankamagames.wakfu.common.game.effectArea.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.characteristic.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.fight.*;
import com.ankamagames.baseImpl.common.clientAndServer.world.topology.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effectArea.*;
import java.util.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;
import com.ankamagames.framework.external.*;
import com.ankamagames.wakfu.common.game.effect.*;

public class PullTargetBackToCaster extends WakfuRunningEffect
{
    private static final ObjectPool m_staticPool;
    private static final ParameterListSet PARAMETERS_LIST_SET;
    private static CellPathData[] m_sourceCellPathData;
    private static CellPathData[] m_destCellPathData;
    private Point3 m_arrivalCell;
    private float m_lifePointsToLose;
    private float m_collisionDamage;
    public BinarSerialPart ADDITIONNAL_DATAS;
    
    @Override
    public ParameterListSet getParametersListSet() {
        return PullTargetBackToCaster.PARAMETERS_LIST_SET;
    }
    
    public PullTargetBackToCaster() {
        super();
        this.ADDITIONNAL_DATAS = new BinarSerialPart(10) {
            @Override
            public void serialize(final ByteBuffer buffer) {
                buffer.putInt(PullTargetBackToCaster.this.m_arrivalCell.getX());
                buffer.putInt(PullTargetBackToCaster.this.m_arrivalCell.getY());
                buffer.putShort(PullTargetBackToCaster.this.m_arrivalCell.getZ());
            }
            
            @Override
            public void unserialize(final ByteBuffer buffer, final int version) {
                PullTargetBackToCaster.this.m_arrivalCell = new Point3(buffer.getInt(), buffer.getInt(), buffer.getShort());
            }
        };
    }
    
    @Override
    public PullTargetBackToCaster newInstance() {
        PullTargetBackToCaster re;
        try {
            re = (PullTargetBackToCaster)PullTargetBackToCaster.m_staticPool.borrowObject();
            re.m_pool = PullTargetBackToCaster.m_staticPool;
        }
        catch (Exception e) {
            re = new PullTargetBackToCaster();
            re.m_pool = null;
            PullTargetBackToCaster.m_logger.error((Object)("Erreur lors d'un checkOut sur un ArenaRunningEffect : " + e.getMessage()));
        }
        re.m_lifePointsToLose = this.m_lifePointsToLose;
        return re;
    }
    
    @Override
    protected void executeOverride(final RunningEffect linkedRE, final boolean trigger) {
        if (this.m_value <= 0) {
            this.setNotified(true);
            return;
        }
        boolean mustBeExecuted = true;
        if (this.m_caster instanceof CarryTarget && ((CarryTarget)this.m_caster).isCarried()) {
            mustBeExecuted = false;
        }
        if (mustBeExecuted) {
            if (this.m_valueComputationEnabled) {
                if (this.m_lifePointsToLose > 0.0f) {
                    final HPLoss hpLoss = HPLoss.checkOut((EffectContext<WakfuEffect>)this.m_context, Elements.EARTH, HPLoss.ComputeMode.CLASSIC, ValueRounder.randomRound(this.m_lifePointsToLose), this.m_caster);
                    hpLoss.disableValueComputation();
                    hpLoss.execute(null, false);
                }
            }
            else if (this.m_arrivalCell == null) {
                PullTargetBackToCaster.m_logger.error((Object)"pas de cellule d'arriv\u00e9e");
                this.setNotified(true);
                return;
            }
            this.m_target.teleport(this.m_arrivalCell.getX(), this.m_arrivalCell.getY(), this.m_arrivalCell.getZ());
            this.notifyExecution(linkedRE, trigger);
            if (this.m_context.getEffectAreaManager() != null) {
                this.m_context.getEffectAreaManager().checkInAndOut(this.m_targetCell.getX(), this.m_targetCell.getY(), this.m_targetCell.getZ(), this.m_arrivalCell.getX(), this.m_arrivalCell.getY(), this.m_arrivalCell.getZ(), this.m_target);
            }
        }
    }
    
    @Override
    public void effectiveComputeValue(final RunningEffect triggerRE) {
        final short level = this.getContainerLevel();
        if (this.m_genericEffect != null) {
            this.m_value = ((WakfuEffect)this.m_genericEffect).getParam(0, level, RoundingMethod.RANDOM);
            this.m_collisionDamage = ((WakfuEffect)this.m_genericEffect).getParam(1, level);
        }
        else {
            this.m_value = 0;
            this.m_collisionDamage = 0.0f;
        }
        if (!(this.m_target instanceof BasicCharacterInfo) || !(this.m_caster instanceof BasicCharacterInfo)) {
            this.m_value = 0;
        }
        if (this.m_value > 0) {
            this.computeMovement();
        }
    }
    
    public void computeMovement() {
        this.m_lifePointsToLose = 0.0f;
        if (this.m_target instanceof BasicCharacterInfo && this.m_caster instanceof BasicCharacterInfo) {
            final FightMap fightMap = this.m_context.getFightMap();
            if (fightMap == null) {
                PullTargetBackToCaster.m_logger.error((Object)("pas de fightmap sur le context " + this.m_context));
                return;
            }
            int x = this.m_caster.getWorldCellX();
            int y = this.m_caster.getWorldCellY();
            short z = this.m_caster.getWorldCellAltitude();
            final BasicCharacterInfo mover = (BasicCharacterInfo)this.m_target;
            final PathChecker pathChecker = new PathChecker();
            pathChecker.setMoverCaracteristics(mover.getHeight(), mover.getPhysicalRadius(), mover.getJumpCapacity());
            TopologyMap map = fightMap.getTopologyMapFromCell(x, y);
            if (map == null) {
                PullTargetBackToCaster.m_logger.error((Object)("The cell (" + x + "; " + y + ") is not in the fightMap"));
                return;
            }
            int sourceNumZ = map.getPathData(x, y, PullTargetBackToCaster.m_sourceCellPathData, 0);
            int sourceIndex = TopologyChecker.getIndexFromZ(0, sourceNumZ, PullTargetBackToCaster.m_sourceCellPathData, z);
            if (sourceIndex == -32768) {
                PullTargetBackToCaster.m_logger.error((Object)("Unable to find the cell (" + x + "; " + y + ") with z value = " + z));
                return;
            }
            final Direction8 dir = new Vector3i(this.m_caster.getWorldCellX(), this.m_caster.getWorldCellY(), this.m_caster.getWorldCellAltitude(), this.m_targetCell.getX(), this.m_targetCell.getY(), this.m_targetCell.getZ()).toDirection4().getOppositeDirection();
            final int dx = dir.m_x;
            final int dy = dir.m_y;
            int cellsCount;
            for (cellsCount = 0; cellsCount < this.m_value; ++cellsCount) {
                final int destX = x + dx;
                final int destY = y + dy;
                if (!map.isInMap(destX, destY)) {
                    map = fightMap.getTopologyMapFromCell(destX, destY);
                    if (map == null) {
                        PullTargetBackToCaster.m_logger.error((Object)("The cell (" + destX + "; " + destY + ") is not in the fightMap"));
                        return;
                    }
                }
                final int destNumZ = map.getPathData(destX, destY, PullTargetBackToCaster.m_destCellPathData, 0);
                final int validIndexesCount = pathChecker.getValidIndexesOnNextCell(sourceIndex, 0, sourceNumZ, PullTargetBackToCaster.m_sourceCellPathData, 0, destNumZ, PullTargetBackToCaster.m_destCellPathData);
                if (validIndexesCount <= 0) {
                    break;
                }
                final short cellArrivalAltitude = PullTargetBackToCaster.m_destCellPathData[pathChecker.m_validIndexes[0]].m_z;
                final CellPathData[] tmp = PullTargetBackToCaster.m_sourceCellPathData;
                PullTargetBackToCaster.m_sourceCellPathData = PullTargetBackToCaster.m_destCellPathData;
                PullTargetBackToCaster.m_destCellPathData = tmp;
                sourceNumZ = destNumZ;
                sourceIndex = pathChecker.m_validIndexes[0];
                if (fightMap.isMovementBlocked(destX, destY, cellArrivalAltitude)) {
                    break;
                }
                x = destX;
                y = destY;
                z = cellArrivalAltitude;
                final Iterable<BasicEffectArea> activeEffectAreas = this.m_context.getEffectAreaManager().getActiveEffectAreas();
                if (activeEffectAreas != null) {
                    final Iterator<BasicEffectArea> effectAreaIterator = (Iterator<BasicEffectArea>)activeEffectAreas.iterator();
                    boolean grip = false;
                    while (effectAreaIterator.hasNext()) {
                        final AbstractEffectArea basicEffectArea = effectAreaIterator.next();
                        if (basicEffectArea.isActiveProperty(EffectAreaPropertyType.GRIP)) {
                            grip = true;
                            break;
                        }
                    }
                    if (grip) {
                        break;
                    }
                }
            }
            this.m_arrivalCell = new Point3(x, y, z);
            if (cellsCount < this.m_value) {
                final int cellLeft = this.m_value - cellsCount;
                if (cellLeft > 0) {
                    this.m_lifePointsToLose += cellLeft * this.m_collisionDamage;
                }
            }
            this.m_value = cellsCount;
        }
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
    
    public Point3 getArrivalCell() {
        return this.m_arrivalCell;
    }
    
    @Override
    public BinarSerialPart getAdditionalDatasBinarSerialPart() {
        return this.ADDITIONNAL_DATAS;
    }
    
    static {
        m_staticPool = new MonitoredPool(new ObjectFactory<PullTargetBackToCaster>() {
            @Override
            public PullTargetBackToCaster makeObject() {
                return new PullTargetBackToCaster();
            }
        });
        PARAMETERS_LIST_SET = new WakfuRunningEffectParameterListSet(new ParameterList[] { new WakfuRunningEffectParameterList("Balance la cible dans le dos du caster \u00e0 un nombre de case d\u00e9finie", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("nombre de case dans le dos du caster", WakfuRunningEffectParameterType.VALUE), new WakfuRunningEffectParameter("D\u00e9g\u00e2ts de collision par cellule", WakfuRunningEffectParameterType.VALUE) }) });
        PullTargetBackToCaster.m_sourceCellPathData = new CellPathData[32];
        for (int i = 0; i < PullTargetBackToCaster.m_sourceCellPathData.length; ++i) {
            PullTargetBackToCaster.m_sourceCellPathData[i] = new CellPathData();
        }
        PullTargetBackToCaster.m_destCellPathData = new CellPathData[32];
        for (int i = 0; i < PullTargetBackToCaster.m_destCellPathData.length; ++i) {
            PullTargetBackToCaster.m_destCellPathData[i] = new CellPathData();
        }
    }
}
