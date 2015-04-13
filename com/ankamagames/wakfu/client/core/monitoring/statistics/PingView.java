package com.ankamagames.wakfu.client.core.monitoring.statistics;

import com.ankamagames.wakfu.client.network.protocol.frame.*;
import java.util.*;

public class PingView extends SimpleElementView
{
    private final byte m_server;
    protected String m_tooltip;
    
    public PingView(final String name, final byte server) {
        super(name, null, new ColorController() {
            @Override
            public String getColorFromValue(final Object value) {
                final float f = Float.parseFloat((String)value);
                if (f > 800.0f || f <= 0.0) {
                    return "ff0000";
                }
                if (f < 250.0f) {
                    return "00ff00";
                }
                return "ff9922";
            }
        });
        this.m_tooltip = null;
        this.m_server = server;
    }
    
    @Override
    public void update() {
        final double rtt = NetSystemNotificationsAndPingFrame.getInstance().getLastPingRtt(this.m_server);
        final double processing = NetSystemNotificationsAndPingFrame.getInstance().getLastPingProcessing(this.m_server);
        this.m_value = String.format("%1.1f", rtt);
        this.m_tooltip = String.format("Total: %1.1f ms\nProcessing: %1.1f ms", rtt, processing);
        for (final ElementView elem : this.m_linkedElements) {
            elem.update();
        }
        super.update();
    }
    
    @Override
    protected String getTooltipText() {
        if (this.m_linkedElements == null) {
            return this.m_tooltip;
        }
        final StringBuilder sb = new StringBuilder();
        if (this.m_tooltip != null) {
            sb.append(this.m_tooltip);
        }
        for (final SimpleElementView view : this.m_linkedElements) {
            sb.append("\n");
            sb.append(view.getName()).append(" : ").append(view.getStringValue(false));
        }
        return sb.toString();
    }
}
