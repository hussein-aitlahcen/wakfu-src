package com.ankamagames.wakfu.client.core.game.soap.shop;

import java.util.*;
import com.ankamagames.wakfu.client.core.game.webShop.*;

public interface ArticlesSearchListener
{
    void onArticlesSearch(ArrayList<Article> p0, int p1);
    
    void onError();
}
