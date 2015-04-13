package com.ankamagames.wakfu.client.core.game.background;

import com.ankamagames.framework.external.*;

public enum BackgroundDisplayType implements ExportableEnum
{
    STONE((short)1, "St\u00e8le"), 
    SCROLL((short)2, "Parchemin"), 
    BOOK((short)3, "Livre", true, 20, 55), 
    PANEL((short)4, "Panneau"), 
    POSTER((short)5, "Affiche"), 
    EMPTY((short)6, "Vide"), 
    GAZETTE((short)7, "Gazette", true, 35, 45, 600148, 600149), 
    DOUBLE_SCROLL((short)8, "Parchemin style Gazette", true, 35, 45), 
    FRIGOST_STONE((short)9, "St\u00e8le frigost");
    
    private final short m_id;
    private final String m_label;
    private final boolean m_doublePage;
    private final int m_outerMargin;
    private final int m_innerMargin;
    private final int m_openSound;
    private final int m_closeSound;
    
    private BackgroundDisplayType(final short id, final String label, final boolean doublePage, final int innerMargin, final int outerMargin, final int openSound, final int closeSound) {
        this.m_id = id;
        this.m_doublePage = doublePage;
        this.m_label = label + (this.m_doublePage ? " (double)" : "");
        this.m_outerMargin = outerMargin;
        this.m_innerMargin = innerMargin;
        this.m_openSound = openSound;
        this.m_closeSound = closeSound;
    }
    
    private BackgroundDisplayType(final short id, final String label, final boolean doublePage, final int innerMargin, final int outerMargin) {
        this(id, label, doublePage, innerMargin, outerMargin, 600120, 600121);
    }
    
    private BackgroundDisplayType(final short id, final String label) {
        this(id, label, false, 25, 25);
    }
    
    public int getId() {
        return this.m_id;
    }
    
    public boolean isDoublePage() {
        return this.m_doublePage;
    }
    
    public int getOuterMargin() {
        return this.m_outerMargin;
    }
    
    public int getInnerMargin() {
        return this.m_innerMargin;
    }
    
    public int getOpenSound() {
        return this.m_openSound;
    }
    
    public int getCloseSound() {
        return this.m_closeSound;
    }
    
    @Override
    public String getEnumId() {
        return Integer.toString(this.m_id);
    }
    
    @Override
    public String getEnumLabel() {
        return this.m_label;
    }
    
    @Override
    public String getEnumComment() {
        return null;
    }
    
    public static BackgroundDisplayType getById(final short id) {
        for (final BackgroundDisplayType b : values()) {
            if (b.getId() == id) {
                return b;
            }
        }
        return null;
    }
}
