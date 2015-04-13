package com.ankamagames.wakfu.client.debug.Components;

import com.ankamagames.baseImpl.graphics.debug.*;
import com.ankamagames.wakfu.client.core.weather.*;
import java.awt.event.*;
import com.ankamagames.baseImpl.graphics.*;
import javax.swing.*;

public class WeatherSwitch implements DebugComponent
{
    private final JCheckBox m_component;
    private final WeatherEffectUpdater m_effectUpdater;
    private WeatherDialog m_dialog;
    
    public WeatherSwitch() {
        super();
        this.m_effectUpdater = new DebugEffectUpdater();
        (this.m_component = new JCheckBox("M\u00e9t\u00e9o", false)).addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                System.out.println(e);
            }
        });
        this.m_component.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(final ItemEvent itemEvent) {
                final boolean checked = itemEvent.getStateChange() == 1;
                WeatherEffectManager.INSTANCE.setParamUpdater(checked ? WeatherSwitch.this.m_effectUpdater : null);
                if (checked) {
                    if (WeatherSwitch.this.m_dialog == null) {
                        WeatherSwitch.this.m_dialog = WeatherDialog.open(WeatherSwitch.this.m_effectUpdater, false);
                    }
                    WeatherSwitch.this.m_dialog.addComponentListener(new ComponentAdapter() {
                        @Override
                        public void componentHidden(final ComponentEvent e) {
                            WeatherSwitch.this.m_component.setSelected(false);
                        }
                    });
                    WeatherSwitch.this.m_dialog.setVisible(true);
                    WeatherSwitch.this.m_dialog.setLocation(WeatherSwitch.this.m_component.getLocationOnScreen());
                }
                else {
                    WeatherSwitch.this.m_dialog.close();
                }
            }
        });
    }
    
    @Override
    public void registerComponent(final AbstractGameClientInstance instance) {
    }
    
    @Override
    public void unregisterComponent(final AbstractGameClientInstance instance) {
        WeatherEffectManager.INSTANCE.setParamUpdater(null);
    }
    
    @Override
    public JComponent getAwtComponent() {
        return this.m_component;
    }
    
    @Override
    public String getName() {
        return "Switch M\u00e9t\u00e9o";
    }
}
