package com.ankamagames.wakfu.common.game.craftNew;

import com.ankamagames.baseImpl.common.clientAndServer.game.time.calendar.*;

public class CraftContractComment
{
    private final long m_contractId;
    private final GameDateConst m_date;
    private final long m_author;
    private final String m_comment;
    
    public CraftContractComment(final long contractId, final GameDateConst date, final long author, final String comment) {
        super();
        this.m_contractId = contractId;
        this.m_date = date;
        this.m_author = author;
        this.m_comment = comment;
    }
    
    public long getContractId() {
        return this.m_contractId;
    }
    
    public GameDateConst getDate() {
        return this.m_date;
    }
    
    public long getAuthor() {
        return this.m_author;
    }
    
    public String getComment() {
        return this.m_comment;
    }
}
