package com.ankamagames.wakfu.client.core.game.soap.shop;

import java.util.*;
import com.ankamagames.wakfu.client.core.game.webShop.*;

public interface ArticlesIdsListener
{
    void onArticlesIds(ArrayList<Article> p0);
    
    void onError();
}
