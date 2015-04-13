package com.ankamagames.wakfu.common.game.nation.government;

public class NationRankRightChecker
{
    public static boolean hasRankToNominate(final NationRank requesterRank, final NationRank requestedRank) {
        if (requesterRank == null || requestedRank == null) {
            return false;
        }
        if (requesterRank == NationRank.GOVERNOR) {
            return requestedRank != NationRank.GOVERNOR;
        }
        return requesterRank == NationRank.DEPUTY && requestedRank != NationRank.GOVERNOR && requestedRank != NationRank.DEPUTY;
    }
    
    public static boolean hasRankToRevoke(final NationRank requesterRank, final NationRank requestedRank) {
        return requesterRank != null && requestedRank != null && (requesterRank == requestedRank || requesterRank == NationRank.GOVERNOR || (requesterRank == NationRank.DEPUTY && requestedRank != NationRank.GOVERNOR));
    }
    
    public static boolean hasRankToManageProtector(final NationRank rank) {
        if (rank == null) {
            return false;
        }
        switch (rank) {
            case GOVERNOR:
            case DEPUTY:
            case CHALLENGER:
            case TREASURER:
            case METEOROLOGIST:
            case ZOOLOGIST: {
                return true;
            }
            default: {
                return false;
            }
        }
    }
    
    public static boolean hasRankToManageChallenge(final NationRank rank) {
        if (rank == null) {
            return false;
        }
        switch (rank) {
            case GOVERNOR:
            case DEPUTY:
            case CHALLENGER: {
                return true;
            }
            default: {
                return false;
            }
        }
    }
    
    public static boolean hasRankToManageEcosystem(final NationRank rank) {
        if (rank == null) {
            return false;
        }
        switch (rank) {
            case GOVERNOR:
            case DEPUTY:
            case ZOOLOGIST: {
                return true;
            }
            default: {
                return false;
            }
        }
    }
    
    public static boolean hasRankToManageTax(final NationRank rank) {
        if (rank == null) {
            return false;
        }
        switch (rank) {
            case GOVERNOR:
            case DEPUTY:
            case TREASURER: {
                return true;
            }
            default: {
                return false;
            }
        }
    }
    
    public static boolean hasRankToManageProtectorBudget(final NationRank rank) {
        if (rank == null) {
            return false;
        }
        switch (rank) {
            case GOVERNOR:
            case DEPUTY:
            case TREASURER: {
                return true;
            }
            default: {
                return false;
            }
        }
    }
    
    public static boolean hasRankToManageWeather(final NationRank rank) {
        if (rank == null) {
            return false;
        }
        switch (rank) {
            case GOVERNOR:
            case DEPUTY:
            case METEOROLOGIST: {
                return true;
            }
            default: {
                return false;
            }
        }
    }
    
    public static boolean hasRankToChangeLaws(final NationRank rank) {
        if (rank == null) {
            return false;
        }
        switch (rank) {
            case GOVERNOR:
            case DEPUTY: {
                return true;
            }
            default: {
                return false;
            }
        }
    }
    
    public static boolean hasRankToChangeDiplomacy(final NationRank rank) {
        if (rank == null) {
            return false;
        }
        switch (rank) {
            case GOVERNOR:
            case DEPUTY: {
                return true;
            }
            default: {
                return false;
            }
        }
    }
}
