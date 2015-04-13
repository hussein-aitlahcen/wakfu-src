package com.ankamagames.wakfu.client.core.preferences;

import org.apache.log4j.*;
import com.ankamagames.framework.graphics.engine.benchmark.*;
import com.ankamagames.wakfu.client.*;
import com.ankamagames.wakfu.client.ui.providers.*;
import java.util.*;
import com.ankamagames.framework.graphics.engine.test.*;

public class WakfuGraphicalPreferences
{
    protected static Logger m_logger;
    private int m_lod;
    private Resolution m_resolution;
    private boolean m_fullScreen;
    private boolean m_activateShaders;
    
    public WakfuGraphicalPreferences() {
        super();
        this.m_lod = 1;
        this.m_fullScreen = true;
        this.m_activateShaders = false;
    }
    
    public void build(final BenchmarkResult benchmarkResult) {
        final GLApplicationUIOptionsFieldProvider resolutionProvider = new GLApplicationUIOptionsFieldProvider(WakfuClientInstance.getInstance().getAppUI());
        final ArrayList<Resolution> availableResolutions = resolutionProvider.getAvailableResolutions();
        this.m_activateShaders = this.shouldActivateShaders(benchmarkResult);
        this.m_resolution = this.selectResolution(availableResolutions, benchmarkResult);
        this.m_lod = this.selectLoD(benchmarkResult);
        this.m_fullScreen = this.selectFullscreenMode(benchmarkResult);
    }
    
    private boolean selectFullscreenMode(final BenchmarkResult benchmarkResult) {
        return true;
    }
    
    private boolean shouldActivateShaders(final BenchmarkResult benchmarkResult) {
        final boolean shadersSupported = HardwareFeatureManager.INSTANCE.isFeatureSupported(HardwareFeature.GL_FRAGMENT_SHADERS);
        return shadersSupported;
    }
    
    private Resolution selectResolution(final ArrayList<Resolution> availableResolutions, final BenchmarkResult benchmarkResult) {
        return availableResolutions.get(0);
    }
    
    private int selectLoD(final BenchmarkResult benchmarkResult) {
        return 1;
    }
    
    public void log() {
        WakfuGraphicalPreferences.m_logger.info((Object)"Graphical preferences :");
        WakfuGraphicalPreferences.m_logger.info((Object)("\t* resolution : " + this.m_resolution));
        WakfuGraphicalPreferences.m_logger.info((Object)("\t* fullscreen : " + this.m_fullScreen));
        WakfuGraphicalPreferences.m_logger.info((Object)("\t* level of details : " + this.m_lod));
        WakfuGraphicalPreferences.m_logger.info((Object)("\t* shaders activated : " + this.m_activateShaders));
    }
    
    static {
        WakfuGraphicalPreferences.m_logger = Logger.getLogger((Class)WakfuGraphicalPreferences.class);
    }
}
