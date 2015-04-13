package com.ankamagames.baseImpl.graphics.isometric.debug.particlesAndLights;

import java.util.*;
import com.ankamagames.framework.graphics.engine.particleSystem.*;
import com.ankamagames.framework.graphics.engine.light.*;
import java.awt.*;
import com.ankamagames.framework.kernel.core.common.message.scheduler.process.*;
import javax.swing.*;

public final class ParticlesAndLightDebug
{
    public static final ParticlesAndLightDebug INSTANCE;
    private boolean m_initialized;
    private JFrame m_frame;
    private DebugView m_view;
    final ArrayList<ParticleSystem> m_systems;
    final ArrayList<LightSource> m_lights;
    int m_maxParticles;
    int m_maxLights;
    
    private ParticlesAndLightDebug() {
        super();
        this.m_systems = new ArrayList<ParticleSystem>();
        this.m_lights = new ArrayList<LightSource>();
    }
    
    public void initialize() {
        if (this.m_initialized) {
            return;
        }
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                ParticlesAndLightDebug.this.m_frame = new JFrame("PSys Debug");
                ParticlesAndLightDebug.this.m_view = new DebugView();
                ParticlesAndLightDebug.this.m_frame.setContentPane(ParticlesAndLightDebug.this.m_view.getRootPanel());
                ParticlesAndLightDebug.this.m_frame.setDefaultCloseOperation(3);
                ParticlesAndLightDebug.this.m_frame.setSize(300, 600);
                ParticlesAndLightDebug.this.m_frame.setVisible(true);
                ProcessScheduler.getInstance().schedule(new Runnable() {
                    @Override
                    public void run() {
                        if (ParticlesAndLightDebug.this.m_view != null) {
                            ParticlesAndLightDebug.this.m_view.updateData(ParticlesAndLightDebug.this.m_systems, ParticlesAndLightDebug.this.m_lights);
                        }
                        final StringBuilder sb = new StringBuilder("PSys Debug ").append(" Part.: ").append(ParticlesAndLightDebug.this.m_systems.size()).append(" (max ").append(ParticlesAndLightDebug.this.m_maxParticles).append(") Lights: ").append(ParticlesAndLightDebug.this.m_lights.size()).append(" (max ").append(ParticlesAndLightDebug.this.m_maxLights).append(")");
                        ParticlesAndLightDebug.this.m_frame.setTitle(sb.toString());
                    }
                }, 1000L, -1);
            }
        });
        this.m_initialized = true;
    }
    
    public void addParticleSystem(final ParticleSystem system) {
        if (!this.m_initialized) {
            return;
        }
        this.m_systems.add(system);
        if (this.m_systems.size() > this.m_maxParticles) {
            this.m_maxParticles = this.m_systems.size();
        }
        if (this.m_view != null) {
            this.m_view.updateData(this.m_systems, this.m_lights);
            this.m_view.getRootPanel().updateUI();
        }
    }
    
    public void removeParticleSystem(final ParticleSystem system) {
        if (!this.m_initialized) {
            return;
        }
        this.m_systems.remove(system);
        if (this.m_view != null) {
            this.m_view.updateData(this.m_systems, this.m_lights);
            this.m_view.getRootPanel().updateUI();
        }
    }
    
    public void addLightSource(final LightSource source) {
        if (!this.m_initialized) {
            return;
        }
        this.m_lights.add(source);
        if (this.m_lights.size() > this.m_maxLights) {
            this.m_maxLights = this.m_lights.size();
        }
        if (this.m_view != null) {
            this.m_view.updateData(this.m_systems, this.m_lights);
            this.m_view.getRootPanel().updateUI();
        }
    }
    
    public void removeLightSource(final LightSource source) {
        if (!this.m_initialized) {
            return;
        }
        this.m_lights.remove(source);
        if (this.m_view != null) {
            this.m_view.updateData(this.m_systems, this.m_lights);
            this.m_view.getRootPanel().updateUI();
        }
    }
    
    static {
        INSTANCE = new ParticlesAndLightDebug();
    }
}
