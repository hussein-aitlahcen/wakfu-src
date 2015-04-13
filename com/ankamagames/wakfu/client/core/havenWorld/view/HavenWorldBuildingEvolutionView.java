package com.ankamagames.wakfu.client.core.havenWorld.view;

import com.ankamagames.wakfu.client.ui.component.*;
import org.apache.log4j.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.time.calendar.*;
import com.ankamagames.wakfu.client.core.havenWorld.view.catalogEntry.*;
import com.ankamagames.wakfu.common.game.havenWorld.definition.*;
import com.ankamagames.wakfu.common.game.havenWorld.definition.entry.*;
import com.ankamagames.wakfu.client.core.world.havenWorld.buildings.*;
import com.ankamagames.wakfu.common.game.havenWorld.*;
import com.ankamagames.wakfu.common.game.world.havenWorld.validator.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.common.game.guild.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.guild.*;
import com.ankamagames.wakfu.common.game.guild.definition.*;
import org.jetbrains.annotations.*;

public class HavenWorldBuildingEvolutionView extends ImmutableFieldProvider
{
    private static final Logger m_logger;
    public static final String BASE_BUILDING_FIELD = "baseBuilding";
    public static final String EVOLVED_BUILDING_FIELD = "evolvedBuilding";
    public static final String CAN_BE_EVOLVED_FIELD = "canBeEvolved";
    public static final String CAN_PAY_FIELD = "canPay";
    public static final String GUILD_NAME_FIELD = "guildName";
    public static final String BUILDING_DATE_FIELD = "buildingDate";
    public static final String HAS_RIGHT_TO_PAY_FIELD = "hasRightToPay";
    public static final String[] FIELDS;
    private GameDate m_buildingDate;
    private long m_baseBuildingUID;
    private final BuildingDefinitionView m_baseBuilding;
    private final HavenWorldCatalogBuildingEntryView m_evolvedBuilding;
    
    public HavenWorldBuildingEvolutionView(final long baseBuildingUid, final AbstractBuildingDefinition buildingCatalogEntryFrom, final BuildingCatalogEntry buildingCatalogEntryTo, final HavenWorldDataProvider havenWorldDataProvider) {
        this(buildingCatalogEntryFrom, buildingCatalogEntryTo, havenWorldDataProvider);
        this.m_baseBuildingUID = baseBuildingUid;
    }
    
    @Override
    public String[] getFields() {
        return HavenWorldBuildingEvolutionView.FIELDS;
    }
    
    public HavenWorldBuildingEvolutionView(final AbstractBuildingDefinition baseBuilding, final BuildingCatalogEntry evolvedBuilding, final HavenWorldDataProvider dataProvider) {
        super();
        this.m_baseBuilding = HavenWorldViewManager.INSTANCE.getBuildingDefinition(baseBuilding);
        this.m_evolvedBuilding = new HavenWorldCatalogBuildingEntryView(evolvedBuilding);
        if (dataProvider != null) {
            final BuildingEvolveConditionValidator validator = new BuildingEvolveConditionValidator(dataProvider);
            final int definitionId = this.m_evolvedBuilding.getFirstBuildingDefinitionId();
            this.m_evolvedBuilding.setAvailable(validator.validate(new BuildingStruct(0L, definitionId, 0, (short)(-32768), (short)(-32768))));
            this.m_evolvedBuilding.setValidator(validator);
        }
    }
    
    @Override
    public Object getFieldValue(final String fieldName) {
        if (fieldName.equals("baseBuilding")) {
            return this.m_baseBuilding;
        }
        if (fieldName.equals("evolvedBuilding")) {
            return this.m_evolvedBuilding;
        }
        if (fieldName.equals("canPay")) {
            final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
            final ClientGuildInformationHandler guildHandler = localPlayer.getGuildHandler();
            final GuildMember guildMember = guildHandler.getMember(localPlayer.getId());
            if (guildMember == null) {
                return false;
            }
            final GuildRank guildRank = guildHandler.getRank(guildMember.getRank());
            if (guildRank == null) {
                return false;
            }
            return this.m_evolvedBuilding.hasEnoughKamas() && this.m_evolvedBuilding.isAvailable() && guildRank.hasAuthorisation(GuildRankAuthorisation.MANAGE_HAVEN_WORLD);
        }
        else {
            if (fieldName.equals("guildName")) {
                return "-";
            }
            if (fieldName.equals("buildingDate")) {
                return (this.m_buildingDate == null) ? null : this.m_buildingDate.toDescriptionString();
            }
            return null;
        }
    }
    
    public void setBuildingDate(final GameDate buildingDate) {
        this.m_buildingDate = buildingDate;
    }
    
    public BuildingDefinitionView getBaseBuilding() {
        return this.m_baseBuilding;
    }
    
    public HavenWorldCatalogBuildingEntryView getEvolvedBuilding() {
        return this.m_evolvedBuilding;
    }
    
    @Nullable
    public long getBaseBuildingUID() {
        return this.m_baseBuildingUID;
    }
    
    static {
        m_logger = Logger.getLogger((Class)HavenWorldBuildingEvolutionView.class);
        FIELDS = new String[] { "baseBuilding", "evolvedBuilding", "canPay", "guildName", "buildingDate" };
    }
}
