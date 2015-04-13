package com.ankamagames.baseImpl.graphics.debug.Components;

import com.ankamagames.baseImpl.graphics.debug.*;
import com.ankamagames.framework.graphics.opengl.*;
import java.awt.event.*;
import com.ankamagames.baseImpl.graphics.alea.display.lights.*;
import com.ankamagames.baseImpl.graphics.*;
import javax.swing.*;

public class LightsSwitch implements DebugComponent, VSyncEventHandler
{
    private final JCheckBox m_component;
    
    public LightsSwitch() {
        super();
        (this.m_component = new JCheckBox("Lights", false)).addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(final ItemEvent itemEvent) {
                IsoSceneLightManager.INSTANCE.enableLightning(itemEvent.getStateChange() == 1);
            }
        });
    }
    
    @Override
    public void registerComponent(final AbstractGameClientInstance instance) {
        this.m_component.setSelected(IsoSceneLightManager.INSTANCE.isLightningEnabled());
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
        return "Switch Lights";
    }
    
    @Override
    public void onVSyncChanged(final boolean activated) {
        this.m_component.setSelected(activated);
    }
}
