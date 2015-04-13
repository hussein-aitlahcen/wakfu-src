package com.ankamagames.framework.graphics.engine.Anm2;

import org.apache.log4j.*;
import com.ankamagames.framework.fileFormat.io.*;
import java.io.*;
import com.ankamagames.framework.graphics.engine.Anm2.actions.*;

public class AnmFrame
{
    private static final Logger m_logger;
    private static final AnmAction[] EMPTY_ACTIONS;
    AnmShape[] m_sprites;
    AnmAction[] m_actions;
    
    static AnmFrame copyWithoutAction(final AnmFrame frame) {
        final AnmFrame f = new AnmFrame();
        f.m_sprites = frame.m_sprites;
        f.m_actions = AnmFrame.EMPTY_ACTIONS;
        return f;
    }
    
    public final int load(final ExtendedDataInputStream bitStream, final int frameIndex) throws IOException {
        final int numSprites = bitStream.readShort() & 0xFFFF;
        this.m_sprites = new AnmShape[numSprites];
        for (int i = 0; i < this.m_sprites.length; ++i) {
            this.m_sprites[i] = AnmShapeFactory.createShape(bitStream);
        }
        return this.loadActions(bitStream, frameIndex);
    }
    
    protected final int loadActions(final ExtendedDataInputStream bitStream, final int frameIndex) {
        final int numActions = bitStream.readByte() & 0xFF;
        if (numActions == 0) {
            this.m_actions = AnmFrame.EMPTY_ACTIONS;
        }
        else {
            this.m_actions = new AnmAction[numActions];
            for (int i = 0; i < this.m_actions.length; ++i) {
                final byte actionId = (byte)(bitStream.readByte() & 0xFF);
                final byte parametersCount = (byte)(bitStream.readByte() & 0xFF);
                final AnmActionTypes actionType = AnmActionTypes.getFromId(actionId);
                final AnmAction anmAction = AnmActionFactoryProvider.INSTANCE.getFactory().fromId(actionType);
                try {
                    anmAction.setFrameIndex(frameIndex);
                    anmAction.load(parametersCount, bitStream);
                    this.m_actions[i] = anmAction;
                }
                catch (Exception e) {
                    AnmFrame.m_logger.error((Object)("Exception durant le chargement d'une action anm actionId=" + actionId + " parametersCount=" + parametersCount), (Throwable)e);
                }
            }
        }
        return bitStream.readShort() & 0xFFFF;
    }
    
    public final AnmShape[] getSprites() {
        return this.m_sprites;
    }
    
    static {
        m_logger = Logger.getLogger((Class)AnmFrame.class);
        EMPTY_ACTIONS = new AnmAction[0];
    }
}
