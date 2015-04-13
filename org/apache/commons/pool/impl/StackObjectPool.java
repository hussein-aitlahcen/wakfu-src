package org.apache.commons.pool.impl;

import org.apache.commons.pool.*;
import java.util.*;

public class StackObjectPool extends BaseObjectPool implements ObjectPool
{
    protected static final int DEFAULT_MAX_SLEEPING = 8;
    protected static final int DEFAULT_INIT_SLEEPING_CAPACITY = 4;
    protected Stack _pool;
    protected PoolableObjectFactory _factory;
    protected int _maxSleeping;
    protected int _numActive;
    
    public StackObjectPool() {
        this(null, 8, 4);
    }
    
    public StackObjectPool(final int maxIdle) {
        this(null, maxIdle, 4);
    }
    
    public StackObjectPool(final int maxIdle, final int initIdleCapacity) {
        this(null, maxIdle, initIdleCapacity);
    }
    
    public StackObjectPool(final PoolableObjectFactory factory) {
        this(factory, 8, 4);
    }
    
    public StackObjectPool(final PoolableObjectFactory factory, final int maxIdle) {
        this(factory, maxIdle, 4);
    }
    
    public StackObjectPool(final PoolableObjectFactory factory, final int maxIdle, final int initIdleCapacity) {
        super();
        this._pool = null;
        this._factory = null;
        this._maxSleeping = 8;
        this._numActive = 0;
        this._factory = factory;
        this._maxSleeping = ((maxIdle < 0) ? 8 : maxIdle);
        final int initcapacity = (initIdleCapacity < 1) ? 4 : initIdleCapacity;
        (this._pool = new Stack()).ensureCapacity((initcapacity > this._maxSleeping) ? this._maxSleeping : initcapacity);
    }
    
    public synchronized Object borrowObject() throws Exception {
        this.assertOpen();
        Object obj;
        for (obj = null; null == obj; obj = null) {
            if (!this._pool.empty()) {
                obj = this._pool.pop();
            }
            else {
                if (null == this._factory) {
                    throw new NoSuchElementException();
                }
                obj = this._factory.makeObject();
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
    
    public synchronized void returnObject(Object obj) throws Exception {
        this.assertOpen();
        boolean success = true;
        if (null != this._factory) {
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
        }
        boolean shouldDestroy = !success;
        --this._numActive;
        if (success) {
            Object toBeDestroyed = null;
            if (this._pool.size() >= this._maxSleeping) {
                shouldDestroy = true;
                toBeDestroyed = this._pool.remove(0);
            }
            this._pool.push(obj);
            obj = toBeDestroyed;
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
        if (null != this._factory) {
            this._factory.destroyObject(obj);
        }
        this.notifyAll();
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
            final Iterator it = this._pool.iterator();
            while (it.hasNext()) {
                try {
                    this._factory.destroyObject(it.next());
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
    
    public synchronized void addObject() throws Exception {
        this.assertOpen();
        final Object obj = this._factory.makeObject();
        ++this._numActive;
        this.returnObject(obj);
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
