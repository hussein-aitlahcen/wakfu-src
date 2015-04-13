package com.ankamagames.wakfu.client.core.world;

import com.ankamagames.baseImpl.graphics.core.partitions.*;
import com.ankamagames.baseImpl.graphics.alea.mobile.*;
import com.ankamagames.wakfu.client.core.game.actor.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.client.core.game.fight.*;
import com.ankamagames.framework.kernel.core.common.collections.*;
import com.ankamagames.wakfu.client.core.game.ressource.*;
import com.ankamagames.wakfu.client.core.game.interactiveElement.*;
import com.ankamagames.baseImpl.client.proxyclient.base.game.interactiveElement.*;
import java.util.*;
import gnu.trove.*;
import com.ankamagames.baseImpl.graphics.isometric.*;
import com.ankamagames.baseImpl.common.clientAndServer.world.*;

public class LocalPartitionManager extends GameLocalPartitionManager<LocalPartition, PathMobile>
{
    private static final LocalPartitionManager m_instance;
    
    public static LocalPartitionManager getInstance() {
        return LocalPartitionManager.m_instance;
    }
    
    private static int diffToLayout(final int dx, final int dy) {
        assert dx >= -2 && dx <= 2;
        assert dy >= -2 && dy <= 2;
        if (dx > 0) {
            if (dy > 0) {
                return 8;
            }
            if (dy == 0) {
                return 5;
            }
            return 2;
        }
        else if (dx == 0) {
            if (dy > 0) {
                return 7;
            }
            if (dy == 0) {
                return 4;
            }
            return 1;
        }
        else {
            if (dy > 0) {
                return 6;
            }
            if (dy == 0) {
                return 3;
            }
            return 0;
        }
    }
    
    @Override
    protected LocalPartition createPartition(final int centerX, final int centerY, final int partitionWidth, final int partitionHeight, final boolean withLayout) {
        final LocalPartition partition = new LocalPartition(centerX, centerY, partitionWidth, partitionHeight, withLayout);
        partition.initialize();
        return partition;
    }
    
    @Override
    protected CharacterActor getReferenceTarget() {
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        if (localPlayer == null) {
            return null;
        }
        return localPlayer.getActor();
    }
    
    public boolean addExternalFight(final ExternalFightInfo fight) {
        assert this.m_currentPartition != null;
        boolean fightAdded = false;
        for (final ObjectPair<Short, Short> partitionCoord : fight.getPartitions()) {
            final LocalPartition partition = ((AbstractLocalPartitionManager<LocalPartition>)this).getLocalPartitionAt(partitionCoord.getFirst(), partitionCoord.getSecond());
            if (partition != null) {
                partition.addExternalFight(fight);
                fightAdded |= true;
            }
        }
        return fightAdded;
    }
    
    public void addResource(final Resource resource) {
        assert this.m_currentPartition != null;
        final LocalPartition[] layout = ((LocalPartition)this.m_currentPartition).getLayout();
        for (int i = 0; i < layout.length; ++i) {
            final LocalPartition currentPartition = layout[i];
            if (currentPartition.isUnitsWithinBounds(resource.getWorldCellX(), resource.getWorldCellY())) {
                currentPartition.addResource(resource);
                return;
            }
        }
        LocalPartitionManager.m_logger.error((Object)("Impossible d'ajouter une resource hors du champ de vision : resource=[" + resource.getWorldCellX() + ':' + resource.getWorldCellY() + "] lpmOrigin=[" + this.getCenterPartitionWorldX() + ':' + this.getCenterPartitionWorldY() + ']'));
    }
    
    public void addInteractiveElement(final WakfuClientMapInteractiveElement element) {
        final LocalPartition[] layout = ((LocalPartition)this.m_currentPartition).getLayout();
        for (int i = 0; i < layout.length; ++i) {
            final LocalPartition partition = layout[i];
            if (partition.isUnitsWithinBounds(element.getWorldCellX(), element.getWorldCellY())) {
                partition.addInteractiveElement(element);
                return;
            }
        }
        LocalPartitionManager.m_logger.error((Object)("Impossible d'ajouter un \u00e9l\u00e9ment interactif hors du champ de vision (cache disabled): ID=" + element.getId()));
    }
    
    public ClientMapInteractiveElement getInteractiveElement(final long elementId) {
        final LocalPartition[] layout = ((LocalPartition)this.m_currentPartition).getLayout();
        for (int i = 0; i < layout.length; ++i) {
            final ClientMapInteractiveElement element = layout[i].getInteractiveElement(elementId);
            if (element != null) {
                return element;
            }
        }
        return null;
    }
    
    public ArrayList<ClientMapInteractiveElement> getInteractiveElementsFromPartitions() {
        final ArrayList<ClientMapInteractiveElement> elements = new ArrayList<ClientMapInteractiveElement>();
        final LocalPartition[] layout = ((LocalPartition)this.m_currentPartition).getLayout();
        for (int i = 0; i < layout.length; ++i) {
            layout[i].foreachInteractiveElement(new TObjectProcedure<ClientMapInteractiveElement>() {
                @Override
                public boolean execute(final ClientMapInteractiveElement object) {
                    elements.add(object);
                    return true;
                }
            });
        }
        return elements;
    }
    
    @Override
    public void cellPositionChanged(final PathMobile mobile, final int x, final int y, final short altitude) {
        final CharacterActor localActor = this.getReferenceTarget();
        if (localActor != null && mobile == localActor) {
            final LocalPartition[] layout = ((LocalPartition)this.m_currentPartition).getLayout();
            for (int i = 0; i < layout.length; ++i) {
                final LocalPartition localPartition = layout[i];
                if (localPartition != null) {
                    localPartition.onLocalCharacterChangedCell(x, y, altitude);
                }
            }
            WakfuAmbianceListener.getInstance().changeCell(x, y);
        }
    }
    
    @Override
    public void clear() {
        super.clear();
        this.despawnContent();
        this.removeAllListeners();
    }
    
    public void initialize(final int worldX, final int worldY, final boolean changeWorld) {
        this.despawnContent();
        super.initialize(worldX, worldY);
    }
    
    private void despawnContent() {
        if (this.m_currentPartition != null) {
            final LocalPartition[] layout = ((LocalPartition)this.m_currentPartition).getLayout();
            for (int i = 0; i < layout.length; ++i) {
                layout[i].deSpawnContent();
            }
        }
    }
    
    public void removeExternalFight(final ExternalFightInfo fight) {
        final LocalPartition[] layout = ((LocalPartition)this.m_currentPartition).getLayout();
        for (int i = 0; i < layout.length; ++i) {
            final LocalPartition currentPartition = layout[i];
            if (currentPartition.hasExternalFight(fight)) {
                currentPartition.despawnExternalFight(fight);
            }
        }
    }
    
    public void removeResource(final Resource resource) {
        final LocalPartition[] layout = ((LocalPartition)this.m_currentPartition).getLayout();
        for (int i = 0; i < layout.length; ++i) {
            final LocalPartition currentPartition = layout[i];
            if (currentPartition.hasResource(resource)) {
                currentPartition.despawnResource(resource);
            }
        }
    }
    
    public void removeInteractiveElement(final ClientMapInteractiveElement element) {
        final LocalPartition[] layout = ((LocalPartition)this.m_currentPartition).getLayout();
        for (int i = 0; i < layout.length; ++i) {
            final LocalPartition currentPartition = layout[i];
            if (currentPartition.hasInteractiveElement(element.getId())) {
                currentPartition.despawnInteractiveElement(element);
                return;
            }
        }
    }
    
    public void removeInteractiveElement(final long elementId) {
        final LocalPartition[] layout = ((LocalPartition)this.m_currentPartition).getLayout();
        for (int i = 0; i < layout.length; ++i) {
            final LocalPartition currentPartition = layout[i];
            if (currentPartition.hasInteractiveElement(elementId)) {
                currentPartition.despawnInteractiveElement(elementId);
                return;
            }
        }
    }
    
    public void setCenterPartition(final int worldX, final int worldY) {
        final LocalPartition oldPartition = ((LocalPartition)this.m_currentPartition).getPartition(4);
        final int px = MapConstants.getMapCoordFromCellX(worldX);
        final int py = MapConstants.getMapCoordFromCellY(worldY);
        int dx = px - oldPartition.getX();
        int dy = py - oldPartition.getY();
        final int absX = Math.abs(dx);
        final int absY = Math.abs(dy);
        final int partitionDistance = (absX > absY) ? absX : absY;
        if (partitionDistance != 0) {
            if (partitionDistance == 1) {
                ((LocalPartition)this.m_currentPartition).translate(diffToLayout(dx, dy));
            }
            else if (partitionDistance == 2) {
                ((LocalPartition)this.m_currentPartition).translate(diffToLayout(dx, dy));
                dx = px - ((LocalPartition)this.m_currentPartition).getPartition(4).getX();
                dy = py - ((LocalPartition)this.m_currentPartition).getPartition(4).getY();
                ((LocalPartition)this.m_currentPartition).translate(diffToLayout(dx, dy));
            }
            else {
                this.initialize(worldX, worldY, false);
            }
        }
        final LocalPartition newCenterPartition = ((LocalPartition)this.m_currentPartition).getPartition(4);
        ((GameLocalPartitionManager<LocalPartition, CharacterActor>)this).firePartitionChanged(this.getReferenceTarget(), oldPartition, newCenterPartition);
    }
    
    public int getCenterPartitionWorldX() {
        assert this.m_currentPartition != null;
        return ((LocalPartition)this.m_currentPartition).getPartition(4).getX() * 18;
    }
    
    public int getCenterPartitionWorldY() {
        assert this.m_currentPartition != null;
        return ((LocalPartition)this.m_currentPartition).getPartition(4).getY() * 18;
    }
    
    public void removeAllListeners() {
        synchronized (this.m_partitionChangedListeners) {
            this.m_partitionChangedListeners.clear();
        }
    }
    
    static {
        m_instance = new LocalPartitionManager();
    }
}
