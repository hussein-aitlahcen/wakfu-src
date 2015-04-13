package com.ankamagames.baseImpl.graphics.isometric.tween;

import com.ankamagames.baseImpl.graphics.isometric.*;
import java.util.*;

public class TweenManager implements RenderProcessHandler
{
    private static final TweenManager m_instance;
    private final ArrayList<Tween> m_tweens;
    
    public TweenManager() {
        super();
        this.m_tweens = new ArrayList<Tween>();
    }
    
    public static TweenManager getInstance() {
        return TweenManager.m_instance;
    }
    
    public void addTween(final Tween tween) {
        this.m_tweens.add(tween);
    }
    
    @Override
    public void process(final IsoWorldScene scene, final int deltaTime) {
        for (int i = 0, tweenSize = this.m_tweens.size(); i < tweenSize; ++i) {
            this.m_tweens.get(i).process(deltaTime);
        }
    }
    
    @Override
    public void prepareBeforeRendering(final IsoWorldScene scene, final float centerScreenIsoWorldX, final float centerScreenIsoWorldY) {
        final Iterator<Tween> iterator = this.m_tweens.iterator();
        while (iterator.hasNext()) {
            final Tween tween = iterator.next();
            if (tween.isRemovable()) {
                iterator.remove();
            }
        }
    }
    
    static {
        m_instance = new TweenManager();
    }
}
