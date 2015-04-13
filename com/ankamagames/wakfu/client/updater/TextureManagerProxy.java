package com.ankamagames.wakfu.client.updater;

import com.ankamagames.baseImpl.graphics.opengl.*;
import com.ankamagames.framework.graphics.engine.texture.*;

public class TextureManagerProxy implements ITextureManager
{
    @Override
    public void setNumClients(final int numClients) {
        final float totalAvailableMemory = GLApplicationUI.GRAPHICS_ENVIRONMENT.getDefaultScreenDevice().getAvailableAcceleratedMemory() / 1024.0f;
        TextureManager.getInstance().setCacheSize(totalAvailableMemory / Math.max(1, numClients));
    }
}
