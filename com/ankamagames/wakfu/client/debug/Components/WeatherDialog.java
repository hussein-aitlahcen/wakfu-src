package com.ankamagames.wakfu.client.debug.Components;

import com.intellij.uiDesigner.core.*;
import java.awt.*;
import javax.swing.*;
import com.ankamagames.wakfu.common.game.weather.*;
import java.awt.event.*;
import com.ankamagames.wakfu.client.core.weather.*;
import javax.swing.event.*;

public class WeatherDialog extends JDialog
{
    private JPanel contentPane;
    public JSlider m_sliderWindStrength;
    public JLabel labelWindStrengthValue;
    public JSlider m_sliderRainStrength;
    public JLabel labelRainStrengthValue;
    public JCheckBox m_desactiverLaMeteoCheckBox;
    public JSlider m_sliderSnowStrength;
    public JLabel labelSnowStrengthValue;
    private WeatherEffectUpdater m_effectModifier;
    private boolean m_disposeOnClose;
    
    private void $$$setupUI$$$() {
        (this.contentPane = new JPanel()).setLayout(new GridLayoutManager(2, 1, new Insets(10, 10, 10, 10), -1, -1));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(4, 3, new Insets(0, 0, 0, 0), -1, -1));
        this.contentPane.add(panel1, new GridConstraints(1, 0, 1, 1, 0, 3, 3, 3, null, null, null, 0, false));
        final Spacer spacer1 = new Spacer();
        panel1.add(spacer1, new GridConstraints(3, 0, 1, 1, 0, 2, 1, 4, null, null, null, 0, false));
        final JLabel label1 = new JLabel();
        label1.setText("Vent");
        panel1.add(label1, new GridConstraints(0, 0, 1, 1, 8, 0, 0, 0, null, null, null, 0, false));
        panel1.add(this.m_sliderWindStrength = new JSlider(), new GridConstraints(0, 1, 1, 1, 8, 1, 4, 0, null, null, null, 0, false));
        (this.labelWindStrengthValue = new JLabel()).setText("0");
        panel1.add(this.labelWindStrengthValue, new GridConstraints(0, 2, 1, 1, 8, 0, 0, 0, null, null, null, 0, false));
        final JLabel label2 = new JLabel();
        label2.setText("Pluie");
        panel1.add(label2, new GridConstraints(1, 0, 1, 1, 8, 0, 0, 0, null, null, null, 0, false));
        panel1.add(this.m_sliderRainStrength = new JSlider(), new GridConstraints(1, 1, 1, 1, 8, 1, 4, 0, null, null, null, 0, false));
        (this.labelRainStrengthValue = new JLabel()).setText("0");
        panel1.add(this.labelRainStrengthValue, new GridConstraints(1, 2, 1, 1, 8, 0, 0, 0, null, null, null, 0, false));
        panel1.add(this.m_sliderSnowStrength = new JSlider(), new GridConstraints(2, 1, 1, 1, 8, 1, 4, 0, null, null, null, 0, false));
        (this.labelSnowStrengthValue = new JLabel()).setText("0");
        panel1.add(this.labelSnowStrengthValue, new GridConstraints(2, 2, 1, 1, 8, 0, 0, 0, null, null, null, 0, false));
        final JLabel label3 = new JLabel();
        label3.setText("Neige");
        panel1.add(label3, new GridConstraints(2, 0, 1, 1, 8, 0, 0, 0, null, null, null, 0, false));
        (this.m_desactiverLaMeteoCheckBox = new JCheckBox()).setText("D\u00e9sactiver la m\u00e9t\u00e9o");
        this.contentPane.add(this.m_desactiverLaMeteoCheckBox, new GridConstraints(0, 0, 1, 1, 8, 0, 3, 0, null, null, null, 0, false));
    }
    
    public JComponent $$$getRootComponent$$$() {
        return this.contentPane;
    }
    
    public WeatherDialog(final WeatherEffectUpdater effectModifier) {
        super();
        this.$$$setupUI$$$();
        this.m_disposeOnClose = true;
        this.m_effectModifier = effectModifier;
        this.setContentPane(this.contentPane);
        this.setDefaultCloseOperation(0);
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(final WindowEvent e) {
                WeatherDialog.this.onCancel();
            }
        });
        this.contentPane.registerKeyboardAction(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                WeatherDialog.this.onCancel();
            }
        }, KeyStroke.getKeyStroke(27, 0), 1);
        this.m_sliderWindStrength.addChangeListener(new Listener() {
            @Override
            protected void onValueChanged(final float value) {
                WeatherDialog.this.displayValue(WeatherDialog.this.labelWindStrengthValue, value);
                WeatherDialog.this.m_effectModifier.setWindStrength(value);
            }
        });
        this.m_sliderRainStrength.addChangeListener(new Listener() {
            @Override
            protected void onValueChanged(final float value) {
                WeatherDialog.this.displayValue(WeatherDialog.this.labelRainStrengthValue, value);
                WeatherDialog.this.m_effectModifier.setStrength(Weather.RAIN, value);
            }
        });
        this.m_sliderSnowStrength.addChangeListener(new Listener() {
            @Override
            protected void onValueChanged(final float value) {
                WeatherDialog.this.displayValue(WeatherDialog.this.labelSnowStrengthValue, value);
                WeatherDialog.this.m_effectModifier.setStrength(Weather.SNOW, value);
            }
        });
        this.m_desactiverLaMeteoCheckBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(final ItemEvent itemEvent) {
                final boolean checked = itemEvent.getStateChange() == 1;
                WeatherDialog.this.m_effectModifier.setActivated(!checked);
            }
        });
    }
    
    private void displayValue(final JLabel label, final float value) {
        label.setText(Float.toString(value));
        label.updateUI();
    }
    
    private static float getValue(final JSlider slider) {
        return slider.getValue() / 100.0f;
    }
    
    private void onCancel() {
        if (this.m_disposeOnClose) {
            this.dispose();
        }
        else {
            this.setVisible(false);
        }
    }
    
    public void close() {
        this.onCancel();
    }
    
    public void setDisposeOnClose(final boolean disposeOnClose) {
        this.m_disposeOnClose = disposeOnClose;
    }
    
    public static WeatherDialog open(final WeatherEffectUpdater updater, final boolean modal) {
        final WeatherDialog dialog = new WeatherDialog(updater);
        dialog.setTitle("Param\u00e8tre m\u00e9t\u00e9o");
        dialog.setDisposeOnClose(modal);
        dialog.setModal(modal);
        dialog.pack();
        dialog.setAlwaysOnTop(true);
        dialog.setVisible(true);
        return dialog;
    }
    
    public static void main(final String[] args) {
        open(new DebugEffectUpdater(), true);
        System.exit(0);
    }
    
    abstract static class Listener implements ChangeListener
    {
        @Override
        public void stateChanged(final ChangeEvent e) {
            final JSlider slider = (JSlider)e.getSource();
            this.onValueChanged(getValue(slider));
        }
        
        protected abstract void onValueChanged(final float p0);
    }
}
