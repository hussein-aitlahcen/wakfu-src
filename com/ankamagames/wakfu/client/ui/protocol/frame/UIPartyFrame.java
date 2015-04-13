package com.ankamagames.wakfu.client.ui.protocol.frame;

import com.ankamagames.framework.kernel.events.*;
import org.apache.log4j.*;
import com.ankamagames.baseImpl.graphics.ui.event.*;
import com.ankamagames.wakfu.client.ui.protocol.message.*;
import com.ankamagames.baseImpl.client.proxyclient.base.chat.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.core.world.*;
import com.ankamagames.baseImpl.common.clientAndServer.global.group.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.wakfu.client.core.game.group.party.*;
import com.ankamagames.wakfu.common.game.group.member.*;
import com.ankamagames.xulor2.core.messagebox.*;
import com.ankamagames.framework.kernel.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.wakfu.client.ui.*;
import com.ankamagames.wakfu.client.ui.actions.*;
import com.ankamagames.wakfu.client.sound.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.framework.kernel.core.common.message.scheduler.clock.*;
import com.ankamagames.wakfu.common.game.group.party.*;
import com.ankamagames.framework.reflect.*;
import gnu.trove.*;

public class UIPartyFrame implements MessageFrame
{
    protected static final Logger m_logger;
    private static UIPartyFrame m_instance;
    private DialogUnloadListener m_dialogUnloadListener;
    private PartyDisplayer m_partyDisplayer;
    private long m_partyHpRegenClock;
    
    public static UIPartyFrame getInstance() {
        return UIPartyFrame.m_instance;
    }
    
    @Override
    public boolean onMessage(final Message message) {
        switch (message.getId()) {
            case 19030: {
                final UIMessage uiMessage = (UIMessage)message;
                final long id = uiMessage.getLongValue();
                this.excludePartyMember(id);
                return false;
            }
            case 19031: {
                final UIMessage uiMessage = (UIMessage)message;
                final boolean follow = uiMessage.getBooleanValue();
                final long id2 = uiMessage.getLongValue();
                this.m_partyDisplayer.followMember(id2, follow);
                return false;
            }
            case 19032: {
                final UIMessage uiMessage = (UIMessage)message;
                final String memberName = uiMessage.getStringValue();
                this.m_partyDisplayer.addPartyMember(memberName);
                final String chatMessage = WakfuTranslator.getInstance().getString("group.party.invitationRequestSent", memberName);
                ChatManager.getInstance().pushMessage(chatMessage, 4);
                return false;
            }
            default: {
                return true;
            }
        }
    }
    
    public void excludePartyMember(final long id) {
        final PartyComportment party = WakfuGameEntity.getInstance().getLocalPlayer().getPartyComportment();
        final PartyMemberInterface member = party.getParty().getMember(id);
        if (member != null) {
            String warning;
            if (id == WakfuGameEntity.getInstance().getLocalPlayer().getId()) {
                warning = WakfuTranslator.getInstance().getString("group.party.leave.warn.you");
            }
            else {
                final String memberName = member.getName();
                warning = WakfuTranslator.getInstance().getString("group.party.leave.warn.others", memberName);
            }
            final WorldInfoManager.WorldInfo worldInfo = WorldInfoManager.getInstance().getInfo(member.getInstanceId());
            if (worldInfo != null && worldInfo.getGrouptype() == GroupType.PARTY) {
                warning = warning + "\n" + WakfuTranslator.getInstance().getString("group.party.leave.warn.dungeon");
            }
            final MessageBoxControler messageBoxControler = Xulor.getInstance().msgBox(warning, WakfuMessageBoxConstants.getMessageBoxIconUrl(0), 2073L, 102, 1);
            messageBoxControler.addEventListener(new MessageBoxEventListener() {
                @Override
                public void messageBoxClosed(final int type, final String userEntry) {
                    if (type == 8) {
                        UIPartyFrame.this.m_partyDisplayer.exclude(id);
                    }
                }
            });
        }
        else {
            UIPartyFrame.m_logger.error((Object)"on demande l'exclusion d'un type qui n'est pas dans le group en local");
        }
    }
    
    @Override
    public long getId() {
        return 0L;
    }
    
    @Override
    public void setId(final long id) {
    }
    
    public void updateMemberAppearance(final long characterId) {
        final PartyDisplayer.PartyMemberDisplayer member = this.m_partyDisplayer.getPartyMember(characterId);
        if (member != null) {
            member.resetIconUrl();
        }
    }
    
    public void onCharacterSpawn(final long characterId) {
        final PartyDisplayer.PartyMemberDisplayer member = this.m_partyDisplayer.getPartyMember(characterId);
        if (member != null) {
            member.onCharacterSpawn();
        }
    }
    
    @Override
    public void onFrameAdd(final FrameHandler frameHandler, final boolean isAboutToBeAdded) {
        if (!isAboutToBeAdded) {
            this.m_dialogUnloadListener = new DialogUnloadListener() {
                @Override
                public void dialogUnloaded(final String id) {
                    if (id.equals("partyListDialog")) {
                        WakfuGameEntity.getInstance().removeFrame(UIPartyFrame.getInstance());
                    }
                }
            };
            Xulor.getInstance().addDialogUnloadListener(this.m_dialogUnloadListener);
            final PartyComportment party = WakfuGameEntity.getInstance().getLocalPlayer().getPartyComportment();
            this.m_partyDisplayer = new PartyDisplayer(party);
            PropertiesProvider.getInstance().setPropertyValue("partyList", this.m_partyDisplayer);
            Xulor.getInstance().load("partyListDialog", Dialogs.getDialogPath("partyListDialog"), 8192L, (short)10000);
            Xulor.getInstance().putActionClass("wakfu.partyList", PartyListDialogActions.class);
            WakfuSoundManager.getInstance().windowFadeIn();
            this.m_partyHpRegenClock = MessageScheduler.getInstance().addClock(new MessageHandler() {
                @Override
                public boolean onMessage(final Message message) {
                    final PartyModelInterface mparty = party.getParty();
                    if (mparty == null) {
                        return false;
                    }
                    final TLongObjectIterator<PartyMemberInterface> it = mparty.getMembers().iterator();
                    while (it.hasNext()) {
                        it.advance();
                        final PlayerCharacter character = (PlayerCharacter)CharacterInfoManager.getInstance().getCharacter(it.key());
                        if (character != null) {
                            character.onHealthRegenTick(((ClockMessage)message).getTimeStamp());
                        }
                    }
                    return false;
                }
                
                @Override
                public long getId() {
                    return 1L;
                }
                
                @Override
                public void setId(final long id) {
                }
            }, 4000L, 159159161);
        }
    }
    
    @Override
    public void onFrameRemove(final FrameHandler frameHandler, final boolean isAboutToBeRemoved) {
        if (!isAboutToBeRemoved) {
            this.m_partyDisplayer = null;
            Xulor.getInstance().removeDialogUnloadListener(this.m_dialogUnloadListener);
            Xulor.getInstance().unload("partyListDialog");
            PropertiesProvider.getInstance().removeProperty("partyList");
            Xulor.getInstance().removeActionClass("wakfu.partyList");
            MessageScheduler.getInstance().removeClock(this.m_partyHpRegenClock);
        }
    }
    
    public void fireUpdate() {
        if (this.m_partyDisplayer != null) {
            final PartyComportment party = WakfuGameEntity.getInstance().getLocalPlayer().getPartyComportment();
            this.m_partyDisplayer.initialize(party);
            PropertiesProvider.getInstance().firePropertyValueChanged(this.m_partyDisplayer, PartyDisplayer.FIELDS);
        }
    }
    
    public void fireMemberStateUpdate(final long characterId) {
        if (this.m_partyDisplayer != null) {
            final PartyDisplayer.PartyMemberDisplayer member = this.m_partyDisplayer.getPartyMember(characterId);
            if (member != null) {
                member.fireStateChanged();
            }
        }
    }
    
    static {
        m_logger = Logger.getLogger((Class)UIPartyFrame.class);
        UIPartyFrame.m_instance = new UIPartyFrame();
    }
}
