package com.ankamagames.wakfu.common.game.effect.runningEffect;

import com.ankamagames.baseImpl.common.clientAndServer.utils.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.fight.*;
import java.util.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.framework.ai.targetfinder.aoe.*;
import com.ankamagames.framework.external.*;
import com.ankamagames.wakfu.common.game.effect.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;

public class RunningEffectGroupEffectRandomInArea extends RandomRunningEffectGroup
{
    private static final ParameterListSet PARAMETERS_LIST_SET;
    private static final ObjectPool m_staticPool;
    private boolean m_checkCriterionOnCells;
    private boolean m_useDirectionToTargetCell;
    private boolean m_useCasterCellToComputeZone;
    private boolean m_removeUsedCells;
    
    public RunningEffectGroupEffectRandomInArea() {
        super();
        this.setTriggersToExecute();
    }
    
    @Override
    public ParameterListSet getParametersListSet() {
        return RunningEffectGroupEffectRandomInArea.PARAMETERS_LIST_SET;
    }
    
    @Override
    public RandomRunningEffectGroup newInstance() {
        RunningEffectGroupEffectRandomInArea re;
        try {
            re = (RunningEffectGroupEffectRandomInArea)RunningEffectGroupEffectRandomInArea.m_staticPool.borrowObject();
            re.m_pool = RunningEffectGroupEffectRandomInArea.m_staticPool;
        }
        catch (Exception e) {
            re = new RunningEffectGroupEffectRandomInArea();
            re.m_pool = null;
            re.m_isStatic = false;
            RunningEffectGroupEffectRandomInArea.m_logger.error((Object)("Erreur lors d'un checkOut sur un EffectRandomInArea : " + e.getMessage()));
        }
        this.copyParams(re);
        return re;
    }
    
    @Override
    protected void executeOverride(final RunningEffect triggerRE, final boolean trigger) {
        if (!this.isValueComputationEnabled()) {
            return;
        }
        if (this.m_genericEffect == null) {
            RunningEffectGroupEffectRandomInArea.m_logger.error((Object)"Pas de genericEffect, \u00e7a ne devrait pas arriver");
            this.setNotified();
            return;
        }
        final FightMap fightMap = this.m_context.getFightMap();
        if (fightMap == null) {
            RunningEffectGroupEffectRandomInArea.m_logger.error((Object)("pas de fightmap sur le context " + this.m_context));
            return;
        }
        final List<int[]> cells = this.getCells();
        if (cells.isEmpty()) {
            this.setNotified();
            return;
        }
        final WakfuEffectExecutionParameters params = WakfuEffectExecutionParameters.checkOut(false, false, (WakfuRunningEffect)triggerRE);
        if (this.hasProperty(RunningEffectPropertyType.TRANSMIT_LEVEL_TO_CHILDREN) && params.getForcedLevel() == -1) {
            params.setForcedLevel(this.getContainerLevel());
        }
        for (int i = 0; i < this.m_maxEffectToExecute; ++i) {
            final int nbCells = cells.size();
            final int randomIndex = MathHelper.random(nbCells);
            final int[] cell = cells.get(randomIndex);
            for (final WakfuEffect e : this.m_effectGroup) {
                final short cellHeight = fightMap.getCellHeight(cell[0], cell[1]);
                e.execute(this.getEffectContainer(), this.m_caster, this.m_context, RunningEffectConstants.getInstance(), cell[0], cell[1], cellHeight, null, params, false);
            }
            if (this.m_removeUsedCells) {
                cells.remove(randomIndex);
            }
        }
    }
    
    private List<int[]> getCells() {
        final FightMap fightMap = this.m_context.getFightMap();
        if (fightMap == null) {
            return Collections.emptyList();
        }
        if (this.isEmptyAreaEffect()) {
            return this.getCellsForEmptyArea(fightMap);
        }
        final Direction8 dir = this.getDirectionToUse();
        final Point3 targetCell = this.getTargetCellToComputeZone();
        final Point3 sourceCell = this.getSourceCellToComputeZone();
        final Iterable<int[]> iterable = ((WakfuEffect)this.m_genericEffect).getAreaOfEffect().getCells(targetCell.getX(), targetCell.getY(), targetCell.getZ(), sourceCell.getX(), sourceCell.getY(), sourceCell.getZ(), dir);
        final ArrayList<int[]> cells = new ArrayList<int[]>();
        final SimpleCriterion conditions = ((WakfuEffect)this.m_genericEffect).getConditions();
        final Point3 temp = new Point3();
        for (final int[] next : iterable) {
            final int cellX = next[0];
            final int cellY = next[1];
            if (!fightMap.isInMap(cellX, cellY)) {
                continue;
            }
            if (!fightMap.isInsideOrBorder(cellX, cellY)) {
                continue;
            }
            final short cellHeight = fightMap.getCellHeight(cellX, cellY);
            if (cellHeight == -32768) {
                continue;
            }
            if (this.m_checkCriterionOnCells && conditions != null) {
                temp.set(cellX, cellY, cellHeight);
                if (!conditions.isValid(this.m_caster, temp, this, this.m_context)) {
                    continue;
                }
            }
            cells.add(next);
        }
        return cells;
    }
    
    private List<int[]> getCellsForEmptyArea(final FightMap fightMap) {
        final List<int[]> res = fightMap.getInsideCells();
        final SimpleCriterion conditions = ((WakfuEffect)this.m_genericEffect).getConditions();
        if (!this.m_checkCriterionOnCells || conditions == null) {
            return res;
        }
        final Point3 temp = new Point3();
        final Iterator<int[]> it = res.iterator();
        while (it.hasNext()) {
            final int[] next = it.next();
            final int cellX = next[0];
            final int cellY = next[1];
            final short cellHeight = fightMap.getCellHeight(cellX, cellY);
            if (cellHeight == -32768) {
                continue;
            }
            temp.set(cellX, cellY, cellHeight);
            if (conditions.isValid(this.m_caster, temp, this, this.m_context)) {
                continue;
            }
            it.remove();
        }
        return res;
    }
    
    private Point3 getSourceCellToComputeZone() {
        if (this.m_caster != null) {
            return this.m_caster.getPosition();
        }
        return this.m_targetCell;
    }
    
    private Point3 getTargetCellToComputeZone() {
        if (this.m_useCasterCellToComputeZone) {
            return this.m_caster.getPosition();
        }
        return this.m_targetCell;
    }
    
    private Direction8 getDirectionToUse() {
        if (this.m_caster == null) {
            return Direction8.NORTH_EAST;
        }
        if (this.m_useDirectionToTargetCell) {
            return this.m_caster.getPosition().getDirection4To(this.m_targetCell);
        }
        return this.m_caster.getDirection();
    }
    
    @Override
    boolean checkConditions(final RunningEffect linkedRE) {
        return (((WakfuEffect)this.m_genericEffect).getParamsCount() >= 3 && ((WakfuEffect)this.m_genericEffect).getParam(2, this.getContainerLevel(), RoundingMethod.LIKE_PREVIOUS_LEVEL) == 1) || super.checkConditions(linkedRE);
    }
    
    @Override
    public void effectiveComputeValue(final RunningEffect triggerRE) {
        super.effectiveComputeValue(triggerRE);
        this.m_checkCriterionOnCells = false;
        if (((WakfuEffect)this.m_genericEffect).getParamsCount() >= 3) {
            this.m_checkCriterionOnCells = (((WakfuEffect)this.m_genericEffect).getParam(2, this.getContainerLevel(), RoundingMethod.LIKE_PREVIOUS_LEVEL) == 1);
        }
        if (((WakfuEffect)this.m_genericEffect).getParamsCount() >= 4) {
            this.m_useDirectionToTargetCell = (((WakfuEffect)this.m_genericEffect).getParam(3, this.getContainerLevel(), RoundingMethod.LIKE_PREVIOUS_LEVEL) == 1);
        }
        if (((WakfuEffect)this.m_genericEffect).getParamsCount() >= 5) {
            this.m_useCasterCellToComputeZone = (((WakfuEffect)this.m_genericEffect).getParam(4, this.getContainerLevel(), RoundingMethod.LIKE_PREVIOUS_LEVEL) == 1);
        }
        if (((WakfuEffect)this.m_genericEffect).getParamsCount() >= 6) {
            this.m_removeUsedCells = (((WakfuEffect)this.m_genericEffect).getParam(5, this.getContainerLevel(), RoundingMethod.LIKE_PREVIOUS_LEVEL) == 1);
        }
    }
    
    @Override
    public void onCheckIn() {
        super.onCheckIn();
        this.m_checkCriterionOnCells = false;
        this.m_useCasterCellToComputeZone = false;
        this.m_useDirectionToTargetCell = false;
        this.m_removeUsedCells = false;
    }
    
    private boolean isEmptyAreaEffect() {
        return ((WakfuEffect)this.m_genericEffect).getAreaOfEffect().getType() == AreaOfEffectEnum.EMPTY;
    }
    
    static {
        PARAMETERS_LIST_SET = new WakfuRunningEffectParameterListSet(new ParameterList[] { new WakfuRunningEffectParameterList("Params", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("nb de rolls", WakfuRunningEffectParameterType.VALUE), new WakfuRunningEffectParameter("0 = cellule, 1 = cibles (default)", WakfuRunningEffectParameterType.CONFIG) }), new WakfuRunningEffectParameterList("V\u00e9rifie le critere sur les cellules de la zone (pas sur l'effet de base)", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("nb de rolls", WakfuRunningEffectParameterType.VALUE), new WakfuRunningEffectParameter("0 = cellule, 1 = cibles (default)", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("Critere sur zone = 1, critere sur effet de base = 0 (defaut)", WakfuRunningEffectParameterType.CONFIG) }), new WakfuRunningEffectParameterList("Utilise la direction vers la cellule cible", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("nb de rolls", WakfuRunningEffectParameterType.VALUE), new WakfuRunningEffectParameter("0 = cellule, 1 = cibles (default)", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("Critere sur zone = 1, critere sur effet de base = 0 (defaut)", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("Utilise la direction vers la cible (1 = oui) (pas la direction du caster)", WakfuRunningEffectParameterType.CONFIG) }), new WakfuRunningEffectParameterList("Calcul la zone avec la cellule du caster en cellule cible", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("nb de rolls", WakfuRunningEffectParameterType.VALUE), new WakfuRunningEffectParameter("0 = cellule, 1 = cibles (default)", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("Critere sur zone = 1, critere sur effet de base = 0 (defaut)", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("Utilise la direction vers la cible (1 = oui) (pas la direction du caster)", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("Calcul la zone avec la cellule du caster en cellule cible (1=oui)", WakfuRunningEffectParameterType.CONFIG) }), new WakfuRunningEffectParameterList("Retrait des cellules deja utilis\u00e9es", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("nb de rolls", WakfuRunningEffectParameterType.VALUE), new WakfuRunningEffectParameter("0 = cellule, 1 = cibles (default)", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("Critere sur zone = 1, critere sur effet de base = 0 (defaut)", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("Utilise la direction vers la cible (1 = oui) (pas la direction du caster)", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("Calcul la zone avec la cellule du caster en cellule cible (1=oui)", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("Retire de la liste les cellules d\u00e9j\u00e0 cibl\u00e9es (1=oui)", WakfuRunningEffectParameterType.CONFIG) }) });
        m_staticPool = new MonitoredPool(new ObjectFactory<RunningEffectGroupEffectRandomInArea>() {
            @Override
            public RunningEffectGroupEffectRandomInArea makeObject() {
                return new RunningEffectGroupEffectRandomInArea();
            }
        });
    }
}
