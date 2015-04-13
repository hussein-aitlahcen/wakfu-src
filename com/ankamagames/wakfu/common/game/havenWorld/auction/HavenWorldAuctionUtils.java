package com.ankamagames.wakfu.common.game.havenWorld.auction;

public class HavenWorldAuctionUtils
{
    public static int getNextBidValue(final int currentBidValue) {
        if (currentBidValue == -1) {
            return 100;
        }
        final int currentStep = currentBidValue / 1000 + 1;
        final int increase = currentStep * 100;
        return currentBidValue + increase;
    }
}
