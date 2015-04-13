package com.ankamagames.baseImpl.graphics.debug.Components;

import com.ankamagames.baseImpl.graphics.debug.*;
import com.ankamagames.framework.graphics.opengl.*;
import com.ankamagames.baseImpl.graphics.alea.display.*;
import java.awt.event.*;
import com.ankamagames.baseImpl.graphics.*;
import javax.swing.*;

public class WalkabilityViewer implements DebugComponent, VSyncEventHandler
{
    private final JCheckBox m_component;
    private AleaWorldScene m_scene;
    
    public WalkabilityViewer() {
        super();
        (this.m_component = new JCheckBox("Cellule walkable", false)).addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(final ItemEvent itemEvent) {
                if (WalkabilityViewer.this.m_scene != null) {
                    WalkabilityViewer.this.m_scene.enableDisplayWalkability(itemEvent.getStateChange() == 1);
                }
            }
        });
    }
    
    @Override
    public void registerComponent(final AbstractGameClientInstance instance) {
        (this.m_scene = instance.getWorldScene()).displayWalkabilityEnabled();
    }
    
    @Override
    public void unregisterComponent(final AbstractGameClientInstance instance) {
    }
    
    @Override
    public JComponent getAwtComponent() {
        return this.m_component;
    }
    
    @Override
    public String getName() {
        return "Afficher Walkabilit\u00e9";
    }
    
    @Override
    public void onVSyncChanged(final boolean activated) {
        this.m_component.setSelected(activated);
    }
}
