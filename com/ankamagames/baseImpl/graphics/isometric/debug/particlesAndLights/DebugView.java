package com.ankamagames.baseImpl.graphics.isometric.debug.particlesAndLights;

import com.ankamagames.framework.graphics.engine.light.*;
import javax.swing.table.*;
import java.util.*;
import com.ankamagames.framework.graphics.engine.particleSystem.*;
import java.awt.*;
import javax.swing.*;

class DebugView
{
    private JPanel m_rootPanel;
    private JTable m_psys;
    private JTable m_lights;
    
    DebugView() {
        super();
        this.$$$setupUI$$$();
    }
    
    public JPanel getRootPanel() {
        return this.m_rootPanel;
    }
    
    private void createData(final ArrayList<ParticleSystem> systems, final ArrayList<LightSource> lights) {
        DefaultTableModel data = new DefaultTableModel();
        data.addColumn("Id");
        data.addColumn("Class");
        data.addColumn("isAlive");
        data.addColumn("emitters");
        data.addColumn("pos");
        for (final ParticleSystem system : systems) {
            final StringBuffer buffer = new StringBuffer();
            final Emitter[] emitters = system.getEmitters();
            if (emitters != null) {
                final int count = emitters.length;
                buffer.append("cnt=").append(count).append(" {");
                for (int i = 0; i < count; ++i) {
                    if (i > 0) {
                        buffer.append(",");
                    }
                    buffer.append(emitters[i].isAlive());
                }
                buffer.append("}");
            }
            else {
                buffer.append("cnt=0");
            }
            data.addRow(new Object[] { system.getId(), system.getClass().getSimpleName(), system.isAlive(), buffer, system.getX() + ";" + system.getY() });
        }
        this.m_psys.setModel(data);
        data = new DefaultTableModel();
        data.addColumn("Id");
        data.addColumn("Pos");
        data.addColumn("Enabled");
        data.addColumn("BaseColor");
        data.addColumn("Range");
        for (final LightSource light : lights) {
            data.addRow(new Object[] { light.getId(), light.getPosition().getX() + ";" + light.getPosition().getY(), light.isEnabled(), light.getBaseColor(), light.getRange() });
        }
        this.m_lights.setModel(data);
    }
    
    public void updateData(final ArrayList<ParticleSystem> systems, final ArrayList<LightSource> lights) {
        this.createData(systems, lights);
    }
    
    private void $$$setupUI$$$() {
        (this.m_rootPanel = new JPanel()).setLayout(new BorderLayout(0, 0));
        final JScrollPane scrollPane1 = new JScrollPane();
        this.m_rootPanel.add(scrollPane1, "Center");
        scrollPane1.setViewportView(this.m_lights = new JTable());
        final JScrollPane scrollPane2 = new JScrollPane();
        this.m_rootPanel.add(scrollPane2, "East");
        scrollPane2.setViewportView(this.m_psys = new JTable());
    }
    
    public JComponent $$$getRootComponent$$$() {
        return this.m_rootPanel;
    }
}
