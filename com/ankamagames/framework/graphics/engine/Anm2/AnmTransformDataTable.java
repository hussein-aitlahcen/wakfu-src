package com.ankamagames.framework.graphics.engine.Anm2;

import com.ankamagames.framework.fileFormat.io.*;
import com.ankamagames.framework.graphics.engine.Anm2.actions.*;

public class AnmTransformDataTable
{
    private static final AnmAction[] EMPTY_ACTIONS;
    private static final float[] EMPTY;
    public final float[] m_colors;
    public final float[] m_rotations;
    public final float[] m_translations;
    public final AnmAction[] m_actions;
    
    public AnmTransformDataTable(final float[] colors, final float[] rotations, final float[] translations, final AnmAction[] actions) {
        super();
        this.m_colors = colors;
        this.m_rotations = rotations;
        this.m_translations = translations;
        this.m_actions = actions;
    }
    
    public static float[] readFloats(final ExtendedDataInputStream bitStream) {
        final int count = bitStream.readInt();
        if (count == 0) {
            return AnmTransformDataTable.EMPTY;
        }
        return bitStream.readFloats(count);
    }
    
    public static AnmAction[] readActions(final ExtendedDataInputStream bitStream) {
        final int count = bitStream.readInt();
        if (count == 0) {
            return AnmTransformDataTable.EMPTY_ACTIONS;
        }
        final AnmActionFactory factory = AnmActionFactoryProvider.INSTANCE.getFactory();
        final AnmAction[] tab = new AnmAction[count];
        for (int i = 0; i < count; ++i) {
            final byte actionId = (byte)(bitStream.readByte() & 0xFF);
            final byte parametersCount = (byte)(bitStream.readByte() & 0xFF);
            tab[i] = factory.fromId(AnmActionTypes.getFromId(actionId));
            try {
                tab[i].load(parametersCount, bitStream);
            }
            catch (Exception ex) {}
        }
        return tab;
    }
    
    public static AnmTransformDataTable createFrom(final ExtendedDataInputStream bitStream) {
        return new AnmTransformDataTable(readFloats(bitStream), readFloats(bitStream), readFloats(bitStream), readActions(bitStream));
    }
    
    static {
        EMPTY_ACTIONS = new AnmAction[0];
        EMPTY = new float[0];
    }
}
