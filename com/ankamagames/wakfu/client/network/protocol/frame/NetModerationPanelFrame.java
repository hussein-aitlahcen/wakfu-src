package com.ankamagames.wakfu.client.network.protocol.frame;

import com.ankamagames.framework.kernel.events.*;
import org.apache.log4j.*;
import com.ankamagames.wakfu.client.moderationNew.panel.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;
import java.util.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.time.calendar.*;
import com.ankamagames.framework.kernel.*;
import java.text.*;

public class NetModerationPanelFrame implements MessageFrame
{
    protected static final Logger m_logger;
    private static final NetModerationPanelFrame m_instance;
    private static final ModerationPanelView m_panel;
    
    public static NetModerationPanelFrame getInstance() {
        return NetModerationPanelFrame.m_instance;
    }
    
    @Override
    public boolean onMessage(final Message message) {
        Label_0427: {
            switch (message.getId()) {
                case 105: {
                    final ModerationCommandResultMessage msg = (ModerationCommandResultMessage)message;
                    this.addTologs(msg.getMessage());
                    return true;
                }
                case 109: {
                    final ModerationCommandResultNewMessage msg2 = (ModerationCommandResultNewMessage)message;
                    final short command = msg2.getCommand();
                    switch (command) {
                        case 1: {
                            final byte whoType = msg2.extractByteParameter();
                            if (whoType == 1) {
                                final int pseudoListSize = msg2.extractIntParameter();
                                if (pseudoListSize == 0) {
                                    this.addTologs("No character found");
                                }
                                final LinkedList<String> result = new LinkedList<String>();
                                for (int i = 0; i < pseudoListSize; ++i) {
                                    result.add(msg2.extractStringParameter());
                                }
                                Collections.sort(result, new WhoComparator());
                                NetModerationPanelFrame.m_panel.setSearchList(result);
                                if (pseudoListSize == 1) {
                                    UIModerationPanelFrame.INSTANCE.getModerationPanelView().addPlayerTab(result.get(0));
                                }
                                return false;
                            }
                            if (whoType != 3) {
                                break Label_0427;
                            }
                            NetModerationPanelFrame.m_panel.getCurrentPlayer().setAll(msg2.extractByteParameter(), msg2.extractStringParameter(), msg2.extractLongParameter(), msg2.extractStringParameter(), msg2.extractLongParameter(), msg2.extractShortParameter(), msg2.extractShortParameter(), msg2.extractLongParameter(), msg2.extractIntParameter(), msg2.extractIntParameter(), msg2.extractIntParameter(), msg2.extractShortParameter(), msg2.extractStringParameter(), msg2.extractIntParameter(), msg2.extractIntParameter(), msg2.extractIntParameter(), msg2.extractShortParameter(), msg2.extractStringParameter());
                            final long guildId = UIModerationPanelFrame.INSTANCE.getModerationPanelView().getCurrentPlayer().getGuildId();
                            if (guildId <= 0L) {
                                return false;
                            }
                            UIModerationPanelFrame.INSTANCE.requestWithServer((short)213, (byte)6, guildId);
                            return false;
                        }
                        case 211: {
                            NetModerationPanelFrame.m_panel.setGameServer(msg2.extractStringParameter());
                            return false;
                        }
                        case 210: {
                            final int moderatorsNumber = msg2.extractIntParameter();
                            final ArrayList<String> moderatorsList = new ArrayList<String>();
                            for (int j = 0; j < moderatorsNumber; ++j) {
                                moderatorsList.add(msg2.extractStringParameter());
                            }
                            NetModerationPanelFrame.m_panel.setModeratorsList(moderatorsList);
                            return false;
                        }
                        case 213: {
                            UIModerationPanelFrame.INSTANCE.getModerationPanelView().getCurrentPlayer().setGuild(msg2.extractStringParameter());
                            return false;
                        }
                    }
                    break;
                }
            }
        }
        return true;
    }
    
    private void addTologs(final String message) {
        final GameDateConst tmp = BaseGameDateProvider.INSTANCE.getDate();
        final String time = "[" + tmp.getHours() + ':' + tmp.getMinutes() + "] ";
        UIModerationPanelFrame.INSTANCE.getModerationPanelView().addToLogs(time + message);
    }
    
    @Override
    public long getId() {
        return 0L;
    }
    
    @Override
    public void setId(final long id) {
    }
    
    @Override
    public void onFrameAdd(final FrameHandler frameHandler, final boolean isAboutToBeAdded) {
    }
    
    @Override
    public void onFrameRemove(final FrameHandler frameHandler, final boolean isAboutToBeRemoved) {
    }
    
    static {
        m_logger = Logger.getLogger((Class)NetModerationPanelFrame.class);
        m_instance = new NetModerationPanelFrame();
        m_panel = UIModerationPanelFrame.INSTANCE.getModerationPanelView();
    }
    
    private class WhoComparator implements Comparator<String>
    {
        @Override
        public int compare(final String string1, final String string2) {
            return Collator.getInstance().compare(string1, string2);
        }
    }
}
