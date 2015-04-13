package com.ankamagames.wakfu.client.core.game.travel;

import com.ankamagames.wakfu.client.ui.component.*;
import com.ankamagames.wakfu.common.game.travel.infos.*;
import gnu.trove.*;
import com.ankamagames.framework.text.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.common.game.descriptionGenerator.*;
import com.ankamagames.framework.graphics.image.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.wakfu.common.game.travel.*;
import com.ankamagames.wakfu.common.game.travel.character.*;

public class BoatInfoFieldProvider extends ImmutableFieldProvider implements TravelInfo
{
    public static final String NAME_FIELD = "name";
    public static final String PRICE_FIELD = "price";
    public static final String IS_ACTIVATED_FIELD = "isActivated";
    public static final String IS_ENABLED_FIELD = "isEnabled";
    public static final String ERROR_TEXT_FIELD = "errorText";
    public static final String[] FIELDS;
    public static final byte NO_DISABLE_REASON = 0;
    public static final byte INVALID_CRITERION = 1;
    public static final byte NOT_USE_BOAT_RIGHT = 2;
    public static final byte NO_HIGH_LEVEL_INSTANCE_ACCESS = 3;
    private final long m_boatFrom;
    private final BoatLink m_boatInfo;
    private final boolean m_enabled;
    private final TByteHashSet m_disableReason;
    
    public BoatInfoFieldProvider(final BoatLink boatInfo, final long boatFrom, final boolean enabled, final TByteHashSet disableReasons) {
        super();
        this.m_boatInfo = boatInfo;
        this.m_boatFrom = boatFrom;
        this.m_enabled = enabled;
        this.m_disableReason = disableReasons;
    }
    
    @Override
    public String[] getFields() {
        return BoatInfoFieldProvider.FIELDS;
    }
    
    @Override
    public Object getFieldValue(final String fieldName) {
        if (fieldName.equals("name")) {
            return this.getName();
        }
        if (fieldName.equals("price")) {
            return this.getCost();
        }
        if (fieldName.equals("isEnabled")) {
            return this.m_enabled;
        }
        if (fieldName.equals("isActivated")) {
            return this.isActive();
        }
        if (fieldName.equals("errorText")) {
            TextWidgetFormater twf = new TextWidgetFormater();
            final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
            if (this.m_disableReason.contains((byte)2)) {
                if (twf.length() == 0) {
                    twf.newLine();
                }
                twf.append(WakfuTranslator.getInstance().getString("error.playerNotUseBoatRight"));
            }
            if (this.m_disableReason.contains((byte)3)) {
                if (twf.length() > 0) {
                    twf.newLine();
                }
                twf.append(WakfuTranslator.getInstance().getString("error.playerNotInstanceAccessRight"));
            }
            final SimpleCriterion criterion = this.m_boatInfo.getCriterion();
            if (this.m_disableReason.contains((byte)1)) {
                if (twf.length() > 0) {
                    twf.newLine();
                }
                twf.append(CriterionDescriptionGenerator.getDescription(criterion));
            }
            if (this.getCost() > localPlayer.getKamasCount()) {
                if (twf.length() > 0) {
                    twf.newLine();
                }
                twf.append(WakfuTranslator.getInstance().getString("error.notEnoughKamas"));
            }
            if (twf.length() > 0) {
                final String toAppend = twf.finishAndToString();
                twf = new TextWidgetFormater();
                twf.openText().addColor(Color.RED.getRGBtoHex());
                twf.append(toAppend);
                twf.closeText();
            }
            return (twf.length() == 0) ? null : twf.finishAndToString();
        }
        return null;
    }
    
    @Override
    public long getId() {
        return (this.m_boatInfo.getEndBoatId() == this.m_boatFrom) ? this.m_boatInfo.getStartBoatId() : ((long)this.m_boatInfo.getEndBoatId());
    }
    
    public String getName() {
        return WakfuTranslator.getInstance().getString(83, (int)this.getId(), new Object[0]);
    }
    
    public boolean isActive() {
        return true;
    }
    
    @Override
    public int getCost() {
        final TravelHandler handler = WakfuGameEntity.getInstance().getLocalPlayer().getTravelHandler();
        return TravelHelper.needsToPayForBoat(handler, this.m_boatInfo) ? this.m_boatInfo.getCost() : 0;
    }
    
    static {
        FIELDS = new String[] { "name", "price" };
    }
}
