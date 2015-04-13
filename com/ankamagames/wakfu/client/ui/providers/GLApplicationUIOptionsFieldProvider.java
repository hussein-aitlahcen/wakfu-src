package com.ankamagames.wakfu.client.ui.providers;

import com.ankamagames.framework.reflect.*;
import org.apache.log4j.*;
import gnu.trove.*;
import com.ankamagames.baseImpl.graphics.opengl.*;
import java.awt.*;
import com.ankamagames.xulor2.property.*;
import java.util.*;
import com.ankamagames.wakfu.client.core.*;

public class GLApplicationUIOptionsFieldProvider implements FieldProvider
{
    public static final Logger m_logger;
    public static final String AVAILABLE_RESOLUTIONS_FIELD = "availableResolutions";
    public static final String AVAILABLE_MODES_FIELD = "availableModes";
    public static final String AVAILABLE_FREQUENCIES_FIELD = "availableFrequencies";
    public static final String SELECTED_RESOLUTION_FIELD = "selectedResolution";
    public static final String SELECTED_MODE_FIELD = "selectedMode";
    public static final String SELECTED_FREQUENCY_FIELD = "selectedFrequency";
    public static final String IS_FULLSCREEN_MODE_SELECTED = "isFullScreenModeSelected";
    public static final String[] FIELDS;
    private final GLApplicationUI m_appUI;
    private Frequency m_selectedFrequency;
    private Resolution m_selectedResolution;
    private ModeView m_selectedMode;
    private static final Frequency DEFAULT_FREQUENCY;
    private static final DisplayMode[] DISPLAY_MODES;
    private final TIntObjectHashMap<ModeView> m_modeViews;
    
    public GLApplicationUIOptionsFieldProvider(final GLApplicationUI appUI) {
        super();
        this.m_modeViews = new TIntObjectHashMap<ModeView>();
        this.m_appUI = appUI;
        final ApplicationResolution currentRes = this.m_appUI.getResolution();
        final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.m_selectedResolution = this.getNearestResolution();
        this.m_selectedFrequency = new Frequency(currentRes.getFrequency());
        for (final ApplicationResolution.Mode m : ApplicationResolution.Mode.values()) {
            this.m_modeViews.put(m.ordinal(), new ModeView(m));
        }
        this.m_selectedMode = this.m_modeViews.get(currentRes.getMode().ordinal());
        currentRes.getMode();
    }
    
    @Override
    public String[] getFields() {
        return GLApplicationUIOptionsFieldProvider.FIELDS;
    }
    
    public void applyResolution(final Object oMode, final Object oResolution, final Object oFrequency) {
        final GraphicsDevice device = GLApplicationUI.GRAPHICS_ENVIRONMENT.getDefaultScreenDevice();
        final DisplayMode currentDisplayMode = device.getDisplayMode();
        final ApplicationResolution.Mode mode = (ApplicationResolution.Mode)oMode;
        ApplicationResolution appRes = null;
        switch (mode) {
            case FULLSCREEN: {
                final Resolution res = (Resolution)oResolution;
                final Frequency frequency = (Frequency)oFrequency;
                appRes = new ApplicationResolution(res.getWidth(), res.getHeight(), currentDisplayMode.getBitDepth(), frequency.m_value, mode);
                break;
            }
            case WINDOWED: {
                final ApplicationResolution defaultAppRes = this.m_appUI.getDefaultResolution();
                appRes = new ApplicationResolution(defaultAppRes.getWidth(), defaultAppRes.getHeight(), -1, 0, mode);
                break;
            }
            case WINDOWED_FULLSCREEN: {
                appRes = new ApplicationResolution(0, 0, -1, 0, mode);
                break;
            }
            default: {
                appRes = this.m_appUI.getDefaultResolution();
                break;
            }
        }
        this.m_appUI.applyResolution(appRes);
    }
    
    public void onModeSelected(final ModeView selectedMode) {
        this.m_selectedMode = selectedMode;
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "isFullScreenModeSelected", "availableFrequencies");
    }
    
    public void onResolutionSelected(final Object o) {
        this.m_selectedResolution = (Resolution)o;
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "availableFrequencies");
    }
    
    public ApplicationResolution.Mode getSelectedMode() {
        return this.m_selectedMode.m_mode;
    }
    
    public Resolution getSelectedResolution() {
        return this.m_selectedResolution;
    }
    
    public Frequency getSelectedFrequency() {
        return this.m_selectedFrequency;
    }
    
    private Resolution getNearestResolution() {
        final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        final float defaultScreenRatio = screenSize.width / screenSize.height;
        final ApplicationResolution currentRes = this.m_appUI.getResolution();
        final Dimension minimumSize = this.m_appUI.getMinimumSize();
        final int currentResSurface = currentRes.getWidth() * currentRes.getHeight();
        Resolution bestResolution = new Resolution(screenSize.width, screenSize.height);
        float bestResolutionRatio = defaultScreenRatio;
        float bestResolutionSurfaceFactorFromCurrent = Math.abs((currentResSurface - screenSize.width * screenSize.height) / currentResSurface);
        for (final DisplayMode mode : GLApplicationUIOptionsFieldProvider.DISPLAY_MODES) {
            final int bitDepth = mode.getBitDepth();
            if (bitDepth == -1 || bitDepth >= this.m_appUI.getMinimumBpp()) {
                if (mode.getWidth() >= minimumSize.width) {
                    if (mode.getHeight() >= minimumSize.height) {
                        final float resRatio = mode.getWidth() / mode.getHeight();
                        if (Math.abs(resRatio - defaultScreenRatio) <= Math.abs(bestResolutionRatio - defaultScreenRatio)) {
                            final float resSurfaceFactor = Math.abs((currentResSurface - mode.getWidth() * mode.getHeight()) / currentResSurface);
                            if (resSurfaceFactor < bestResolutionSurfaceFactorFromCurrent) {
                                bestResolution = new Resolution(mode.getWidth(), mode.getHeight());
                                bestResolutionRatio = resRatio;
                                bestResolutionSurfaceFactorFromCurrent = resSurfaceFactor;
                            }
                        }
                    }
                }
            }
        }
        return bestResolution;
    }
    
    private ArrayList<Frequency> getAvailableFrequencies() {
        final ArrayList<Frequency> frequencies = new ArrayList<Frequency>();
        for (final DisplayMode mode : GLApplicationUIOptionsFieldProvider.DISPLAY_MODES) {
            final int bitDepth = mode.getBitDepth();
            if (bitDepth == -1 || bitDepth >= this.m_appUI.getMinimumBpp()) {
                if (mode.getWidth() == this.m_selectedResolution.getWidth()) {
                    if (mode.getHeight() == this.m_selectedResolution.getHeight()) {
                        final Frequency freq = new Frequency(mode.getRefreshRate());
                        if (!frequencies.contains(freq)) {
                            frequencies.add(freq);
                        }
                    }
                }
            }
        }
        if (!frequencies.contains(GLApplicationUIOptionsFieldProvider.DEFAULT_FREQUENCY)) {
            frequencies.add(GLApplicationUIOptionsFieldProvider.DEFAULT_FREQUENCY);
        }
        Collections.sort(frequencies);
        return frequencies;
    }
    
    public ArrayList<Resolution> getAvailableResolutions() {
        final ArrayList<Resolution> resolutions = new ArrayList<Resolution>();
        final Dimension minimumSize = this.m_appUI.getMinimumSize();
        for (final DisplayMode mode : GLApplicationUIOptionsFieldProvider.DISPLAY_MODES) {
            final int bitDepth = mode.getBitDepth();
            if (bitDepth == -1 || bitDepth >= this.m_appUI.getMinimumBpp()) {
                if (mode.getWidth() >= minimumSize.width) {
                    if (mode.getHeight() >= minimumSize.height) {
                        final Resolution res = new Resolution(mode.getWidth(), mode.getHeight());
                        if (!resolutions.contains(res)) {
                            resolutions.add(res);
                        }
                    }
                }
            }
        }
        Collections.sort(resolutions);
        return resolutions;
    }
    
    private Object[] getAvailableModes() {
        return this.m_modeViews.getValues();
    }
    
    @Override
    public Object getFieldValue(final String fieldName) {
        if ("availableResolutions".equals(fieldName)) {
            return this.getAvailableResolutions();
        }
        if ("availableModes".equals(fieldName)) {
            return this.getAvailableModes();
        }
        if ("availableFrequencies".equals(fieldName)) {
            return this.getAvailableFrequencies();
        }
        if ("selectedResolution".equals(fieldName)) {
            return this.m_selectedResolution;
        }
        if ("selectedMode".equals(fieldName)) {
            return this.m_selectedMode;
        }
        if ("selectedFrequency".equals(fieldName)) {
            return this.m_selectedFrequency;
        }
        if ("isFullScreenModeSelected".equals(fieldName)) {
            return this.m_selectedMode.m_mode == ApplicationResolution.Mode.FULLSCREEN;
        }
        System.err.println("Not found : " + fieldName);
        return null;
    }
    
    @Override
    public void setFieldValue(final String fieldName, final Object value) {
    }
    
    @Override
    public void prependFieldValue(final String fieldName, final Object value) {
    }
    
    @Override
    public void appendFieldValue(final String fieldName, final Object value) {
    }
    
    @Override
    public boolean isFieldSynchronisable(final String fieldName) {
        return false;
    }
    
    static {
        m_logger = Logger.getLogger((Class)GLApplicationUIOptionsFieldProvider.class);
        FIELDS = new String[] { "availableResolutions", "availableModes", "availableFrequencies", "selectedResolution", "selectedMode", "selectedFrequency", "isFullScreenModeSelected" };
        DEFAULT_FREQUENCY = new Frequency(0);
        final ArrayList<DisplayMode> validModes = new ArrayList<DisplayMode>();
        final DisplayMode[] arr$;
        final DisplayMode[] modes = arr$ = GLApplicationUI.GRAPHICS_ENVIRONMENT.getDefaultScreenDevice().getDisplayModes();
        for (final DisplayMode mode : arr$) {
            if (mode.getWidth() >= 1024) {
                if (mode.getHeight() >= 768) {
                    validModes.add(mode);
                }
            }
        }
        DISPLAY_MODES = validModes.toArray(new DisplayMode[validModes.size()]);
    }
    
    public static class ModeView
    {
        private final ApplicationResolution.Mode m_mode;
        
        private ModeView(final ApplicationResolution.Mode mode) {
            super();
            this.m_mode = mode;
        }
        
        @Override
        public String toString() {
            return WakfuTranslator.getInstance().getString(this.m_mode.name());
        }
    }
    
    private static final class Frequency implements Comparable<Frequency>
    {
        private final int m_value;
        
        private Frequency(final int value) {
            super();
            this.m_value = value;
        }
        
        public int getValue() {
            return this.m_value;
        }
        
        @Override
        public boolean equals(final Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || this.getClass() != o.getClass()) {
                return false;
            }
            final Frequency frequency = (Frequency)o;
            return this.m_value == frequency.m_value;
        }
        
        @Override
        public int hashCode() {
            return this.m_value;
        }
        
        @Override
        public String toString() {
            if (this.m_value == 0) {
                return "Default";
            }
            return Integer.toString(this.m_value) + " Hz";
        }
        
        @Override
        public int compareTo(final Frequency frequency) {
            return this.m_value - frequency.m_value;
        }
    }
}
