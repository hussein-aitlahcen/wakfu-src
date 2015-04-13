package com.ankamagames.wakfu.client.core.game.travel;

import com.ankamagames.wakfu.client.ui.component.*;
import org.apache.log4j.*;
import com.ankamagames.framework.kernel.core.common.collections.lightweight.intKey.*;
import com.ankamagames.wakfu.client.core.game.interactiveElement.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.interactiveElement.*;
import com.ankamagames.wakfu.common.account.subscription.*;
import com.ankamagames.wakfu.common.game.travel.infos.*;
import com.ankamagames.wakfu.common.account.subscription.instanceRight.*;
import com.ankamagames.wakfu.common.configuration.*;
import gnu.trove.*;
import com.ankamagames.wakfu.common.game.travel.*;
import com.ankamagames.wakfu.common.game.travel.character.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.framework.text.*;
import java.util.*;

public class BoatTicketOfficeFieldProvider extends ImmutableFieldProvider
{
    protected static final Logger m_logger;
    public static final String CURRENT_NAME = "currentName";
    public static final String LINKS_BY_ISLAND_LIST = "linksByIsland";
    public static final String ICON_URL = "iconUrl";
    private final IntObjectLightWeightMap<BoatIslandFieldProvider> m_boatsByIsland;
    private long m_currentBoatId;
    private int m_uiGfxId;
    
    public BoatTicketOfficeFieldProvider() {
        super();
        this.m_boatsByIsland = new IntObjectLightWeightMap<BoatIslandFieldProvider>();
    }
    
    public boolean initialise(final long currentBoatId, final TravelMachine boat) {
        this.m_boatsByIsland.clear();
        this.m_currentBoatId = currentBoatId;
        final TravelInfo boatInfo = TravelInfoManager.INSTANCE.getInfo(TravelType.BOAT, currentBoatId);
        this.m_uiGfxId = boatInfo.getUiGfxId();
        final LocalPlayerCharacter player = WakfuGameEntity.getInstance().getLocalPlayer();
        TravelInfoManager.INSTANCE.foreachBoatLink(new TObjectProcedure<BoatLink>() {
            @Override
            public boolean execute(final BoatLink link) {
                BoatTicketOfficeFieldProvider.this.addBoat(link, currentBoatId, player, boat);
                return true;
            }
        });
        return true;
    }
    
    private void addBoat(final BoatLink link, final long currentBoatId, final LocalPlayerCharacter player, final TravelMachine boat) {
        if (link.getStartBoatId() != currentBoatId && link.getEndBoatId() != currentBoatId) {
            return;
        }
        final int endBoatId = (link.getStartBoatId() == currentBoatId) ? link.getEndBoatId() : link.getStartBoatId();
        if (!link.isCriterionDisplayValid(player, boat)) {
            return;
        }
        final TByteHashSet disableReasons = new TByteHashSet();
        boolean enabled = true;
        if (!link.isCriterionValid(player, boat)) {
            enabled = false;
            disableReasons.add((byte)1);
        }
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        if (this.getCost(link) > 0 && !localPlayer.hasSubscriptionRight(SubscriptionRight.USE_BOAT)) {
            enabled = false;
            disableReasons.add((byte)2);
        }
        final BoatInfo boatInfo = TravelInfoManager.INSTANCE.getInfo(TravelType.BOAT, endBoatId);
        if (boatInfo == null) {
            return;
        }
        final int exitWorldId = boatInfo.getExitWorldId();
        final InstanceInteractionLevel interactionLevel = InstanceInteractionLevelManager.INSTANCE.getInstanceInteractionLevel(exitWorldId, player.getAccountInformationHandler().getActiveSubscriptionLevel());
        if (interactionLevel == InstanceInteractionLevel.FORBIDDEN_ACCESS) {
            enabled = false;
            disableReasons.add((byte)3);
        }
        final TIntHashSet forbiddenInstances = SystemConfiguration.INSTANCE.getIntHashSet(SystemConfigurationType.WORLD_INSTANCES_FORBIDDEN);
        if (forbiddenInstances.contains(exitWorldId)) {
            return;
        }
        BoatIslandFieldProvider boatIsland = this.m_boatsByIsland.get(exitWorldId);
        if (boatIsland == null) {
            boatIsland = new BoatIslandFieldProvider(exitWorldId);
            this.m_boatsByIsland.put(exitWorldId, boatIsland);
        }
        boatIsland.addBoat(new BoatInfoFieldProvider(link, this.m_currentBoatId, enabled, disableReasons));
    }
    
    public int getCost(final BoatLink boatLink) {
        final TravelHandler handler = WakfuGameEntity.getInstance().getLocalPlayer().getTravelHandler();
        return TravelHelper.needsToPayForBoat(handler, boatLink) ? boatLink.getCost() : 0;
    }
    
    @Override
    public String[] getFields() {
        return null;
    }
    
    @Override
    public Object getFieldValue(final String fieldName) {
        if (fieldName.equals("linksByIsland")) {
            if (this.m_boatsByIsland.size() == 0) {
                return null;
            }
            return this.m_boatsByIsland;
        }
        else {
            if (fieldName.equals("currentName")) {
                return this.getBoatInstanceName();
            }
            if (fieldName.equals("iconUrl")) {
                return WakfuConfiguration.getInstance().getIconUrl("zaapTypeIconPath", "defaultIconPath", (this.m_uiGfxId == -1) ? TravelGfxType.BOAT.getGfxId() : this.m_uiGfxId);
            }
            return null;
        }
    }
    
    private String getBoatInstanceName() {
        return WakfuTranslator.getInstance().getString(83, (int)this.m_currentBoatId, new Object[0]);
    }
    
    public String getDescription() {
        final TextWidgetFormater sb = new TextWidgetFormater();
        if (this.m_boatsByIsland.size() != 0) {
            boolean first = true;
            for (final BoatIslandFieldProvider boatsByIsland : this.m_boatsByIsland) {
                final ArrayList<BoatInfoFieldProvider> boatList = boatsByIsland.getBoatList();
                final String name = boatsByIsland.getIslandName();
                for (int i = 0, size = boatList.size(); i < size; ++i) {
                    if (first) {
                        first = false;
                    }
                    else {
                        sb.newLine();
                    }
                    final BoatInfoFieldProvider boat = boatList.get(i);
                    sb.append(boat.getName()).append(" (").append(name).append(")");
                }
            }
        }
        else {
            sb.append(WakfuTranslator.getInstance().getString("boat.noDestinationsAvailable"));
        }
        return sb.finishAndToString();
    }
    
    static {
        m_logger = Logger.getLogger((Class)BoatTicketOfficeFieldProvider.class);
    }
}
