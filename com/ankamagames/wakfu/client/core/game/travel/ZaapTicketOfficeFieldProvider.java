package com.ankamagames.wakfu.client.core.game.travel;

import com.ankamagames.wakfu.client.ui.component.*;
import org.apache.log4j.*;
import com.ankamagames.framework.kernel.core.common.collections.lightweight.intKey.*;
import com.ankamagames.wakfu.common.configuration.*;
import com.ankamagames.wakfu.common.game.travel.*;
import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.wakfu.common.account.subscription.instanceRight.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.common.game.travel.infos.*;
import java.util.*;
import com.ankamagames.wakfu.client.core.*;
import gnu.trove.*;

public class ZaapTicketOfficeFieldProvider extends ImmutableFieldProvider
{
    protected static final Logger m_logger;
    public static final String CURRENT_NAME = "currentName";
    public static final String LINKS_BY_ISLAND_LIST = "linksByIsland";
    public static final String ICON_URL = "iconUrl";
    private final IntObjectLightWeightMap<ZaapIslandFieldProvider> m_zaapsByIsland;
    private long m_currentZaapId;
    private int m_uiGfxId;
    private boolean m_forZaapOutOnly;
    
    public ZaapTicketOfficeFieldProvider() {
        super();
        this.m_zaapsByIsland = new IntObjectLightWeightMap<ZaapIslandFieldProvider>();
    }
    
    public boolean initialise(final long currentZaapId) {
        final TIntHashSet forbiddenInstances = SystemConfiguration.INSTANCE.getIntHashSet(SystemConfigurationType.WORLD_INSTANCES_FORBIDDEN);
        this.m_forZaapOutOnly = false;
        this.m_zaapsByIsland.clear();
        this.m_currentZaapId = currentZaapId;
        final TravelInfo currentZaap = TravelInfoManager.INSTANCE.getInfo(TravelType.ZAAP, this.m_currentZaapId);
        this.m_uiGfxId = currentZaap.getUiGfxId();
        final LocalPlayerCharacter player = WakfuGameEntity.getInstance().getLocalPlayer();
        final TIntIterator it = player.getTravelHandler().knownZaapIterator();
        while (it.hasNext()) {
            final int zaapId = it.next();
            final ZaapInfo zaapInfo = TravelInfoManager.INSTANCE.getInfo(TravelType.ZAAP, zaapId);
            if (zaapInfo != null && zaapInfo.getId() != currentZaapId) {
                if (!zaapInfo.isDestinationValid(player)) {
                    continue;
                }
                final int exitWorldId = zaapInfo.getExitWorldId();
                if (forbiddenInstances.contains(exitWorldId)) {
                    continue;
                }
                ZaapIslandFieldProvider zaapIsland = this.m_zaapsByIsland.get(exitWorldId);
                if (zaapIsland == null) {
                    zaapIsland = new ZaapIslandFieldProvider(exitWorldId);
                    this.m_zaapsByIsland.put(exitWorldId, zaapIsland);
                }
                final ZaapLink link = TravelInfoManager.INSTANCE.getZaapLink(currentZaapId, zaapInfo.getId());
                boolean enabled = true;
                final TByteHashSet disableReasons = new TByteHashSet();
                final InstanceInteractionLevel interactionLevel = InstanceInteractionLevelManager.INSTANCE.getInstanceInteractionLevel(exitWorldId, player.getAccountInformationHandler().getActiveSubscriptionLevel());
                if (interactionLevel == InstanceInteractionLevel.FORBIDDEN_ACCESS) {
                    enabled = false;
                    disableReasons.add((byte)1);
                }
                zaapIsland.addZaap(new ZaapInfoFieldProvider(link, this.m_currentZaapId, enabled, disableReasons));
            }
        }
        return true;
    }
    
    @Override
    public String[] getFields() {
        return null;
    }
    
    public ArrayList<ZaapIslandFieldProvider> createSortedZaapList() {
        final ArrayList<ZaapIslandFieldProvider> zaaps = new ArrayList<ZaapIslandFieldProvider>();
        for (int i = 0, size = this.m_zaapsByIsland.size(); i < size; ++i) {
            zaaps.add(this.m_zaapsByIsland.getQuickValue(i));
        }
        Collections.sort(zaaps, ZaapIslandNameComparator.COMPARATOR);
        return zaaps;
    }
    
    @Override
    public Object getFieldValue(final String fieldName) {
        if (fieldName.equals("linksByIsland")) {
            if (this.m_zaapsByIsland.size() == 0) {
                return null;
            }
            return this.createSortedZaapList();
        }
        else if (fieldName.equals("currentName")) {
            if (this.m_forZaapOutOnly) {
                return WakfuTranslator.getInstance().getString("haven.world.zaap.name");
            }
            return WakfuTranslator.getInstance().getString(36, (int)this.m_currentZaapId, new Object[0]);
        }
        else {
            if (fieldName.equals("iconUrl")) {
                return WakfuConfiguration.getInstance().getIconUrl("zaapTypeIconPath", "defaultIconPath", (this.m_uiGfxId == -1) ? TravelGfxType.ZAAP.getGfxId() : this.m_uiGfxId);
            }
            return null;
        }
    }
    
    public void initialiseForZaapOut(final long zaapIds, final int cost) {
        this.m_uiGfxId = -1;
        this.m_zaapsByIsland.clear();
        this.m_currentZaapId = zaapIds;
        this.m_forZaapOutOnly = true;
        final LocalPlayerCharacter player = WakfuGameEntity.getInstance().getLocalPlayer();
        final TIntIterator it = player.getTravelHandler().knownZaapIterator();
        while (it.hasNext()) {
            final int zaapId = it.next();
            final ZaapInfo zaapInfo = TravelInfoManager.INSTANCE.getInfo(TravelType.ZAAP, zaapId);
            if (zaapInfo != null) {
                if (!zaapInfo.isDestinationValid(player)) {
                    continue;
                }
                final int exitWorldId = zaapInfo.getExitWorldId();
                ZaapIslandFieldProvider zaapIsland = this.m_zaapsByIsland.get(exitWorldId);
                if (zaapIsland == null) {
                    zaapIsland = new ZaapIslandFieldProvider(exitWorldId);
                    this.m_zaapsByIsland.put(exitWorldId, zaapIsland);
                }
                boolean enabled = true;
                final TByteHashSet disableReasons = new TByteHashSet();
                final InstanceInteractionLevel interactionLevel = InstanceInteractionLevelManager.INSTANCE.getInstanceInteractionLevel(exitWorldId, player.getAccountInformationHandler().getActiveSubscriptionLevel());
                if (interactionLevel == InstanceInteractionLevel.FORBIDDEN_ACCESS) {
                    enabled = false;
                    disableReasons.add((byte)1);
                }
                zaapIsland.addZaap(new ZaapInfoFieldProvider(new ZaapLink(0L, (int)zaapIds, (int)zaapInfo.getId(), cost), this.m_currentZaapId, enabled, disableReasons));
            }
        }
    }
    
    static {
        m_logger = Logger.getLogger((Class)ZaapTicketOfficeFieldProvider.class);
    }
    
    private static class ZaapIslandNameComparator implements Comparator<ZaapIslandFieldProvider>
    {
        static final Comparator<ZaapIslandFieldProvider> COMPARATOR;
        
        @Override
        public int compare(final ZaapIslandFieldProvider o1, final ZaapIslandFieldProvider o2) {
            final String name1 = WakfuTranslator.getInstance().getString(77, o1.getInstanceId(), new Object[0]);
            final String name2 = WakfuTranslator.getInstance().getString(77, o2.getInstanceId(), new Object[0]);
            return name1.compareTo(name2);
        }
        
        static {
            COMPARATOR = new ZaapIslandNameComparator();
        }
    }
}
