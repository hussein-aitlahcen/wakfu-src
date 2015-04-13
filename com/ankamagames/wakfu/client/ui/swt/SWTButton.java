package com.ankamagames.wakfu.client.ui.swt;

import org.eclipse.swt.widgets.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;

public class SWTButton extends Canvas
{
    private Point m_prefSize;
    private int m_mouse;
    private boolean m_hit;
    private Image m_normal;
    private Image m_over;
    private Image m_pressed;
    
    public SWTButton(final Composite parent, final int style) {
        super(parent, style);
        this.m_mouse = 0;
        this.m_hit = false;
        this.addPaintListener((PaintListener)new PaintListener() {
            public void paintControl(final PaintEvent e) {
                switch (SWTButton.this.m_mouse) {
                    case 0: {
                        e.gc.drawImage(SWTButton.this.m_normal, 0, 0);
                        break;
                    }
                    case 1: {
                        e.gc.drawImage(SWTButton.this.m_over, 0, 0);
                        break;
                    }
                    case 2: {
                        e.gc.drawImage(SWTButton.this.m_pressed, 0, 0);
                        break;
                    }
                }
            }
        });
        this.addMouseMoveListener((MouseMoveListener)new MouseMoveListener() {
            public void mouseMove(final MouseEvent e) {
                if (!SWTButton.this.m_hit) {
                    return;
                }
                SWTButton.this.m_mouse = 2;
                if (e.x < 0 || e.y < 0 || e.x > SWTButton.this.getBounds().width || e.y > SWTButton.this.getBounds().height) {
                    SWTButton.this.m_mouse = 0;
                }
                SWTButton.this.redraw();
            }
        });
        this.addMouseTrackListener((MouseTrackListener)new MouseTrackAdapter() {
            public void mouseEnter(final MouseEvent e) {
                SWTButton.this.m_mouse = 1;
                SWTButton.this.redraw();
            }
            
            public void mouseExit(final MouseEvent e) {
                SWTButton.this.m_mouse = 0;
                SWTButton.this.redraw();
            }
        });
        this.addMouseListener((MouseListener)new MouseAdapter() {
            public void mouseDown(final MouseEvent e) {
                SWTButton.this.m_hit = true;
                SWTButton.this.m_mouse = 2;
                SWTButton.this.redraw();
            }
            
            public void mouseUp(final MouseEvent e) {
                SWTButton.this.m_hit = false;
                SWTButton.this.m_mouse = 1;
                if (e.x < 0 || e.y < 0 || e.x > SWTButton.this.getBounds().width || e.y > SWTButton.this.getBounds().height) {
                    SWTButton.this.m_mouse = 0;
                }
                SWTButton.this.redraw();
                if (SWTButton.this.m_mouse == 1) {
                    SWTButton.this.notifyListeners(13, new Event());
                }
            }
        });
        this.addKeyListener((KeyListener)new KeyAdapter() {
            public void keyPressed(final KeyEvent e) {
                if (e.keyCode == 13 || e.character == ' ') {
                    final Event event = new Event();
                    SWTButton.this.notifyListeners(13, event);
                }
            }
        });
    }
    
    public void setNormalIcon(final ImageData data) {
        if (this.m_normal != null) {
            this.m_normal.dispose();
        }
        this.m_normal = new Image((Device)this.getDisplay(), data);
    }
    
    public void setOverIcon(final ImageData data) {
        if (this.m_over != null) {
            this.m_over.dispose();
        }
        this.m_over = new Image((Device)this.getDisplay(), data);
    }
    
    public void setPressedIcon(final ImageData data) {
        if (this.m_pressed != null) {
            this.m_pressed.dispose();
        }
        this.m_pressed = new Image((Device)this.getDisplay(), data);
    }
    
    public Point computeSize(final int wHint, final int hHint) {
        return this.computeSize(wHint, hHint, false);
    }
    
    public Point computeSize(final int wHint, final int hHint, final boolean changed) {
        if (!changed && this.m_prefSize != null) {
            return this.m_prefSize;
        }
        if (this.m_normal != null) {
            final Rectangle bounds = this.m_normal.getBounds();
            this.m_prefSize = new Point(bounds.width, bounds.height);
        }
        else {
            this.m_prefSize = new Point(0, 0);
        }
        return this.m_prefSize;
    }
    
    public void dispose() {
        super.dispose();
        if (this.m_normal != null) {
            this.m_normal.dispose();
            this.m_normal = null;
        }
        if (this.m_over != null) {
            this.m_over.dispose();
            this.m_over = null;
        }
        if (this.m_pressed != null) {
            this.m_pressed.dispose();
            this.m_pressed = null;
        }
    }
}
