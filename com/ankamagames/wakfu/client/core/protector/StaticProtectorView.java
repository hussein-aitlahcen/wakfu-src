package com.ankamagames.wakfu.client.core.protector;

import gnu.trove.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.wakfu.common.game.protector.event.*;
import com.ankamagames.wakfu.client.core.game.protector.event.*;
import com.ankamagames.wakfu.client.core.game.protector.snapshot.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.framework.text.*;
import com.ankamagames.wakfu.client.core.game.protector.*;

public class StaticProtectorView extends ProtectorView
{
    public static final StaticProtectorView INSTANCE;
    private int m_protectorId;
    private final TIntObjectHashMap<Interval> m_protectorIntervals;
    
    public StaticProtectorView() {
        super();
        this.m_protectorId = -1;
        this.m_protectorIntervals = new TIntObjectHashMap<Interval>();
    }
    
    public void setProtectorId(final int protectorId) {
        if (protectorId == this.m_protectorId) {
            return;
        }
        this.clean();
        if (protectorId == -1) {
            PropertiesProvider.getInstance().setPropertyValue("protector", null);
            return;
        }
        this.m_protectorId = protectorId;
        StaticProtectorView.INSTANCE.initialize();
        PropertiesProvider.getInstance().setPropertyValue("protector", StaticProtectorView.INSTANCE);
    }
    
    @Override
    public void clean() {
        super.clean();
        this.m_protectorId = -1;
        this.m_protectorIntervals.clear();
        ProtectorEventDispatcher.INSTANCE.removeListener(this);
    }
    
    @Override
    public void onProtectorEvent(final ProtectorEvent e) {
    }
    
    @Override
    public Object getFieldValue(final String fieldName) {
        if (fieldName.equals("animation")) {
            return this.getAnimation(this.m_protectorId, ProtectorMood.NEUTRAL);
        }
        if (fieldName.equals("name")) {
            return WakfuTranslator.getInstance().getString(String.format("staticProtector.%d", this.getProtectorId()));
        }
        if (fieldName.equals("description")) {
            final PlainTextWidgetFormater sb = new PlainTextWidgetFormater();
            return sb.openText().addColor(TerritoryViewConstants.ENNEMY.getRGBtoHex()).append(WakfuTranslator.getInstance().getString("protector.noBuff")).closeText();
        }
        return super.getFieldValue(fieldName);
    }
    
    @Override
    public int getProtectorId() {
        return this.m_protectorId;
    }
    
    @Override
    public boolean isStaticProtector() {
        return true;
    }
    
    public void addProtectorInterval(final int familyId, final Interval interval) {
        this.m_protectorIntervals.put(familyId, interval);
    }
    
    public Interval getProtectorInterval(final int familyId) {
        return this.m_protectorIntervals.get(familyId);
    }
    
    @Override
    public Territory getTerritory() {
        return null;
    }
    
    static {
        INSTANCE = new StaticProtectorView();
    }
}
