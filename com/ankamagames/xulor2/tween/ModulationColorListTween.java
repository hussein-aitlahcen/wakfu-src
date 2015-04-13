package com.ankamagames.xulor2.tween;

import com.ankamagames.framework.graphics.image.*;
import java.util.*;
import com.ankamagames.xulor2.appearance.*;

public class ModulationColorListTween extends AbstractTween<Color>
{
    private ArrayList<ModulationColorClient> m_list;
    
    public ModulationColorListTween(final Color a, final Color b, final ArrayList<ModulationColorClient> list, final int delay, final int duration, final int repeat, final TweenFunction function) {
        super();
        this.setA(a);
        this.setB(b);
        this.m_list = list;
        this.setDelay(delay);
        this.setDuration(duration);
        this.setRepeat(repeat);
        this.setTweenFunction(function);
    }
    
    @Override
    public boolean process(final int deltaTime) {
        if (!super.process(deltaTime)) {
            return false;
        }
        if (this.m_function != null) {
            final Color a = (Color)this.m_a;
            final Color b = (Color)this.m_b;
            final float r1 = this.m_function.compute(a.getRed(), b.getRed(), this.m_elapsedTime, this.m_duration);
            final float g1 = this.m_function.compute(a.getGreen(), b.getGreen(), this.m_elapsedTime, this.m_duration);
            final float b2 = this.m_function.compute(a.getBlue(), b.getBlue(), this.m_elapsedTime, this.m_duration);
            final float a2 = this.m_function.compute(a.getAlpha(), b.getAlpha(), this.m_elapsedTime, this.m_duration);
            final Color c = new Color(r1, g1, b2, a2);
            for (int i = this.m_list.size() - 1; i >= 0; --i) {
                this.m_list.get(i).setModulationColor(c);
            }
        }
        return true;
    }
    
    @Override
    public void onEnd() {
        for (int i = this.m_list.size() - 1; i >= 0; --i) {
            this.m_list.get(i).setModulationColor((Color)this.m_b);
        }
        super.onEnd();
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("[ModulationColorListTween] ").append(this.m_a).append(" -> ").append(this.m_b);
        return sb.toString();
    }
}
