package com.ankamagames.baseImpl.graphics.sound.binary;

import gnu.trove.*;
import com.ankamagames.framework.fileFormat.io.*;

public class AnimatedElementRunScriptDataFactoryManager
{
    private final TIntObjectHashMap<AnimatedElementRunScriptDataFactory> m_factories;
    public static final AnimatedElementRunScriptDataFactoryManager INSTANCE;
    static final int PLAY_BARK = 1;
    static final int PLAY_GROUND = 2;
    static final int PLAY_SOUND = 3;
    static final int PLAY_RANDOM_SOUND = 4;
    static final int PLAY_LOCAL_SOUND = 5;
    static final int PLAY_LOCAL_RANDOM_SOUND = 6;
    
    public AnimatedElementRunScriptDataFactoryManager() {
        super();
        (this.m_factories = new TIntObjectHashMap<AnimatedElementRunScriptDataFactory>()).put(1, new AnimatedElementRunScriptDataFactory() {
            @Override
            public AnimatedElementRunScriptData createData(final ExtendedDataInputStream is) {
                return new PlayBarkSoundData(is);
            }
        });
        this.m_factories.put(2, new AnimatedElementRunScriptDataFactory() {
            @Override
            public AnimatedElementRunScriptData createData(final ExtendedDataInputStream is) {
                return new PlayGroundSoundData(is);
            }
        });
        this.m_factories.put(3, new AnimatedElementRunScriptDataFactory() {
            @Override
            public AnimatedElementRunScriptData createData(final ExtendedDataInputStream is) {
                return new PlaySoundData(is);
            }
        });
        this.m_factories.put(4, new AnimatedElementRunScriptDataFactory() {
            @Override
            public AnimatedElementRunScriptData createData(final ExtendedDataInputStream is) {
                return new PlayRandomSoundData(is);
            }
        });
        this.m_factories.put(5, new AnimatedElementRunScriptDataFactory() {
            @Override
            public AnimatedElementRunScriptData createData(final ExtendedDataInputStream is) {
                return new PlayLocalSoundData(is);
            }
        });
        this.m_factories.put(6, new AnimatedElementRunScriptDataFactory() {
            @Override
            public AnimatedElementRunScriptData createData(final ExtendedDataInputStream is) {
                return new PlayLocalRandomSoundData(is);
            }
        });
    }
    
    public AnimatedElementRunScriptData createData(final int type, final ExtendedDataInputStream is) {
        final AnimatedElementRunScriptDataFactory factory = this.m_factories.get(type);
        return factory.createData(is);
    }
    
    static {
        INSTANCE = new AnimatedElementRunScriptDataFactoryManager();
    }
    
    private interface AnimatedElementRunScriptDataFactory
    {
        AnimatedElementRunScriptData createData(ExtendedDataInputStream p0);
    }
}
