package com.ankamagames.framework.fileFormat.io.binaryStorage;

import org.apache.log4j.*;
import com.ankamagames.framework.fileFormat.io.binaryStorage.handler.*;
import java.util.concurrent.*;
import java.util.concurrent.locks.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.impl.*;
import org.apache.commons.pool.*;
import java.util.*;

public abstract class AbstractBinaryStorage extends Thread
{
    private static final boolean DEBUG_MODE = false;
    protected static final Logger m_logger;
    protected final ArrayList<BinaryStorageHandler> m_handlers;
    private final Queue<BinaryStorageOperation> m_operations;
    private volatile boolean m_running;
    private int m_storageId;
    private final Lock m_sleepingLock;
    private final Condition m_sleepingCond;
    protected static final String DEFAULT_ID_INDEX = "id";
    protected static final int DEFAULT_ID_INDEX_HASHCODE;
    private final ObjectPool m_operationsPool;
    
    public AbstractBinaryStorage() {
        super();
        this.m_handlers = new ArrayList<BinaryStorageHandler>();
        this.m_operations = new ConcurrentLinkedQueue<BinaryStorageOperation>();
        this.m_sleepingLock = new ReentrantLock();
        this.m_sleepingCond = this.m_sleepingLock.newCondition();
        this.m_operationsPool = new StackObjectPool(new ObjectFactory<BinaryStorageOperation>() {
            @Override
            public BinaryStorageOperation makeObject() {
                return new BinaryStorageOperation();
            }
        });
    }
    
    private BinaryStorageOperation checkOutOperation() {
        try {
            return (BinaryStorageOperation)this.m_operationsPool.borrowObject();
        }
        catch (Exception e) {
            AbstractBinaryStorage.m_logger.error((Object)"Exception lev\u00e9e lors d'un checkOut d'op\u00e9ration", (Throwable)e);
            return null;
        }
    }
    
    private void releaseOperation(final BinaryStorageOperation binaryStorageOperation) {
        try {
            this.m_operationsPool.returnObject(binaryStorageOperation);
        }
        catch (Exception e) {
            AbstractBinaryStorage.m_logger.error((Object)"Exception lev\u00e9e lors du retour au pool d'un process", (Throwable)e);
        }
    }
    
    public void registerHandler(final BinaryStorageHandler hand) {
        if (!this.m_handlers.contains(hand)) {
            this.m_handlers.add(hand);
        }
    }
    
    public void removeHandler(final BinaryStorageHandler hand) {
        this.m_handlers.remove(hand);
    }
    
    @Override
    public synchronized void start() {
        if (!this.m_running) {
            this.m_running = true;
            super.start();
        }
    }
    
    public void shutdown() {
        this.pushOperation(OperationType.STOP_RUNNING, null);
    }
    
    public boolean isRunning() {
        return this.m_running;
    }
    
    public void setRunning(final boolean running) {
        this.m_running = running;
        this.wakeUp();
    }
    
    private void wakeUp() {
        this.m_sleepingLock.lock();
        this.m_sleepingCond.signal();
        this.m_sleepingLock.unlock();
    }
    
    public void pushOperation(final OperationType op, final Object param) {
        final BinaryStorageOperation operation = this.checkOutOperation();
        if (operation != null) {
            operation.type = op;
            operation.param = param;
            this.m_operations.offer(operation);
            this.wakeUp();
        }
    }
    
    public int getPendingOperationsCount() {
        return this.m_operations.size();
    }
    
    public int getStorageId() {
        return this.m_storageId;
    }
    
    public void setStorageId(final int storageId) {
        this.m_storageId = storageId;
    }
    
    public abstract BinaryStorable getById(final int p0, final BinaryStorable p1);
    
    public abstract BinaryStorable[] getAll(final BinaryStorable p0);
    
    public abstract BinaryStorable[] getByIndex(final String p0, final Object p1, final BinaryStorable p2);
    
    protected abstract boolean init();
    
    protected abstract void remove(final BinaryStorable p0);
    
    protected abstract void save(final BinaryStorable p0);
    
    protected abstract String getCurrentWorkspacePath();
    
    protected abstract void cleanUpFiles();
    
    @Override
    public void run() {
        AbstractBinaryStorage.m_logger.info((Object)("BinaryStorage started " + this));
        int nbtotal = 0;
        int nbsave = 0;
        int nbdestroy = 0;
        this.m_running = true;
        while (this.m_running) {
            BinaryStorageOperation operation;
            while ((operation = this.m_operations.poll()) != null) {
                final OperationType otype = operation.type;
                switch (otype) {
                    case DESTROY: {
                        ++nbdestroy;
                        final BinaryStorable bs = (BinaryStorable)operation.param;
                        this.remove(bs);
                        for (final BinaryStorageHandler hand : this.m_handlers) {
                            hand.onDelete(this, bs);
                        }
                        break;
                    }
                    case SAVE: {
                        ++nbsave;
                        final BinaryStorable bs = (BinaryStorable)operation.param;
                        this.save(bs);
                        for (final BinaryStorageHandler hand : this.m_handlers) {
                            hand.onSave(this, bs);
                        }
                        break;
                    }
                    case STOP_RUNNING: {
                        this.setRunning(false);
                        for (final BinaryStorageHandler hand2 : this.m_handlers) {
                            hand2.onShutdown(this);
                        }
                        break;
                    }
                }
                ++nbtotal;
                this.releaseOperation(operation);
            }
            if (this.m_running && this.m_sleepingLock.tryLock()) {
                try {
                    this.m_sleepingCond.await();
                }
                catch (InterruptedException e) {
                    AbstractBinaryStorage.m_logger.warn((Object)"Interrupt", (Throwable)e);
                }
                finally {
                    this.m_sleepingLock.unlock();
                }
            }
        }
        AbstractBinaryStorage.m_logger.info((Object)("BinaryStorage stopped : " + nbtotal + " operations, " + nbsave + " saved, " + nbdestroy + " destroyed"));
    }
    
    static {
        m_logger = Logger.getLogger((Class)BinaryStorage.class);
        DEFAULT_ID_INDEX_HASHCODE = "id".hashCode();
    }
    
    public enum OperationType
    {
        DESTROY, 
        SAVE, 
        STOP_RUNNING;
    }
    
    private static class BinaryStorageOperation implements Poolable
    {
        OperationType type;
        Object param;
        
        @Override
        public void onCheckIn() {
            this.type = null;
            this.param = null;
        }
        
        @Override
        public void onCheckOut() {
        }
    }
}
