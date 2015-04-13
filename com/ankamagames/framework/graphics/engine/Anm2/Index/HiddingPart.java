package com.ankamagames.framework.graphics.engine.Anm2.Index;

public final class HiddingPart
{
    public final int crcKey;
    public final int crcToHide;
    
    public HiddingPart(final int crcKey, final int crcToHide) {
        super();
        this.crcKey = crcKey;
        this.crcToHide = crcToHide;
    }
    
    @Override
    public int hashCode() {
        return 31 * this.crcKey + this.crcToHide;
    }
    
    @Override
    public boolean equals(final Object obj) {
        if (!(obj instanceof HiddingPart)) {
            return false;
        }
        final HiddingPart hp = (HiddingPart)obj;
        return this.crcKey == hp.crcKey && this.crcToHide == hp.crcToHide;
    }
}
