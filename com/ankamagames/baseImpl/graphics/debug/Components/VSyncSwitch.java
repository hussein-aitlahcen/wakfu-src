package com.ankamagames.baseImpl.graphics.debug.Components;

import com.ankamagames.baseImpl.graphics.debug.*;
import com.ankamagames.framework.graphics.opengl.*;
import com.ankamagames.baseImpl.graphics.*;
import java.awt.event.*;
import javax.swing.*;

public class VSyncSwitch implements DebugComponent, VSyncEventHandler
{
    private final JCheckBox m_component;
    AbstractGameClientInstance m_clientInstance;
    
    public VSyncSwitch() {
        super();
        (this.m_component = new JCheckBox("VSync", false)).addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(final ItemEvent itemEvent) {
                if (VSyncSwitch.this.m_clientInstance == null) {
                    return;
                }
                final boolean newVSyncState = itemEvent.getStateChange() == 1;
                VSyncSwitch.this.m_clientInstance.getRenderer().requestVSync(newVSyncState);
            }
        });
    }
    
    @Override
    public void registerComponent(final AbstractGameClientInstance instance) {
        this.m_clientInstance = instance;
        instance.getRenderer().addVSyncEventsHandler(this);
        this.m_component.setSelected(instance.getRenderer().isVSyncEnabled());
    }
    
    @Override
    public void unregisterComponent(final AbstractGameClientInstance instance) {
        this.m_clientInstance = null;
        instance.getRenderer().removeVSyncEventsHandler(this);
    }
    
    @Override
    public JComponent getAwtComponent() {
        return this.m_component;
    }
    
    @Override
    public String getName() {
        return "Switch VSync";
    }
    
    @Override
    public void onVSyncChanged(final boolean activated) {
        this.m_component.setSelected(activated);
    }
}
