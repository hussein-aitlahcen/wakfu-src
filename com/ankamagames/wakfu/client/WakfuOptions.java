package com.ankamagames.wakfu.client;

import org.kohsuke.args4j.*;

final class WakfuOptions
{
    @Option(name = "-c", usage = "Configuration file path")
    private String m_configurationFile;
    @Option(name = "-p", usage = "Directory containing preferences files")
    private String m_preferenceStorePath;
    @Option(name = "-n", usage = "Directory containing cache files")
    private String m_cachePath;
    @Option(name = "-L", usage = "Language code for session")
    private String m_langageCode;
    @Option(name = "-C", usage = "Country code for session")
    private String m_countryCode;
    @Option(name = "--install-folder", usage = "Local path of installation")
    private String m_instalationDirectoryPath;
    @Option(name = "--updater-service-port", usage = "Port to use for communication with updater", hidden = true)
    private int m_updaterCommunicationPort;
    @Option(name = "--update-state", usage = "Initial update state (updating, uptodate) to consider", hidden = true)
    private String m_initialUpdateState;
    @Option(name = "-P", usage = "Active profiler", hidden = true)
    private boolean m_profilerActivated;
    @Option(name = "-T", usage = "Active debugging of particle and light", hidden = true)
    private boolean m_particleDebuggingActivated;
    @Option(name = "-R", usage = "Active debugging of render tree", hidden = true)
    private boolean m_renderTreeDebuggingActivated;
    @Option(name = "-M", usage = "Active debugging of memory pool objects", hidden = true)
    private boolean m_memoryPoolDebuggingActivated;
    @Option(name = "-t", usage = "Active debugging of pathfinding", hidden = true)
    private boolean m_pathfindingDebuggingActivated;
    @Option(name = "--partner", usage = "Partner (values : steam)", hidden = true)
    private String m_partner;
    
    String getConfigurationFile() {
        return this.m_configurationFile;
    }
    
    String getPreferenceStorePath() {
        return this.m_preferenceStorePath;
    }
    
    String getCachePath() {
        return this.m_cachePath;
    }
    
    String getLangageCode() {
        return this.m_langageCode;
    }
    
    String getCountryCode() {
        return this.m_countryCode;
    }
    
    boolean isProfilerActivated() {
        return this.m_profilerActivated;
    }
    
    boolean isParticleDebuggingActivated() {
        return this.m_particleDebuggingActivated;
    }
    
    boolean isRenderTreeDebuggingActivated() {
        return this.m_renderTreeDebuggingActivated;
    }
    
    boolean isMemoryPoolDebuggingActivated() {
        return this.m_memoryPoolDebuggingActivated;
    }
    
    boolean isPathfindingDebuggingActivated() {
        return this.m_pathfindingDebuggingActivated;
    }
    
    String getInstalationDirectoryPath() {
        return this.m_instalationDirectoryPath;
    }
    
    int getUpdaterCommunicationPort() {
        return this.m_updaterCommunicationPort;
    }
    
    String getInitialUpdateState() {
        return this.m_initialUpdateState;
    }
    
    String getPartner() {
        return this.m_partner;
    }
}
