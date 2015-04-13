package com.ankamagames.wakfu.client.core.game.ressource;

import org.apache.log4j.*;
import com.ankamagames.baseImpl.graphics.alea.display.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.baseImpl.graphics.isometric.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.wakfu.client.core.world.*;
import java.util.*;
import com.ankamagames.baseImpl.common.clientAndServer.world.topology.*;
import com.ankamagames.wakfu.client.console.command.display.*;
import com.ankamagames.baseImpl.graphics.alea.animatedElement.*;
import com.ankamagames.baseImpl.graphics.isometric.maskableLayer.*;
import gnu.trove.*;

public class ResourceManager extends AbstractInteractiveElementManager<Resource>
{
    public static final Logger m_logger;
    private static ResourceManager m_instance;
    private static final int[] m_updateTime;
    protected TLongObjectHashMap<Resource> m_resources;
    ArrayList<Resource> m_resourcesToDelete;
    private boolean m_displayResources;
    private final ArrayList<ResourceCreationListener> m_permanentCreationListeners;
    private final ArrayList<ResourceDestructionListener> m_permanentDestructionListeners;
    private final ArrayList<ResourceCreationListener> m_creationListeners;
    private final ArrayList<ResourceDestructionListener> m_destructionListeners;
    
    public ResourceManager() {
        super();
        this.m_resourcesToDelete = new ArrayList<Resource>(8);
        this.m_displayResources = true;
        this.m_resources = new TLongObjectHashMap<Resource>();
        this.m_permanentCreationListeners = new ArrayList<ResourceCreationListener>();
        this.m_permanentDestructionListeners = new ArrayList<ResourceDestructionListener>();
        this.m_creationListeners = new ArrayList<ResourceCreationListener>();
        this.m_destructionListeners = new ArrayList<ResourceDestructionListener>();
    }
    
    @Override
    public void process(final AleaWorldScene worldScene, final int deltaTime) {
        final LocalPlayerCharacter playerInfos = WakfuGameEntity.getInstance().getLocalPlayer();
        if (playerInfos == null) {
            return;
        }
        ResourceManager.m_updateTime[0] = 2;
        this.m_displayedElements.clear();
        this.m_resources.forEachValue(new TObjectProcedure<Resource>() {
            @Override
            public boolean execute(final Resource resource) {
                if (resource.update(worldScene, deltaTime, ResourceManager.m_updateTime)) {
                    if (resource.canBeDeleted()) {
                        ResourceManager.this.m_resourcesToDelete.add(resource);
                    }
                    else {
                        ResourceManager.this.m_displayedElements.add(resource);
                    }
                }
                return true;
            }
        });
        for (int resourcesToDeleteSize = this.m_resourcesToDelete.size(), i = 0; i < resourcesToDeleteSize; ++i) {
            this.removeResource(this.m_resourcesToDelete.get(i));
        }
        this.m_resourcesToDelete.clear();
    }
    
    @Override
    public void prepareBeforeRendering(final AleaWorldScene scene, final float centerScreenIsoWorldX, final float centerScreenIsoWorldY) {
        this.m_displayedElements.clear();
        if (!this.m_displayResources) {
            return;
        }
        this.m_resources.forEachValue(new TObjectProcedure<Resource>() {
            @Override
            public boolean execute(final Resource resource) {
                if (resource.addToScene(scene)) {
                    ResourceManager.this.updateScreenPosition(resource, scene);
                    ResourceManager.this.m_displayedElements.add(resource);
                }
                return true;
            }
        });
    }
    
    private void fireResourceCreation(final Resource resource) {
        for (int i = 0, size = this.m_creationListeners.size(); i < size; ++i) {
            this.m_creationListeners.get(i).onResourceCreation(resource);
        }
    }
    
    private void fireResourceDestruction(final Resource resource) {
        for (int i = 0, size = this.m_destructionListeners.size(); i < size; ++i) {
            this.m_destructionListeners.get(i).onResourceDestruction(resource);
        }
    }
    
    public void addRessource(final Resource resource) {
        final long key = MathHelper.getLongFromTwoInt(resource.getWorldCellX(), resource.getWorldCellY());
        if (!this.m_resources.containsKey(key)) {
            this.m_resources.put(key, resource);
            LocalPartitionManager.getInstance().addResource(resource);
            this.fireElementAdded(resource, resource.getWorldCellX(), resource.getWorldCellY(), resource.getWorldCellAltitude());
            resource.updateBlocking();
            this.fireResourceCreation(resource);
        }
        else {
            ResourceManager.m_logger.warn((Object)("Impossible d'ajouter une resource en " + resource.getWorldCellX() + ':' + resource.getWorldCellY() + " car il en existe d\u00e9j\u00e0 une."));
        }
    }
    
    public void clear() {
        final TLongObjectIterator<Resource> resourceIter = this.m_resources.iterator();
        while (resourceIter.hasNext()) {
            resourceIter.advance();
            final Resource resource = resourceIter.value();
            resource.dispose();
            resource.release();
        }
        this.m_resources.clear();
        this.m_displayedElements.clear();
        this.m_creationListeners.clear();
        this.m_creationListeners.addAll(this.m_permanentCreationListeners);
        this.m_destructionListeners.clear();
        this.m_destructionListeners.addAll(this.m_permanentDestructionListeners);
    }
    
    public Resource getResource(final int worldX, final int worldY) {
        return this.m_resources.get(MathHelper.getLongFromTwoInt(worldX, worldY));
    }
    
    public void foreachResource(final TObjectProcedure<Resource> procedure) {
        this.m_resources.forEachValue(procedure);
    }
    
    public void removeAllResources() {
        ResourceManager.m_logger.info((Object)"Supression de toutes les resources du ResourceManager.");
        this.m_resources.forEachValue(new TObjectProcedure<Resource>() {
            @Override
            public boolean execute(final Resource resource) {
                ResourceManager.this.removeResource(resource);
                return true;
            }
        });
        if (!this.m_resources.isEmpty()) {
            ResourceManager.m_logger.error((Object)("Il reste encore " + this.m_resources.size() + " apr\u00e8s la supression !!!"));
        }
        this.m_creationListeners.clear();
        this.m_creationListeners.addAll(this.m_permanentCreationListeners);
        this.m_destructionListeners.clear();
        this.m_destructionListeners.addAll(this.m_permanentDestructionListeners);
    }
    
    public void removeResource(final Resource resource) {
        if (resource != null) {
            this.m_resources.remove(resource.getId());
            LocalPartitionManager.getInstance().removeResource(resource);
            final TopologyMapInstance map = TopologyMapManager.getMapFromCell((short)resource.getWorldCellX(), (short)resource.getWorldCellY());
            if (map != null) {
                map.setCellBlocked(resource.getWorldCellX(), resource.getWorldCellY(), false);
            }
            else {
                ResourceManager.m_logger.warn((Object)("retrait d'une ressource en (" + resource.getWorldCellX() + ", " + resource.getWorldCellY() + ") alors que la map est inconnue/pas charg\u00e9e\t"));
            }
            this.fireResourceDestruction(resource);
            resource.release();
        }
        else {
            ResourceManager.m_logger.error((Object)"Impossible de retirer une resource null");
        }
    }
    
    public void removeResource(final long resourceId) {
        final Resource resource = this.m_resources.get(resourceId);
        if (resource != null) {
            this.removeResource(resource);
        }
        else {
            ResourceManager.m_logger.warn((Object)("Impossible de supprimer une resource d'ID " + resourceId + " qui n'existe pas"));
        }
    }
    
    public boolean resourceAlreadyExist(final int worldX, final int worldY) {
        return this.m_resources.contains(MathHelper.getLongFromTwoInt(worldX, worldY));
    }
    
    public static ResourceManager getInstance() {
        return ResourceManager.m_instance;
    }
    
    public void addPermanentCreationListener(final ResourceCreationListener listener) {
        if (!this.m_permanentCreationListeners.contains(listener)) {
            this.m_permanentCreationListeners.add(listener);
        }
    }
    
    public void addPermanentDestructionListener(final ResourceDestructionListener listener) {
        if (!this.m_permanentDestructionListeners.contains(listener)) {
            this.m_permanentDestructionListeners.add(listener);
        }
    }
    
    public void addCreationListener(final ResourceCreationListener listener) {
        if (!this.m_creationListeners.contains(listener)) {
            this.m_creationListeners.add(listener);
        }
    }
    
    public void addDestructionListener(final ResourceDestructionListener listener) {
        if (!this.m_destructionListeners.contains(listener)) {
            this.m_destructionListeners.add(listener);
        }
    }
    
    public boolean removeCreationListener(final ResourceCreationListener listener) {
        return this.m_creationListeners.remove(listener);
    }
    
    public boolean removeDestructionListener(final ResourceDestructionListener listener) {
        return this.m_destructionListeners.remove(listener);
    }
    
    @Override
    protected void fireElementAdded(final Resource element, final int x, final int y, final int z) {
        super.fireElementAdded(element, x, y, z);
        HideFightOccluders.hide(element);
    }
    
    @Override
    public void setUndefinedMaskLayer() {
        this.m_resources.forEachValue(new TObjectProcedure<Resource>() {
            @Override
            public boolean execute(final Resource object) {
                MaskableHelper.setUndefined(object);
                return true;
            }
        });
    }
    
    static {
        m_logger = Logger.getLogger((Class)ResourceManager.class);
        ResourceManager.m_instance = new ResourceManager();
        m_updateTime = new int[1];
    }
}
