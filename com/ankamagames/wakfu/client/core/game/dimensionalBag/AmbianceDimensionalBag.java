package com.ankamagames.wakfu.client.core.game.dimensionalBag;

import com.ankamagames.wakfu.client.core.game.ambiance.*;
import com.ankamagames.wakfu.client.core.game.actor.*;
import com.ankamagames.baseImpl.graphics.alea.display.lights.*;
import com.ankamagames.wakfu.client.alea.ambiance.*;

public class AmbianceDimensionalBag implements Ambiance
{
    private static final AmbianceDimensionalBag m_instance;
    private final LitSceneModifier m_cellLightModifier;
    private byte m_inBag;
    private int m_shaderId;
    private DimensionalBagView m_bag;
    
    private AmbianceDimensionalBag() {
        super();
        this.m_inBag = 0;
        this.m_shaderId = 0;
        this.m_cellLightModifier = new LitSceneModifier() {
            @Override
            public void update(final int deltaTime) {
            }
            
            @Override
            public int getPriority() {
                return 450;
            }
            
            @Override
            public boolean useless() {
                return false;
            }
            
            @Override
            public void apply(final int x, final int y, final int z, final int layerId, final float[] colors) {
                final int n = 0;
                colors[n] *= 0.85f;
                final int n2 = 1;
                colors[n2] *= 0.85f;
                final int n3 = 2;
                colors[n3] *= 0.85f;
            }
            
            public boolean needPrecompute() {
                return false;
            }
        };
    }
    
    public static AmbianceDimensionalBag getInstance() {
        return AmbianceDimensionalBag.m_instance;
    }
    
    public void applyAmbiance(final Actor localActor) {
        switch (this.m_inBag) {
            case 1: {
                assert this.m_bag != null;
                this.enterBag(localActor);
                break;
            }
            case -1: {
                assert this.m_bag == null;
                this.onExit();
                break;
            }
        }
        this.m_inBag = 0;
    }
    
    @Override
    public void apply() {
        IsoSceneLightManager.INSTANCE.addLightingModifier(this.m_cellLightModifier);
        this.m_shaderId = Shaders.applyInBag();
    }
    
    @Override
    public void onExit() {
        IsoSceneLightManager.INSTANCE.removeLightingModifier(this.m_cellLightModifier);
        Shaders.removeEffect(this.m_shaderId);
    }
    
    public void enterInBag(final DimensionalBagView bag) {
        assert bag != null;
        this.m_inBag = 1;
        this.m_bag = bag;
    }
    
    public void goOut() {
        this.m_inBag = -1;
        this.m_bag = null;
    }
    
    private void enterBag(final Actor localActor) {
        if (this.m_bag != null) {
            this.apply();
        }
    }
    
    static {
        m_instance = new AmbianceDimensionalBag();
    }
}
