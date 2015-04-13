package com.ankamagames.wakfu.client.ui.script.function.bubbleText;

import com.ankamagames.baseImpl.graphics.ui.event.*;

final class BubbleTextDialogUnloadListener implements DialogUnloadListener
{
    @Override
    public void dialogUnloaded(final String id) {
        final int intId = id.hashCode();
        final BubbleClosedListener obs = BubbleManager.getInstance().getEndFunctionRunner(intId);
        if (obs == null) {
            return;
        }
        obs.onClose();
        BubbleManager.getInstance().removeFromEndFunctionRunners(intId);
    }
}
