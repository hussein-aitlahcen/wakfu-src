package com.ankamagames.wakfu.common.game.world.havenWorld.validator;

import com.ankamagames.baseImpl.common.clientAndServer.game.time.calendar.*;
import com.ankamagames.wakfu.common.game.havenWorld.buff.*;
import com.ankamagames.wakfu.common.game.time.calendar.*;
import com.ankamagames.wakfu.common.game.havenWorld.definition.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.wakfu.common.game.havenWorld.*;
import gnu.trove.*;
import com.ankamagames.wakfu.common.game.havenWorld.definition.entry.*;
import com.ankamagames.wakfu.common.game.havenWorld.procedure.*;

public class BuildingValidationHelper
{
    public static GameDateConst getEvolutionEndDate(final short worldId, final BuildingEvolution evolution, final long creationDate) {
        if (evolution.waitForResource()) {
            return null;
        }
        final HavenWorldZoneBuffs worldZoneBuffs = HavenWorldZoneBuffsManager.INSTANCE.getBuffs(worldId);
        final int percentModificator = worldZoneBuffs.getBuildDurationPercentModificator();
        final float factor = (100 + percentModificator) / 100.0f;
        final GameInterval delay = new GameInterval(evolution.getDelay());
        delay.multiply(factor);
        return GameDate.fromLong(creationDate).add(delay);
    }
    
    public static boolean isEvolutionTimeFinished(final short worldId, final Building building) {
        final BuildingEvolution evolution = HavenWorldDefinitionManager.INSTANCE.getEvolutionFromBuilding(building.getDefinition().getId());
        if (evolution == null) {
            return false;
        }
        final GameDateConst evolutionEndDate = getEvolutionEndDate(worldId, evolution, building.getCreationDate());
        return evolutionEndDate == null || !evolutionEndDate.after(WakfuGameCalendar.getInstance().getDate());
    }
    
    public static int getAdjustedKamaCost(final AbstractBuildingDefinition definition, final short worldId) {
        final int kamasCost = definition.getKamasCost();
        final HavenWorldZoneBuffs buffs = HavenWorldZoneBuffsManager.INSTANCE.getBuffs(worldId);
        if (buffs == null) {
            return kamasCost;
        }
        return MathHelper.ensureInt((long)(kamasCost * (100.0 + buffs.getBuildKamaCostFactor()) / 100.0));
    }
    
    public static int getAdjustedResourceCost(final AbstractBuildingDefinition definition, final short worldId) {
        final int resourcesCost = definition.getResourcesCost();
        final HavenWorldZoneBuffs buffs = HavenWorldZoneBuffsManager.INSTANCE.getBuffs(worldId);
        if (buffs == null) {
            return resourcesCost;
        }
        return MathHelper.ensureInt((long)(resourcesCost * (100.0 + buffs.getBuildResourceCostFactor()) / 100.0));
    }
    
    public static long getMissingKamas(final AbstractBuildingStruct info, final HavenWorldDataProvider dataProvider) {
        return Math.max(0L, getAdjustedKamaCost(info.getDefinition(), dataProvider.getWorldId()) - dataProvider.getRemainingKama());
    }
    
    public static int getMissingResources(final AbstractBuildingStruct info, final HavenWorldDataProvider dataProvider) {
        return Math.max(0, getAdjustedResourceCost(info.getDefinition(), dataProvider.getWorldId()) - dataProvider.getResources());
    }
    
    public static int getMissingWorkers(final AbstractBuildingStruct info, final HavenWorldDataProvider dataProvider) {
        return Math.max(0, info.getDefinition().getNeededWorkers() - dataProvider.getCurrentRemainingWorkers());
    }
    
    public static boolean checkMaxQuantity(final AbstractBuildingStruct info, final HavenWorldDataProvider dataProvider, final boolean evolution) {
        final AbstractBuildingDefinition definition = info.getDefinition();
        final BuildingCatalogEntry catalogEntry = info.getCatalogEntry();
        if (catalogEntry.getMaxQuantity() <= 0) {
            return true;
        }
        final BuildingAndEvolutionCount2 computer = new BuildingAndEvolutionCount2(definition.getId());
        dataProvider.forEachBuilding(computer);
        int count = computer.getCount();
        if (evolution) {
            --count;
        }
        final int availableBuildings = catalogEntry.getMaxQuantity() - count;
        return availableBuildings > 0;
    }
    
    public static MissingBuildingCountComputer2 checkConditions(final AbstractBuildingStruct info, final HavenWorldDataProvider dataProvider) {
        final MissingBuildingCountComputer2 computer = new MissingBuildingCountComputer2(dataProvider, info.getDefinition().getId());
        return computer;
    }
}
