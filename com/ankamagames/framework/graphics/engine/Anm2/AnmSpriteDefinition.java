package com.ankamagames.framework.graphics.engine.Anm2;

import org.apache.log4j.*;
import com.ankamagames.framework.fileFormat.io.*;
import java.io.*;
import java.util.*;
import com.ankamagames.framework.graphics.engine.Anm2.actions.*;

@Deprecated
public class AnmSpriteDefinition extends SpriteDefinition
{
    private static final Logger m_logger;
    AnmFrame[] m_frames;
    AnmFrame m_currentFrame;
    final boolean m_optimized;
    protected int m_currentSprite;
    
    public AnmSpriteDefinition(final boolean optimized) {
        super();
        this.m_currentSprite = -1;
        this.m_optimized = optimized;
    }
    
    @Override
    public final void load(final ExtendedDataInputStream bitStream) throws IOException {
        super.load(bitStream);
        if (!this.m_optimized) {
            final int numShapes = bitStream.readByte() & 0xFF;
            if (numShapes != 0) {
                AnmSpriteDefinition.m_logger.warn((Object)"Sprite Def avec des shapes");
                for (int i = 0; i < numShapes; ++i) {
                    AnmShapeFactory.createShape(bitStream);
                }
            }
            final int numSprites = bitStream.readByte() & 0xFF;
            if (numSprites != 0) {
                AnmSpriteDefinition.m_logger.warn((Object)"Sprite Def avec des sprites def");
                for (int j = 0; j < numSprites; ++j) {
                    AnmShapeFactory.createShape(bitStream);
                }
            }
        }
        final int numFrames = bitStream.readShort() & 0xFFFF;
        this.m_frames = new AnmFrame[numFrames];
        for (int i = 0; i < this.m_frames.length; ++i) {
            this.m_frames[i] = new AnmFrame();
            final int count = this.m_frames[i].load(bitStream, i);
            if (count > 0) {
                try {
                    final AnmFrame frameWithoutAction = AnmFrame.copyWithoutAction(this.m_frames[i]);
                    for (int k = 0; k < count; ++k) {
                        this.m_frames[++i] = frameWithoutAction;
                    }
                }
                catch (Exception ex) {
                    throw new IOException("erreur avec la definition " + this.m_name + ". nombre de frame incorrete");
                }
            }
            final int spriteCount = this.m_frames[i].getSprites().length;
            if (spriteCount > this.m_maxSpriteCount) {
                this.m_maxSpriteCount = spriteCount;
            }
        }
        if (this.m_frames.length == 1) {
            final AnmAction[] actions = this.m_frames[0].m_actions;
            int j = 0;
            while (j < actions.length) {
                switch (actions[j].getType()) {
                    case GO_TO_ANIMATION:
                    case GO_TO_IF_PREVIOUS_ANIMATION:
                    case GO_TO_RANDOM_ANIMATION:
                    case GO_TO_STATIC_ANIMATION: {
                        this.setIsAnimationNode();
                    }
                    default: {
                        ++j;
                        continue;
                    }
                }
            }
        }
    }
    
    @Override
    public void pushActions(final int frameIndex, final ArrayList<AnmAction> actions) {
        if (frameIndex >= this.m_frames.length) {
            return;
        }
        final AnmAction[] frameActions = this.m_frames[frameIndex].m_actions;
        for (int j = 0; j < frameActions.length; ++j) {
            actions.add(frameActions[j]);
        }
    }
    
    @Override
    public short[] getShapesIds(final int frameIndex) {
        final AnmShape[] frameSprites = this.m_frames[frameIndex].m_sprites;
        final short[] ids = new short[frameSprites.length];
        for (int i = 0; i < frameSprites.length; ++i) {
            ids[i] = frameSprites[i].m_id;
        }
        return ids;
    }
    
    @Override
    public final boolean hasOnlyOneSprite() {
        return this.m_frames.length == 1 && this.m_frames[0].m_sprites.length == 1;
    }
    
    @Override
    public final short firstSpriteId() {
        assert this.hasOnlyOneSprite();
        return this.m_frames[0].m_sprites[0].m_id;
    }
    
    @Override
    public final int getFrameCount() {
        return this.m_frames.length;
    }
    
    @Override
    public final void nextSprite() {
        ++this.m_currentSprite;
    }
    
    @Override
    public final int beginProcessFrame(final int index) {
        this.m_currentSprite = -1;
        this.m_currentFrame = this.m_frames[index];
        return this.m_currentFrame.m_sprites.length;
    }
    
    @Override
    public final short process(final AnmTransform parentTransform, final AnmTransform result) {
        final AnmShape sprite = this.m_currentFrame.m_sprites[this.m_currentSprite];
        sprite.process(parentTransform, result);
        return sprite.m_id;
    }
    
    @Override
    public int getShapeCount() {
        int shapeCount = 0;
        AnmFrame lastFrame = null;
        for (final AnmFrame frame : this.m_frames) {
            assert frame != null;
            if (frame != lastFrame) {
                shapeCount += frame.getSprites().length;
            }
            lastFrame = frame;
        }
        return shapeCount;
    }
    
    @Override
    public int getRealShapeCount() {
        int realCount = 0;
        for (final AnmFrame frame : this.m_frames) {
            assert frame != null;
            realCount += frame.getSprites().length;
        }
        return realCount;
    }
    
    static {
        m_logger = Logger.getLogger((Class)AnmSpriteDefinition.class);
    }
}
