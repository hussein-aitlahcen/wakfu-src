package com.ankamagames.framework.graphics.engine.test;

import org.apache.log4j.*;
import java.util.*;

public class HardwareFeatureTester implements HardwareFeatureTest
{
    protected static Logger m_logger;
    public static final HardwareFeatureTester INSTANCE;
    private final ArrayList<HardwareFeatureTest> m_hardwareTests;
    
    private HardwareFeatureTester() {
        super();
        this.m_hardwareTests = new ArrayList<HardwareFeatureTest>();
    }
    
    public void addTest(final HardwareFeatureTest hardwareTest) {
        if (!this.m_hardwareTests.contains(hardwareTest)) {
            this.m_hardwareTests.add(hardwareTest);
        }
    }
    
    @Override
    public void initialize() {
        HardwareFeatureTester.m_logger.info((Object)"Initializing hardware tests...");
        for (int i = 0, size = this.m_hardwareTests.size(); i < size; ++i) {
            final HardwareFeatureTest featureTest = this.m_hardwareTests.get(i);
            try {
                featureTest.initialize();
            }
            catch (Throwable e) {
                HardwareFeatureTester.m_logger.error((Object)("Erreur \u00e0 l'initialisation du test hardware " + featureTest.getFeature().getFeatureName()), e);
            }
        }
    }
    
    @Override
    public boolean runTest() {
        HardwareFeatureTester.m_logger.info((Object)"Testing supported features...");
        for (int i = 0, size = this.m_hardwareTests.size(); i < size; ++i) {
            final HardwareFeatureTest featureTest = this.m_hardwareTests.get(i);
            final HardwareFeature feature = featureTest.getFeature();
            boolean isSupported = false;
            try {
                isSupported = featureTest.runTest();
            }
            catch (Throwable e) {
                HardwareFeatureTester.m_logger.error((Object)("Erreur pendant le test hardware " + feature.getFeatureName()), e);
            }
            HardwareFeatureTester.m_logger.info((Object)("\t* " + feature.getFeatureName() + "..." + (isSupported ? " supported !" : " not supported !")));
            HardwareFeatureManager.INSTANCE.setFeatureSupported(feature, isSupported);
        }
        return true;
    }
    
    @Override
    public void cleanUp() {
        HardwareFeatureTester.m_logger.info((Object)"Cleaning up hardware tests...");
        for (int i = 0, size = this.m_hardwareTests.size(); i < size; ++i) {
            final HardwareFeatureTest featureTest = this.m_hardwareTests.get(i);
            try {
                featureTest.cleanUp();
            }
            catch (Throwable e) {
                HardwareFeatureTester.m_logger.error((Object)("Erreur pendant le cleanup du test hardware " + featureTest.getFeature().getFeatureName()), e);
            }
        }
    }
    
    @Override
    public HardwareFeature getFeature() {
        return null;
    }
    
    static {
        HardwareFeatureTester.m_logger = Logger.getLogger((Class)HardwareFeatureTester.class);
        INSTANCE = new HardwareFeatureTester();
    }
}
