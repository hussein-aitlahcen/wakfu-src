package com.ankamagames.wakfu.common.constants;

import com.ankamagames.baseImpl.common.clientAndServer.game.time.calendar.*;
import java.util.*;
import com.ankamagames.wakfu.common.game.nation.election.*;

public class NationConstants
{
    public static final GameInterval FORCED_VOTE_DURATION;
    public static final GameInterval FORCED_VOTE_FREQUENCY;
    public static final byte COMMAND_SET = 1;
    public static final byte COMMAND_DUMP = 2;
    public static final byte COMMAND_VOTE_START = 3;
    public static final byte COMMAND_VOTE_END = 4;
    public static final byte COMMAND_VOTE_INFO = 5;
    public static final byte COMMAND_ADD_CITIZEN_POINTS = 9;
    public static final byte COMMAND_CHANGE_ALIGNEMENT = 10;
    public static final byte COMMAND_GO_IN_PRISON = 11;
    public static final byte COMMAND_REMOVE_OFFENSE = 12;
    public static final byte COMMAND_ADD_OFFENSE = 13;
    public static final byte COMMAND_SWITCH_PASSEPORT_ACTIVATION = 14;
    public static final byte HELP = 15;
    public static final byte COMMAND_SET_RANK = 16;
    public static final byte SHOW_RANKS = 17;
    public static final int VOTE_REGISTER_FEE = 3000;
    public static final int NO_NATION_ID = -1;
    public static final short MINIMUM_LEVEL_REQUIRED_TO_VOTE = 1;
    public static final short MINIMUM_LEVEL_REQUIRED_TO_BE_CANDIDATE = 1;
    public static final short MINIMUM_CRIME_SCORE_TO_BE_JAILED_BY_PROTECTOR = 0;
    public static final int CANDIDATE_PER_PAGE = 3;
    public static final int TOTAL_LAW_POINTS = 50;
    public static final int CITIZEN_SCORE_COST_TO_CHANGE_LAWS = -500;
    public static final float CITIZEN_SCORE_RATIO_ON_GOVERNOR_REVOKED = 0.5f;
    public static final int RIKTUS_PROTECTOR_ID = 275;
    public static final Comparator<CandidateInfo> CANDIDATES_ELECTION_COMPARATOR;
    
    static {
        FORCED_VOTE_DURATION = new GameInterval(0, 10, 0, 0);
        FORCED_VOTE_FREQUENCY = new GameInterval(0, 30, 0, 0);
        CANDIDATES_ELECTION_COMPARATOR = new Comparator<CandidateInfo>() {
            @Override
            public int compare(final CandidateInfo o1, final CandidateInfo o2) {
                if (o1 == o2) {
                    return 0;
                }
                if (o1.isWithDraw() && o2.isWithDraw()) {
                    return 0;
                }
                if (o1.isWithDraw()) {
                    return 1;
                }
                if (o2.isWithDraw()) {
                    return -1;
                }
                if (o1.getBallotCount() != o2.getBallotCount()) {
                    return o2.getBallotCount() - o1.getBallotCount();
                }
                if (o1.getCitizenScore() != o2.getCitizenScore()) {
                    return o2.getCitizenScore() - o1.getCitizenScore();
                }
                return (o1.getId() <= o2.getId()) ? -1 : 1;
            }
        };
    }
}
