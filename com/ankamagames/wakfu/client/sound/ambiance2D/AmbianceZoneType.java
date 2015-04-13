package com.ankamagames.wakfu.client.sound.ambiance2D;

import com.ankamagames.framework.external.*;

public enum AmbianceZoneType implements ExportableEnum
{
    NONE((byte)(-1), "Aucun"), 
    PLAIN((byte)0, "Plaine"), 
    PLAIN_DARK((byte)25, "Plaine sombre"), 
    PLAIN_CHUCHOTEUR((byte)39, "Plaine Chuchoteur"), 
    MEADOW((byte)1, "Prairie"), 
    MEADOW_DARK((byte)23, "Prairie sombre"), 
    FOREST((byte)2, "For\u00eat"), 
    FOREST_DARK((byte)9, "Foret sombre"), 
    DESERT_COLD((byte)3, "D\u00e9sert froid"), 
    DESERT_HOT((byte)20, "D\u00e9sert chaud"), 
    DESERT_WIND((byte)50, "D\u00e9sert venteux"), 
    MOUNTAIN((byte)4, "Montagne"), 
    CAVE((byte)6, "Grotte", true), 
    CRYPT((byte)7, "Crypte"), 
    CEMETERY((byte)8, "Cimeti\u00e8re", true), 
    JAIL((byte)10, "Prison"), 
    JAIL_EXT((byte)41, "Prison exterieur"), 
    JAIL_ALERT((byte)49, "Prison alarm"), 
    VILLAGE((byte)11, "Village"), 
    VILLAGE_DARK((byte)27, "Village sombre"), 
    VILLAGE_CHUCHOTEUR((byte)38, "Village Chuchoteur"), 
    FALLS_SMALL((byte)15, "Petites chutes d'eau"), 
    FALLS_LARGE((byte)12, "Grandes chutes d'eau"), 
    SWAMP((byte)13, "Marais"), 
    UNDERGROUNDS((byte)14, "Souterrains"), 
    UNDERGROUNDS_CHUCHOTEUR((byte)40, "Souterrain chuchoteur"), 
    UNDERGROUNDS_FRIGOST((byte)45, "Souterrain Frigost"), 
    UNDERGROUNDS_LAB((byte)48, "Souterrain Laboratoire"), 
    VOLCANO((byte)16, "Volcan"), 
    VOLCANO_EXT((byte)24, "Volcan Ext"), 
    BEACH((byte)18, "Plage"), 
    JUNGLE((byte)19, "Jungle"), 
    ISLAND((byte)29, "Ile"), 
    POUPISLAND((byte)42, "Poup Island"), 
    SHUKROUTE((byte)21, "Shukroute"), 
    PORT((byte)22, "Port"), 
    INCARNAM((byte)5, "Incarnam"), 
    AMAKNA_WHISTLE_ROCKS((byte)17, "Rocher sifflant Amakna"), 
    DARK_HOLE((byte)26, "Troue sombre"), 
    CHAOS((byte)28, "Chaos Ogrest"), 
    BOAT((byte)30, "Bateau"), 
    BOAT_BADWEATHER((byte)35, "Bateau - mauvais temps"), 
    BOAT_STORM((byte)31, "Bateau - tempete"), 
    CAPUCHE_SHIP((byte)43, "Donjon Capuche"), 
    ZENITH((byte)32, "Zenith"), 
    ZENITH_BOTTOM((byte)33, "Mont Zenith bas"), 
    ZENITH_TOP((byte)34, "Mont Zenith haut"), 
    MEDIEVAl_CHAOS((byte)36, "Medieval chaos"), 
    KATREPATE((byte)37, "Katrepate"), 
    FRIGOST((byte)44, "Frigost"), 
    ZEPPLIN((byte)46, "Zepplin"), 
    GELAX_DIMENSION((byte)47, "Dimension Gelax");
    
    private byte m_id;
    private String m_label;
    private boolean m_hasMessageBanner;
    
    private AmbianceZoneType(final byte id, final String label) {
        this.m_hasMessageBanner = false;
        this.m_id = id;
        this.m_label = label;
    }
    
    private AmbianceZoneType(final byte id, final String label, final boolean hasMessageBanner) {
        this.m_hasMessageBanner = false;
        this.m_id = id;
        this.m_label = label;
        this.m_hasMessageBanner = hasMessageBanner;
    }
    
    public byte getId() {
        return this.m_id;
    }
    
    public boolean hasMessageBanner() {
        return this.m_hasMessageBanner;
    }
    
    @Override
    public String getEnumId() {
        return String.valueOf(this.m_id);
    }
    
    @Override
    public String getEnumLabel() {
        return this.m_label;
    }
    
    @Override
    public String getEnumComment() {
        return null;
    }
    
    public static AmbianceZoneType getFromId(final byte id) {
        for (final AmbianceZoneType type : values()) {
            if (type.m_id == id) {
                return type;
            }
        }
        return null;
    }
    
    public static AmbianceZoneType[] realValues() {
        final AmbianceZoneType[] types = values();
        final AmbianceZoneType[] ret = new AmbianceZoneType[types.length - 1];
        int i = 0;
        for (final AmbianceZoneType type : types) {
            if (type != AmbianceZoneType.NONE) {
                ret[i++] = type;
            }
        }
        return ret;
    }
    
    @Override
    public String toString() {
        return this.m_label;
    }
}
