package com.ankamagames.wakfu.client.core.monitoring.statistics;

import java.util.*;

public class DateElementView extends SimpleElementView
{
    private static final String[] m_days;
    
    public DateElementView(final String name, final String key, final ColorController colorController) {
        super(name, key, colorController);
    }
    
    public DateElementView(final String name, final String key) {
        super(name, key);
    }
    
    @Override
    public String getStringValue(final boolean formatted) {
        if (!(this.m_value instanceof Long)) {
            return super.getStringValue(formatted);
        }
        if ((long)this.m_value <= 0L) {
            return "-";
        }
        final Date date = new Date((long)this.m_value);
        final String jour = DateElementView.m_days[date.getDay()] + " " + date.getDate() + "/" + (date.getMonth() + 1) + " " + (date.getYear() + 1900);
        final String heures = ((date.getHours() < 10) ? ("0" + date.getHours()) : date.getHours()) + ":" + ((date.getMinutes() < 10) ? ("0" + date.getMinutes()) : date.getMinutes());
        return formatted ? this.format(jour + " \u00e0 " + heures) : (jour + " \u00e0 " + heures);
    }
    
    static {
        m_days = new String[] { "Dim.", "Lun.", "Mar.", "Mer.", "Jeu.", "Ven.", "Sam." };
    }
}
