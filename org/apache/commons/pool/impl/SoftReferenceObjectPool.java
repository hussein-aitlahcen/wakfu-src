package org.apache.commons.pool.impl;

import org.apache.commons.pool.*;
import java.lang.ref.*;
import java.util.*;

public class SoftReferenceObjectPool extends BaseObjectPool implements ObjectPool
{
    private List _pool;
    private PoolableObjectFactory _factory;
    private int _numActive;
    
    public SoftReferenceObjectPool() {
        super();
        this._pool = null;
        this._factory = null;
        this._numActive = 0;
        this._pool = new ArrayList();
        this._factory = null;
    }
    
    public SoftReferenceObjectPool(final PoolableObjectFactory factory) {
        super();
        this._pool = null;
        this._factory = null;
        this._numActive = 0;
        this._pool = new ArrayList();
        this._factory = factory;
    }
    
    public SoftReferenceObjectPool(final PoolableObjectFactory factory, final int initSize) throws Exception {
        super();
        this._pool = null;
        this._factory = null;
        this._numActive = 0;
        this._pool = new ArrayList();
        this._factory = factory;
        if (null != this._factory) {
            for (int i = 0; i < initSize; ++i) {
                final Object obj = this._factory.makeObject();
                this._factory.passivateObject(obj);
                this._pool.add(new SoftReference<Object>(obj));
            }
        }
    }
    
    public synchronized Object borrowObject() throws Exception {
        this.assertOpen();
        Object obj;
        for (obj = null; null == obj; obj = null) {
            if (this._pool.isEmpty()) {
                if (null == this._factory) {
                    throw new NoSuchElementException();
                }
                obj = this._factory.makeObject();
            }
            else {
                final SoftReference ref = this._pool.remove(this._pool.size() - 1);
                obj = ref.get();
            }
            if (null != this._factory && null != obj) {
                this._factory.activateObject(obj);
            }
            if (null != this._factory && null != obj && !this._factory.validateObject(obj)) {
                this._factory.destroyObject(obj);
            }
        }
        ++this._numActive;
        return obj;
    }
    
    public synchronized void returnObject(final Object obj) throws Exception {
        this.assertOpen();
        boolean success = true;
        if (!this._factory.validateObject(obj)) {
            success = false;
        }
        else {
            try {
                this._factory.passivateObject(obj);
            }
            catch (Exception e) {
                success = false;
            }
        }
        final boolean shouldDestroy = !success;
        --this._numActive;
        if (success) {
            this._pool.add(new SoftReference<Object>(obj));
        }
        this.notifyAll();
        if (shouldDestroy) {
            try {
                this._factory.destroyObject(obj);
            }
            catch (Exception ex) {}
        }
    }
    
    public synchronized void invalidateObject(final Object obj) throws Exception {
        this.assertOpen();
        --this._numActive;
        this._factory.destroyObject(obj);
        this.notifyAll();
    }
    
    public synchronized void addObject() throws Exception {
        this.assertOpen();
        final Object obj = this._factory.makeObject();
        ++this._numActive;
        this.returnObject(obj);
    }
    
    public synchronized int getNumIdle() {
        this.assertOpen();
        return this._pool.size();
    }
    
    public synchronized int getNumActive() {
        this.assertOpen();
        return this._numActive;
    }
    
    public synchronized void clear() {
        this.assertOpen();
        if (null != this._factory) {
            final Iterator iter = this._pool.iterator();
            while (iter.hasNext()) {
                try {
                    final Object obj = iter.next().get();
                    if (null == obj) {
                        continue;
                    }
                    this._factory.destroyObject(obj);
                }
                catch (Exception e) {}
            }
        }
        this._pool.clear();
    }
    
    public synchronized void close() throws Exception {
        this.clear();
        this._pool = null;
        this._factory = null;
        super.close();
    }
    
    public synchronized void setFactory(final PoolableObjectFactory factory) throws IllegalStateException {
        this.assertOpen();
        if (0 < this.getNumActive()) {
            throw new IllegalStateException("Objects are already active");
        }
        this.clear();
        this._factory = factory;
    }
}
