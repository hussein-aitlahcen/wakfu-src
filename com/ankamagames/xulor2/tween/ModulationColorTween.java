package com.ankamagames.xulor2.tween;

import com.ankamagames.framework.graphics.image.*;
import com.ankamagames.xulor2.appearance.*;

public class ModulationColorTween extends AbstractTween<Color>
{
    private final Color m_oldColor;
    private final boolean m_keepOldColor;
    
    public ModulationColorTween(final Color a, final Color b, final ModulationColorClient w, final int delay, final int duration, final int repeat, final TweenFunction function) {
        this(a, b, w, delay, duration, repeat, true, function);
    }
    
    public ModulationColorTween(final Color a, final Color b, final ModulationColorClient w, final int delay, final int duration, final int repeat, final boolean keepOldColor, final TweenFunction function) {
        super();
        this.setA(a);
        this.setB(b);
        this.setTweenClient((TweenClient)w);
        this.setDelay(delay);
        this.setDuration(duration);
        this.setRepeat(repeat);
        this.setTweenFunction(function);
        this.m_oldColor = w.getModulationColor();
        this.m_keepOldColor = keepOldColor;
    }
    
    @Override
    public boolean process(final int deltaTime) {
        if (!super.process(deltaTime)) {
            return false;
        }
        if (this.m_function == null) {
            return true;
        }
        final float r1 = this.m_function.compute(((Color)this.m_a).getRed(), ((Color)this.m_b).getRed(), this.m_elapsedTime, this.m_duration);
        final float g1 = this.m_function.compute(((Color)this.m_a).getGreen(), ((Color)this.m_b).getGreen(), this.m_elapsedTime, this.m_duration);
        final float b1 = this.m_function.compute(((Color)this.m_a).getBlue(), ((Color)this.m_b).getBlue(), this.m_elapsedTime, this.m_duration);
        final float a1 = this.m_function.compute(((Color)this.m_a).getAlpha(), ((Color)this.m_b).getAlpha(), this.m_elapsedTime, this.m_duration);
        final Color c = new Color(r1, g1, b1, a1);
        ((ModulationColorClient)this.m_client).setModulationColor(c);
        return true;
    }
    
    @Override
    public void onEnd() {
        if (this.m_keepOldColor) {
            ((ModulationColorClient)this.m_client).setModulationColor(this.m_oldColor);
        }
        else {
            ((ModulationColorClient)this.m_client).setModulationColor((Color)this.m_b);
        }
        super.onEnd();
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("[ModulationColorTween] ").append(this.m_a).append(" -> ").append(this.m_b);
        return sb.toString();
    }
}
