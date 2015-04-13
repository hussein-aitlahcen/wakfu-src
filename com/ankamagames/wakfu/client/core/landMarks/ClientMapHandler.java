package com.ankamagames.wakfu.client.core.landMarks;

import com.ankamagames.wakfu.common.game.map.*;
import java.util.*;
import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.wakfu.client.ui.component.*;
import com.ankamagames.wakfu.client.core.*;

public class ClientMapHandler extends MapHandler
{
    private final ArrayList<LandMarkGfx> m_gfxs;
    private LandMarkGfx m_compassGfx;
    
    public ClientMapHandler() {
        super();
        this.m_gfxs = new ArrayList<LandMarkGfx>();
        this.m_compassGfx = new LandMarkGfx(34);
    }
    
    @Override
    public boolean learnLandMark(final byte landMarkId) {
        if (super.learnLandMark(landMarkId)) {
            this.m_gfxs.add(new LandMarkGfx(landMarkId));
            Collections.sort(this.m_gfxs);
            return true;
        }
        return false;
    }
    
    public ArrayList<LandMarkGfx> getGfxs() {
        return this.m_gfxs;
    }
    
    public LandMarkGfx getCompassGfx() {
        return this.m_compassGfx;
    }
    
    @Override
    public void clear() {
        super.clear();
        this.m_gfxs.clear();
    }
    
    @Override
    public void toRaw(final CharacterSerializedLandMarkInventory part) {
        throw new UnsupportedOperationException("Pas de serialisation de landmarks dans le client");
    }
    
    public static class LandMarkGfx extends ImmutableFieldProvider implements Comparable<LandMarkGfx>
    {
        public static final String ICON_URL = "iconUrl";
        private int m_gfxId;
        
        public LandMarkGfx(final int gfxId) {
            super();
            this.m_gfxId = gfxId;
        }
        
        public int getGfxId() {
            return this.m_gfxId;
        }
        
        public String getIconUrl() {
            return WakfuConfiguration.getInstance().getIconUrl("pointsOfInterestIconPath", "defaultIconPath", this.m_gfxId);
        }
        
        @Override
        public String[] getFields() {
            return null;
        }
        
        @Override
        public Object getFieldValue(final String fieldName) {
            if (fieldName.equals("iconUrl")) {
                return this.getIconUrl();
            }
            return null;
        }
        
        @Override
        public int compareTo(final LandMarkGfx o) {
            return o.m_gfxId - this.m_gfxId;
        }
    }
}
