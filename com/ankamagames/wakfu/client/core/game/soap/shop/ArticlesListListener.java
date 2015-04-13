package com.ankamagames.wakfu.client.core.game.soap.shop;

import java.util.*;
import com.ankamagames.wakfu.client.core.game.webShop.*;

public interface ArticlesListListener
{
    void onArticlesList(ArrayList<Article> p0, int p1);
    
    void onError();
}
