package com.ankamagames.wakfu.common.constants;

import com.ankamagames.baseImpl.common.clientAndServer.game.time.calendar.*;

public final class ChallengeConstants
{
    public static final boolean DEBUG_MODE = false;
    public static final long CHALLENGE_RANKING_UPDATE_TIME = 30000L;
    public static final long CHALLENGE_WAITING_TIME = 300000L;
    public static final long CHALLENGE_PROPOSAL_TIME = 0L;
    public static final long CHALLENGE_BUY_DURATION = 86400000L;
    public static final int TERRITORY_LEFT_FAIL_DELAY_SECS = 10;
    public static final long TERRITORY_LEFT_FAIL_DELAY = 10000L;
    public static final GameInterval CHALLENGE_UNAVAILABILITY_DURATION;
    public static final String LAST_CHALLENGE_WIN_DATE_ACHIEVEMENT_VARIABLE_NAME = "lastChallengeWinDate";
    public static final long REWARD_WAIT_TIME = 20000L;
    public static final byte MAX_ACTIVE_CHALLENGES = 21;
    public static final byte ACTIVE_STATE = 0;
    public static final byte FAILED_STATE = 1;
    public static final byte SUCCESS_STATE = 2;
    public static final byte ABANDONNED_STATE = 3;
    public static final byte AVAILABLE_STATE = 4;
    public static final byte NOT_LOADED = 0;
    public static final byte LOADED = 1;
    public static final byte COMPLETE = 2;
    public static final int NOT_ENOUGH_PLAYERS = 5;
    public static final int REFUSED = 6;
    public static final int[] MONEY_CHALLENGES;
    
    static {
        CHALLENGE_UNAVAILABILITY_DURATION = new GameInterval(50, 4, 0, 0);
        MONEY_CHALLENGES = new int[] { -764, -765, -766, -767 };
    }
}
