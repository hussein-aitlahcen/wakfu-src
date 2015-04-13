package com.ankamagames.baseImpl.graphics.debug.Components;

import com.ankamagames.framework.graphics.opengl.*;
import com.ankamagames.baseImpl.graphics.debug.*;
import javax.media.opengl.*;
import com.ankamagames.baseImpl.graphics.*;
import javax.swing.*;
import java.awt.*;

public class FpsViewer implements RendererEventsHandler, DebugComponent
{
    private final FpsViewerComponent m_component;
    private static final int FRAMES_USED_FOR_CALCULATION = 5;
    private static final long MIN_TIME_BETWEEN_DISPLAY_UPDATES_NS = 60000000L;
    private final long[] m_displayTimes;
    private int m_displayTimeIndex;
    private long m_lastDisplayUpdate;
    
    public FpsViewer() {
        super();
        this.m_displayTimes = new long[5];
        this.m_displayTimeIndex = -1;
        this.m_lastDisplayUpdate = 0L;
        this.m_component = new FpsViewerComponent();
    }
    
    private int boundedIndex(final int i) {
        final int newIndex = i % 5;
        if (newIndex >= 0) {
            return newIndex;
        }
        return newIndex + 5;
    }
    
    @Override
    public void onDisplay(final GLAutoDrawable glAutoDrawable) {
        final int currentIndex = this.boundedIndex(++this.m_displayTimeIndex);
        final long now = System.nanoTime();
        this.m_displayTimes[currentIndex] = now;
        if (now - this.m_lastDisplayUpdate <= 60000000L) {
            return;
        }
        final long timeForAllFrames = now - this.m_displayTimes[this.boundedIndex(currentIndex + 1)];
        if (timeForAllFrames == 0L) {
            return;
        }
        final long fps = Math.round(1.0E9 / timeForAllFrames * 4.0);
        this.m_component.m_txt.setText(Long.toString(fps));
        this.m_lastDisplayUpdate = now;
    }
    
    @Override
    public void registerComponent(final AbstractGameClientInstance instance) {
        instance.getRenderer().addRendererEventsHandler(this);
    }
    
    @Override
    public void unregisterComponent(final AbstractGameClientInstance instance) {
        instance.getRenderer().removeRendererEventsHandler(this);
    }
    
    @Override
    public JComponent getAwtComponent() {
        return this.m_component;
    }
    
    @Override
    public String getName() {
        return "FPS Viewer";
    }
    
    @Override
    public void onInit(final GLAutoDrawable glAutoDrawable) {
    }
    
    @Override
    public void onReshape(final GLAutoDrawable glAutoDrawable, final int x, final int y, final int width, final int height) {
    }
    
    @Override
    public void onDisplayChanged(final GLAutoDrawable glAutoDrawable, final boolean modeChanged, final boolean deviceChanged) {
    }
    
    private static class FpsViewerComponent extends JPanel
    {
        JTextField m_txt;
        JLabel m_lbl;
        
        private FpsViewerComponent() {
            super(new FlowLayout(0, 0, 0), true);
            this.m_lbl = new JLabel("fps");
            (this.m_txt = new JTextField()).setEnabled(false);
            this.m_txt.setDisabledTextColor(Color.BLACK);
            this.m_txt.setHorizontalAlignment(4);
            this.m_txt.setPreferredSize(new Dimension(40, 20));
            this.add(this.m_txt);
            this.add(this.m_lbl);
        }
    }
}
