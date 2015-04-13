package com.ankamagames.wakfu.client.core.game.soap.shop;

import org.apache.log4j.*;
import com.ankamagames.framework.net.soap.data.*;
import java.util.*;
import com.ankamagames.wakfu.client.core.game.webShop.*;
import com.ankamagames.framework.net.soap.*;

public class HomeAnswer implements SOAPAnswerProcessor<Home>
{
    private static final Logger m_logger;
    private static final String HOME_RESPONSE = "HomeResponse";
    private static final String RESULT = "result";
    private static final String CATEGORIES = "categories";
    
    @Override
    public void process(final SOAPEnvelope envelope, final Home provider) {
        final SOAPElement getEventResponse = envelope.getBody().getElement("HomeResponse");
        final MapData data = (MapData)getEventResponse.getFirstData();
        if (!data.getValue("result").getBooleanValue()) {
            provider.getListener().onError();
            return;
        }
        try {
            final MapData content = (MapData)data.getValue("content");
            final ArrayList<Category> categoryList = ShopHelper.createCategoryList((MapData)content.getValue("categories"));
            final ArrayList<GondolaHead> gondolaMain = ShopHelper.createGondolaHeads((MapData)content.getValue("gondolahead_main"));
            final ArrayList<Article> gondolaArticles = ShopHelper.createArticlesList(content.getValue("gondolahead_article"));
            final ArrayList<Highlight> carrousel = ShopHelper.createHighlightsList(content.getValue("hightlight_carousel"));
            final ArrayList<Highlight> image = ShopHelper.createHighlightsList(content.getValue("hightlight_image"));
            provider.getListener().onHome(categoryList, carrousel, image, gondolaMain, gondolaArticles);
        }
        catch (Exception e) {
            HomeAnswer.m_logger.warn((Object)"Probl\u00e8me \u00e0 la d\u00e9s\u00e9rialisation des donn\u00e9es de home", (Throwable)e);
        }
    }
    
    @Override
    public void onError(final Home provider) {
        provider.getListener().onError();
    }
    
    static {
        m_logger = Logger.getLogger((Class)HomeAnswer.class);
    }
}
