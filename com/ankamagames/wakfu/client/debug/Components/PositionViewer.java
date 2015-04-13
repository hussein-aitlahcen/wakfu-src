package com.ankamagames.wakfu.client.debug.Components;

import com.ankamagames.framework.graphics.opengl.*;
import com.ankamagames.baseImpl.graphics.debug.*;
import com.ankamagames.framework.kernel.core.controllers.*;
import javax.media.opengl.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.baseImpl.graphics.*;
import com.ankamagames.wakfu.client.*;
import com.ankamagames.baseImpl.graphics.alea.utils.*;
import com.ankamagames.baseImpl.graphics.alea.display.*;
import java.awt.event.*;
import java.awt.*;
import javax.swing.*;

public class PositionViewer implements RendererEventsHandler, DebugComponent, MouseController
{
    private final PositionViewerComponent m_component;
    
    public PositionViewer() {
        super();
        this.m_component = new PositionViewerComponent();
    }
    
    public static void main(final String[] args) {
        final JDialog d = new JDialog();
        d.add(new PositionViewerComponent());
        d.setVisible(true);
    }
    
    @Override
    public void onDisplay(final GLAutoDrawable glAutoDrawable) {
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        if (localPlayer != null) {
            final Point3 pos = localPlayer.getPositionConst();
            this.m_component.displayPlayerPosition(pos, localPlayer.getInstanceId());
        }
    }
    
    @Override
    public void registerComponent(final AbstractGameClientInstance instance) {
        instance.getRenderer().addRendererEventsHandler(this);
        instance.getRenderer().pushMouseController(this, true);
    }
    
    @Override
    public void unregisterComponent(final AbstractGameClientInstance instance) {
        instance.getRenderer().removeRendererEventsHandler(this);
        instance.getRenderer().removeMouseController(this);
    }
    
    @Override
    public JComponent getAwtComponent() {
        return this.m_component;
    }
    
    @Override
    public String getName() {
        return "Position Viewer";
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
    
    @Override
    public boolean mouseClicked(final MouseEvent mouseEvent) {
        return false;
    }
    
    @Override
    public boolean mousePressed(final MouseEvent mouseEvent) {
        return false;
    }
    
    @Override
    public boolean mouseReleased(final MouseEvent mouseEvent) {
        return false;
    }
    
    @Override
    public boolean mouseEntered(final MouseEvent mouseEvent) {
        return false;
    }
    
    @Override
    public boolean mouseExited(final MouseEvent mouseEvent) {
        return false;
    }
    
    @Override
    public boolean mouseDragged(final MouseEvent mouseEvent) {
        return false;
    }
    
    @Override
    public boolean mouseMoved(final MouseEvent mouseEvent) {
        final AleaWorldScene scene = WakfuClientInstance.getInstance().getWorldScene();
        final Point3 pos = WorldSceneInteractionUtils.getNearestPoint3(scene, mouseEvent.getX(), mouseEvent.getY(), false);
        this.m_component.displayMousePosition(pos);
        return false;
    }
    
    @Override
    public boolean mouseWheelMoved(final MouseWheelEvent mouseEvent) {
        return false;
    }
    
    private static class PositionViewerComponent extends JPanel
    {
        private final JTextField m_txtPlayer;
        private final JTextField m_txtMouse;
        
        private PositionViewerComponent() {
            super(new GridBagLayout(), true);
            this.m_txtPlayer = this.createCoordDisplayer("perso", 0);
            this.m_txtMouse = this.createCoordDisplayer("souris", 1);
        }
        
        private JTextField createCoordDisplayer(final String label, final int row) {
            final JTextField txt = new JTextField();
            txt.setEnabled(false);
            txt.setDisabledTextColor(Color.BLACK);
            txt.setHorizontalAlignment(2);
            txt.setPreferredSize(new Dimension(130, 18));
            final GridBagConstraints c = new GridBagConstraints();
            c.fill = 2;
            c.gridx = 0;
            c.gridy = row;
            c.weightx = 0.0;
            this.add(new JLabel(label), c);
            c.gridx = 1;
            c.weightx = 1.0;
            this.add(txt, c);
            return txt;
        }
        
        private void displayPosition(final JTextField txt, final Point3 pos, final Integer worldId) {
            if (pos == null) {
                txt.setText("null");
            }
            else {
                String t = pos.getX() + ", " + pos.getY() + ", " + pos.getZ();
                if (worldId != null) {
                    t = t + " @" + worldId;
                }
                txt.setText(t);
            }
        }
        
        void displayPlayerPosition(final Point3 pos, final int worldId) {
            this.displayPosition(this.m_txtPlayer, pos, worldId);
        }
        
        void displayMousePosition(final Point3 pos) {
            this.displayPosition(this.m_txtMouse, pos, null);
        }
    }
}
