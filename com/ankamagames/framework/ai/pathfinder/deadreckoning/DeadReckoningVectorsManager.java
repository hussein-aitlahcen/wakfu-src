package com.ankamagames.framework.ai.pathfinder.deadreckoning;

import org.apache.log4j.*;
import java.util.*;
import com.ankamagames.framework.kernel.core.common.message.scheduler.process.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.framework.kernel.core.maths.motion.*;

public final class DeadReckoningVectorsManager
{
    protected static final Logger m_logger;
    private static final boolean DEBUG_MODE = false;
    private static final ArrayList<DeadReckoningVectorsManager> m_managers;
    private static boolean m_initialized;
    private static double GAP_THRESHOLD;
    private LinearTrajectory m_projectedTrajectory;
    private LinearTrajectory m_smoothedTrajectory;
    private final ArrayList<DeadReckoningEventsHandler> m_handlers;
    
    private static void updateManagers() {
        for (final DeadReckoningVectorsManager manager : DeadReckoningVectorsManager.m_managers) {
            try {
                manager.computeTrajectoryHistory();
            }
            catch (Exception e) {
                DeadReckoningVectorsManager.m_logger.error((Object)"Exception", (Throwable)e);
            }
        }
    }
    
    private static void initialize() {
        if (!DeadReckoningVectorsManager.m_initialized) {
            ProcessScheduler.getInstance().schedule(new Runnable() {
                @Override
                public void run() {
                    updateManagers();
                }
            }, 50L);
            DeadReckoningVectorsManager.m_initialized = true;
        }
    }
    
    private static void stats() {
        DeadReckoningVectorsManager.m_logger.trace((Object)(DeadReckoningVectorsManager.m_managers.size() + " manager(s) registered"));
    }
    
    public static boolean addManager(final DeadReckoningVectorsManager o) {
        if (!DeadReckoningVectorsManager.m_initialized) {
            initialize();
        }
        if (!DeadReckoningVectorsManager.m_managers.contains(o)) {
            final boolean bAdd = DeadReckoningVectorsManager.m_managers.add(o);
            return bAdd;
        }
        return false;
    }
    
    public static boolean removeManager(final DeadReckoningVectorsManager o) {
        final boolean bRemove = DeadReckoningVectorsManager.m_managers.remove(o);
        if (!bRemove) {
            DeadReckoningVectorsManager.m_logger.error((Object)("Impossible de retirer le manager " + o + " de la liste."));
        }
        return bRemove;
    }
    
    public static double getGapThreshold() {
        return DeadReckoningVectorsManager.GAP_THRESHOLD;
    }
    
    public static void setGapThreshold(final double threashold) {
        DeadReckoningVectorsManager.GAP_THRESHOLD = threashold;
    }
    
    public DeadReckoningVectorsManager(final boolean initAsEmitter) {
        this(initAsEmitter, null);
    }
    
    public DeadReckoningVectorsManager(final boolean initAsEmitter, final DeadReckoningEventsHandler handler) {
        super();
        this.m_handlers = new ArrayList<DeadReckoningEventsHandler>();
        if (initAsEmitter) {
            this.m_projectedTrajectory = new LinearTrajectory();
            this.addEventsHandler(handler);
        }
        else {
            addManager(this);
            this.addEventsHandler(handler);
        }
    }
    
    public void addEventsHandler(final DeadReckoningEventsHandler handler) {
        if (handler != null && !this.m_handlers.contains(handler)) {
            this.m_handlers.add(handler);
        }
    }
    
    public void removeEventsHandler(final DeadReckoningEventsHandler handler) {
        if (handler != null) {
            this.m_handlers.remove(handler);
        }
    }
    
    public void updateEmitter(final long time, final LinearTrajectory currentTrajectory, final boolean bForceSend) {
        if (currentTrajectory.getInitialVelocity().sqrLength() == 0.0f && currentTrajectory.getFinalVelocity().sqrLength() == 0.0f) {
            return;
        }
        final Vector3 cpos = currentTrajectory.getPosition(time);
        final Vector3 lpos = this.currentProjectedPosition(time);
        final double dist = lpos.sub(cpos).length();
        if (bForceSend || dist >= DeadReckoningVectorsManager.GAP_THRESHOLD || currentTrajectory.getFinalVelocity().sqrLength() == 0.0f) {
            final LinearTrajectory newTrajectory = new LinearTrajectory(currentTrajectory);
            for (final DeadReckoningEventsHandler handler : this.m_handlers) {
                handler.onGapThresholdReached(time, this.m_projectedTrajectory, newTrajectory);
            }
            this.m_projectedTrajectory = newTrajectory;
        }
    }
    
    public Vector3 currentProjectedPosition(final long time) {
        return this.m_projectedTrajectory.getPosition(time);
    }
    
    public boolean isHistoryEmpty() {
        return this.m_smoothedTrajectory == null;
    }
    
    public void updateReceiver(final long time, final LinearTrajectory newTrajectory) {
        if (this.m_smoothedTrajectory == null || !newTrajectory.getFinalPosition().equals(this.m_smoothedTrajectory.getFinalPosition())) {
            this.m_smoothedTrajectory = new LinearTrajectory(newTrajectory);
        }
    }
    
    private void computeTrajectoryHistory() {
        if (this.m_smoothedTrajectory != null) {
            final long time = System.currentTimeMillis();
            for (final DeadReckoningEventsHandler handler : this.m_handlers) {
                handler.onTrajectoryUpdate(time, new LinearTrajectory(this.m_smoothedTrajectory));
            }
            this.m_smoothedTrajectory = null;
        }
    }
    
    @Override
    public String toString() {
        if (this.m_smoothedTrajectory != null) {
            return this.m_smoothedTrajectory.toString();
        }
        if (this.m_projectedTrajectory != null) {
            return this.m_projectedTrajectory.toString();
        }
        return "<undefined>";
    }
    
    public static void removeAllManagers() {
        if (DeadReckoningVectorsManager.m_managers != null) {
            final int len = DeadReckoningVectorsManager.m_managers.size();
            DeadReckoningVectorsManager.m_managers.clear();
            DeadReckoningVectorsManager.m_initialized = false;
            DeadReckoningVectorsManager.m_logger.info((Object)("Nettoyage des vecteurs de mouvement des personnages (" + DeadReckoningVectorsManager.m_managers.size() + " restants sur " + len + ")"));
        }
    }
    
    static {
        m_logger = Logger.getLogger((Class)DeadReckoningVectorsManager.class);
        m_managers = new ArrayList<DeadReckoningVectorsManager>();
        DeadReckoningVectorsManager.GAP_THRESHOLD = 3.0;
    }
}
