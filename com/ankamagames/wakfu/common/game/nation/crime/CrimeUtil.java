package com.ankamagames.wakfu.common.game.nation.crime;

public final class CrimeUtil
{
    public static final int SERVER_TRIGGER_REDITION_EVENT_ID = 6003720;
    public static final int SERVER_TRIGGER_USELESS_REDITION_EVENT_ID = 6005892;
    public static final int SERVER_TRIGGER_NEGOTIATION_EVENT_ID = 6103765;
    public static final int SERVER_TRIGGER_PURGATION_COOLDOWN_EXPIRED = 10006703;
    private static final int PDC_PER_HOUR = 500;
    public static final int REDITION_CRIME_REDUCTION_PERCENT = 20;
    
    public static long getJailDurationFromCrimePoints(final int crimePoints) {
        return (int)(-crimePoints * 3600000L / 500.0);
    }
    
    public static long getReducedJailDuration(final long standartDuration, final int percent) {
        return (int)(standartDuration - standartDuration * (percent / 100.0));
    }
    
    public static int getDecrementedCrimeScore(final int crimePoints, final long purgationDurationPassed) {
        final double reduction = 500L * purgationDurationPassed / 3600000.0;
        return (int)Math.round(crimePoints + reduction);
    }
}
