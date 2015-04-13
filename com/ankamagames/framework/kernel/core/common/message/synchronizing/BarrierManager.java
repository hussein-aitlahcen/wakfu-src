package com.ankamagames.framework.kernel.core.common.message.synchronizing;

import org.apache.log4j.*;
import com.ankamagames.framework.kernel.core.common.message.scheduler.process.*;
import gnu.trove.*;
import java.util.*;

public final class BarrierManager
{
    public static final BarrierManager INSTANCE;
    protected static final Logger m_logger;
    private final Object m_mutex;
    private final TIntObjectHashMap<Barrier> m_barriers;
    
    private BarrierManager() {
        super();
        this.m_mutex = new Object();
        this.m_barriers = new TIntObjectHashMap<Barrier>();
    }
    
    public void setBarrier(final BarrierHandle handle) {
        if (handle == null) {
            return;
        }
        synchronized (this.m_mutex) {
            final int barrierId = handle.getId();
            Barrier barrier = this.m_barriers.get(barrierId);
            if (barrier == null) {
                barrier = new Barrier(barrierId);
                this.m_barriers.put(barrierId, barrier);
            }
            else {
                final Barrier barrier2 = barrier;
                ++barrier2.m_count;
            }
        }
    }
    
    public void removeBarrier(final BarrierHandle handle) {
        if (handle == null) {
            return;
        }
        synchronized (this.m_mutex) {
            final int barrierId = handle.getId();
            final Barrier barrier = this.m_barriers.get(barrierId);
            if (barrier != null) {
                final ArrayList<Runnable> procs = barrier.getProcess();
                if (barrier.m_count <= 0) {
                    this.m_barriers.remove(barrierId);
                    this.filterProcs(procs);
                    this.runProcs(procs);
                }
                else {
                    final Barrier barrier2 = barrier;
                    --barrier2.m_count;
                }
            }
        }
    }
    
    private void runProcs(final ArrayList<Runnable> procs) {
        if (procs.isEmpty()) {
            return;
        }
        for (int i = 0, size = procs.size(); i < size; ++i) {
            final Runnable runnable = procs.get(i);
            if (runnable != null) {
                ProcessScheduler.getInstance().schedule(runnable);
            }
        }
    }
    
    private void filterProcs(final ArrayList<Runnable> procs) {
        if (!this.m_barriers.isEmpty()) {
            this.m_barriers.forEachEntry(new TIntObjectProcedure<Barrier>() {
                @Override
                public boolean execute(final int id, final Barrier barrier) {
                    final Iterator<Runnable> it = procs.iterator();
                    while (it.hasNext()) {
                        final Runnable runnable = it.next();
                        if (!barrier.canGoThru(runnable)) {
                            it.remove();
                        }
                    }
                    return !procs.isEmpty();
                }
            });
        }
    }
    
    public void blockOrExecute(final Runnable runnable, final BarrierHandle... handles) {
        if (handles == null) {
            return;
        }
        if (runnable == null) {
            return;
        }
        boolean blocked = false;
        for (final BarrierHandle handle : handles) {
            final int barrierId = handle.getId();
            final Barrier barrier = this.m_barriers.get(barrierId);
            if (barrier != null) {
                blocked = true;
                barrier.addProcess(runnable);
            }
        }
        if (!blocked) {
            runnable.run();
        }
    }
    
    static {
        INSTANCE = new BarrierManager();
        m_logger = Logger.getLogger((Class)BarrierManager.class);
    }
    
    private static class Barrier
    {
        int m_id;
        int m_count;
        final ArrayList<Runnable> m_process;
        
        Barrier(final int id) {
            super();
            this.m_process = new ArrayList<Runnable>();
            this.m_id = id;
            this.m_count = 0;
        }
        
        boolean canGoThru(final Runnable runnable) {
            return !this.m_process.contains(runnable);
        }
        
        ArrayList<Runnable> getProcess() {
            return this.m_process;
        }
        
        void addProcess(final Runnable runnable) {
            if (!this.m_process.contains(runnable)) {
                this.m_process.add(runnable);
            }
        }
    }
}
