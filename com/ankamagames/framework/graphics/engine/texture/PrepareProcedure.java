package com.ankamagames.framework.graphics.engine.texture;

import gnu.trove.*;
import com.ankamagames.framework.graphics.engine.*;

final class PrepareProcedure implements TObjectProcedure<Texture>
{
    private static final int ONE_MILLI_SEC = 1000000;
    private Renderer m_renderer;
    private long m_start;
    private float m_preparedSize;
    
    float getPreparedSize() {
        return this.m_preparedSize;
    }
    
    void reset(final Renderer renderer) {
        this.m_renderer = renderer;
        this.m_start = System.nanoTime();
        this.m_preparedSize = 0.0f;
    }
    
    @Override
    public boolean execute(final Texture texture) {
        if (texture == null || texture.isReady()) {
            return true;
        }
        if (texture.prepare(this.m_renderer)) {
            this.m_preparedSize += texture.getMemorySize();
            texture.m_managerState = Texture.ManagerState.PREPARED;
        }
        return System.nanoTime() - this.m_start <= 1000000L;
    }
}
