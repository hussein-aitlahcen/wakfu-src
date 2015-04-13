package com.ankamagames.wakfu.client.ui.mru.MRUActions;

import com.ankamagames.wakfu.client.core.game.interactiveElement.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;
import com.ankamagames.baseImpl.client.proxyclient.base.game.interactiveElement.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.client.core.game.actor.*;
import com.ankamagames.framework.ai.pathfinder.*;
import com.ankamagames.baseImpl.graphics.alea.mobile.*;

abstract class EndPathListenerMRUAction extends AbstractMRUAction implements MobileEndPathListener
{
    @Override
    public void run() {
        if (!this.isRunnable()) {
            return;
        }
        final WakfuClientMapInteractiveElement dummy = (WakfuClientMapInteractiveElement)this.m_source;
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        final Actor localActor = localPlayer.getActor();
        if ((!dummy.hasToFinishOnIE() && dummy.isInApproachPoint(localActor.getWorldCoordinates())) || (dummy.hasToFinishOnIE() && localActor.getWorldCoordinates().equals(dummy.getWorldCellX(), dummy.getWorldCellY(), dummy.getWorldCellAltitude()))) {
            this.startUse();
        }
        else {
            final PathFindResult path = UIWorldInteractionFrame.getPathForModel(localActor, dummy);
            if (path.isPathFound()) {
                localActor.addEndPositionListener(this);
                localPlayer.applyPathResult(path, true);
            }
            else {
                EndPathListenerMRUAction.m_logger.warn((Object)("Impossible de trouver un chemin jusqu'a l'element interactif " + this.m_source));
            }
        }
    }
    
    @Override
    public void pathEnded(final PathMobile mobile, final int x, final int y, final short altitude) {
        mobile.removeEndPositionListener(this);
        this.startUse();
    }
    
    protected abstract void startUse();
}
