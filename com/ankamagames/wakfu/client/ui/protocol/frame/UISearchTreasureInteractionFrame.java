package com.ankamagames.wakfu.client.ui.protocol.frame;

import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.wakfu.client.core.game.item.action.*;
import com.ankamagames.baseImpl.common.clientAndServer.world.topology.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.client.core.game.dimensionalBag.*;

public class UISearchTreasureInteractionFrame extends UIAbstractItemUseInteractionFrame
{
    public static final UISearchTreasureInteractionFrame INSTANCE;
    
    @Override
    protected void refreshParticles() {
    }
    
    @Override
    public void onSeedSucceed() {
    }
    
    @Override
    protected void addToSelection(final Point3 target) {
        this.m_elementSelection.add(target.getX(), target.getY(), target.getZ());
    }
    
    @Override
    protected void sendItemActionRequest(final int seedPositionX, final int seedPositionY) {
        ((SearchTreasureItemAction)this.m_item.getReferenceItem().getItemAction()).sendRequest(this.m_item, seedPositionX, seedPositionY);
    }
    
    @Override
    protected String getMouseInfoText() {
        return null;
    }
    
    @Override
    protected int checkValidity(final Point3 target) {
        if (this.m_item == null) {
            return -1;
        }
        if (TopologyMapManager.isIESterileOrNotWalkable(target.getX(), target.getY(), target.getZ())) {
            return -1;
        }
        if (TopologyMapManager.isIndoor(target.getX(), target.getY(), target.getZ())) {
            return -1;
        }
        final LocalPlayerCharacter player = WakfuGameEntity.getInstance().getLocalPlayer();
        final DimensionalBagView visitingBag = player.getVisitingDimentionalBag();
        if (visitingBag != null && !visitingBag.canPlayerInteractWithContentInRoom(player, target.getX(), target.getY())) {
            return -1;
        }
        return 100;
    }
    
    @Override
    protected void clearParticles() {
    }
    
    static {
        INSTANCE = new UISearchTreasureInteractionFrame();
    }
}
