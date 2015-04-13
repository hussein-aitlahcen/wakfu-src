package com.ankamagames.framework.kernel.core.maths;

public final class ProbaHelper
{
    public static float inter(final float pA, final float pB) {
        assertInBounds(pA, pB);
        return pA * pB;
    }
    
    public static float union(final float pA, final float pB) {
        assertInBounds(pA, pB);
        return pA + pB - inter(pA, pB);
    }
    
    public static float union(final float pA, final float pB, final float pC) {
        assertInBounds(pA, pB);
        return pA + pB + pC - inter(pA, pB) - inter(pA, pC) - inter(pB, pC) + inter(pA, pB, pC);
    }
    
    public static float inter(final float... probas) {
        if (probas.length == 0) {
            return 0.0f;
        }
        double res = probas[0];
        for (int i = 1; i < probas.length; ++i) {
            final double probaI = probas[i];
            assertInBounds(probaI);
            res *= probaI;
        }
        return (float)res;
    }
    
    private static void assertInBounds(final double... args) {
        for (int i = 0; i < args.length; ++i) {
            if (args[i] < 0.0) {
                throw new IllegalArgumentException("a < 0.0f");
            }
            if (args[i] > 1.0) {
                throw new IllegalArgumentException("a > 1,0f");
            }
        }
    }
}
