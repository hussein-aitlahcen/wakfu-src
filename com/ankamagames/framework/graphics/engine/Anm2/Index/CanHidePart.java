package com.ankamagames.framework.graphics.engine.Anm2.Index;

public final class CanHidePart
{
    public final String itemName;
    public final int crcKey;
    
    public CanHidePart(final String itemName, final int crcKey) {
        super();
        this.itemName = itemName;
        this.crcKey = crcKey;
    }
    
    @Override
    public int hashCode() {
        return 31 * this.itemName.hashCode() + this.crcKey;
    }
    
    @Override
    public boolean equals(final Object obj) {
        if (!(obj instanceof CanHidePart)) {
            return false;
        }
        final CanHidePart hp = (CanHidePart)obj;
        return this.crcKey == hp.crcKey && this.itemName.equals(hp.itemName);
    }
}
