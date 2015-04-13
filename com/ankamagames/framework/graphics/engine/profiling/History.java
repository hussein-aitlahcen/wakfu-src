package com.ankamagames.framework.graphics.engine.profiling;

import com.ankamagames.framework.kernel.core.maths.*;

class History
{
    int m_value;
    int m_avg;
    int m_avgByFrame;
    int m_min;
    int m_max;
    int m_total;
    int[] m_history;
    
    History() {
        super();
        this.m_history = new int[500];
    }
    
    final void newFrame(final long frameCount) {
        this.m_min = 0;
        this.m_max = 0;
        final int length = this.m_history.length;
        this.m_history[(int)(frameCount % length)] = this.m_value;
        long total = 0L;
        int count = 0;
        for (int i = 0; i < length; ++i) {
            final int v = this.m_history[i];
            total += v;
            if (v < this.m_min || this.m_min <= 0) {
                this.m_min = v;
            }
            if (v > this.m_max) {
                this.m_max = v;
            }
            if (v != 0) {
                ++count;
            }
        }
        this.m_avg = (int)MathHelper.fastRound(total / count);
        this.m_avgByFrame = (int)MathHelper.fastRound(total / length);
        this.m_total = (int)total;
        this.m_value = 0;
    }
    
    final void incValue(final int value) {
        this.m_value += value;
    }
    
    final void clear() {
        this.m_value = 0;
        this.m_avg = 0;
        this.m_min = 0;
        this.m_max = 0;
        for (int i = 0; i < this.m_history.length; ++i) {
            this.m_history[i] = 0;
        }
    }
}
