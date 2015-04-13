package com.ankamagames.wakfu.client.core.game.interactiveElement.itemizable;

import com.ankamagames.wakfu.client.core.game.interactiveElement.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.wakfu.common.rawData.*;
import java.util.*;
import com.ankamagames.baseImpl.client.proxyclient.base.game.interactiveElement.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.ui.mru.MRUActions.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.interactiveElement.*;
import com.ankamagames.wakfu.common.game.personalSpace.*;

abstract class BasicItemizableInfo<T extends WakfuClientMapInteractiveElement> implements ItemizableInfo
{
    protected final T m_linkedElement;
    private long m_ownerId;
    
    BasicItemizableInfo(final T linkedElement) {
        super();
        this.m_linkedElement = linkedElement;
    }
    
    @Override
    public DragInfo getDragInfo() {
        return this.m_linkedElement.getDragInfo();
    }
    
    @Override
    public final void fromRawSynchronisationData(final RawItemizableSynchronisationData rawData) {
        this.m_ownerId = rawData.ownerId;
        this.m_linkedElement.getPosition().set(rawData.positionX, rawData.positionY, rawData.positionZ);
        this.m_linkedElement.setDirection(Direction8.getDirectionFromIndex(rawData.direction));
    }
    
    @Override
    public void toRawPersistantData(final RawInteractiveElementPersistantData data) {
        throw new UnsupportedOperationException("Pas de persistance des donn\u00e9es des RoomContent dans le client");
    }
    
    @Override
    public final boolean fromRawPersistantData(final RawInteractiveElementPersistantData data) {
        this.m_linkedElement.setPosition(new Point3(data.positionX, data.positionY, data.positionZ));
        this.m_linkedElement.setDirection(Direction8.getDirectionFromIndex(data.direction));
        this.unserializePersistantData(data.specificData);
        return true;
    }
    
    protected abstract void unserializePersistantData(final AbstractRawPersistantData p0);
    
    @Override
    public int getWorldCellX() {
        return this.m_linkedElement.getWorldCellX();
    }
    
    @Override
    public int getWorldCellY() {
        return this.m_linkedElement.getWorldCellY();
    }
    
    @Override
    public short getWorldCellAltitude() {
        return this.m_linkedElement.getWorldCellAltitude();
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
    public boolean isBlockingMovements() {
        return this.m_linkedElement.isBlockingMovements();
    }
    
    @Override
    public long getOwnerId() {
        return this.m_ownerId;
    }
    
    @Override
    public void notifyViews() {
        this.m_linkedElement.notifyViews();
    }
    
    @Override
    public void setBag(final PersonalSpace bag) {
    }
    
    @Override
    public Collection<ClientInteractiveElementView> getViews() {
        return this.m_linkedElement.getViews();
    }
    
    @Override
    public void setOverHeadable(final boolean b) {
        this.m_linkedElement.setOverHeadable(b);
    }
    
    @Override
    public final InteractiveElementAction[] getInteractiveUsableActions() {
        final InteractiveElementAction[] additional = this.m_linkedElement.getInteractiveUsableActions();
        final InteractiveElementAction[] result = new InteractiveElementAction[3 + additional.length];
        result[0] = InteractiveElementAction.MOVE;
        result[1] = InteractiveElementAction.REPACK;
        result[2] = InteractiveElementAction.ROTATE;
        System.arraycopy(additional, 0, result, 3, additional.length);
        return result;
    }
    
    @Override
    public final AbstractMRUAction[] getInteractiveMRUActions() {
        final AbstractMRUAction[] additionalMRU = this.m_linkedElement.getInteractiveMRUActions();
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        if (this.m_ownerId != localPlayer.getId() && !MRUActionUtils.canManageInHavenWorld(this.m_ownerId)) {
            return additionalMRU;
        }
        final AbstractMRUAction[] actions = new AbstractMRUAction[3 + additionalMRU.length];
        actions[0] = MRUActions.REPACK_ITEMIZABLE_ELEMENT_ACTION.getMRUAction();
        actions[1] = MRUActions.MOVE_ITEMIZABLE_ELEMENT_ACTION.getMRUAction();
        actions[2] = MRUActions.ROTATE_ITEMIZABLE_ELEMENT_ACTION.getMRUAction();
        System.arraycopy(additionalMRU, 0, actions, 3, additionalMRU.length);
        return actions;
    }
    
    @Override
    public boolean onAction(final InteractiveElementAction action, final InteractiveElementUser user) {
        if (action == InteractiveElementAction.REPACK || action == InteractiveElementAction.MOVE || action == InteractiveElementAction.ROTATE) {
            this.m_linkedElement.sendActionMessage(action);
            return true;
        }
        return false;
    }
    
    @Override
    public void release() {
        this.m_linkedElement.release();
    }
    
    @Override
    public boolean canBeAddedIn(final Room room) {
        return true;
    }
}
