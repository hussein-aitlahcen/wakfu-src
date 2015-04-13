package com.ankamagames.xulor2.tween;

import com.ankamagames.xulor2.component.mesh.*;
import com.ankamagames.xulor2.component.*;

public class ImageScaleTween extends AbstractWidgetTween<Float>
{
    private ImageMesh m_imageMesh;
    
    public ImageScaleTween(final Float a, final Float b, final Widget w, final int delay, final int duration, final TweenFunction function, final ImageMesh imageMesh, final int repeatCount) {
        super(a, b, w, delay, duration, function);
        this.m_imageMesh = imageMesh;
        this.setRepeat(repeatCount);
    }
    
    @Override
    public boolean process(final int deltaTime) {
        if (!super.process(deltaTime)) {
            return false;
        }
        if (this.m_function == null) {
            return true;
        }
        final float scale = this.m_function.compute((float)this.m_a, (float)this.m_b, this.m_elapsedTime, this.m_duration);
        this.m_imageMesh.setScale(scale, scale, 1.0f);
        return true;
    }
    
    @Override
    public void onEnd() {
        super.onEnd();
        this.m_imageMesh.setScale((float)this.m_b, 1.0f, 1.0f);
    }
}
