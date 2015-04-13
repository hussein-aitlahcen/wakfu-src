package com.ankamagames.wakfu.client.core.game.soap.shop;

import com.ankamagames.wakfu.client.core.game.webShop.*;
import gnu.trove.*;
import java.util.*;

public class ArticlesCache
{
    public static final ArticlesCache INSTANCE;
    private final Map<String, Article> m_articlesByKey;
    
    private ArticlesCache() {
        super();
        this.m_articlesByKey = new THashMap<String, Article>();
    }
    
    public void loadArticleByKey(final String key, final ArticlesKeyListener l) {
        final Article article = this.m_articlesByKey.get(key);
        if (article != null) {
            l.onArticlesKey(Collections.singletonList(article));
        }
        else {
            ArticlesKeyLoader.INSTANCE.getArticlesKey(key, new ArticlesKeyListener() {
                @Override
                public void onArticlesKey(final List<Article> articles) {
                    if (articles.isEmpty()) {
                        l.onArticlesKey(articles);
                    }
                    else {
                        final Article article = articles.get(0);
                        ArticlesCache.this.m_articlesByKey.put(key, article);
                        l.onArticlesKey(Collections.singletonList(article));
                    }
                }
                
                @Override
                public void onError() {
                    l.onError();
                }
            });
        }
    }
    
    public void remove(final String key) {
        this.m_articlesByKey.remove(key);
    }
    
    public void clear() {
        this.m_articlesByKey.clear();
    }
    
    static {
        INSTANCE = new ArticlesCache();
    }
}
