package com.ankamagames.wakfu.client.ui.protocol.frame;

import com.ankamagames.framework.kernel.events.*;
import org.apache.log4j.*;
import com.ankamagames.wakfu.client.core.game.hero.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.framework.kernel.*;
import com.ankamagames.wakfu.common.game.hero.*;
import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.xulor2.property.*;
import java.util.*;

public class UIHeroesFrame implements MessageFrame
{
    private static final Logger m_logger;
    public static final UIHeroesFrame INSTANCE;
    private HeroesManagerClientListener m_listener;
    
    @Override
    public boolean onMessage(final Message message) {
        return true;
    }
    
    @Override
    public void onFrameAdd(final FrameHandler frameHandler, final boolean isAboutToBeAdded) {
        if (!isAboutToBeAdded) {
            this.m_listener = new HeroesManagerClientListener();
            HeroesManager.INSTANCE.addListener((HeroesManagerListener<BasicCharacterInfo>)this.m_listener);
            final List<PlayerCharacter> local = new ArrayList<PlayerCharacter>();
            local.add(WakfuGameEntity.getInstance().getLocalPlayer());
            PropertiesProvider.getInstance().setPropertyValue("heroesParty", local);
        }
    }
    
    @Override
    public void onFrameRemove(final FrameHandler frameHandler, final boolean isAboutToBeRemoved) {
        if (!isAboutToBeRemoved) {
            HeroesManager.INSTANCE.removeListener((HeroesManagerListener<BasicCharacterInfo>)this.m_listener);
            this.m_listener = null;
        }
    }
    
    @Override
    public long getId() {
        return 0L;
    }
    
    @Override
    public void setId(final long id) {
    }
    
    static {
        m_logger = Logger.getLogger((Class)UIHeroesFrame.class);
        INSTANCE = new UIHeroesFrame();
    }
}
