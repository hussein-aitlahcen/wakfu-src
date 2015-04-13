package com.ankamagames.framework.kernel.core.common.collections;

import java.util.*;

public class LRUCache<K, V> implements Iterable
{
    protected final LinkedHashMap<K, V> m_cache;
    private int m_cacheMaxSize;
    private int m_cacheMissCount;
    private int m_cacheHitCount;
    
    public LRUCache(final int cacheMaxSize) {
        super();
        this.m_cacheMissCount = 0;
        this.m_cacheHitCount = 1;
        this.m_cacheMaxSize = cacheMaxSize;
        this.m_cache = new LinkedHashMap<K, V>(cacheMaxSize, 0.75f, true) {
            @Override
            protected boolean removeEldestEntry(final Map.Entry<K, V> eldest) {
                return this.size() > LRUCache.this.m_cacheMaxSize;
            }
        };
    }
    
    public V get(final K key) {
        return this.m_cache.get(key);
    }
    
    public void put(final K key, final V value) {
        this.m_cache.put(key, value);
    }
    
    public V remove(final K key) {
        return this.m_cache.remove(key);
    }
    
    public void clear() {
        this.m_cache.clear();
    }
    
    public int getCacheMaxSize() {
        return this.m_cacheMaxSize;
    }
    
    public void setCacheMaxSize(final int cacheMaxSize) {
        this.m_cacheMaxSize = cacheMaxSize;
        this.m_cacheHitCount = 1;
        this.m_cacheMissCount = 0;
    }
    
    public int getCacheMissCount() {
        return this.m_cacheMissCount;
    }
    
    public int getCacheHitCount() {
        return this.m_cacheHitCount;
    }
    
    public int getCacheUsage() {
        return this.m_cache.size() * 100 / this.m_cacheMaxSize;
    }
    
    public int size() {
        return this.m_cache.size();
    }
    
    @Override
    public Iterator iterator() {
        return this.m_cache.values().iterator();
    }
}
