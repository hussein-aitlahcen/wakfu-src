package com.ankamagames.wakfu.client.debug.Components;

import com.ankamagames.baseImpl.graphics.debug.*;
import java.awt.event.*;
import com.ankamagames.baseImpl.graphics.isometric.particles.*;
import javax.swing.border.*;
import com.ankamagames.xulor2.graphics.*;
import java.awt.*;
import com.ankamagames.baseImpl.graphics.*;
import javax.swing.*;

public class ParticlesSwitch implements DebugComponent
{
    private final JPanel m_component;
    private final JCheckBox m_xulorCheckbox;
    private final JCheckBox m_sceneCheckbox;
    
    public ParticlesSwitch() {
        super();
        (this.m_component = new JPanel()).setLayout(new BoxLayout(this.m_component, 1));
        (this.m_sceneCheckbox = new JCheckBox("Scene partic.", true)).addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(final ItemEvent itemEvent) {
                IsoParticleSystemManager.getInstance().setParticleSystemsAllowed(itemEvent.getStateChange() == 1);
            }
        });
        this.m_sceneCheckbox.setBorder(new EmptyBorder(0, 0, 0, 0));
        (this.m_xulorCheckbox = new JCheckBox("Xulor partic.", true)).addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(final ItemEvent itemEvent) {
                XulorParticleSystemManager.INSTANCE.setParticleSystemsAllowed(itemEvent.getStateChange() == 1);
            }
        });
        this.m_xulorCheckbox.setBorder(new EmptyBorder(0, 0, 0, 0));
        this.m_component.add(this.m_sceneCheckbox);
        this.m_component.add(this.m_xulorCheckbox);
    }
    
    @Override
    public void registerComponent(final AbstractGameClientInstance instance) {
        this.m_sceneCheckbox.setSelected(IsoParticleSystemManager.getInstance().isParticleSystemsAllowed());
        this.m_xulorCheckbox.setSelected(XulorParticleSystemManager.INSTANCE.isParticleSystemsAllowed());
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
        return "Switch Particles";
    }
}
