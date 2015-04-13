package com.ankamagames.wakfu.client.ui.protocol.frame;

import com.ankamagames.framework.kernel.events.*;
import org.apache.log4j.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.client.alea.highlightingCells.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.wakfu.client.ui.protocol.message.worldScene.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.core.game.actor.characterActorHelpers.*;
import com.ankamagames.xulor2.core.graphicalMouse.*;
import com.ankamagames.xulor2.core.*;
import com.ankamagames.framework.kernel.*;
import com.ankamagames.wakfu.client.core.game.actor.*;
import com.ankamagames.framework.ai.*;
import com.ankamagames.baseImpl.graphics.*;
import com.ankamagames.wakfu.client.alea.*;
import com.ankamagames.wakfu.client.*;
import com.ankamagames.baseImpl.graphics.alea.animatedElement.*;
import com.ankamagames.xulor2.util.alignment.*;
import com.ankamagames.wakfu.common.game.effect.*;

public abstract class UIAbstractFightCastInteractionFrame implements MessageFrame
{
    private static Logger m_logger;
    protected CharacterInfo m_character;
    protected RangeAndEffectDisplayer m_rangeDisplayer;
    public static final byte ITEM_TYPE = 0;
    public static final byte SPELL_TYPE = 1;
    public static final byte SPELL_AND_ITEM_TYPE = 2;
    private Point3 m_lastOverHeadTarget;
    private boolean m_frameAdded;
    
    protected UIAbstractFightCastInteractionFrame() {
        super();
        this.m_character = null;
        this.m_rangeDisplayer = null;
        this.m_lastOverHeadTarget = null;
        this.m_frameAdded = false;
    }
    
    public void setCharacter(final CharacterInfo fighter) {
        this.m_character = fighter;
    }
    
    protected abstract EffectContainer getEffectContainer();
    
    protected abstract void sendCastMessage(final int p0, final int p1, final short p2);
    
    protected abstract void updateUsage();
    
    protected abstract byte getCastType();
    
    @Override
    public long getId() {
        return 0L;
    }
    
    @Override
    public void setId(final long id) {
    }
    
    public void selectRange() {
        if (this.m_rangeDisplayer != null) {
            this.m_rangeDisplayer.clearZoneEffectAndRange();
        }
        this.refreshCursorInfos();
    }
    
    protected abstract String getMouseText();
    
    protected abstract String getCastMouseIcon();
    
    @Override
    public boolean onMessage(final Message message) {
        switch (message.getId()) {
            case 18015: {
                this.onCellChanged();
                return true;
            }
            case 19992: {
                if (this.m_character != null) {
                    final UIWorldSceneMouseMessage msg = (UIWorldSceneMouseMessage)message;
                    if (msg.isButtonLeft()) {
                        this.selectTargetedPosition();
                    }
                    WakfuGameEntity.getInstance().removeFrame(this);
                }
                return false;
            }
            default: {
                return true;
            }
        }
    }
    
    private void onCellChanged() {
        this.refreshRangeDisplay();
        final CharacterInfo previousCharacter = (CharacterInfo)this.m_character.getCurrentFight().getCharacterInfoAtPosition(this.m_lastOverHeadTarget);
        final CharacterInfo character = (CharacterInfo)this.m_character.getCurrentFight().getCharacterInfoAtPosition(UIFightFrame.getLastTarget());
        if (character == previousCharacter) {
            return;
        }
        if (previousCharacter != null) {
            CharacterActorSelectionChangeListener.unselect(previousCharacter.getActor());
        }
        if (character != null) {
            CharacterActorSelectionChangeListener.select(character.getActor());
            this.m_lastOverHeadTarget = new Point3(UIFightFrame.getLastTarget());
        }
        else {
            this.m_lastOverHeadTarget = null;
        }
        this.refreshCursorInfos();
    }
    
    private void refreshCursorInfos() {
        GraphicalMouseManager.getInstance().hide();
        final String iconName = this.getCastMouseIcon();
        if (iconName != null) {
            this.showCastMouseInfos(iconName, this.getMouseText());
        }
        else {
            CursorFactory.getInstance().unlock();
        }
        this.refreshCursorText();
    }
    
    private void refreshCursorText() {
        GraphicalMouseManager.getInstance().setText(this.getMouseText());
    }
    
    public void selectTargetedPosition() {
        boolean isValid = false;
        int castPositionX = 0;
        int castPositionY = 0;
        short castPositionZ = 0;
        if (this.m_rangeDisplayer.rangeContains(UIFightFrame.m_lastTarget)) {
            castPositionX = UIFightFrame.m_lastTarget.getX();
            castPositionY = UIFightFrame.m_lastTarget.getY();
            castPositionZ = UIFightFrame.m_lastTarget.getZ();
            isValid = true;
        }
        if (isValid) {
            this.sendCastMessage(castPositionX, castPositionY, castPositionZ);
            this.updateUsage();
        }
        WakfuGameEntity.getInstance().removeFrame(this);
    }
    
    public boolean isFrameAdded() {
        return this.m_frameAdded;
    }
    
    @Override
    public void onFrameAdd(final FrameHandler frameHandler, final boolean isAboutToBeAdded) {
        if (!this.m_frameAdded) {
            this.m_frameAdded = true;
            UIFightMovementFrame.getInstance().clearPathSelection();
            if (WakfuGameEntity.getInstance().hasFrame(UIFightMovementFrame.getInstance())) {
                WakfuGameEntity.getInstance().removeFrame(UIFightMovementFrame.getInstance());
            }
            final OverHeadTarget target = UIOverHeadInfosFrame.getInstance().getCurrentTarget();
            if (target != null && target instanceof CharacterActor) {
                final Point3 selectedCell = UIFightFrame.getLastTarget();
                final CharacterActor actor = (CharacterActor)target;
                if (DistanceUtils.getIntersectionDistance(actor, selectedCell) > 0) {
                    CharacterActorSelectionChangeListener.unselect(actor);
                }
            }
            this.onCellChanged();
        }
    }
    
    @Override
    public void onFrameRemove(final FrameHandler frameHandler, final boolean isAboutToBeRemoved) {
        if (this.m_frameAdded) {
            this.m_rangeDisplayer.clearZoneEffectAndRange();
            CursorFactory.getInstance().unlock();
            GraphicalMouseManager.getInstance().hide();
            WakfuGameEntity.getInstance().pushFrame(UIFightMovementFrame.getInstance());
            this.m_frameAdded = false;
            final OverHeadTarget target = UIOverHeadInfosFrame.getInstance().getCurrentTarget();
            if (target instanceof CharacterActor) {
                final CharacterActor actor = (CharacterActor)target;
                if (!actor.isSelected()) {
                    CharacterActorSelectionChangeListener.unselect(actor);
                }
            }
            final WakfuWorldScene worldScene = (WakfuWorldScene)WakfuClientInstance.getInstance().getWorldScene();
            final AnimatedInteractiveElement elementUnderMouse = worldScene.getNearestElement();
            if (elementUnderMouse != null && elementUnderMouse instanceof CharacterActor) {
                CharacterActorSelectionChangeListener.select((CharacterActor)elementUnderMouse);
            }
        }
    }
    
    private void showCastMouseInfos(final String iconUrl, final String text) {
        if (iconUrl == null) {
            return;
        }
        switch (this.getCastType()) {
            case 0:
            case 2: {
                CursorFactory.getInstance().show(CursorFactory.CursorType.HAND, true);
                break;
            }
            case 1: {
                CursorFactory.getInstance().show(CursorFactory.CursorType.CUSTOM3, true);
                break;
            }
        }
        GraphicalMouseManager.getInstance().showMouseInformation(iconUrl, text, 10, -30, Alignment9.NORTH_WEST);
    }
    
    public void refreshRangeDisplay() {
        final Point3 lastTarget = UIFightFrame.m_lastTarget;
        if (lastTarget != null && this.m_rangeDisplayer.rangeContains(lastTarget)) {
            this.m_rangeDisplayer.selectZoneEffect(this.getEffectContainer(), this.m_character, lastTarget);
        }
        else {
            this.m_rangeDisplayer.clearZoneEffect();
        }
    }
    
    static {
        UIAbstractFightCastInteractionFrame.m_logger = Logger.getLogger((Class)UIAbstractFightCastInteractionFrame.class);
    }
}
