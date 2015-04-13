package com.ankamagames.wakfu.common.game.havenWorld.procedure;

import gnu.trove.*;
import com.ankamagames.wakfu.common.game.havenWorld.*;

public class WorkerBuildingCount implements TObjectProcedure<Building>
{
    private int m_remainingWorker;
    private int m_totalWorker;
    
    public WorkerBuildingCount(final HavenWorld world) {
        super();
        this.m_remainingWorker = world.getDefinition().getWorkers();
        this.m_totalWorker = world.getDefinition().getWorkers();
    }
    
    @Override
    public boolean execute(final Building object) {
        this.m_remainingWorker -= object.getDefinition().getNeededWorkers();
        this.m_remainingWorker += object.getDefinition().getGrantedWorkers();
        this.m_totalWorker += object.getDefinition().getGrantedWorkers();
        return true;
    }
    
    public int getRemainingWorker() {
        return this.m_remainingWorker;
    }
    
    public int getTotalWorker() {
        return this.m_totalWorker;
    }
    
    @Override
    public String toString() {
        return "WorkerBuildingCount{m_remainingWorker=" + this.m_remainingWorker + '}';
    }
}
