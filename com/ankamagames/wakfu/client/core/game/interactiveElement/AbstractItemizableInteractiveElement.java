package com.ankamagames.wakfu.client.core.game.interactiveElement;

import com.ankamagames.wakfu.client.core.game.interactiveElement.itemizable.*;
import com.ankamagames.framework.kernel.core.common.serialization.*;
import java.nio.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.interactiveElement.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.ui.mru.MRUActions.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.wakfu.common.game.personalSpace.*;
import com.ankamagames.wakfu.common.rawData.*;

public abstract class AbstractItemizableInteractiveElement extends WakfuClientMapInteractiveElement implements ItemizableInfo
{
    private long m_ownerId;
    private PersonalSpace m_bag;
    private final BinarSerialPart SHARED_DATAS;
    
    public AbstractItemizableInteractiveElement() {
        super();
        this.SHARED_DATAS = new BinarSerialPart() {
            @Override
            public void serialize(final ByteBuffer buffer) {
                throw new UnsupportedOperationException("La synchronisation du contenu de l'objet est faite depuis le serveur => par de s\u00e9rialisation");
            }
            
            @Override
            public void unserialize(final ByteBuffer buffer, final int version) {
                final int x = buffer.getInt();
                final int y = buffer.getInt();
                final short z = buffer.getShort();
                AbstractItemizableInteractiveElement.this.m_position.set(x, y, z);
                AbstractItemizableInteractiveElement.this.setDirection(Direction8.getDirectionFromIndex(buffer.get()));
                AbstractItemizableInteractiveElement.this.m_ownerId = buffer.getLong();
                AbstractItemizableInteractiveElement.this.unserializeSpecificSharedData(buffer);
            }
        };
    }
    
    protected abstract void unserializeSpecificSharedData(final ByteBuffer p0);
    
    @Override
    public final long getOwnerId() {
        return this.m_ownerId;
    }
    
    @Override
    public boolean canBeRepacked() {
        return true;
    }
    
    @Override
    public void onCheckIn() {
        super.onCheckIn();
        this.m_bag = null;
        this.getDragInfo().clearCheckValidation();
    }
    
    @Override
    public void onCheckOut() {
        super.onCheckOut();
        this.setTransitionModel(TransitionModel.FORCE_NO_TRANS);
        this.m_ownerId = 0L;
        this.getDragInfo().clear();
        this.m_position.reset();
        assert this.m_bag == null;
    }
    
    @Override
    public boolean spawnInWorld() {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public void despawnFromWorld() {
        throw new UnsupportedOperationException();
    }
    
    @Override
    protected final BinarSerialPart getSynchronizationSpecificPart() {
        return this.SHARED_DATAS;
    }
    
    @Override
    protected BinarSerialPart getItemizableSynchronisationPart() {
        return BinarSerialPart.EMPTY;
    }
    
    @Override
    public boolean onAction(final InteractiveElementAction action, final InteractiveElementUser user) {
        if (action == InteractiveElementAction.REPACK || action == InteractiveElementAction.MOVE || action == InteractiveElementAction.ROTATE) {
            this.sendActionMessage(action);
            return true;
        }
        return false;
    }
    
    @Override
    public final InteractiveElementAction[] getInteractiveUsableActions() {
        final InteractiveElementAction[] additional = this.getAdditionalUsableActions();
        final InteractiveElementAction[] result = new InteractiveElementAction[3 + additional.length];
        result[0] = InteractiveElementAction.MOVE;
        result[1] = InteractiveElementAction.REPACK;
        result[2] = InteractiveElementAction.ROTATE;
        System.arraycopy(additional, 0, result, 3, additional.length);
        return result;
    }
    
    protected abstract InteractiveElementAction[] getAdditionalUsableActions();
    
    @Override
    public final AbstractMRUAction[] getInteractiveMRUActions() {
        final AbstractMRUAction[] additionalMRU = this.getAdditionalMRUActions();
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        if (this.getOwnerId() != localPlayer.getId() && !MRUActionUtils.canManageInHavenWorld(this.m_ownerId)) {
            return additionalMRU;
        }
        final AbstractMRUAction[] actions = new AbstractMRUAction[3 + additionalMRU.length];
        actions[0] = MRUActions.REPACK_ITEMIZABLE_ELEMENT_ACTION.getMRUAction();
        actions[1] = MRUActions.MOVE_ITEMIZABLE_ELEMENT_ACTION.getMRUAction();
        actions[2] = MRUActions.ROTATE_ITEMIZABLE_ELEMENT_ACTION.getMRUAction();
        System.arraycopy(additionalMRU, 0, actions, 3, additionalMRU.length);
        return actions;
    }
    
    protected abstract AbstractMRUAction[] getAdditionalMRUActions();
    
    @Override
    public final void toRawPersistantData(final RawInteractiveElementPersistantData data) {
        throw new UnsupportedOperationException("Pas de persistance des donn\u00e9es des RoomContent dans le client");
    }
    
    @Override
    public final boolean fromRawPersistantData(final RawInteractiveElementPersistantData data) {
        this.setPosition(new Point3(data.positionX, data.positionY, data.positionZ));
        this.setDirection(Direction8.getDirectionFromIndex(data.direction));
        this.unserializePersistantData(data.specificData);
        return true;
    }
    
    @Override
    public boolean canBeAddedIn(final Room room) {
        return true;
    }
    
    protected abstract void unserializePersistantData(final AbstractRawPersistantData p0);
    
    @Override
    public void setBag(final PersonalSpace bag) {
        this.m_bag = bag;
    }
    
    @Override
    public ItemizableInfo getOrCreateItemizableInfo() {
        return this;
    }
    
    @Override
    public ItemizableInfo getItemizableInfo() {
        return this;
    }
    
    @Override
    public void fromRawSynchronisationData(final RawItemizableSynchronisationData rawData) {
    }
}
