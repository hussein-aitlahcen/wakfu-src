package com.ankamagames.wakfu.common.game.havenWorld;

import com.ankamagames.wakfu.common.game.havenWorld.exception.*;
import gnu.trove.*;
import com.ankamagames.wakfu.common.game.havenWorld.definition.*;
import com.ankamagames.wakfu.common.game.havenWorld.action.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.interactiveElement.*;
import com.ankamagames.wakfu.common.game.havenWorld.procedure.*;
import com.ankamagames.framework.kernel.core.maths.*;

public class HavenWorldController
{
    private final HavenWorldModel m_world;
    
    public HavenWorldController(final HavenWorld world) {
        super();
        this.m_world = (HavenWorldModel)world;
    }
    
    public HavenWorld getWorld() {
        return this.m_world;
    }
    
    public final void setGuild(final GuildInfo guildInfo) throws HavenWorldException {
        if (this.m_world.getGuildInfo() != null) {
            throw new HavenWorldException(HavenWorldError.WORLD_ALREADY_HAVE_GUILD, "Le Havre-monde a d\u00e9j\u00e0 une guilde");
        }
        this.m_world.setGuildInfo(guildInfo);
    }
    
    public final void forcedSetGuild(final GuildInfo guildInfo) throws HavenWorldException {
        this.m_world.setGuildInfo(guildInfo);
    }
    
    protected final Building addBuilding(final AbstractBuildingStruct info, final long creationDate) throws HavenWorldException {
        final AbstractBuildingDefinition buildingDef = info.getDefinition();
        final Building building = new BuildingModel(buildingDef, info.getBuildingUid(), info.getItemId(), creationDate, info.getCellX(), info.getCellY());
        this.addBuilding(building);
        return building;
    }
    
    public final void addBuilding(final Building building) throws HavenWorldException {
        if (building.getDefinition() == null) {
            throw new HavenWorldException(HavenWorldError.NO_DEFINITION, "Aucune d\u00e9finition pour le b\u00e2timent");
        }
        if (this.m_world.getBuilding(building.getUid()) != null) {
            throw new HavenWorldException(HavenWorldError.BUILDING_ALREADY_EXIST, "Le b\u00e2timent existe d\u00e9j\u00e0");
        }
        this.m_world.addBuilding(building);
    }
    
    public final void removeBuilding(final long buildingUid) throws HavenWorldException {
        final Building building = this.m_world.getBuilding(buildingUid);
        if (building == null) {
            throw new HavenWorldException(HavenWorldError.BUILDING_NOT_FOUND, "Le b\u00e2timent n'existe pas");
        }
        if (building.hasElement()) {
            throw new HavenWorldException(HavenWorldError.BUILDING_ELEMENT_REMAINING, "Il existe encore des \u00e9l\u00e9ments li\u00e9s \u00e0 ce b\u00e2timent");
        }
        this.m_world.removeBuilding(buildingUid);
    }
    
    protected final void addBuildingElement(final long buildingUid, final long uid, final long elementId) throws HavenWorldException {
        final BuildingModel building = (BuildingModel)this.m_world.getBuilding(buildingUid);
        if (building == null) {
            throw new HavenWorldException(HavenWorldError.BUILDING_NOT_FOUND, "Le b\u00e2timent n'existe pas " + buildingUid);
        }
        if (building.getElement(uid) != null) {
            throw new HavenWorldException(HavenWorldError.BUILDING_ELEMENT_ALREADY_EXIST, "L'\u00e9l\u00e9ment existe d\u00e9j\u00e0");
        }
        building.addElement(new BuildingElementModel(uid, elementId));
    }
    
    protected void createAllIE(final AbstractBuildingStruct info, final HavenWorldInteractiveElementSpawner interactiveElementHandler) {
        final AbstractBuildingDefinition building = info.getDefinition();
        final long buildingUid = info.getBuildingUid();
        final int x = info.getCellX();
        final int y = info.getCellY();
        building.forEachElement(new TObjectProcedure<BuildingIEDefinition>() {
            @Override
            public boolean execute(final BuildingIEDefinition object) {
                final Point3 relativePos = new Point3(object.getRelativePos());
                relativePos.add(x, y);
                final long elementUID = HavenWorldElementGUID.getNextGUID();
                final long ieId = InteractiveElementIDGenerator.nextDynamicId();
                HavenWorldController.this.addBuildingElement(buildingUid, elementUID, ieId);
                final MapInteractiveElement interactiveElement = interactiveElementHandler.requestSpawnInteractiveElement(buildingUid, object.getTemplateId(), elementUID, ieId, relativePos);
                if (interactiveElement == null) {
                    HavenWorldController.this.removeBuildingElement(elementUID);
                    throw new HavenWorldException(HavenWorldError.BUILDING_ELEMENT_COULD_NOT_BE_SPAWNED, "Impossible de spawner l'\u00e9l\u00e9ment.");
                }
                return true;
            }
        });
    }
    
    protected final void removeBuildingElement(final long elementUid) throws HavenWorldException {
        final FindBuildingFromElementUid findElement = new FindBuildingFromElementUid(elementUid);
        this.m_world.forEachBuilding(findElement);
        final long buildingUid = findElement.getBuildingUid();
        final BuildingModel building = (BuildingModel)this.m_world.getBuilding(buildingUid);
        if (building == null) {
            throw new HavenWorldException(HavenWorldError.BUILDING_NOT_FOUND, "Le b\u00e2timent n'existe pas");
        }
        if (building.getElement(elementUid) == null) {
            throw new HavenWorldException(HavenWorldError.BUILDING_ELEMENT_NOT_FOUND, "L'\u00e9l\u00e9ment n'existe pas");
        }
        building.removeElement(elementUid);
    }
    
    protected final void setEquippedItemId(final long buildingUid, final int itemId) throws HavenWorldException {
        final BuildingModel building = (BuildingModel)this.m_world.getBuilding(buildingUid);
        if (building == null) {
            throw new HavenWorldException(HavenWorldError.BUILDING_NOT_FOUND, "Le b\u00e2timent n'existe pas");
        }
        final AbstractBuildingDefinition def = building.getDefinition();
        if (itemId != 0 && !def.acceptItem(itemId)) {
            throw new HavenWorldException(HavenWorldError.BUILDING_WRONG_ITEM, "Impossible d'\u00e9quipper l'item " + itemId);
        }
        building.setEquippedItemId(itemId);
    }
    
    public final Partition addPartition(final short x, final short y) throws HavenWorldException {
        final Partition partition = new PartitionModel(x, y);
        this.addPartition(partition);
        return partition;
    }
    
    protected final Partition addPartition(final short x, final short y, final short topLeftPatch, final short topRightPatch, final short bottomLeftPatch, final short bottomRightPatch) {
        final Partition partition = new PartitionModel(x, y, topLeftPatch, topRightPatch, bottomLeftPatch, bottomRightPatch);
        this.addPartition(partition);
        return partition;
    }
    
    protected final void addPartition(final Partition partition) throws HavenWorldException {
        if (this.m_world.getPartition(partition.getX(), partition.getY()) != null) {
            throw new HavenWorldException(HavenWorldError.PARTITION_ALREADY_EXIST, "La partition existe d\u00e9j\u00e0");
        }
        this.m_world.addPartition(partition);
    }
    
    public final void modifyPatches(final short partitionX, final short partitionY, final short topLeftPatch, final short topRightPatch, final short bottomLeftPatch, final short bottomRightPatch) throws HavenWorldException {
        final PartitionModel partition = (PartitionModel)this.m_world.getPartition(partitionX, partitionY);
        if (partition == null) {
            throw new HavenWorldException(HavenWorldError.PARTITION_NOT_FOUND, "La partition n'existe pas");
        }
        final boolean changed = partition.modifyPatches(topLeftPatch, topRightPatch, bottomLeftPatch, bottomRightPatch);
        if (changed) {
            this.m_world.onPartitionModified(partition);
        }
    }
    
    public final void addResources(final int resources) throws HavenWorldException {
        if (resources < 0) {
            throw new HavenWorldException(HavenWorldError.BAD_RESOURCES_NUMBER, "Impossible de rajouter une quantit\u00e9 de resources n\u00e9gative");
        }
        final HavenWorldModel world = (HavenWorldModel)this.getWorld();
        world.setResources(Math.min(50000000, MathHelper.ensureInt(world.getResources() + resources)));
    }
    
    protected final void removeResources(final int resources) throws HavenWorldException {
        if (resources < 0) {
            throw new HavenWorldException(HavenWorldError.BAD_RESOURCES_NUMBER, "Impossible de supprimer une quantit\u00e9 de resources n\u00e9gative");
        }
        final HavenWorldModel world = (HavenWorldModel)this.getWorld();
        if (world.getResources() - resources < 0) {
            throw new HavenWorldException(HavenWorldError.BAD_RESOURCES_NUMBER, "Impossible de descendre en dessous de 0 ressources");
        }
        world.setResources(MathHelper.ensurePositiveInt(world.getResources() - resources));
    }
    
    public final void setResources(final int resources) throws HavenWorldException {
        if (resources < 0) {
            throw new HavenWorldException(HavenWorldError.BAD_RESOURCES_NUMBER, "Impossible de d\u00e9finir une quantit\u00e9 de resources n\u00e9gative");
        }
        final HavenWorldModel world = (HavenWorldModel)this.getWorld();
        world.setResources(resources);
    }
    
    @Override
    public String toString() {
        return "HavenWorldController{m_world=" + this.m_world + '}';
    }
}
