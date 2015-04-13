package com.ankamagames.wakfu.client.core.game.gift;

public class ConsumeRequest
{
    private GiftPackage m_package;
    private GiftItem m_gift;
    private boolean m_all;
    
    public ConsumeRequest(final GiftPackage aPackage, final GiftItem gift, final boolean all) {
        super();
        this.m_package = aPackage;
        this.m_gift = gift;
        this.m_all = all;
    }
    
    public GiftPackage getPackage() {
        return this.m_package;
    }
    
    public GiftItem getGift() {
        return this.m_gift;
    }
    
    public boolean isAll() {
        return this.m_all;
    }
}
