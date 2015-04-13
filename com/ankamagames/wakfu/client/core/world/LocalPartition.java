package com.ankamagames.wakfu.client.core.world;

import com.ankamagames.baseImpl.graphics.core.partitions.*;
import com.ankamagames.baseImpl.client.proxyclient.base.game.interactiveElement.*;
import org.apache.log4j.*;
import com.ankamagames.wakfu.client.core.game.fight.*;
import com.ankamagames.framework.kernel.core.common.collections.*;
import com.ankamagames.wakfu.client.alea.environment.*;
import java.util.*;
import com.ankamagames.baseImpl.graphics.alea.environment.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.interactiveElement.*;
import com.ankamagames.wakfu.client.core.game.interactiveElement.*;
import com.ankamagames.wakfu.client.core.game.ressource.*;
import gnu.trove.*;

public class LocalPartition extends GamePartition<LocalPartition>
{
    private static final TObjectProcedure<ClientMapInteractiveElement> CLEAR_IE_PROCEDURE;
    private static final TLongProcedure REMOVE_RESOURCE_PROCEDURE;
    private final TLongProcedure CLEAR_FIGHT_PROCEDURE;
    protected static final Logger m_logger;
    protected final TLongObjectHashMap<Resource> m_resources;
    protected final TLongObjectHashMap<ClientMapInteractiveElement> m_interactiveElements;
    protected final TLongObjectHashMap<ExternalFightInfo> m_externalFight;
    private final List<LocalPartition> _partitionToDelete;
    
    LocalPartition(final int x, final int y, final int width, final int height, final boolean withLayout) {
        super();
        this.CLEAR_FIGHT_PROCEDURE = new TLongProcedure() {
            @Override
            public boolean execute(final long fightId) {
                LocalPartition.this.m_externalFight.get(fightId).removeReference();
                return true;
            }
        };
        this.m_resources = new TLongObjectHashMap<Resource>();
        this.m_interactiveElements = new TLongObjectHashMap<ClientMapInteractiveElement>();
        this.m_externalFight = new TLongObjectHashMap<ExternalFightInfo>();
        this._partitionToDelete = new ArrayList<LocalPartition>(5);
        this.setBounds(x, y, width, height);
        if (withLayout) {
            this.setLayoutArray(new LocalPartition[9]);
        }
    }
    
    @Override
    public void initialize() {
        super.initialize();
        final short x = (short)this.m_x;
        final short y = (short)this.m_y;
        final TIntObjectIterator<FightInfo> iterator = FightManager.getInstance().getFightsIterator();
        while (iterator.hasNext()) {
            iterator.advance();
            final FightInfo fight = iterator.value();
            if (fight instanceof ExternalFightInfo) {
                final ExternalFightInfo externalFight = (ExternalFightInfo)fight;
                for (final ObjectPair<Short, Short> part : externalFight.getPartitions()) {
                    if (x == part.getFirst() && y == part.getSecond()) {
                        this.addExternalFight(externalFight);
                        break;
                    }
                }
            }
        }
        final WakfuClientEnvironmentMap map = (WakfuClientEnvironmentMap)EnvironmentMapManager.getInstance().getMap(x, y);
        if (map == null) {
            return;
        }
        final InteractiveElementDef[] interactiveElementDefinitions = map.getInteractiveElements();
        if (interactiveElementDefinitions != null) {
            InteractiveElementDefinitionsManager.loadDefinitionsForPartition(this, interactiveElementDefinitions);
            InteractiveElementDefinitionsManager.spawnClientInteractiveElements(this, interactiveElementDefinitions);
        }
    }
    
    void addResource(final Resource resource) {
        if (!this.m_resources.containsKey(resource.getId())) {
            this.m_resources.put(resource.getId(), resource);
        }
        else {
            LocalPartition.m_logger.error((Object)("Impossible d'ajouter une resource d'ID=" + resource.getId() + " \u00e0 la partition " + this + " qui la contient d\u00e9j\u00e0."));
        }
    }
    
    public void onCheckOut() {
    }
    
    public void onCheckIn() {
        this.m_resources.clear();
        this.m_externalFight.clear();
        this.m_interactiveElements.clear();
    }
    
    void despawnExternalFight(final ExternalFightInfo fight) {
        this.m_externalFight.remove(fight.getId());
    }
    
    void despawnResource(final Resource resource) {
        this.m_resources.remove(resource.getId());
    }
    
    boolean hasExternalFight(final ExternalFightInfo fight) {
        return this.m_externalFight.contains(fight.getId());
    }
    
    boolean hasResource(final Resource resource) {
        return this.m_resources.contains(resource.getId());
    }
    
    void despawnInteractiveElement(final long elementId) {
        final ClientMapInteractiveElement element = this.m_interactiveElements.remove(elementId);
        if (element != null) {
            element.onDeSpawn();
        }
    }
    
    void despawnInteractiveElement(final ClientMapInteractiveElement element) {
        this.m_interactiveElements.remove(element.getId());
        element.onDeSpawn();
    }
    
    void addInteractiveElement(final ClientMapInteractiveElement element) {
        try {
            if (!this.m_interactiveElements.containsKey(element.getId())) {
                this.m_interactiveElements.put(element.getId(), element);
                element.onSpawn();
            }
            else {
                LocalPartition.m_logger.error((Object)("Impossible d'ajouter un \u00e9l\u00e9ments interactif d'ID=" + element.getId() + " \u00e0 la partition " + this + " qui le contient d\u00e9j\u00e0."));
            }
        }
        catch (Exception e) {
            LocalPartition.m_logger.error((Object)"", (Throwable)e);
        }
    }
    
    boolean hasInteractiveElement(final long elementId) {
        return this.m_interactiveElements.contains(elementId);
    }
    
    public void foreachInteractiveElement(final TObjectProcedure<ClientMapInteractiveElement> procedure) {
        this.m_interactiveElements.forEachValue(procedure);
    }
    
    ClientMapInteractiveElement getInteractiveElement(final long elementId) {
        return this.m_interactiveElements.get(elementId);
    }
    
    void addExternalFight(final ExternalFightInfo fight) {
        if (!this.m_externalFight.contains(fight.getId())) {
            fight.addReference();
            this.m_externalFight.put(fight.getId(), fight);
        }
        else {
            LocalPartition.m_logger.error((Object)("Impossible d'ajouter un item d'ID=" + fight.getId() + " \u00e0 la partition " + this + " qui le contient d\u00e9j\u00e0."));
        }
    }
    
    @Override
    protected void clear() {
        super.clear();
        this.m_resources.clear();
        this.m_interactiveElements.clear();
        this.m_externalFight.clear();
    }
    
    @Override
    public void deSpawnContent() {
        super.deSpawnContent();
        this.m_resources.forEachKey(LocalPartition.REMOVE_RESOURCE_PROCEDURE);
        this.m_externalFight.forEachKey(this.CLEAR_FIGHT_PROCEDURE);
        InteractiveElementDefinitionsManager.unloadDefinitionsForPartition(this.m_x, this.m_y);
        this.m_interactiveElements.forEachValue(LocalPartition.CLEAR_IE_PROCEDURE);
        this.clear();
    }
    
    private static LocalPartition createPartitionAround(final LocalPartition center, final int offsetX, final int offsetY) {
        final LocalPartition partition = new LocalPartition(center.getX() + offsetX, center.getY() + offsetY, 18, 18, false);
        partition.initialize();
        return partition;
    }
    
    private void renewPartition(final int layoutEntry) {
        final LocalPartition[] layout = this.getLayout();
        final LocalPartition center = layout[4];
        switch (layoutEntry) {
            case 7: {
                layout[7] = createPartitionAround(center, 0, 1);
                break;
            }
            case 1: {
                layout[1] = createPartitionAround(center, 0, -1);
                break;
            }
            case 3: {
                layout[3] = createPartitionAround(center, -1, 0);
                break;
            }
            case 5: {
                layout[5] = createPartitionAround(center, 1, 0);
                break;
            }
            case 0: {
                layout[0] = createPartitionAround(center, -1, -1);
                break;
            }
            case 2: {
                layout[2] = createPartitionAround(center, 1, -1);
                break;
            }
            case 8: {
                layout[8] = createPartitionAround(center, 1, 1);
                break;
            }
            case 6: {
                layout[6] = createPartitionAround(center, -1, 1);
                break;
            }
            default: {
                LocalPartition.m_logger.error((Object)"Tentative de renew d'une LocalPartition inexistante");
                break;
            }
        }
    }
    
    void translate(final int layoutEntry) {
        final LocalPartition[] layout = this.getLayout();
        switch (layoutEntry) {
            case 7: {
                this._partitionToDelete.add(layout[1]);
                this._partitionToDelete.add(layout[0]);
                this._partitionToDelete.add(layout[2]);
                layout[1] = layout[4];
                layout[0] = layout[3];
                layout[2] = layout[5];
                layout[4] = layout[7];
                layout[3] = layout[6];
                layout[5] = layout[8];
                this.renewPartition(7);
                this.renewPartition(6);
                this.renewPartition(8);
                break;
            }
            case 1: {
                this._partitionToDelete.add(layout[7]);
                this._partitionToDelete.add(layout[6]);
                this._partitionToDelete.add(layout[8]);
                layout[7] = layout[4];
                layout[6] = layout[3];
                layout[8] = layout[5];
                layout[4] = layout[1];
                layout[3] = layout[0];
                layout[5] = layout[2];
                this.renewPartition(1);
                this.renewPartition(0);
                this.renewPartition(2);
                break;
            }
            case 3: {
                this._partitionToDelete.add(layout[5]);
                this._partitionToDelete.add(layout[2]);
                this._partitionToDelete.add(layout[8]);
                layout[5] = layout[4];
                layout[2] = layout[1];
                layout[8] = layout[7];
                layout[4] = layout[3];
                layout[1] = layout[0];
                layout[7] = layout[6];
                this.renewPartition(0);
                this.renewPartition(3);
                this.renewPartition(6);
                break;
            }
            case 5: {
                this._partitionToDelete.add(layout[3]);
                this._partitionToDelete.add(layout[0]);
                this._partitionToDelete.add(layout[6]);
                layout[3] = layout[4];
                layout[0] = layout[1];
                layout[6] = layout[7];
                layout[4] = layout[5];
                layout[1] = layout[2];
                layout[7] = layout[8];
                this.renewPartition(2);
                this.renewPartition(5);
                this.renewPartition(8);
                break;
            }
            case 0: {
                this._partitionToDelete.add(layout[5]);
                this._partitionToDelete.add(layout[2]);
                this._partitionToDelete.add(layout[8]);
                this._partitionToDelete.add(layout[7]);
                this._partitionToDelete.add(layout[6]);
                layout[8] = layout[4];
                layout[4] = layout[0];
                layout[5] = layout[1];
                layout[7] = layout[3];
                this.renewPartition(1);
                this.renewPartition(2);
                this.renewPartition(0);
                this.renewPartition(3);
                this.renewPartition(6);
                break;
            }
            case 2: {
                this._partitionToDelete.add(layout[0]);
                this._partitionToDelete.add(layout[3]);
                this._partitionToDelete.add(layout[6]);
                this._partitionToDelete.add(layout[7]);
                this._partitionToDelete.add(layout[8]);
                layout[6] = layout[4];
                layout[3] = layout[1];
                layout[7] = layout[5];
                layout[4] = layout[2];
                this.renewPartition(0);
                this.renewPartition(1);
                this.renewPartition(2);
                this.renewPartition(5);
                this.renewPartition(8);
                break;
            }
            case 8: {
                this._partitionToDelete.add(layout[2]);
                this._partitionToDelete.add(layout[1]);
                this._partitionToDelete.add(layout[0]);
                this._partitionToDelete.add(layout[3]);
                this._partitionToDelete.add(layout[6]);
                layout[0] = layout[4];
                layout[1] = layout[5];
                layout[3] = layout[7];
                layout[4] = layout[8];
                this.renewPartition(2);
                this.renewPartition(5);
                this.renewPartition(8);
                this.renewPartition(7);
                this.renewPartition(6);
                break;
            }
            case 6: {
                this._partitionToDelete.add(layout[0]);
                this._partitionToDelete.add(layout[1]);
                this._partitionToDelete.add(layout[2]);
                this._partitionToDelete.add(layout[5]);
                this._partitionToDelete.add(layout[8]);
                layout[1] = layout[3];
                layout[2] = layout[4];
                layout[4] = layout[6];
                layout[5] = layout[7];
                this.renewPartition(0);
                this.renewPartition(3);
                this.renewPartition(6);
                this.renewPartition(7);
                this.renewPartition(8);
                break;
            }
            default: {
                LocalPartition.m_logger.error((Object)"Translation d'une LocalPartition dans une direction invalide, bizarre...");
                break;
            }
        }
        for (int i = 0, size = this._partitionToDelete.size(); i < size; ++i) {
            this._partitionToDelete.get(i).deSpawnContent();
        }
        this._partitionToDelete.clear();
    }
    
    @Override
    public String toString() {
        return "(" + this.getX() + ',' + this.getY() + ')';
    }
    
    void onLocalCharacterChangedCell(final int x, final int y, final short altitude) {
        if (this.m_interactiveElements.isEmpty()) {
            return;
        }
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        if (!localPlayer.canActivateInteractiveElements()) {
            return;
        }
        final Point3 pos = new Point3();
        this.m_interactiveElements.forEachValue(new TObjectProcedure<ClientMapInteractiveElement>() {
            @Override
            public boolean execute(final ClientMapInteractiveElement element) {
                pos.set(x, y, altitude);
                if ((element instanceof PositionTrigger || element instanceof DimensionalBagExitTrigger) && element.getWorldCellX() == x && element.getWorldCellY() == y && element.getWorldCellAltitude() <= altitude && element.getWorldCellAltitude() + 6 > altitude) {
                    element.fireAction(InteractiveElementAction.WALKON, localPlayer);
                }
                else if (element instanceof ZoneTrigger) {
                    final ZoneTrigger zt = (ZoneTrigger)element;
                    final boolean isTrigger = zt.isTrigger(pos);
                    final boolean localPlayerWasInside = zt.isLocalPlayerWasInside();
                    if (isTrigger && !localPlayerWasInside) {
                        element.fireAction(InteractiveElementAction.WALKIN, localPlayer);
                    }
                    else if (!isTrigger && localPlayerWasInside) {
                        element.fireAction(InteractiveElementAction.WALKOUT, localPlayer);
                    }
                    else if (isTrigger && localPlayerWasInside) {
                        element.fireAction(InteractiveElementAction.WALKON, localPlayer);
                    }
                }
                else if (element.isTrigger(pos)) {
                    element.fireAction(element.getDefaultAction(), localPlayer);
                }
                return true;
            }
        });
    }
    
    static {
        CLEAR_IE_PROCEDURE = new TObjectProcedure<ClientMapInteractiveElement>() {
            @Override
            public boolean execute(final ClientMapInteractiveElement element) {
                element.onDeSpawn();
                return true;
            }
        };
        REMOVE_RESOURCE_PROCEDURE = new TLongProcedure() {
            @Override
            public boolean execute(final long resourceId) {
                ResourceManager.getInstance().removeResource(resourceId);
                return true;
            }
        };
        m_logger = Logger.getLogger((Class)LocalPartition.class);
    }
}
