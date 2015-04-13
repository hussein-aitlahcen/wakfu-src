package com.ankamagames.wakfu.client.core.game.dimensionalBag.gfxProvider;

import com.ankamagames.framework.kernel.core.maths.*;
import gnu.trove.*;
import com.ankamagames.baseImpl.graphics.alea.display.*;

public class BorderPattern implements ListElementUsed
{
    private static final int[] EMPTY;
    public static final int MASK_NE = 1;
    public static final int MASK_SE = 2;
    public static final int MASK_SW = 4;
    public static final int MASK_NW = 8;
    private final int[] m_dir1;
    private final int[] m_dir3;
    private final int[] m_dir5;
    private final int[] m_dir7;
    private final int m_dir0;
    private final int m_dir2;
    private final int m_dir4;
    private final int m_dir6;
    
    public BorderPattern(final int[] dir1, final int[] dir3, final int[] dir5, final int[] dir7, final int dir0, final int dir2, final int dir4, final int dir6) {
        super();
        this.m_dir1 = dir1;
        this.m_dir3 = dir3;
        this.m_dir5 = dir5;
        this.m_dir7 = dir7;
        this.m_dir0 = dir0;
        this.m_dir2 = dir2;
        this.m_dir4 = dir4;
        this.m_dir6 = dir6;
    }
    
    public BorderPattern(final int[] dir1, final int[] dir3, final int dir0, final int dir2, final int dir4, final int dir6) {
        this(dir1, dir3, BorderPattern.EMPTY, BorderPattern.EMPTY, dir0, dir2, dir4, dir6);
    }
    
    public BorderPattern() {
        this(BorderPattern.EMPTY, BorderPattern.EMPTY, BorderPattern.EMPTY, BorderPattern.EMPTY, 0, 0, 0, 0);
    }
    
    public int getElementId(final int dir) {
        switch (dir) {
            case 0: {
                return this.m_dir0;
            }
            case 1: {
                return getRandom(this.m_dir1);
            }
            case 2: {
                return this.m_dir2;
            }
            case 3: {
                return getRandom(this.m_dir3);
            }
            case 4: {
                return this.m_dir4;
            }
            case 5: {
                return getRandom(this.m_dir5);
            }
            case 6: {
                return this.m_dir6;
            }
            case 7: {
                return getRandom(this.m_dir7);
            }
            default: {
                return 0;
            }
        }
    }
    
    public int getElementIdFromMask(final int mask) {
        switch (mask) {
            case 0: {
                return 0;
            }
            case 2: {
                return this.getElementId(1);
            }
            case 4: {
                return this.getElementId(3);
            }
            case 8: {
                return this.getElementId(5);
            }
            case 1: {
                return this.getElementId(7);
            }
            case 3: {
                return this.getElementId(0);
            }
            case 6: {
                return this.getElementId(2);
            }
            case 12: {
                return this.getElementId(4);
            }
            case 9: {
                return this.getElementId(6);
            }
            case 5:
            case 7:
            case 10:
            case 11:
            case 13:
            case 14:
            case 15: {
                return -1;
            }
            default: {
                return -1;
            }
        }
    }
    
    private static int getRandom(final int[] array) {
        return array[MathHelper.random(array.length)];
    }
    
    @Override
    public void getAllElementIds(final TIntHashSet elementIds) {
        elementIds.addAll(this.m_dir1);
        elementIds.addAll(this.m_dir3);
        elementIds.addAll(this.m_dir5);
        elementIds.addAll(this.m_dir7);
        elementIds.add(this.m_dir0);
        elementIds.add(this.m_dir2);
        elementIds.add(this.m_dir4);
        elementIds.add(this.m_dir6);
    }
    
    public static void changeZOrder(final DisplayedScreenElement element, final int borderMask) {
        if (borderMask == 8 || borderMask == 1 || borderMask == 9) {
            element.changeZOrder(-2);
        }
    }
    
    static {
        EMPTY = new int[] { 0 };
    }
}
