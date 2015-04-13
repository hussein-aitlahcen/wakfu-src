package com.ankamagames.wakfu.client.core.game.interactiveElement;

import org.apache.log4j.*;
import com.ankamagames.framework.kernel.core.common.serialization.*;
import java.nio.*;
import com.ankamagames.wakfu.client.core.game.item.*;
import com.ankamagames.wakfu.common.game.fight.*;
import com.ankamagames.wakfu.client.core.action.*;
import com.ankamagames.framework.script.action.*;
import com.ankamagames.wakfu.client.core.world.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.client.core.game.fight.*;
import java.util.*;
import com.ankamagames.baseImpl.client.proxyclient.base.game.interactiveElement.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.interactiveElement.*;
import com.ankamagames.wakfu.client.ui.mru.MRUActions.*;
import com.ankamagames.wakfu.client.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.framework.fileFormat.io.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;

public class FloorItemInteractiveElement extends WakfuClientMapInteractiveElement
{
    private static final Logger m_logger;
    private final ArrayList<FloorItem> m_floorItems;
    private final ArrayList<Long> m_floorItemIds;
    private final BinarSerialPart SHARED_DATAS;
    
    public FloorItemInteractiveElement() {
        super();
        this.m_floorItems = new ArrayList<FloorItem>();
        this.m_floorItemIds = new ArrayList<Long>();
        this.SHARED_DATAS = new BinarSerialPart() {
            @Override
            public void serialize(final ByteBuffer buffer) {
                throw new UnsupportedOperationException("La synchronisation du contenu de l'objet est faite depuis le serveur => par de s\u00e9rialisation");
            }
            
            @Override
            public void unserialize(final ByteBuffer buffer, final int version) {
                final ArrayList<FloorItem> floorItem = new ArrayList<FloorItem>();
                final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
                FloorItemInteractiveElement.this.m_position.setX(buffer.getInt());
                FloorItemInteractiveElement.this.m_position.setY(buffer.getInt());
                FloorItemInteractiveElement.this.m_position.setZ(buffer.getShort());
                boolean isConcernedByClientFight = false;
                for (int i = buffer.get() - 1; i >= 0; --i) {
                    final int size = buffer.getInt();
                    final byte[] serializedFloorItem = new byte[size];
                    buffer.get(serializedFloorItem);
                    final FloorItem item = new FloorItem();
                    item.unserialize(serializedFloorItem);
                    if (!FloorItemInteractiveElement.this.m_floorItemIds.contains(item.getId())) {
                        item.setFloorItemInteractiveElement(FloorItemInteractiveElement.this);
                        FloorItemManager.getInstance().addFloorItem(item);
                        FloorItemInteractiveElement.this.m_floorItems.add(item);
                        FloorItemInteractiveElement.this.m_floorItemIds.add(item.getId());
                        final Fight currentOrObservedFight = localPlayer.getCurrentOrObservedFight();
                        isConcernedByClientFight = (isConcernedByClientFight || (item.getCurrentFightId() != -1 && currentOrObservedFight != null && currentOrObservedFight.getId() == item.getCurrentFightId()));
                        if (isConcernedByClientFight) {
                            item.setVisible(false);
                            floorItem.add(item);
                            FloorItemInteractiveElement.this.setCurrentFightId(currentOrObservedFight.getId());
                        }
                        else {
                            FloorItemInteractiveElement.this.setCurrentFightId(-1);
                        }
                    }
                }
                for (int i = buffer.get() - 1; i >= 0; --i) {
                    final long id = buffer.getLong();
                    final Iterator<FloorItem> it = FloorItemInteractiveElement.this.m_floorItems.iterator();
                    while (it.hasNext()) {
                        final FloorItem item2 = it.next();
                        if (item2.getId() == id) {
                            it.remove();
                            FloorItemInteractiveElement.this.m_floorItemIds.remove(item2.getId());
                        }
                    }
                }
                FloorItemInteractiveElement.this.initialize();
                if (!floorItem.isEmpty()) {
                    final int actionTypeId = FightActionType.ITEM_DROP.getId();
                    if (isConcernedByClientFight) {
                        final DropItemAction action = new DropItemAction(TimedAction.getNextUid(), actionTypeId, 0, localPlayer.getCurrentOrObservedFight().getId(), floorItem);
                        FightActionGroupManager.getInstance().addActionToPendingGroup(localPlayer.getCurrentOrObservedFight(), action);
                    }
                }
                if (FloorItemInteractiveElement.this.m_floorItems.size() <= 0) {
                    LocalPartitionManager.getInstance().removeInteractiveElement(FloorItemInteractiveElement.this);
                }
            }
        };
    }
    
    @Override
    public void onCheckIn() {
        super.onCheckIn();
        this.m_floorItems.clear();
        this.m_floorItemIds.clear();
    }
    
    @Override
    public void onCheckOut() {
        super.onCheckOut();
        this.m_overHeadable = true;
        this.m_overheadOffset = 40;
        this.m_oldState = 0;
        this.m_state = 0;
        assert this.m_floorItemIds.isEmpty();
        assert this.m_floorItems.isEmpty();
    }
    
    @Override
    protected BinarSerialPart getSynchronizationSpecificPart() {
        return this.SHARED_DATAS;
    }
    
    @Override
    public void onViewUpdated(final ClientInteractiveElementView view) {
    }
    
    @Override
    public boolean onAction(final InteractiveElementAction action, final InteractiveElementUser user) {
        this.runScript(action);
        return false;
    }
    
    public InteractiveElementAction getInteractiveDefaultAction() {
        return null;
    }
    
    @Override
    public InteractiveElementAction[] getInteractiveUsableActions() {
        return InteractiveElementAction.EMPTY_ACTIONS;
    }
    
    @Override
    public String getName() {
        final StringBuilder text = new StringBuilder();
        WakfuClientInstance.getInstance();
        final LocalPlayerCharacter localplayer = WakfuClientInstance.getGameEntity().getLocalPlayer();
        final int localFightId = localplayer.isOnFight() ? localplayer.getCurrentFight().getId() : -1;
        for (final FloorItem floorItem : this.m_floorItems) {
            if (!floorItem.isVisible()) {
                break;
            }
            if (floorItem.getCurrentFightId() != localFightId) {
                continue;
            }
            text.append(floorItem.getItem().getName()).append("(").append(floorItem.getItem().getQuantity()).append(")( ");
            if (floorItem.getLock() == null || floorItem.getLock().contains(localplayer.getId())) {
                text.append(WakfuTranslator.getInstance().getString("desc.loot.available"));
            }
            else {
                text.append(WakfuTranslator.getInstance().getString("desc.loot.notAvailable"));
            }
            text.append(" )\n");
        }
        return text.toString();
    }
    
    @Override
    public boolean isVisible() {
        Fight fight = null;
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        if (localPlayer != null && localPlayer.getCurrentOrObservedFight() != null) {
            fight = localPlayer.getCurrentOrObservedFight();
        }
        final ArrayList<FloorItem> floorItems = this.getFloorItems();
        final int fightId = (fight != null) ? fight.getId() : -1;
        for (int i = 0; i < floorItems.size(); ++i) {
            if (floorItems.get(i).getCurrentFightId() == fightId && floorItems.get(i).isVisible()) {
                return super.isVisible();
            }
        }
        return false;
    }
    
    @Override
    public boolean isMRUPositionable() {
        return true;
    }
    
    @Override
    public short getMRUHeight() {
        return (short)(this.getHeight() * 10.0f);
    }
    
    @Override
    public AbstractMRUAction[] getInteractiveMRUActions() {
        return new AbstractMRUAction[] { MRUActions.ITEM_PICK_UP_ACTION.getMRUAction() };
    }
    
    public boolean add(final FloorItem floorItem) {
        if (!this.m_floorItems.contains(floorItem)) {
            this.m_floorItemIds.add(floorItem.getId());
            return this.m_floorItems.add(floorItem);
        }
        return false;
    }
    
    public ArrayList<FloorItem> getFloorItems() {
        return this.m_floorItems;
    }
    
    public boolean remove(final FloorItem removed) {
        final boolean remove = this.m_floorItems.remove(removed);
        this.m_floorItemIds.remove(removed.getId());
        return remove;
    }
    
    @Override
    public void onDeSpawn() {
        super.onDeSpawn();
    }
    
    public void initialize() {
        for (final ClientInteractiveElementView view : this.getViews()) {
            if (view instanceof WakfuClientInteractiveAnimatedElementSceneView) {
                this.initializeView((WakfuClientInteractiveAnimatedElementSceneView)view);
            }
        }
    }
    
    private void initializeView(final WakfuClientInteractiveAnimatedElementSceneView view) {
        view.setOccluder(false);
        if (this.m_floorItems.size() == 1) {
            final int floorGfxId = this.m_floorItems.get(0).getFloorGfxId();
            if (floorGfxId > 0 && this.equipementFileExists(floorGfxId)) {
                view.setAnmPath("ANMEquipmentPath");
                view.setViewGfxId(floorGfxId);
            }
            else {
                view.setAnmPath("ANMInteractiveElementPath");
                view.setViewGfxId(WakfuClientConstants.DROP_CONTAINER_ANM_IDS[0]);
            }
        }
        else {
            view.setAnmPath("ANMInteractiveElementPath");
            int value = 0;
            for (final FloorItem floorItem : this.m_floorItems) {
                value += floorItem.getItem().getLevel();
            }
            int i;
            for (i = 0; i < WakfuClientConstants.DROP_CONTAINER_ANM_IDS.length && (i + 1 >= WakfuClientConstants.DROP_CONTAINER_ANM_IDS.length || value > WakfuClientConstants.DROP_CONTAINER_ANM_IDS[i + 1]); ++i) {}
            view.setViewGfxId(WakfuClientConstants.DROP_CONTAINER_ANM_IDS[i]);
        }
    }
    
    private boolean equipementFileExists(final int floorGfxId) {
        try {
            String interactiveElementPath = WakfuConfiguration.getInstance().getString("ANMEquipmentPath");
            interactiveElementPath = String.format(interactiveElementPath, floorGfxId);
            ContentFileHelper.openFile(interactiveElementPath);
        }
        catch (Exception e) {
            FloorItemInteractiveElement.m_logger.error((Object)"", (Throwable)e);
            return false;
        }
        return true;
    }
    
    static {
        m_logger = Logger.getLogger((Class)FloorItemInteractiveElement.class);
    }
    
    public static class FloorItemInteractiveElementFactory extends ObjectFactory<WakfuClientMapInteractiveElement>
    {
        private static final MonitoredPool m_pool;
        
        @Override
        public WakfuClientMapInteractiveElement makeObject() {
            FloorItemInteractiveElement element;
            try {
                element = (FloorItemInteractiveElement)FloorItemInteractiveElementFactory.m_pool.borrowObject();
                element.setPool(FloorItemInteractiveElementFactory.m_pool);
            }
            catch (Exception e) {
                FloorItemInteractiveElement.m_logger.error((Object)"Erreur lors de l'extraction d'un Lever du pool", (Throwable)e);
                element = new FloorItemInteractiveElement();
            }
            return element;
        }
        
        static {
            m_pool = new MonitoredPool(new ObjectFactory<FloorItemInteractiveElement>() {
                @Override
                public FloorItemInteractiveElement makeObject() {
                    return new FloorItemInteractiveElement();
                }
            });
        }
    }
}
