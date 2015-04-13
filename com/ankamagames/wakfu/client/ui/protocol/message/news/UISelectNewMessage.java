package com.ankamagames.wakfu.client.ui.protocol.message.news;

import com.ankamagames.wakfu.client.ui.protocol.message.*;
import com.ankamagames.wakfu.client.core.news.*;

public class UISelectNewMessage extends UIMessage
{
    private NewsItemView m_newsItemView;
    
    public UISelectNewMessage(final NewsItemView newsItemView) {
        super();
        this.m_newsItemView = newsItemView;
    }
    
    public NewsItemView getNewsItemView() {
        return this.m_newsItemView;
    }
    
    @Override
    public int getId() {
        return 16112;
    }
}
