package com.ankamagames.baseImpl.graphics.opengl;

import java.util.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.*;

public class FrameResizer extends MouseAdapter implements MouseMotionListener
{
    private final JFrame destination;
    private Dimension minimumSize;
    private Component source;
    private boolean resizeOnX;
    private boolean resizeOnY;
    private ArrayList<JComponent> m_componentsToRepaint;
    private Point pressed;
    private Dimension size;
    private boolean m_enabled;
    
    public FrameResizer(final JFrame destinationFrame, final Component... components) {
        super();
        this.resizeOnX = false;
        this.resizeOnY = false;
        this.m_componentsToRepaint = new ArrayList<JComponent>();
        this.m_enabled = true;
        this.destination = destinationFrame;
        for (final Component c : components) {
            this.registerComponent(c);
        }
    }
    
    public void setMinimumSize(final Dimension s) {
        this.minimumSize = s;
    }
    
    public void addComponentsToRepain(final JComponent... components) {
        for (final JComponent c : components) {
            if (!this.m_componentsToRepaint.contains(c)) {
                this.m_componentsToRepaint.add(c);
            }
        }
    }
    
    public void deregisterComponent(final Component... components) {
        for (final Component component : components) {
            component.removeMouseListener(this);
        }
    }
    
    public void setEnabled(final boolean enabled) {
        this.m_enabled = enabled;
    }
    
    public void registerComponent(final Component component) {
        component.addMouseListener(this);
    }
    
    @Override
    public void mousePressed(final MouseEvent e) {
        if (!this.m_enabled) {
            return;
        }
        this.setupForResizing(e);
    }
    
    private void setupForResizing(final MouseEvent e) {
        if (!this.destination.isResizable()) {
            return;
        }
        if (this.destination.getExtendedState() == 6) {
            return;
        }
        (this.source = e.getComponent()).addMouseMotionListener(this);
        this.pressed = new Point(e.getPoint());
        final Point posOnFrame = SwingUtilities.convertPoint(this.source, this.pressed, this.destination);
        this.resizeOnX = (posOnFrame.x >= this.destination.getWidth() - 10);
        this.resizeOnY = (posOnFrame.y >= this.destination.getHeight() - 10);
        SwingUtilities.convertPointToScreen(this.pressed, this.source);
        this.size = this.destination.getSize();
    }
    
    @Override
    public void mouseDragged(final MouseEvent e) {
        if (this.source == null) {
            return;
        }
        final Point dragged = MouseInfo.getPointerInfo().getLocation();
        int finalWidth = this.size.width;
        int finalHeight = this.size.height;
        if (this.resizeOnX) {
            finalWidth += dragged.x - this.pressed.x;
        }
        if (this.resizeOnY) {
            finalHeight += dragged.y - this.pressed.y;
        }
        if (this.minimumSize != null) {
            if (finalWidth < this.minimumSize.width) {
                finalWidth = this.minimumSize.width;
            }
            if (finalHeight < this.minimumSize.height) {
                finalHeight = this.minimumSize.height;
            }
        }
        this.destination.setSize(finalWidth, finalHeight);
        this.destination.setVisible(true);
    }
    
    @Override
    public void mouseMoved(final MouseEvent mouseEvent) {
    }
    
    @Override
    public void mouseReleased(final MouseEvent e) {
        if (this.source != null) {
            this.source.removeMouseMotionListener(this);
        }
        this.source = null;
    }
}
