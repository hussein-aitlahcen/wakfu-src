package com.ankamagames.wakfu.client;

import com.ankamagames.framework.kernel.core.controllers.*;
import com.ankamagames.wakfu.client.core.webBrowser.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.wakfu.client.steam.*;
import com.ankamagames.wakfu.client.core.game.steam.*;
import com.ankamagames.steam.client.*;
import javax.swing.*;
import com.ankamagames.framework.graphics.engine.texture.*;
import com.ankamagames.baseImpl.graphics.opengl.*;
import com.ankamagames.framework.graphics.image.io.reader.*;
import com.ankamagames.framework.kernel.core.io.*;
import com.ankamagames.wakfu.common.constants.*;
import org.kohsuke.args4j.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.framework.preferences.*;
import com.ankamagames.baseImpl.common.clientAndServer.utils.*;
import com.google.common.base.*;
import com.ankamagames.framework.kernel.utils.*;
import org.apache.log4j.*;
import java.io.*;
import java.util.*;
import com.google.inject.*;
import com.ankama.wakfu.utils.metrics.module.*;
import com.ankama.wakfu.utils.injection.*;

public class WakfuClient
{
    private static Logger m_logger;
    public static WakfuClient INSTANCE;
    private long m_startTime;
    private String m_logPath;
    private String m_reportPath;
    private String m_screenshotsPath;
    
    WakfuClient() {
        super();
        this.m_logPath = ".";
        this.m_reportPath = ".";
        this.m_screenshotsPath = ".";
        WakfuClient.INSTANCE = this;
        this.m_startTime = System.nanoTime();
    }
    
    public String getLogPath() {
        return this.m_logPath;
    }
    
    void start() {
        RepeatingReleasedEventsFixer.install();
        this.startClient();
    }
    
    private void configure() {
        WakfuSWT.initDisplay();
        this.configureGraphicEngine();
    }
    
    private boolean initSteam() {
        if (!Partner.getCurrentPartner().isEnableSteam()) {
            PropertiesProvider.getInstance().setPropertyValue("steam", null);
            return false;
        }
        SteamDisplayer.INSTANCE.setConnected(false);
        PropertiesProvider.getInstance().setPropertyValue("steam", SteamDisplayer.INSTANCE);
        final SteamClient client = SteamHelper.createClient();
        if (client == null) {
            WakfuClient.m_logger.fatal((Object)"Impossible de cr\u00e9er le client steam.");
            return false;
        }
        final boolean init = SteamHelper.initializeUserContext(client);
        if (!init) {
            WakfuClient.m_logger.error((Object)"[Steam] Probl\u00e8me \u00e0 l'initialisation du UserContext");
        }
        SteamHelper.debugInfo();
        return init;
    }
    
    private void startClient() {
        final WakfuClientInstance client = WakfuClientInstance.getInstance();
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    client.initialize();
                }
                catch (Throwable e) {
                    WakfuClient.m_logger.fatal((Object)"Erreur au lancement", e);
                    System.exit(1);
                }
            }
        });
        WakfuSWT.runEventPump();
    }
    
    private void configureGraphicEngine() {
        TextureManager.getInstance().setCacheSize(GLApplicationUI.GRAPHICS_ENVIRONMENT.getDefaultScreenDevice().getAvailableAcceleratedMemory() / 1024.0f);
        ImageReaderFactory.getInstance().registerReader("JPG", new JPGReader());
        ImageReaderFactory.getInstance().registerReader("PNG", new PNGReader());
        ImageReaderFactory.getInstance().registerReader("DDSM", new DDSMReader());
        ImageReaderFactory.getInstance().registerReader("DDS", new DDSReader());
        ImageReaderFactory.getInstance().registerReader("TGA", new TGAReader());
        ImageReaderFactory.getInstance().registerReader("TGAM", new TGAMReader());
        AsyncLoader.getInstance().start();
    }
    
    private void displayBasicInformations() {
        Version.display();
        WakfuClient.m_logger.info((Object)("java.vm.vendor = " + System.getProperty("java.vm.vendor")));
        WakfuClient.m_logger.info((Object)("java.runtime.name = " + System.getProperty("java.runtime.name")));
        WakfuClient.m_logger.info((Object)("java.vm.name = " + System.getProperty("java.vm.name")));
        WakfuClient.m_logger.info((Object)("java.vm.version = " + System.getProperty("java.vm.version")));
        WakfuClient.m_logger.info((Object)("java.runtime.version = " + System.getProperty("java.runtime.version")));
        WakfuClient.m_logger.info((Object)("os.name = " + System.getProperty("os.name")));
        WakfuClient.m_logger.info((Object)("os.arch = " + System.getProperty("os.arch")));
        WakfuClient.m_logger.info((Object)("os.version = " + System.getProperty("os.version")));
        WakfuClient.m_logger.info((Object)("sun.os.patch.level = " + System.getProperty("sun.os.patch.level")));
        WakfuClient.m_logger.info((Object)("user.language = " + System.getProperty("user.language")));
        WakfuClient.m_logger.info((Object)("user.country = " + System.getProperty("user.country")));
        WakfuClient.m_logger.trace((Object)("Locale: " + Locale.getDefault().getDisplayName()));
        Locale.setDefault(Locale.ENGLISH);
        WakfuClient.m_logger.info((Object)("log path=" + this.m_logPath));
    }
    
    private void parseCommandLineArguments(final String[] arguments) {
        final WakfuOptions options = new WakfuOptions();
        final CmdLineParser cmdLineParser = new CmdLineParser((Object)options);
        try {
            cmdLineParser.parseArgument((Collection)Arrays.asList(arguments));
            cmdLineParser.stopOptionParsing();
        }
        catch (CmdLineException e) {
            System.err.println("Fail to parse arguments: " + e.getMessage());
            cmdLineParser.printUsage((OutputStream)System.err);
            System.exit(1);
        }
        if (options.getPreferenceStorePath() != null) {
            final String userPath = options.getPreferenceStorePath();
            this.m_logPath = userPath;
            this.m_reportPath = userPath;
            if (!this.m_reportPath.endsWith("/")) {
                this.m_reportPath += "/";
            }
            this.m_reportPath += "report/";
            this.m_screenshotsPath = userPath;
            if (!this.m_screenshotsPath.endsWith("/")) {
                this.m_screenshotsPath += "/";
            }
            this.m_screenshotsPath += "screenshots/";
            final String preferencesPath = userPath + "/preferences";
            new File(preferencesPath).mkdirs();
            System.setProperty("preferenceStorePath", preferencesPath);
        }
        this.configureLogger();
        final String configurationFile = options.getConfigurationFile();
        boolean configurationLoaded = false;
        if (configurationFile != null) {
            configurationLoaded = WakfuConfiguration.getInstance().load(configurationFile);
            if (configurationLoaded) {
                WakfuClient.m_logger.info((Object)("Configuration loaded from file set on command line: " + configurationFile));
            }
        }
        if (options.getPartner() != null) {
            Partner.initializeCurrentPartner(options.getPartner());
        }
        PropertiesProvider.getInstance().setPropertyValue("partner", Partner.getCurrentPartner().getName());
        if (!configurationLoaded) {
            try {
                final PreferenceStore preferenceStore = new PreferenceStore("region.properties");
                preferenceStore.load();
                final Optional<Region> region = (Optional<Region>)Enums.getIfPresent((Class)Region.class, preferenceStore.getString("REGION"));
                if (region.isPresent()) {
                    configurationLoaded = WakfuConfiguration.getInstance().load(((Region)region.get()).getConfigFile());
                    System.setProperty("REGION", ((Region)region.get()).name());
                    if (configurationLoaded) {
                        WakfuClient.m_logger.info((Object)("Configuration loaded for region " + region.get() + " (set in preference store): " + ((Region)region.get()).getConfigFile()));
                    }
                }
            }
            catch (IOException e2) {
                WakfuClient.m_logger.error((Object)"Unable to load region preferences", (Throwable)e2);
            }
        }
        if (!configurationLoaded) {
            final Region region2 = Region.getRegionFromCountryCode(options.getCountryCode());
            configurationLoaded = WakfuConfiguration.getInstance().load(region2.getConfigFile());
            System.setProperty("REGION", region2.name());
            if (configurationLoaded) {
                WakfuClient.m_logger.info((Object)("Configuration loaded for region " + System.getProperty("REGION", "") + " (by country detection for " + options.getCountryCode() + "): " + region2.getConfigFile()));
            }
        }
        if (!configurationLoaded) {
            WakfuClient.m_logger.info((Object)"Loading configuration from default file");
        }
        if (!configurationLoaded && !WakfuConfiguration.getInstance().load()) {
            WakfuClient.m_logger.fatal((Object)"Echec du chargement de la configuration, WakfuConfiguration introuvable");
            return;
        }
        if (options.getCachePath() != null) {
            final String cachePath = options.getCachePath() + File.separator;
            WakfuConfiguration.getInstance().setString("cacheDirectory", cachePath);
        }
        WakfuConfiguration.getInstance().setInteger("UPDATER_COMMUNICATION_PORT", options.getUpdaterCommunicationPort());
        if (options.getInitialUpdateState() != null) {
            WakfuConfiguration.getInstance().setString("UPDATER_INITIAL_STATE", options.getInitialUpdateState());
        }
        String languageCode = null;
        if (options.getLangageCode() != null) {
            languageCode = options.getLangageCode();
            if ("zh".equals(languageCode)) {
                languageCode = "ch";
            }
            if ("zh-TW".equals(languageCode)) {
                languageCode = "tw";
            }
            System.setProperty("wakfu.language", languageCode);
        }
    }
    
    private void configureLogger() {
        final Properties props = new Properties();
        try {
            final InputStream configStream = WakfuClient.class.getResourceAsStream("log4j.properties");
            props.load(configStream);
            configStream.close();
        }
        catch (IOException e) {
            System.err.println("Error: Cannot load configuration file ");
        }
        if (!StringUtils.isEmptyOrNull(this.m_logPath)) {
            final Enumeration<?> properties = props.propertyNames();
            while (properties.hasMoreElements()) {
                final String propertyName = (String)properties.nextElement();
                if (!propertyName.endsWith(".File")) {
                    continue;
                }
                props.setProperty(propertyName, this.m_logPath + '/' + props.getProperty(propertyName));
            }
        }
        LogManager.resetConfiguration();
        PropertyConfigurator.configure(props);
    }
    
    public long getRunningTime() {
        return (System.nanoTime() - this.m_startTime) / 1000000L;
    }
    
    public static void main(final String[] arguments) {
        final Injector injector = Guice.createInjector(new Module[] { getApplicationModule() });
        Injection.getInstance().setInjector(injector);
        final WakfuClient client = (WakfuClient)injector.getInstance((Class)WakfuClient.class);
        client.parseCommandLineArguments(arguments);
        client.configure();
        client.initSteam();
        client.displayBasicInformations();
        client.start();
    }
    
    private static Module getApplicationModule() {
        return getModuleResolver().resolveDependencies(new WakfuClientModule(), new MetricNoModule());
    }
    
    public String getReportPath() {
        return this.m_reportPath;
    }
    
    public String getScreenshotsPath() {
        return this.m_screenshotsPath;
    }
    
    public static IModuleResolver getModuleResolver() {
        return ModuleResolver.get();
    }
    
    static {
        WakfuClient.m_logger = Logger.getLogger((Class)WakfuClient.class);
    }
}
