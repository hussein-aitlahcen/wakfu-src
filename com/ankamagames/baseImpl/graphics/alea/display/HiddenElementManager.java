package com.ankamagames.baseImpl.graphics.alea.display;

import gnu.trove.*;

public class HiddenElementManager
{
    private float m_transitionTime;
    private boolean m_isHiding;
    private int m_elapsedTime;
    private float m_red;
    private float m_green;
    private float m_blue;
    private float m_alpha;
    private float m_targetRed;
    private float m_targetGreen;
    private float m_targetBlue;
    private float m_targetAlpha;
    private float m_sourceRed;
    private float m_sourceGreen;
    private float m_sourceBlue;
    private float m_sourceAlpha;
    private final TLongObjectHashMap<Fade> m_objectFader;
    private final TShortObjectHashMap<Fade> m_objectFaderByLayer;
    private final UpdateProcedure UPDATE_PROCEDURE;
    private final UpdateProcedure2 UPDATE_PROCEDURE_2;
    private static final HiddenElementManager m_instance;
    
    private HiddenElementManager() {
        super();
        this.m_transitionTime = 1000.0f;
        this.m_isHiding = false;
        this.m_red = 1.0f;
        this.m_green = 1.0f;
        this.m_blue = 1.0f;
        this.m_alpha = 1.0f;
        this.m_targetRed = 1.0f;
        this.m_targetGreen = 1.0f;
        this.m_targetBlue = 1.0f;
        this.m_targetAlpha = 1.0f;
        this.m_sourceRed = 1.0f;
        this.m_sourceGreen = 1.0f;
        this.m_sourceBlue = 1.0f;
        this.m_sourceAlpha = 1.0f;
        this.m_objectFader = new TLongObjectHashMap<Fade>();
        this.m_objectFaderByLayer = new TShortObjectHashMap<Fade>();
        this.UPDATE_PROCEDURE = new UpdateProcedure();
        this.UPDATE_PROCEDURE_2 = new UpdateProcedure2();
    }
    
    public static HiddenElementManager getInstance() {
        return HiddenElementManager.m_instance;
    }
    
    final Fade getFade(final long hash) {
        return this.m_objectFader.isEmpty() ? null : this.m_objectFader.get(hash);
    }
    
    final Fade getFadeByLayer(final short layer) {
        return this.m_objectFaderByLayer.isEmpty() ? null : this.m_objectFaderByLayer.get(layer);
    }
    
    public void set(final long hash, final float r, final float g, final float b, final float a) {
        this.set(hash, r, g, b, a, 1000.0f);
    }
    
    public void set(final long hash, final float r, final float g, final float b, final float a, final float time) {
        Fade fade = this.m_objectFader.get(hash);
        if (fade == null) {
            fade = new Fade();
            this.m_objectFader.put(hash, fade);
        }
        fade.setTarget(r, g, b, a, time);
    }
    
    public void setByLayer(final short layer, final float r, final float g, final float b, final float a) {
        this.setByLayer(layer, r, g, b, a, 1000.0f);
    }
    
    public void setByLayer(final short layer, final float r, final float g, final float b, final float a, final float time) {
        Fade fade = this.m_objectFaderByLayer.get(layer);
        if (fade == null) {
            fade = new Fade();
            this.m_objectFaderByLayer.put(layer, fade);
        }
        fade.setTarget(r, g, b, a, time);
    }
    
    public final void clear() {
        this.m_objectFader.clear();
        this.m_objectFaderByLayer.clear();
    }
    
    public void hide(final boolean hide, final int time) {
        this.m_transitionTime = time;
        if (this.m_isHiding != hide) {
            this.m_sourceRed = this.m_red;
            this.m_sourceGreen = this.m_green;
            this.m_sourceBlue = this.m_blue;
            this.m_sourceAlpha = this.m_alpha;
        }
        if (!hide) {
            this.m_targetRed = 1.0f;
            this.m_targetGreen = 1.0f;
            this.m_targetBlue = 1.0f;
            this.m_targetAlpha = 1.0f;
        }
        this.m_isHiding = hide;
        this.m_elapsedTime = 0;
    }
    
    public boolean isInTransition() {
        return this.m_elapsedTime < this.m_transitionTime;
    }
    
    public void update(final int deltaTime) {
        if (!this.m_objectFader.isEmpty()) {
            this.UPDATE_PROCEDURE.m_deltaTime = deltaTime;
            this.m_objectFader.forEachEntry(this.UPDATE_PROCEDURE);
            this.UPDATE_PROCEDURE.clean();
        }
        if (!this.m_objectFaderByLayer.isEmpty()) {
            this.UPDATE_PROCEDURE_2.m_deltaTime = deltaTime;
            this.m_objectFaderByLayer.forEachEntry(this.UPDATE_PROCEDURE_2);
            this.UPDATE_PROCEDURE_2.clean();
        }
        if (this.m_elapsedTime == Integer.MAX_VALUE) {
            return;
        }
        this.m_elapsedTime += deltaTime;
        if (this.m_elapsedTime > this.m_transitionTime) {
            this.m_elapsedTime = Integer.MAX_VALUE;
            final float targetRed = this.m_targetRed;
            this.m_sourceRed = targetRed;
            this.m_red = targetRed;
            final float targetGreen = this.m_targetGreen;
            this.m_sourceGreen = targetGreen;
            this.m_green = targetGreen;
            final float targetBlue = this.m_targetBlue;
            this.m_sourceBlue = targetBlue;
            this.m_blue = targetBlue;
            final float targetAlpha = this.m_targetAlpha;
            this.m_sourceAlpha = targetAlpha;
            this.m_alpha = targetAlpha;
            return;
        }
        final float t = this.m_elapsedTime / this.m_transitionTime;
        this.m_red = this.m_sourceRed + (this.m_targetRed - this.m_sourceRed) * t;
        this.m_green = this.m_sourceGreen + (this.m_targetGreen - this.m_sourceGreen) * t;
        this.m_blue = this.m_sourceBlue + (this.m_targetBlue - this.m_sourceBlue) * t;
        this.m_alpha = this.m_sourceAlpha + (this.m_targetAlpha - this.m_sourceAlpha) * t;
    }
    
    public void computeColor(final float[] color) {
        assert color.length == 4;
        final int n = 0;
        color[n] *= this.m_red;
        final int n2 = 1;
        color[n2] *= this.m_green;
        final int n3 = 2;
        color[n3] *= this.m_blue;
        final int n4 = 3;
        color[n4] *= this.m_alpha;
    }
    
    public void setFullAlpha(final float alphaRate) {
        this.m_targetRed = 0.3f * alphaRate;
        this.m_targetGreen = 0.3f * alphaRate;
        this.m_targetBlue = 0.3f * alphaRate;
        this.m_targetAlpha = alphaRate;
    }
    
    static {
        m_instance = new HiddenElementManager();
    }
    
    private class UpdateProcedure implements TLongObjectProcedure<Fade>
    {
        int m_deltaTime;
        final TLongArrayList m_toRemove;
        
        private UpdateProcedure() {
            super();
            this.m_toRemove = new TLongArrayList();
        }
        
        @Override
        public boolean execute(final long hash, final Fade fade) {
            if (fade.update(this.m_deltaTime)) {
                this.m_toRemove.add(hash);
            }
            return true;
        }
        
        void clean() {
            for (int i = this.m_toRemove.size() - 1; i >= 0; --i) {
                HiddenElementManager.this.m_objectFader.remove(this.m_toRemove.getQuick(i));
            }
            this.m_toRemove.clear();
        }
    }
    
    private class UpdateProcedure2 implements TShortObjectProcedure<Fade>
    {
        int m_deltaTime;
        final TShortArrayList m_toRemove;
        
        private UpdateProcedure2() {
            super();
            this.m_toRemove = new TShortArrayList();
        }
        
        @Override
        public boolean execute(final short layer, final Fade fade) {
            if (fade.update(this.m_deltaTime)) {
                this.m_toRemove.add(layer);
            }
            return true;
        }
        
        void clean() {
            for (int i = this.m_toRemove.size() - 1; i >= 0; --i) {
                HiddenElementManager.this.m_objectFaderByLayer.remove(this.m_toRemove.getQuick(i));
            }
            this.m_toRemove.clear();
        }
    }
    
    static class Fade
    {
        float m_red;
        float m_green;
        float m_blue;
        float m_alpha;
        private float m_sourceRed;
        private float m_sourceGreen;
        private float m_sourceBlue;
        private float m_sourceAlpha;
        private float m_targetRed;
        private float m_targetGreen;
        private float m_targetBlue;
        private float m_targetAlpha;
        private float m_remainingTime;
        private float m_targetTime;
        
        private Fade() {
            super();
            this.m_red = 1.0f;
            this.m_green = 1.0f;
            this.m_blue = 1.0f;
            this.m_alpha = 1.0f;
            this.m_sourceRed = 0.0f;
            this.m_sourceGreen = 0.0f;
            this.m_sourceBlue = 0.0f;
            this.m_sourceAlpha = 0.0f;
            this.m_targetRed = 0.0f;
            this.m_targetGreen = 0.0f;
            this.m_targetBlue = 0.0f;
            this.m_targetAlpha = 0.0f;
        }
        
        void setTarget(final float r, final float g, final float b, final float a, final float time) {
            this.m_targetRed = r;
            this.m_targetGreen = g;
            this.m_targetBlue = b;
            this.m_targetAlpha = a;
            this.m_sourceRed = this.m_red;
            this.m_sourceGreen = this.m_green;
            this.m_sourceBlue = this.m_blue;
            this.m_sourceAlpha = this.m_alpha;
            this.m_remainingTime = time;
            this.m_targetTime = time;
        }
        
        public boolean update(final int deltaTime) {
            if (this.m_remainingTime >= 0.0f) {
                this.m_remainingTime -= deltaTime;
                final float t = 1.0f - this.m_remainingTime / this.m_targetTime;
                this.m_red = this.m_sourceRed + (this.m_targetRed - this.m_sourceRed) * t;
                this.m_green = this.m_sourceGreen + (this.m_targetGreen - this.m_sourceGreen) * t;
                this.m_blue = this.m_sourceBlue + (this.m_targetBlue - this.m_sourceBlue) * t;
                this.m_alpha = this.m_sourceAlpha + (this.m_targetAlpha - this.m_sourceAlpha) * t;
                return false;
            }
            if (this.m_remainingTime == Float.NEGATIVE_INFINITY) {
                return false;
            }
            final float targetRed = this.m_targetRed;
            this.m_sourceRed = targetRed;
            this.m_red = targetRed;
            final float targetGreen = this.m_targetGreen;
            this.m_sourceGreen = targetGreen;
            this.m_green = targetGreen;
            final float targetBlue = this.m_targetBlue;
            this.m_sourceBlue = targetBlue;
            this.m_blue = targetBlue;
            final float targetAlpha = this.m_targetAlpha;
            this.m_sourceAlpha = targetAlpha;
            this.m_alpha = targetAlpha;
            this.m_remainingTime = Float.NEGATIVE_INFINITY;
            return this.m_red == 1.0f && this.m_green == 1.0f && this.m_blue == 1.0f && this.m_alpha == 1.0f;
        }
    }
}
