package com.ankamagames.wakfu.client.core.nation;

public enum GovernorHonorificTitle
{
    WISE((short)0, "governorTitle.wise"), 
    DESPOT((short)1, "governorTitle.despot"), 
    NEGOCIATOR((short)2, "governorTitle.negociator"), 
    OPPRESSOR((short)3, "governorTitle.oppressor"), 
    STRATEGIST((short)4, "governorTitle.strategist"), 
    MACHIAVELLIAN((short)5, "governorTitle.machiavellian"), 
    DIPLOMAT((short)6, "governorTitle.diplomat"), 
    BULLY((short)7, "governorTitle.bully"), 
    PROTECTOR((short)8, "governorTitle.protector"), 
    WARLIKE((short)9, "governorTitle.warlike"), 
    CAREFUL((short)10, "governorTitle.careful"), 
    RECKLESS((short)11, "governorTitle.reckless"), 
    BENEFACTOR((short)12, "governorTitle.benefactor"), 
    CRIMINAL((short)13, "governorTitle.criminal"), 
    REFORMER((short)14, "governorTitle.reformer"), 
    CONSERVATIVE((short)15, "governorTitle.conservative");
    
    private short m_id;
    private String m_translatorKey;
    
    private GovernorHonorificTitle(final short id, final String translatorKey) {
        this.m_id = id;
        this.m_translatorKey = translatorKey;
    }
    
    public short getId() {
        return this.m_id;
    }
    
    public String getTranslatorKey() {
        return this.m_translatorKey;
    }
    
    public static GovernorHonorificTitle getHonorificTitleFromId(final short id) {
        for (final GovernorHonorificTitle honorificTitle : values()) {
            if (honorificTitle.getId() == id) {
                return honorificTitle;
            }
        }
        return null;
    }
}
