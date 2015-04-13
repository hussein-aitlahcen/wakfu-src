package com.ankamagames.baseImpl.graphics.opengl;

import java.awt.event.*;
import javax.swing.*;
import java.awt.*;

public class FrameMover extends MouseAdapter implements MouseMotionListener
{
    private final JFrame destination;
    private Component source;
    private Point pressed;
    private Point location;
    private ScreenVerticalConstraint[] m_screenVerticalConstraints;
    
    public FrameMover(final JFrame destinationComponent, final Component... components) {
        super();
        this.destination = destinationComponent;
        this.registerComponent(components);
    }
    
    public void deregisterComponent(final Component... components) {
        for (final Component component : components) {
            component.removeMouseListener(this);
        }
    }
    
    public void registerComponent(final Component... components) {
        for (final Component component : components) {
            component.addMouseListener(this);
        }
    }
    
    @Override
    public void mousePressed(final MouseEvent e) {
        this.setupForDragging(e);
    }
    
    private void setupForDragging(final MouseEvent e) {
        if (this.destination.getExtendedState() == 6) {
            return;
        }
        (this.source = e.getComponent()).addMouseMotionListener(this);
        final GraphicsDevice[] screenDevices = GLApplicationUI.GRAPHICS_ENVIRONMENT.getScreenDevices();
        this.m_screenVerticalConstraints = new ScreenVerticalConstraint[screenDevices.length];
        for (int i = 0; i < screenDevices.length; ++i) {
            final GraphicsDevice screen = screenDevices[i];
            final GraphicsConfiguration screenConf = screen.getDefaultConfiguration();
            final Rectangle screenBounds = screenConf.getBounds();
            final Insets screenInsets = Toolkit.getDefaultToolkit().getScreenInsets(screenConf);
            final ScreenVerticalConstraint c = new ScreenVerticalConstraint();
            c.left = (int)screenBounds.getMinX();
            c.right = (int)screenBounds.getMaxX();
            c.minY = (int)screenBounds.getMinY() + screenInsets.top;
            c.maxY = (int)screenBounds.getMaxY() - screenInsets.bottom;
            this.m_screenVerticalConstraints[i] = c;
        }
        SwingUtilities.convertPointToScreen(this.pressed = new Point(e.getPoint()), this.source);
        this.location = this.destination.getLocation();
    }
    
    @Override
    public void mouseDragged(final MouseEvent e) {
        if (this.source == null || this.m_screenVerticalConstraints == null) {
            return;
        }
        final Point dragged = MouseInfo.getPointerInfo().getLocation();
        final int dragX = dragged.x - this.pressed.x;
        final int dragY = dragged.y - this.pressed.y;
        final int newX = this.location.x + dragX;
        int newY = this.location.y + dragY;
        for (int i = 0; i < this.m_screenVerticalConstraints.length; ++i) {
            final ScreenVerticalConstraint c = this.m_screenVerticalConstraints[i];
            if (dragged.x >= c.left && dragged.x < c.right) {
                newY = Math.max(Math.min(newY, c.maxY), c.minY);
            }
        }
        this.destination.setLocation(newX, newY);
    }
    
    @Override
    public void mouseMoved(final MouseEvent mouseEvent) {
    }
    
    @Override
    public void mouseReleased(final MouseEvent e) {
        if (this.source != null) {
            this.source.removeMouseMotionListener(this);
            this.source = null;
        }
        this.m_screenVerticalConstraints = null;
    }
    
    private static class ScreenVerticalConstraint
    {
        public int left;
        public int right;
        public int minY;
        public int maxY;
        
        private ScreenVerticalConstraint() {
            super();
            this.left = 0;
            this.right = 0;
            this.minY = 0;
            this.maxY = 0;
        }
    }
}
