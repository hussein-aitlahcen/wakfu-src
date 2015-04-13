package com.ankamagames.wakfu.client.core.game.soap.shop;

import java.util.*;
import com.ankamagames.wakfu.client.core.game.webShop.*;

public interface HomeListener
{
    void onHome(ArrayList<Category> p0, ArrayList<Highlight> p1, ArrayList<Highlight> p2, ArrayList<GondolaHead> p3, ArrayList<Article> p4);
    
    void onError();
}
