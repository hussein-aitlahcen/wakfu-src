package com.ankamagames.wakfu.client.core.weather;

import com.ankamagames.baseImpl.graphics.isometric.*;
import com.ankamagames.wakfu.client.core.game.ressource.*;
import java.util.*;
import com.ankamagames.wakfu.common.game.world.*;
import com.ankamagames.framework.graphics.engine.entity.*;
import com.ankamagames.framework.graphics.engine.transformer.*;
import com.ankamagames.framework.kernel.core.maths.*;

class Wind extends WeatherEffect
{
    private static final int RANGE_WIDTH = 18;
    private static final int RANGE_HEIGHT = 18;
    private float m_iter;
    
    Wind() {
        super();
        this.m_iter = 0.0f;
    }
    
    @Override
    void start(final IsoWorldScene scene) {
        super.start(scene);
        this.m_isRunning = true;
    }
    
    @Override
    void stop() {
        super.stop();
        this.m_isRunning = false;
    }
    
    @Override
    void update(final IsoWorldScene scene, final float windDirection, final float windStrength) {
        if (!this.m_isRunning) {
            return;
        }
        final float wind = windStrength * windDirection;
        final float maxBend = -0.3f * wind;
        final float pBend = maxBend / 2.0f;
        this.m_iter += 0.17453292f * wind;
        final ArrayList<Resource> resources = ResourceManager.getInstance().getDisplayedElements();
        for (int i = 0, size = resources.size(); i < size; ++i) {
            this.applyBending(pBend, resources.get(i));
        }
    }
    
    private void applyBending(final float pBend, final Resource resource) {
        final Entity anmEntity = resource.getEntity();
        if (anmEntity == null) {
            return;
        }
        final BatchTransformer batchTransformer = anmEntity.getTransformer();
        final TransformerSRT transformer = (TransformerSRT)batchTransformer.getTransformer(0);
        final Matrix44 m = transformer.getAdditionalTransform();
        if (!ResourceSizeCategory.applyWind(resource.getCurrentSizeCategory())) {
            if (!m.isIdentity()) {
                m.setIdentity();
                transformer.setToUpdate();
                batchTransformer.setToUpdate();
            }
            return;
        }
        final float by = resource.getWorldX() * resource.getWorldY() * 1.0f * 0.017453292f;
        final float bend = pBend + pBend * MathHelper.sinf(this.m_iter + by);
        m.set(4, bend);
        transformer.setToUpdate();
        batchTransformer.setToUpdate();
    }
}
