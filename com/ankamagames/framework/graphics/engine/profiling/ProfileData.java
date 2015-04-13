package com.ankamagames.framework.graphics.engine.profiling;

import javax.media.opengl.*;

class ProfileData
{
    String m_name;
    float[] m_color;
    long m_startTime;
    final History m_duration;
    final History m_callCount;
    long m_lastFrameUsage;
    
    ProfileData(final String name) {
        super();
        this.m_color = null;
        this.m_duration = new History();
        this.m_callCount = new History();
        this.m_name = name;
        this.m_duration.newFrame(Profiler.m_frameCount);
        this.m_callCount.newFrame(Profiler.m_frameCount);
    }
    
    public final void start() {
        this.m_callCount.incValue(1);
        this.m_startTime = System.nanoTime();
    }
    
    public final void newFrame(final long frameCount) {
        this.m_duration.newFrame(frameCount);
        this.m_callCount.newFrame(frameCount);
    }
    
    public final void end(final long time) {
        this.m_duration.incValue((int)(time - this.m_startTime));
    }
    
    public String toString(final String s) {
        return String.format("%15s|% 6.1f|% 6.1f|% 6.1f|% 6.1f|%3d", this.m_name + s, this.m_duration.m_avg / 1000000.0 / this.m_callCount.m_avg, this.m_duration.m_avg / 1000000.0, this.m_duration.m_min / 1000000.0, this.m_duration.m_max / 1000000.0, this.m_callCount.m_avg);
    }
    
    public void drawCurve(final GL gl) {
        drawHistory(gl, this.m_duration, this.m_color, 2.0E-5);
    }
    
    public static void drawHistory(final GL gl, final History history, final float[] colors, final double heightFactor) {
        if (colors == null) {
            return;
        }
        gl.glColor4fv(colors, 0);
        gl.glBegin(1);
        final int count = (int)Math.min(Profiler.m_frameCount, 500L);
        final int[] d = history.m_history;
        final float x = -500.0f;
        final int start = (int)(Profiler.m_frameCount % 500L);
        for (int i = 0; i < count - 1; ++i) {
            gl.glVertex2f(i * 2.0f - 500.0f, (float)(d[(i + start) % 500] * heightFactor) - 300.0f);
            gl.glVertex2f((i + 1) * 2.0f - 500.0f, (float)(d[(i + 1 + start) % 500] * heightFactor) - 300.0f);
        }
        gl.glEnd();
    }
}
