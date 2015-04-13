package com.ankamagames.framework.kernel;

import org.apache.log4j.*;
import com.ankamagames.framework.kernel.events.*;
import java.util.*;
import com.ankamagames.framework.kernel.core.common.message.*;

public class FrameHandler implements MessageHandler
{
    protected static final Logger m_logger;
    private final ArrayList<MessageFrame> m_frames;
    private final List<MessageFrame> m_framesToRemoveBefore;
    private final List<MessageFrame> m_framesToAddBefore;
    private final List<MessageFrame> m_framesToRemoveAfter;
    private final List<MessageFrame> m_framesToAddAfter;
    private boolean m_runningFrame;
    private long m_id;
    
    public FrameHandler() {
        super();
        this.m_frames = new ArrayList<MessageFrame>();
        this.m_framesToRemoveBefore = new ArrayList<MessageFrame>();
        this.m_framesToAddBefore = new ArrayList<MessageFrame>();
        this.m_framesToRemoveAfter = new ArrayList<MessageFrame>();
        this.m_framesToAddAfter = new ArrayList<MessageFrame>();
        this.m_id = 0L;
        this.m_runningFrame = false;
    }
    
    @Override
    public long getId() {
        return this.m_id;
    }
    
    @Override
    public void setId(final long id) {
        this.m_id = id;
    }
    
    @Override
    public boolean onMessage(final Message message) {
        boolean retFlag = true;
        this.preFrameExecutionSetup();
        synchronized (this.m_frames) {
            this.m_runningFrame = true;
            final int size = this.m_frames.size();
            if (size > 0) {
                for (int i = 0; i < size; ++i) {
                    final MessageFrame frame = this.m_frames.get(i);
                    if (frame != null) {
                        try {
                            retFlag = frame.onMessage(message);
                        }
                        catch (RuntimeException e) {
                            FrameHandler.m_logger.error((Object)("Exception lev\u00e9e lors du traitement d'un message : " + message), (Throwable)e);
                        }
                        if (!retFlag) {
                            break;
                        }
                    }
                }
            }
            else {
                FrameHandler.m_logger.warn((Object)("L'entit\u00e9 destinataire du message n'a pas de frames, message : " + message.getClass().getSimpleName()));
                retFlag = true;
            }
            this.m_runningFrame = false;
        }
        if (retFlag) {
            FrameHandler.m_logger.warn((Object)("[DEFAUT DE CONCEPTION] Message (" + message.getClass().getSimpleName() + ") non trait\u00e9" + ", type " + message.getId() + ", Handler=" + this + ", Message=" + message));
        }
        this.postFrameExecutionSetup();
        return false;
    }
    
    private void preFrameExecutionSetup() {
        synchronized (this.m_frames) {
            synchronized (this.m_framesToAddBefore) {
                for (int count = this.m_framesToAddBefore.size(), i = 0; i < count; ++i) {
                    final MessageFrame frame = this.m_framesToAddBefore.get(i);
                    if (!this.m_frames.contains(frame)) {
                        this.m_frames.add(0, frame);
                        frame.onFrameAdd(this, false);
                    }
                }
                this.m_framesToAddBefore.clear();
            }
            synchronized (this.m_framesToRemoveBefore) {
                for (int count = this.m_framesToRemoveBefore.size(), i = 0; i < count; ++i) {
                    final MessageFrame frame = this.m_framesToRemoveBefore.get(i);
                    if (this.m_frames.remove(frame)) {
                        frame.onFrameRemove(this, false);
                    }
                }
                this.m_framesToRemoveBefore.clear();
            }
        }
    }
    
    private void postFrameExecutionSetup() {
        synchronized (this.m_frames) {
            synchronized (this.m_framesToAddAfter) {
                for (int count = this.m_framesToAddAfter.size(), i = 0; i < count; ++i) {
                    final MessageFrame frame = this.m_framesToAddAfter.get(i);
                    if (!this.m_frames.contains(frame)) {
                        this.m_frames.add(0, frame);
                        frame.onFrameAdd(this, false);
                    }
                }
                this.m_framesToAddAfter.clear();
            }
            synchronized (this.m_framesToRemoveAfter) {
                for (int count = this.m_framesToRemoveAfter.size(), i = 0; i < count; ++i) {
                    final MessageFrame frame = this.m_framesToRemoveAfter.get(i);
                    if (this.m_frames.remove(frame)) {
                        frame.onFrameRemove(this, false);
                    }
                }
                this.m_framesToRemoveAfter.clear();
            }
        }
    }
    
    public void pushFrame(final MessageFrame frame) {
        if (this.m_runningFrame) {
            final boolean bRemoveAfter;
            synchronized (this.m_framesToRemoveAfter) {
                bRemoveAfter = this.m_framesToRemoveAfter.contains(frame);
            }
            if (bRemoveAfter) {
                synchronized (this.m_framesToAddBefore) {
                    if (!this.m_framesToAddBefore.contains(frame)) {
                        this.m_framesToAddBefore.add(frame);
                    }
                    frame.onFrameAdd(this, true);
                }
                synchronized (this.m_framesToRemoveBefore) {
                    this.m_framesToRemoveBefore.remove(frame);
                }
            }
            else {
                synchronized (this.m_framesToAddAfter) {
                    this.m_framesToAddAfter.add(frame);
                    frame.onFrameAdd(this, true);
                }
            }
        }
        else {
            synchronized (this.m_frames) {
                if (!this.m_frames.contains(frame)) {
                    this.m_frames.add(0, frame);
                    frame.onFrameAdd(this, false);
                }
            }
        }
    }
    
    public void removeFrame(final MessageFrame frame) {
        if (this.m_runningFrame) {
            final boolean bAddAfter;
            synchronized (this.m_framesToAddAfter) {
                bAddAfter = this.m_framesToAddAfter.contains(frame);
            }
            if (bAddAfter) {
                synchronized (this.m_framesToRemoveBefore) {
                    if (!this.m_framesToRemoveBefore.contains(frame)) {
                        this.m_framesToRemoveBefore.add(frame);
                    }
                }
                synchronized (this.m_framesToAddBefore) {
                    if (this.m_framesToAddBefore.remove(frame)) {
                        frame.onFrameRemove(this, false);
                    }
                }
            }
            else {
                synchronized (this.m_framesToRemoveAfter) {
                    if (!this.m_framesToRemoveAfter.contains(frame)) {
                        this.m_framesToRemoveAfter.add(frame);
                        frame.onFrameRemove(this, true);
                    }
                }
            }
        }
        else {
            synchronized (this.m_frames) {
                if (this.m_frames.remove(frame)) {
                    frame.onFrameRemove(this, false);
                }
            }
        }
    }
    
    public void removeAllFrames() {
        final Object[] frames;
        synchronized (this.m_frames) {
            frames = this.m_frames.toArray();
        }
        if (this.m_runningFrame) {
            synchronized (this.m_framesToRemoveAfter) {
                for (final Object frameObject : frames) {
                    final MessageFrame frame = (MessageFrame)frameObject;
                    if (!this.m_framesToRemoveAfter.contains(frame)) {
                        this.m_framesToRemoveAfter.add(frame);
                        frame.onFrameRemove(this, true);
                    }
                }
            }
        }
        else {
            synchronized (this.m_frames) {
                this.m_frames.clear();
            }
            for (final Object frameObject2 : frames) {
                final MessageFrame frame2 = (MessageFrame)frameObject2;
                frame2.onFrameRemove(this, false);
            }
        }
    }
    
    public boolean hasFrame(final MessageFrame frame) {
        boolean bHashFrame;
        if (this.m_runningFrame) {
            bHashFrame = this.m_frames.contains(frame);
        }
        else {
            synchronized (this.m_frames) {
                bHashFrame = this.m_frames.contains(frame);
            }
        }
        if (!bHashFrame) {
            synchronized (this.m_framesToAddBefore) {
                bHashFrame = this.m_framesToAddBefore.contains(frame);
            }
        }
        if (!bHashFrame) {
            synchronized (this.m_framesToAddAfter) {
                bHashFrame = this.m_framesToAddAfter.contains(frame);
            }
        }
        return bHashFrame;
    }
    
    public void setRunningFrame(final boolean runningFrame) {
        this.m_runningFrame = runningFrame;
    }
    
    public ArrayList<MessageFrame> getFrames() {
        return this.m_frames;
    }
    
    static {
        m_logger = Logger.getLogger((Class)FrameHandler.class);
    }
}
