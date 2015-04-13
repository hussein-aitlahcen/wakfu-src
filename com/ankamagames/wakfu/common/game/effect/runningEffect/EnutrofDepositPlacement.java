package com.ankamagames.wakfu.common.game.effect.runningEffect;

import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;
import com.ankamagames.wakfu.common.game.effect.genericEffect.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.fight.*;
import org.jetbrains.annotations.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effectArea.*;
import com.ankamagames.wakfu.common.game.effectArea.*;
import java.util.*;
import com.ankamagames.framework.ai.targetfinder.aoe.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.baseImpl.common.clientAndServer.world.topology.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.pathfind.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;
import com.ankamagames.framework.external.*;
import com.ankamagames.wakfu.common.game.effect.*;

public final class EnutrofDepositPlacement extends WakfuRunningEffect
{
    private static final ObjectPool m_staticPool;
    private static final ParameterListSet PARAMETERS_LIST_SET;
    private static final int MAX_TRY_COUNT_TO_RANDOM_CELL = 3;
    private int m_nbDepositMin;
    private int m_nbDepositMax;
    private int m_discoverRareDepositPercentage;
    private boolean m_randomCell;
    
    @Override
    public ParameterListSet getParametersListSet() {
        return EnutrofDepositPlacement.PARAMETERS_LIST_SET;
    }
    
    public EnutrofDepositPlacement() {
        super();
        this.setTriggersToExecute();
    }
    
    @Override
    public EnutrofDepositPlacement newInstance() {
        EnutrofDepositPlacement re;
        try {
            re = (EnutrofDepositPlacement)EnutrofDepositPlacement.m_staticPool.borrowObject();
            re.m_pool = EnutrofDepositPlacement.m_staticPool;
        }
        catch (Exception e) {
            re = new EnutrofDepositPlacement();
            re.m_pool = null;
            re.m_isStatic = false;
            EnutrofDepositPlacement.m_logger.error((Object)("Erreur lors d'un checkOut sur un EnutrofDepositPlacement : " + e.getMessage()));
        }
        return re;
    }
    
    @Override
    public void effectiveComputeValue(final RunningEffect triggerRE) {
        if (this.m_genericEffect == null) {
            return;
        }
        if (((WakfuEffect)this.m_genericEffect).getParamsCount() == 0) {
            this.m_nbDepositMax = 1;
            this.m_nbDepositMin = 1;
            this.m_discoverRareDepositPercentage = 0;
            this.m_randomCell = false;
            return;
        }
        if (((WakfuEffect)this.m_genericEffect).getParamsCount() < 3) {
            return;
        }
        this.m_randomCell = true;
        this.m_nbDepositMin = ((WakfuEffect)this.m_genericEffect).getParam(0, this.getContainerLevel(), RoundingMethod.LIKE_PREVIOUS_LEVEL);
        this.m_nbDepositMax = ((WakfuEffect)this.m_genericEffect).getParam(1, this.getContainerLevel(), RoundingMethod.LIKE_PREVIOUS_LEVEL);
        if (this.m_nbDepositMax < this.m_nbDepositMin) {
            final int nbDepositMax = this.m_nbDepositMax;
            this.m_nbDepositMax = this.m_nbDepositMin;
            this.m_nbDepositMin = nbDepositMax;
        }
        if (this.m_nbDepositMin < 0) {
            this.m_nbDepositMin = 1;
        }
        this.m_discoverRareDepositPercentage = ((WakfuEffect)this.m_genericEffect).getParam(2, this.getContainerLevel(), RoundingMethod.LIKE_PREVIOUS_LEVEL);
    }
    
    @Override
    protected void executeOverride(final RunningEffect triggerRE, final boolean trigger) {
        if (!this.isValueComputationEnabled()) {
            return;
        }
        if (this.m_caster == null || !(this.m_caster instanceof BasicCharacterInfo)) {
            return;
        }
        if (this.m_nbDepositMin <= 0) {
            return;
        }
        final List<AbstractDepositEffectArea> depositAreas = StaticEffectAreaManager.getInstance().getDepositAreas();
        final short casterLevel = ((BasicCharacterInfo)this.m_caster).getLevel();
        final int roll = MathHelper.random(100);
        boolean rareIsDiscovered = false;
        if (roll < this.m_discoverRareDepositPercentage) {
            rareIsDiscovered = true;
        }
        int nbDeposit = MathHelper.random(this.m_nbDepositMin, this.m_nbDepositMax + 1);
        if (rareIsDiscovered) {
            this.spawnRareDeposit(depositAreas, casterLevel);
            --nbDeposit;
        }
        for (int i = 0; i < nbDeposit; ++i) {
            this.spawnDeposit(depositAreas, casterLevel);
        }
    }
    
    private void spawnRareDeposit(final List<AbstractDepositEffectArea> depositAreas, final short casterLevel) {
        AbstractDepositEffectArea rareDeposit = null;
        for (int i = 0, n = depositAreas.size(); i < n; ++i) {
            final AbstractDepositEffectArea area = depositAreas.get(i);
            if (rareDeposit == null) {
                rareDeposit = area;
            }
            else if (area.getDepositLevel() > rareDeposit.getDepositLevel()) {
                rareDeposit = area;
            }
        }
        this.spawnDeposit((int)rareDeposit.getBaseId());
    }
    
    private void spawnDeposit(final int areaId) {
        final Point3 cell = this.getSpawnCell();
        if (cell == null) {
            return;
        }
        final SetEffectArea setEffectArea = SetEffectArea.checkOut((EffectContext<WakfuEffect>)this.m_context, cell, areaId);
        setEffectArea.setCaster(((BasicCharacterInfo)this.m_caster).getController());
        setEffectArea.setShouldBeInfinite(true);
        setEffectArea.setZoneLevel((short)1);
        ((RunningEffect<DefaultFightInstantEffectAndDontTrigger, EC>)setEffectArea).setGenericEffect(DefaultFightInstantEffectAndDontTrigger.getInstance());
        setEffectArea.setParent(this);
        setEffectArea.askForExecution();
    }
    
    @Nullable
    private Point3 getSpawnCell() {
        final FightMap fightMap = this.m_context.getFightMap();
        if (this.m_randomCell && fightMap == null) {
            EnutrofDepositPlacement.m_logger.warn((Object)("pas de fightmap sur le context " + this.m_context));
            return null;
        }
        if (!this.m_randomCell) {
            return this.m_targetCell;
        }
        return this.getRandomCell(fightMap);
    }
    
    private Point3 getRandomCell(final FightMap fightMap) {
        Point3 cell = null;
        for (int i = 0; i < 3; ++i) {
            cell = this.getRandomCellInEffectArea(fightMap);
            if (cell != null) {
                cell.setZ(fightMap.getCellHeight(cell.getX(), cell.getY()));
                final int path = this.getPathToCellValidity(fightMap, cell);
                if (path == -1) {
                    cell = null;
                }
                else {
                    if (this.cellAlreadyContainsCasterDeposit(cell)) {
                        cell = null;
                    }
                    if (cell != null) {
                        return cell;
                    }
                }
            }
        }
        return cell;
    }
    
    private boolean cellAlreadyContainsCasterDeposit(final Point3 cell) {
        final Collection<BasicEffectArea> areas = this.m_context.getEffectAreaManager().getActiveEffectAreas();
        for (final BasicEffectArea area : areas) {
            if (area.getOwner() == this.m_caster && area.getType() == EffectAreaType.ENUTROF_DEPOSIT.getTypeId() && area.contains(cell)) {
                return true;
            }
        }
        return false;
    }
    
    private Point3 getRandomCellInEffectArea(final FightMap fightMap) {
        final AreaOfEffect areaOfEffect = ((WakfuEffect)this.m_genericEffect).getAreaOfEffect();
        if (areaOfEffect.getType() == AreaOfEffectEnum.EMPTY) {
            return fightMap.getInsideRandomCell();
        }
        final Direction8 dir = this.m_caster.getDirection();
        final Point3 casterCell = this.m_caster.getPosition();
        final Iterable<int[]> iterable = ((WakfuEffect)this.m_genericEffect).getAreaOfEffect().getCells(this.m_targetCell.getX(), this.m_targetCell.getY(), this.m_targetCell.getZ(), casterCell.getX(), casterCell.getY(), casterCell.getZ(), dir);
        final ArrayList<int[]> cells = new ArrayList<int[]>();
        for (final int[] next : iterable) {
            final int cellX = next[0];
            final int cellY = next[1];
            if (!fightMap.isInMap(cellX, cellY)) {
                continue;
            }
            if (!fightMap.isInside(cellX, cellY)) {
                continue;
            }
            final short cellHeight = fightMap.getCellHeight(cellX, cellY);
            if (cellHeight == -32768) {
                continue;
            }
            cells.add(next);
        }
        if (cells.isEmpty()) {
            EnutrofDepositPlacement.m_logger.error((Object)("Pas de cellule trouvee pour le spawn d'un gisement " + areaOfEffect.getType()));
            return null;
        }
        return new Point3(cells.get(MathHelper.random(cells.size())));
    }
    
    private int getPathToCellValidity(final FightMap fightMap, final Point3 cell) {
        fightMap.setIgnoreAllMovementObstacles(true);
        final PathFinder pathFinder = PathFinder.checkOut();
        int path = -1;
        try {
            pathFinder.setMoverCaracteristics(this.m_caster.getHeight(), this.m_caster.getPhysicalRadius(), ((BasicCharacterInfo)this.m_caster).getJumpCapacity());
            pathFinder.setTopologyMapInstanceSet(fightMap);
            pathFinder.addStartCell(this.m_caster.getPosition());
            pathFinder.setStopCell(cell);
            final PathFinderParameters parameters = new PathFinderParameters();
            parameters.m_maxPathLength = fightMap.getHeight() + fightMap.getWidth();
            parameters.m_searchLimit = 2048;
            pathFinder.setParameters(parameters);
            path = pathFinder.findPath();
        }
        catch (Exception e) {
            EnutrofDepositPlacement.m_logger.error((Object)"Exception levee", (Throwable)e);
        }
        finally {
            pathFinder.release();
            fightMap.setIgnoreAllMovementObstacles(false);
        }
        return path;
    }
    
    private void spawnDeposit(final List<AbstractDepositEffectArea> depositAreas, final short casterLevel) {
        final int roll = MathHelper.random(100);
        AbstractDepositEffectArea deposit = null;
        for (int i = 0, n = depositAreas.size(); i < n; ++i) {
            final AbstractDepositEffectArea area = depositAreas.get(i);
            if (deposit == null) {
                deposit = area;
            }
            else if ((roll < deposit.getDepositLevel() && roll > area.getDepositLevel()) || (roll > area.getDepositLevel() && area.getDepositLevel() > deposit.getDepositLevel())) {
                deposit = area;
            }
        }
        this.spawnDeposit((int)deposit.getBaseId());
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
    public void onCheckIn() {
        this.m_nbDepositMin = 0;
        this.m_discoverRareDepositPercentage = 0;
        this.m_randomCell = true;
        super.onCheckIn();
    }
    
    static {
        m_staticPool = new MonitoredPool(new ObjectFactory<EnutrofDepositPlacement>() {
            @Override
            public EnutrofDepositPlacement makeObject() {
                return new EnutrofDepositPlacement();
            }
        });
        PARAMETERS_LIST_SET = new WakfuRunningEffectParameterListSet(new ParameterList[] { new WakfuRunningEffectParameterList("Params", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("Nb de gisements min", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("Nb de gisements max", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("% d'avoir du rare", WakfuRunningEffectParameterType.CONFIG) }), new WakfuRunningEffectParameterList("Spawn un gisement sur la cellule cible", new WakfuRunningEffectParameter[0]) });
    }
}
