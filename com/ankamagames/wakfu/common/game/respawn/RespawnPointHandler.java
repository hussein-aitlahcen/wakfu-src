package com.ankamagames.wakfu.common.game.respawn;

import org.apache.log4j.*;
import com.ankamagames.wakfu.common.datas.*;
import gnu.trove.*;

public class RespawnPointHandler
{
    private static final Logger m_logger;
    public static final int NO_PHOENIX = -1;
    private final TIntHashSet m_knownPhoenixs;
    private int m_selectedPhoenix;
    
    public RespawnPointHandler() {
        super();
        this.m_knownPhoenixs = new TIntHashSet();
    }
    
    public boolean isDiscovered(final int phoenixId) {
        return this.m_knownPhoenixs.contains(phoenixId);
    }
    
    public boolean isSelected(final int phoenixId) {
        return phoenixId == this.m_selectedPhoenix;
    }
    
    public void selectPhoenix(final int phoenix) {
        this.m_knownPhoenixs.add(phoenix);
        this.m_selectedPhoenix = phoenix;
    }
    
    public void unSelectPhoenix() {
        this.m_selectedPhoenix = -1;
    }
    
    public int getSelectedPhoenix() {
        return this.m_selectedPhoenix;
    }
    
    public void clear() {
        this.m_knownPhoenixs.clear();
        this.m_selectedPhoenix = -1;
    }
    
    public void toRaw(final CharacterSerializedDiscoveredItemsInventory part) {
        part.selectedPhoenix = this.m_selectedPhoenix;
        final TIntIterator it = this.m_knownPhoenixs.iterator();
        while (it.hasNext()) {
            final CharacterSerializedDiscoveredItemsInventory.Phoenix phoenix = new CharacterSerializedDiscoveredItemsInventory.Phoenix();
            phoenix.phoenixId = it.next();
            part.phoenix.add(phoenix);
        }
    }
    
    public void fromRaw(final CharacterSerializedDiscoveredItemsInventory part) {
        this.m_selectedPhoenix = part.selectedPhoenix;
        for (int i = 0, size = part.phoenix.size(); i < size; ++i) {
            this.m_knownPhoenixs.add(part.phoenix.get(i).phoenixId);
        }
    }
    
    static {
        m_logger = Logger.getLogger((Class)RespawnPointHandler.class);
    }
}
