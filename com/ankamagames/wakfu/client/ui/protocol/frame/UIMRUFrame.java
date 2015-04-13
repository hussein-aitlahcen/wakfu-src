package com.ankamagames.wakfu.client.ui.protocol.frame;

import com.ankamagames.framework.kernel.events.*;
import com.ankamagames.xulor2.util.*;
import org.apache.log4j.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.baseImpl.graphics.alea.animatedElement.*;
import com.ankamagames.wakfu.client.core.game.actor.*;
import com.ankamagames.wakfu.client.ui.mru.*;
import com.ankamagames.wakfu.client.core.game.interactiveElement.*;
import com.ankamagames.wakfu.client.core.game.ressource.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.baseImpl.client.proxyclient.base.game.interactiveElement.*;
import com.ankamagames.framework.kernel.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.wakfu.client.alea.*;
import java.util.*;

public class UIMRUFrame implements MessageFrame, MRUManager
{
    private static final Logger m_logger;
    private static final UIMRUFrame m_instance;
    private static final boolean DISABLE_COLLECT = false;
    private MRU m_currentOpenedMRU;
    
    public static UIMRUFrame getInstance() {
        return UIMRUFrame.m_instance;
    }
    
    private UIMRUFrame() {
        super();
        this.m_currentOpenedMRU = null;
    }
    
    public MRU getCurrentMRU() {
        return this.m_currentOpenedMRU;
    }
    
    @Override
    public void closeCurrentMRU() {
        if (this.m_currentOpenedMRU != null) {
            this.m_currentOpenedMRU.closeMRUWidget();
            this.m_currentOpenedMRU = null;
        }
    }
    
    public void setCurrentMRU(final MRU mru) {
        this.m_currentOpenedMRU = mru;
    }
    
    @Override
    public boolean onMessage(final Message message) {
        return true;
    }
    
    public static void addToMRU(final MRU mru, final AnimatedInteractiveElement elem) {
        if (elem instanceof CharacterActor) {
            final CharacterActor characterActor = (CharacterActor)elem;
            final CharacterInfo character = characterActor.getCharacterInfo();
            if (character != null) {
                mru.add(character, elem);
            }
            else {
                UIMRUFrame.m_logger.error((Object)"Le mobile s\u00e9lectionn\u00e9 n'a pas de CharacterInfo associ\u00e9 !");
            }
        }
        else if (elem instanceof WakfuClientInteractiveAnimatedElementSceneView) {
            final WakfuClientInteractiveAnimatedElementSceneView IEView = (WakfuClientInteractiveAnimatedElementSceneView)elem;
            final ClientMapInteractiveElement interactiveElement = IEView.getInteractiveElement();
            if (interactiveElement.isUsable()) {
                mru.add((MRUable)interactiveElement, elem);
            }
        }
        else if (elem instanceof Resource) {
            mru.add(((Resource)elem).getMRUSkillActions(), elem);
        }
        else if (elem instanceof MRUable) {
            mru.add((MRUable)elem, elem);
        }
    }
    
    @Override
    public void onFrameAdd(final FrameHandler frameHandler, final boolean isAboutToBeAdded) {
        if (!isAboutToBeAdded) {
            Xulor.getInstance().setMruManager(this);
        }
    }
    
    @Override
    public void onFrameRemove(final FrameHandler frameHandler, final boolean isAboutToBeRemoved) {
        if (!isAboutToBeRemoved) {
            this.m_currentOpenedMRU = null;
        }
    }
    
    @Override
    public long getId() {
        return 0L;
    }
    
    @Override
    public void setId(final long id) {
    }
    
    public void checkForMRUable(final WakfuWorldScene worldScene, final int x, final int y) {
        final ArrayList<AnimatedInteractiveElement> displayedElementsMouseOver = worldScene.selectAllNearestElement(x, y);
        if (displayedElementsMouseOver.isEmpty()) {
            return;
        }
        this.m_currentOpenedMRU = new MRU();
        for (int i = 0, size = displayedElementsMouseOver.size(); i < size; ++i) {
            final AnimatedInteractiveElement elem = displayedElementsMouseOver.get(i);
            addToMRU(this.m_currentOpenedMRU, elem);
        }
        if (this.m_currentOpenedMRU.isDisplayable()) {
            this.m_currentOpenedMRU.display();
            return;
        }
        this.m_currentOpenedMRU = null;
    }
    
    static {
        m_logger = Logger.getLogger((Class)UIMRUFrame.class);
        m_instance = new UIMRUFrame();
    }
}
