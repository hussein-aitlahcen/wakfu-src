package com.ankamagames.wakfu.client.core.emote;

import com.ankamagames.wakfu.client.ui.protocol.frame.helpers.*;
import com.ankamagames.framework.kernel.events.*;
import com.ankamagames.baseImpl.graphics.alea.cellSelector.*;
import com.ankamagames.wakfu.client.core.game.actor.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.framework.kernel.*;
import com.ankamagames.xulor2.core.*;
import com.ankamagames.xulor2.core.graphicalMouse.*;
import com.ankamagames.xulor2.util.alignment.*;
import com.ankamagames.wakfu.client.alea.*;
import com.ankamagames.wakfu.client.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.wakfu.client.ui.protocol.message.worldScene.*;
import com.ankamagames.baseImpl.graphics.alea.utils.*;
import com.ankamagames.baseImpl.graphics.alea.display.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.baseImpl.graphics.alea.animatedElement.*;
import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.baseImpl.graphics.alea.mobile.*;
import com.ankamagames.framework.ai.targetfinder.*;

class EmoteTargetFrame extends UIFrameMouseKey implements MessageFrame
{
    public static final EmoteTargetFrame INSTANCE;
    private final ElementSelection m_selection;
    private CharacterActor m_character;
    private Emote m_emote;
    private EmoteRunnableHandler m_runnableHandler;
    private Point3 m_clickedPosition;
    
    EmoteTargetFrame() {
        super();
        this.m_selection = new ElementSelection("emoteSelectTarget", ClientEmoteHandler.GOOD_CELL_COLOR);
        this.m_clickedPosition = null;
    }
    
    @Override
    public void onFrameAdd(final FrameHandler frameHandler, final boolean isAboutToBeAdded) {
        if (isAboutToBeAdded) {
            return;
        }
        CursorFactory.getInstance().show(CursorFactory.CursorType.HAND, true);
        final String iconUrl = this.m_emote.getIconUrl();
        if (iconUrl != null) {
            GraphicalMouseManager.getInstance().showMouseInformation(iconUrl, null, 30, 0, Alignment9.WEST);
        }
        final WakfuWorldScene worldScene = (WakfuWorldScene)WakfuClientInstance.getInstance().getWorldScene();
        this.selectCell(worldScene.getMouseX(), worldScene.getMouseY());
    }
    
    @Override
    public void onFrameRemove(final FrameHandler frameHandler, final boolean isAboutToBeRemoved) {
        if (isAboutToBeRemoved) {
            return;
        }
        CursorFactory.getInstance().unlock();
        GraphicalMouseManager.getInstance().hide();
        this.m_selection.clear();
        if (this.m_character != null) {
            this.m_character.unHighlightCharacter();
        }
        this.m_clickedPosition = null;
    }
    
    @Override
    public boolean onMessage(final Message message) {
        switch (message.getId()) {
            case 19994: {
                final UIWorldSceneMouseMessage msg = (UIWorldSceneMouseMessage)message;
                return this.selectCell(msg.getMouseX(), msg.getMouseY());
            }
            case 19998: {
                return this.onMousePressed((UIWorldSceneMouseMessage)message);
            }
            case 19992: {
                return this.onMouseRelease((UIWorldSceneMouseMessage)message);
            }
            case 19995: {
                return false;
            }
            default: {
                return true;
            }
        }
    }
    
    private boolean selectCell(final int mouseX, final int mouseY) {
        final WakfuWorldScene worldScene = (WakfuWorldScene)WakfuClientInstance.getInstance().getWorldScene();
        final AnimatedInteractiveElement elt = worldScene.getNearestElement();
        this.m_selection.clear();
        if (this.m_character != null) {
            this.m_character.unHighlightCharacter();
        }
        this.m_character = null;
        if (!(elt instanceof CharacterActor) || !(((CharacterActor)elt).getCharacterInfo() instanceof PlayerCharacter)) {
            final Point3 pos = WorldSceneInteractionUtils.getNearestPoint3(worldScene, mouseX, mouseY, false);
            if (pos == null) {
                return true;
            }
            this.m_selection.setColor(ClientEmoteHandler.BAD_CELL_COLOR);
            this.m_selection.add(pos.getX(), pos.getY(), pos.getZ());
            return true;
        }
        else {
            final CharacterActor characterActor = (CharacterActor)elt;
            final BasicCharacterInfo character = characterActor.getCharacterInfo();
            final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
            if (character == localPlayer || character.isDead() || character.isOnFight() || character.getCurrentOccupation() != null) {
                return true;
            }
            (this.m_character = characterActor).highlightCharacter();
            this.m_selection.setColor(ClientEmoteHandler.GOOD_CELL_COLOR);
            this.m_selection.add(this.m_character.getWorldCellX(), this.m_character.getWorldCellY(), this.m_character.getWorldCellAltitude());
            return false;
        }
    }
    
    private boolean onMousePressed(final UIWorldSceneMouseMessage message) {
        if (!message.isButtonLeft()) {
            return false;
        }
        this.m_clickedPosition = this.getCellUnderMouse(message);
        return false;
    }
    
    private boolean onMouseRelease(final UIWorldSceneMouseMessage message) {
        if (!message.isButtonLeft()) {
            WakfuGameEntity.getInstance().removeFrame(this);
        }
        else if (this.canExecute(message)) {
            this.execute();
        }
        this.m_clickedPosition = null;
        return false;
    }
    
    private boolean canExecute(final UIWorldSceneMouseMessage message) {
        if (!message.isButtonLeft()) {
            return false;
        }
        final Point3 position = this.getCellUnderMouse(message);
        return this.m_clickedPosition != null && position != null && this.m_clickedPosition.equals(position.getX(), position.getY(), position.getZ()) && this.m_character != null;
    }
    
    private boolean execute() {
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        final ClientEmoteHandler emoteHandler = localPlayer.getEmoteHandler();
        if (!emoteHandler.canUseEmote(this.m_emote, this.m_runnableHandler)) {
            WakfuGameEntity.getInstance().removeFrame(this);
            return false;
        }
        final int distance = localPlayer.getPosition().getDistance(this.m_character.getCharacterInfo().getPosition());
        if (!this.m_emote.isMoveToTarget() || distance == 1) {
            this.m_runnableHandler.runEmote(this.m_emote, this.m_character.getId());
            WakfuGameEntity.getInstance().removeFrame(this);
            return false;
        }
        final MobileEndPathListener pathListener = new MobileEndPathListener() {
            @Override
            public void pathEnded(final PathMobile mobile, final int x, final int y, final short altitude) {
                final int distance = localPlayer.getPosition().getDistance(EmoteTargetFrame.this.m_character.getCharacterInfo().getPosition());
                if (distance == 1) {
                    EmoteTargetFrame.this.m_runnableHandler.runEmote(EmoteTargetFrame.this.m_emote, EmoteTargetFrame.this.m_character.getId());
                }
                mobile.removeEndPositionListener(this);
            }
        };
        final CharacterActor localPlayerActor = localPlayer.getActor();
        localPlayerActor.addEndPositionListener(pathListener);
        if (!localPlayer.moveNearTarget(this.m_character.getCharacterInfo(), true, true)) {
            localPlayerActor.removeEndPositionListener(pathListener);
        }
        WakfuGameEntity.getInstance().removeFrame(this);
        return false;
    }
    
    private Point3 getCellUnderMouse(final UIWorldSceneMouseMessage message) {
        final WakfuWorldScene worldScene = (WakfuWorldScene)WakfuClientInstance.getInstance().getWorldScene();
        return WorldSceneInteractionUtils.getNearestPoint3(worldScene, message.getMouseX(), message.getMouseY(), true);
    }
    
    @Override
    public long getId() {
        return 0L;
    }
    
    @Override
    public void setId(final long id) {
    }
    
    public void init(final Emote emote, final EmoteRunnableHandler emoteRunnableHandler) {
        if (this.m_emote == emote && WakfuGameEntity.getInstance().hasFrame(this)) {
            WakfuGameEntity.getInstance().removeFrame(this);
            return;
        }
        this.m_emote = emote;
        this.m_runnableHandler = emoteRunnableHandler;
        if (WakfuGameEntity.getInstance().hasFrame(this)) {
            WakfuGameEntity.getInstance().removeFrame(this);
        }
        WakfuGameEntity.getInstance().pushFrame(this);
    }
    
    static {
        INSTANCE = new EmoteTargetFrame();
    }
}
