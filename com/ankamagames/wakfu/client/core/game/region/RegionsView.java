package com.ankamagames.wakfu.client.core.game.region;

import com.ankamagames.wakfu.client.ui.component.*;
import org.apache.log4j.*;
import com.google.common.collect.*;
import com.ankamagames.baseImpl.common.clientAndServer.utils.*;
import java.util.*;
import com.ankamagames.wakfu.common.constants.*;
import com.google.common.base.*;
import org.jetbrains.annotations.*;
import com.ankamagames.framework.preferences.*;
import java.io.*;

public class RegionsView extends ImmutableFieldProvider
{
    private static final Logger m_logger;
    public static final String LIST = "list";
    public static final String SELECTED_REGION = "selectedRegion";
    public static final String ENABLED = "enabled";
    public static final String REGION_PROPERTIES_FILENAME = "region.properties";
    private final ImmutableList<RegionView> m_views;
    private final boolean m_enabled;
    private RegionView m_currentRegion;
    
    public RegionsView() {
        super();
        final String regionName = System.getProperty("REGION", "");
        final Optional<Region> regionO = (Optional<Region>)Enums.getIfPresent((Class)Region.class, regionName);
        final Region currentRegion = (Region)(regionO.isPresent() ? regionO.get() : Region.WESTERN);
        final Collection<RegionView> views = new ArrayList<RegionView>();
        for (final Region r : Region.values()) {
            if (!r.isHidden()) {
                final RegionView view = new RegionView(r);
                views.add(view);
                if (r == currentRegion) {
                    this.m_currentRegion = view;
                }
            }
        }
        this.m_views = (ImmutableList<RegionView>)ImmutableList.copyOf((Collection)views);
        this.m_enabled = Partner.getCurrentPartner().isEnableRegionSelection();
    }
    
    @Override
    public String[] getFields() {
        return RegionsView.NO_FIELDS;
    }
    
    @Nullable
    @Override
    public Object getFieldValue(final String fieldName) {
        if ("list".equals(fieldName)) {
            return this.m_views;
        }
        if ("selectedRegion".equals(fieldName)) {
            return this.m_currentRegion;
        }
        if ("enabled".equals(fieldName)) {
            return this.m_enabled;
        }
        return null;
    }
    
    public void setCurrentRegion(final RegionView currentRegion) {
        this.m_currentRegion = currentRegion;
        System.setProperty("REGION", this.m_currentRegion.getRegion().name());
        final PreferenceStore preferenceStore = new PreferenceStore("region.properties");
        try {
            preferenceStore.setValue("REGION", this.m_currentRegion.getRegion().name());
            preferenceStore.save();
        }
        catch (IOException e) {
            RegionsView.m_logger.error((Object)"Probl\u00e8me \u00e0 la sauvegarde du fichier de pr\u00e9f\u00e9rence region.properties", (Throwable)e);
        }
    }
    
    static {
        m_logger = Logger.getLogger((Class)RegionsView.class);
    }
}
