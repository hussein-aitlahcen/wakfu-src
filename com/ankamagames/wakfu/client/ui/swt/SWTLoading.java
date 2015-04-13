package com.ankamagames.wakfu.client.ui.swt;

import org.apache.log4j.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.wakfu.client.core.webBrowser.*;

public class SWTLoading extends Canvas
{
    private static final Logger m_logger;
    private int m_currentFrame;
    private int m_totalFrames;
    private int m_frameDurationInMs;
    private Color m_backgroundColor;
    private Color m_foregroundColor;
    private float m_angle;
    private int m_numCircles;
    private int m_circleSize;
    private int m_radius;
    private RunningThread m_thread;
    private Runnable m_displayRunnable;
    private GC m_gc;
    
    public SWTLoading(final Composite parent, final int style) {
        super(parent, style);
        this.m_angle = 0.0f;
        this.m_totalFrames = 150;
        this.m_circleSize = 4;
        this.m_numCircles = 15;
        this.m_radius = 15;
        this.m_frameDurationInMs = 15;
        this.m_displayRunnable = this.createRunnable();
        this.m_backgroundColor = new Color((Device)this.getDisplay(), 64, 64, 64);
        this.m_foregroundColor = new Color((Device)this.getDisplay(), 192, 192, 192);
        this.addPaintListener((PaintListener)new PaintListener() {
            public void paintControl(final PaintEvent paintEvent) {
                SWTLoading.this.paintFull(paintEvent.gc);
            }
        });
        this.addDisposeListener((DisposeListener)new DisposeListener() {
            public void widgetDisposed(final DisposeEvent disposeEvent) {
                SWTLoading.this.stop();
                if (SWTLoading.this.m_gc != null) {
                    SWTLoading.this.m_gc.dispose();
                }
            }
        });
    }
    
    public void initialize() {
        (this.m_gc = new GC((Drawable)this)).setAntialias(-1);
    }
    
    public void start() {
        if (this.m_gc == null) {
            throw new IllegalStateException("initialize() n'a pas \u00e9t\u00e9 appel\u00e9 !");
        }
        this.m_angle = 0.0f;
        this.m_currentFrame = 0;
        this.m_numCircles = 16;
        this.m_radius = 15;
        if (this.m_thread == null) {
            (this.m_thread = new RunningThread()).start();
        }
    }
    
    public void stop() {
        if (this.m_thread != null) {
            this.m_thread.setRunning(false);
            this.m_thread = null;
        }
    }
    
    private Runnable createRunnable() {
        return new Runnable() {
            @Override
            public void run() {
                if (SWTLoading.this.isDisposed()) {
                    return;
                }
                SWTLoading.this.m_currentFrame = (SWTLoading.this.m_currentFrame + 1) % SWTLoading.this.m_totalFrames;
                SWTLoading.this.m_angle = 6.2831855f / SWTLoading.this.m_totalFrames * SWTLoading.this.m_currentFrame;
                SWTLoading.this.paintCircles(SWTLoading.this.m_gc);
            }
        };
    }
    
    private void paintFull(final GC gc) {
        final Rectangle bounds = this.getBounds();
        gc.setBackground(this.m_backgroundColor);
        gc.fillRectangle(bounds);
        this.paintCircles(gc);
    }
    
    private void paintCircles(final GC gc) {
        final Rectangle bounds = this.getBounds();
        final int centerX = bounds.width / 2;
        final int centerY = bounds.height / 2;
        final float betweenCirclesAngle = 6.2831855f / this.m_numCircles;
        for (int i = 0; i < this.m_numCircles; ++i) {
            final float angle = betweenCirclesAngle * i;
            final int x = Math.round(MathHelper.cosf(angle) * this.m_radius + centerX);
            final int y = Math.round(MathHelper.sinf(angle) * this.m_radius + centerY);
            final Color color = this.colorAtAngle(angle);
            gc.setBackground(color);
            gc.fillOval(x, y, this.m_circleSize, this.m_circleSize);
            color.dispose();
        }
    }
    
    private Color colorAtAngle(float angle) {
        if (angle < this.m_angle) {
            angle += 6.2831855f;
        }
        final float relightQuandrant = 6.2831855f / this.m_numCircles;
        final float deltaAngle = angle - this.m_angle - relightQuandrant;
        float normalizedDelta;
        if (deltaAngle >= 0.0f) {
            normalizedDelta = deltaAngle / (6.2831855f - relightQuandrant);
        }
        else {
            normalizedDelta = -deltaAngle / relightQuandrant;
        }
        final int red = (int)MathHelper.lerp(this.m_backgroundColor.getRed(), this.m_foregroundColor.getRed(), normalizedDelta);
        final int green = (int)MathHelper.lerp(this.m_backgroundColor.getGreen(), this.m_foregroundColor.getGreen(), normalizedDelta);
        final int blue = (int)MathHelper.lerp(this.m_backgroundColor.getBlue(), this.m_foregroundColor.getBlue(), normalizedDelta);
        return new Color((Device)this.getDisplay(), red, green, blue);
    }
    
    static {
        m_logger = Logger.getLogger((Class)SWTLoading.class);
    }
    
    private class RunningThread extends Thread
    {
        private boolean m_running;
        
        private RunningThread() {
            super();
            this.m_running = false;
        }
        
        @Override
        public void run() {
            this.m_running = true;
            while (this.m_running) {
                WakfuSWT.runAsync(SWTLoading.this.m_displayRunnable);
                try {
                    Thread.sleep(SWTLoading.this.m_frameDurationInMs);
                }
                catch (InterruptedException e) {
                    SWTLoading.m_logger.warn((Object)"Probl\u00e8me lors du sleep", (Throwable)e);
                }
            }
        }
        
        public void setRunning(final boolean running) {
            this.m_running = running;
        }
    }
}
