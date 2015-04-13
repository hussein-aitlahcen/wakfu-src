package com.ankamagames.wakfu.client.core.game.challenge;

import org.apache.log4j.*;

public class ChallengeRewardModel implements Comparable<ChallengeRewardModel>
{
    private static final Logger m_logger;
    private final int m_id;
    private final int m_gfxId;
    private final int m_itemId;
    private final short m_quantity;
    private final int m_xp;
    private final int m_kama;
    private String m_iconUrl;
    private final String m_criterionCommentary;
    private final boolean m_isSuccess;
    private final byte m_order;
    
    public ChallengeRewardModel(final int id, final int gfxId, final int itemId, final short quantity, final int xp, final int kama, final String criterionCommentary, final boolean isSuccess, final byte order) {
        super();
        this.m_id = id;
        this.m_gfxId = gfxId;
        this.m_itemId = itemId;
        this.m_quantity = quantity;
        this.m_xp = xp;
        this.m_kama = kama;
        this.m_criterionCommentary = criterionCommentary;
        this.m_isSuccess = isSuccess;
        this.m_order = order;
    }
    
    public int getKama() {
        return this.m_kama;
    }
    
    public int getId() {
        return this.m_id;
    }
    
    public int getItemId() {
        return this.m_itemId;
    }
    
    public short getQuantity() {
        return this.m_quantity;
    }
    
    public boolean isSuccess() {
        return this.m_isSuccess;
    }
    
    public byte getOrder() {
        return this.m_order;
    }
    
    @Override
    public int compareTo(final ChallengeRewardModel o) {
        return this.m_order - o.m_order;
    }
    
    public int getXp() {
        return this.m_xp;
    }
    
    static {
        m_logger = Logger.getLogger((Class)ChallengeRewardModel.class);
    }
}
