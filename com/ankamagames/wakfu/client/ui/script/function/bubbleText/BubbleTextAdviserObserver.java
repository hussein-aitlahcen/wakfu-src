package com.ankamagames.wakfu.client.ui.script.function.bubbleText;

import com.ankamagames.baseImpl.graphics.alea.adviser.*;

final class BubbleTextAdviserObserver implements AdviserObserver
{
    @Override
    public void onAdviserEvent(final AdviserEvent event) {
        assert event != null : "Event null at BubbleText.onAdviserEvent";
        if (event.getType() != AdviserEvent.AdviserEventType.ADVISER_REMOVED) {
            return;
        }
        final int id = event.getAdviser().getId();
        final BubbleClosedListener obs = BubbleManager.getInstance().getAdviserEventObserver(id);
        if (obs == null) {
            return;
        }
        obs.onClose();
        BubbleManager.getInstance().removeFromAdviserEventObservers(id);
    }
}
