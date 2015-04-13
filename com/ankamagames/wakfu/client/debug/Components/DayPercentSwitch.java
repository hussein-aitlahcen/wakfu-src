package com.ankamagames.wakfu.client.debug.Components;

import com.ankamagames.baseImpl.graphics.debug.*;
import com.ankamagames.baseImpl.graphics.alea.display.lights.*;
import java.awt.*;
import java.awt.event.*;
import com.ankamagames.baseImpl.graphics.*;
import javax.swing.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.time.calendar.*;

public class DayPercentSwitch implements DebugComponent, SunLightModifier.DayPercentDelegate
{
    private final JPanel m_panel;
    private final JCheckBox m_component;
    private final JSlider m_slider;
    
    public DayPercentSwitch() {
        super();
        this.m_panel = new JPanel(new FlowLayout());
        this.m_component = new JCheckBox("Heure:", false);
        (this.m_slider = new JSlider(0, 100)).setPaintTicks(true);
        this.m_slider.setMajorTickSpacing(25);
        this.m_panel.add(this.m_component, 0);
        this.m_panel.add(this.m_slider, 1);
        this.m_component.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
            }
        });
    }
    
    @Override
    public void registerComponent(final AbstractGameClientInstance instance) {
        SunLightModifier.INSTANCE.setDayPercentDelegate(this);
    }
    
    @Override
    public void unregisterComponent(final AbstractGameClientInstance instance) {
        SunLightModifier.INSTANCE.setDayPercentDelegate(null);
    }
    
    @Override
    public JComponent getAwtComponent() {
        return this.m_panel;
    }
    
    @Override
    public String getName() {
        return "Switch Heure du jour";
    }
    
    @Override
    public float getDayPercent(final GameCalendar calendar) {
        if (!this.m_component.isSelected()) {
            final float percentage = calendar.getDayPercentage();
            this.m_slider.setValue((int)percentage);
            return percentage;
        }
        return this.m_slider.getValue();
    }
}
