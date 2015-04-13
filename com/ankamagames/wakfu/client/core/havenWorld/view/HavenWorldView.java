package com.ankamagames.wakfu.client.core.havenWorld.view;

import com.ankamagames.wakfu.client.ui.component.*;
import org.apache.log4j.*;
import com.ankamagames.wakfu.common.game.havenWorld.*;
import com.ankamagames.wakfu.client.core.game.time.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.guild.*;
import com.ankamagames.wakfu.common.game.havenWorld.procedure.*;
import gnu.trove.*;
import com.ankamagames.wakfu.common.game.havenWorld.definition.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.time.calendar.*;
import java.util.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.framework.reflect.*;

public class HavenWorldView extends ImmutableFieldProvider implements TimeTickListener
{
    private static final Logger m_logger;
    public static final String GUILD_NAME_FIELD = "guildName";
    public static final String BUILDING_LIST_FIELD = "buildingList";
    public static final String EVOLUTING_BUILDING_LIST_FIELD = "evolutingBuildingList";
    public static final String BUILDINGS_OPENNED_FIELD = "buildingsOpenned";
    public static final String EVOLUTING_BUILDINGS_OPENNED_FIELD = "evolutingBuildingsOpenned";
    public static final String GUILD_RANK_FIELD = "guildRank";
    public static final String CONQUEST_RANK_FIELD = "conquestRank";
    public static final String HAVEN_WORLD_NAME_FIELD = "havenWorldName";
    public static final String EXIT_INSTANCE_NAME = "exitInstanceName";
    public static final String NUM_BUILDINGS = "numBuildings";
    public static final String KAMAS_TOTAL_COST = "kamasTotalCost";
    public static final String RESOURCES_TOTAL_COST = "resourcesTotalCost";
    public static final String NEXT_BUILDING_TEXT = "nextBuildingText";
    public static final String[] FIELDS;
    private long m_kamasCost;
    private long m_resourcesCost;
    private final String m_guildName;
    private boolean m_buildingsOpenned;
    private boolean m_evolutingBuildingsOpenned;
    private final ArrayList<HavenWorldElementView> m_buildingList;
    private final ArrayList<HavenWorldElementView> m_buildingInProgress;
    private int m_guildRank;
    private int m_conquestRank;
    private short m_worldId;
    
    @Override
    public String[] getFields() {
        return HavenWorldView.FIELDS;
    }
    
    public HavenWorldView(final short worldId, final String guildName, final ArrayList<Building> buildings, final int guildRank, final int conquestRank) {
        this(guildName, buildings);
        this.m_worldId = worldId;
        this.m_guildRank = guildRank;
        this.m_conquestRank = conquestRank;
    }
    
    public HavenWorldView(final String guildName, final ArrayList<Building> buildings) {
        super();
        this.m_buildingList = new ArrayList<HavenWorldElementView>();
        this.m_buildingInProgress = new ArrayList<HavenWorldElementView>();
        this.m_guildName = guildName;
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        if (localPlayer.isInGuild()) {
            final ClientGuildInformationHandler guildHandler = localPlayer.getGuildHandler();
            final HavenWorldDefinition worldDefinition = HavenWorldDefinitionManager.INSTANCE.getWorld(guildHandler.getHavenWorldId());
            this.m_worldId = (short)((worldDefinition == null) ? 0 : worldDefinition.getWorldInstanceId());
        }
        this.createBuildingList(buildings);
        this.m_buildingsOpenned = true;
        TimeManager.INSTANCE.addListener(this);
    }
    
    private void createBuildingList(final ArrayList<Building> buildings) {
        for (final Building b : buildings) {
            final HavenWorldElementView element = HavenWorldViewManager.INSTANCE.getElement(b.getUid());
            final AbstractBuildingDefinition definition = b.getDefinition();
            final BuildingTotalCost procedure = new BuildingTotalCost(definition.getId());
            HavenWorldDefinitionManager.INSTANCE.foreachBuilding(procedure);
            this.m_kamasCost += procedure.getKamasTotalCost();
            this.m_resourcesCost += procedure.getResourcesTotalCost();
            if (definition.isDecoOnly()) {
                continue;
            }
            if (BuildingDefinitionHelper.getState(definition) == BuildingDefinitionHelper.ConstructionState.DONE) {
                this.m_buildingList.add(element);
            }
            else {
                this.m_buildingInProgress.add(element);
            }
        }
        Collections.sort(this.m_buildingInProgress, new Comparator<HavenWorldElementView>() {
            @Override
            public int compare(final HavenWorldElementView o1, final HavenWorldElementView o2) {
                final GameDateConst buildingEvolutionEndDate1 = o1.getBuildingEvolutionEndDate();
                final GameDateConst buildingEvolutionEndDate2 = o2.getBuildingEvolutionEndDate();
                if (buildingEvolutionEndDate1 == null || buildingEvolutionEndDate2 == null) {
                    return 0;
                }
                if (buildingEvolutionEndDate1.before(buildingEvolutionEndDate2)) {
                    return -1;
                }
                return 1;
            }
        });
    }
    
    @Override
    public Object getFieldValue(final String fieldName) {
        if (fieldName.equals("guildName")) {
            return (this.m_guildName != null && this.m_guildName.length() > 0) ? this.m_guildName : WakfuTranslator.getInstance().getString("noOwner");
        }
        if (fieldName.equals("buildingList")) {
            return this.m_buildingList;
        }
        if (fieldName.equals("buildingsOpenned")) {
            return this.m_buildingsOpenned && this.m_buildingList.size() > 0;
        }
        if (fieldName.equals("evolutingBuildingList")) {
            return this.m_buildingInProgress;
        }
        if (fieldName.equals("evolutingBuildingsOpenned")) {
            return this.m_evolutingBuildingsOpenned && this.m_buildingInProgress.size() > 0;
        }
        if (fieldName.equals("guildRank")) {
            return this.m_guildRank;
        }
        if (fieldName.equals("conquestRank")) {
            return this.m_conquestRank;
        }
        if (fieldName.equals("havenWorldName")) {
            return WakfuTranslator.getInstance().getString(77, this.m_worldId, new Object[0]);
        }
        if (fieldName.equals("exitInstanceName")) {
            final HavenWorldDefinition world = HavenWorldDefinitionManager.INSTANCE.getWorldFromInstance(this.m_worldId);
            if (world != null) {
                return WakfuTranslator.getInstance().getString(77, world.getExitWorldInstanceId(), new Object[0]);
            }
            return null;
        }
        else {
            if (fieldName.equals("numBuildings")) {
                return this.m_buildingInProgress.size() + this.m_buildingList.size();
            }
            if (fieldName.equals("kamasTotalCost")) {
                return WakfuTranslator.getInstance().formatNumber(this.m_kamasCost);
            }
            if (fieldName.equals("resourcesTotalCost")) {
                return WakfuTranslator.getInstance().formatNumber(this.m_resourcesCost);
            }
            if (!fieldName.equals("nextBuildingText")) {
                return null;
            }
            if (this.m_buildingInProgress.isEmpty()) {
                return WakfuTranslator.getInstance().getString("none");
            }
            final HavenWorldElementView havenWorldElementView = this.m_buildingInProgress.get(0);
            final AbstractBuildingDefinition building = havenWorldElementView.getBuildingDefinitionView().getBuilding();
            final AbstractBuildingDefinition lastBuildingFor = BuildingDefinitionHelper.getLastBuildingFor(building);
            if (lastBuildingFor == null) {
                HavenWorldView.m_logger.error((Object)("pas de batiment pour " + building.getId()));
                return null;
            }
            final String buildingName = WakfuTranslator.getInstance().getString(126, lastBuildingFor.getId(), new Object[0]);
            return WakfuTranslator.getInstance().getString("havenWorld.nextBuildingRemainingTime", buildingName, havenWorldElementView.getRemainingTime(false));
        }
    }
    
    public boolean isBuildingsOpenned() {
        return this.m_buildingsOpenned;
    }
    
    public boolean isEvolutingBuildingsOpenned() {
        return this.m_evolutingBuildingsOpenned;
    }
    
    public void setBuildingsOpenned(final boolean buildingsOpenned) {
        this.m_buildingsOpenned = buildingsOpenned;
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "buildingsOpenned");
    }
    
    public void setEvolutingBuildingsOpenned(final boolean buildingsOpenned) {
        this.m_evolutingBuildingsOpenned = buildingsOpenned;
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "evolutingBuildingsOpenned");
    }
    
    @Override
    public void tick() {
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "nextBuildingText");
    }
    
    public void removeFromTimeManager() {
        TimeManager.INSTANCE.removeListener(this);
    }
    
    static {
        m_logger = Logger.getLogger((Class)HavenWorldView.class);
        FIELDS = new String[] { "guildName", "buildingList", "evolutingBuildingList", "buildingsOpenned", "evolutingBuildingsOpenned", "guildRank", "conquestRank", "havenWorldName", "kamasTotalCost", "resourcesTotalCost", "nextBuildingText" };
    }
}
