package com.ankamagames.wakfu.client.ui.protocol.message.webShop;

import com.ankamagames.wakfu.client.ui.protocol.message.*;
import com.ankamagames.wakfu.client.core.game.webShop.*;

public class UIArticleMessage extends UIMessage
{
    private Article m_article;
    
    public UIArticleMessage(final short id, final Article article) {
        super(id);
        this.m_article = article;
    }
    
    public Article getArticle() {
        return this.m_article;
    }
}
