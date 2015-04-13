package com.ankamagames.wakfu.common.game.xp;

import gnu.trove.*;

public final class RerollXpFactorComputer
{
    public static float getRerollXpFactor(final short currentLevel, final TShortArrayList accountCharactersLevel) {
        final int size = accountCharactersLevel.size();
        int characterWithSuperiorLevelCount = 0;
        for (int i = 0; i < size; ++i) {
            final short level = accountCharactersLevel.get(i);
            if (currentLevel < level) {
                ++characterWithSuperiorLevelCount;
            }
        }
        return Math.min(characterWithSuperiorLevelCount + 1, 3.0f);
    }
}
