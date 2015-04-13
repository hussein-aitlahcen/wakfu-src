package com.ankamagames.wakfu.client.ui.protocol.frame;

import com.ankamagames.framework.kernel.events.*;
import org.apache.log4j.*;
import com.ankamagames.baseImpl.graphics.ui.event.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.wakfu.client.ui.protocol.message.dimensionalBag.*;
import com.ankamagames.wakfu.common.game.personalSpace.impl.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.framework.reflect.*;
import com.ankamagames.wakfu.client.ui.protocol.message.*;
import com.ankamagames.baseImpl.client.proxyclient.base.chat.*;
import com.ankamagames.framework.kernel.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.wakfu.client.ui.*;
import com.ankamagames.wakfu.client.ui.actions.*;
import com.ankamagames.wakfu.client.sound.*;
import com.ankamagames.wakfu.common.game.personalSpace.impl.rights.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.client.core.game.dimensionalBag.*;

public class UIDimensionalBagRoomAdministrationFrame implements MessageFrame
{
    private static final Logger m_logger;
    private static final UIDimensionalBagRoomAdministrationFrame m_instance;
    private DimBagRightsView m_dimBagRightsView;
    private DialogUnloadListener m_dialogUnloadListener;
    
    public static UIDimensionalBagRoomAdministrationFrame getInstance() {
        return UIDimensionalBagRoomAdministrationFrame.m_instance;
    }
    
    @Override
    public boolean onMessage(final Message message) {
        switch (message.getId()) {
            case 17002: {
                final UIDimensionalBagChangeRoomPermissionMessage mess = (UIDimensionalBagChangeRoomPermissionMessage)message;
                DimBagGroupRight groupRight = this.m_dimBagRightsView.getBag().getGroupRight(GroupType.GUILD);
                if (groupRight == null) {
                    groupRight = new DimBagGroupRight(GroupType.GUILD);
                    this.m_dimBagRightsView.addGroupRight(groupRight);
                }
                if (mess.getBooleanValue()) {
                    groupRight.allow(GemType.getFromItemReferenceId(mess.getIntValue()));
                }
                else {
                    groupRight.disallow(GemType.getFromItemReferenceId(mess.getIntValue()));
                }
                PropertiesProvider.getInstance().firePropertyValueChanged(mess.getRoomView(), "guildPerms");
                return false;
            }
            case 17004: {
                final UIDimensionalBagChangeRoomPermissionMessage mess = (UIDimensionalBagChangeRoomPermissionMessage)message;
                DimBagGroupRight groupRight = this.m_dimBagRightsView.getBag().getGroupRight(GroupType.ALL);
                if (groupRight == null) {
                    groupRight = new DimBagGroupRight(GroupType.ALL);
                    this.m_dimBagRightsView.addGroupRight(groupRight);
                }
                if (mess.getBooleanValue()) {
                    groupRight.allow(GemType.getFromItemReferenceId(mess.getIntValue()));
                }
                else {
                    groupRight.disallow(GemType.getFromItemReferenceId(mess.getIntValue()));
                }
                PropertiesProvider.getInstance().firePropertyValueChanged(mess.getRoomView(), "anonymousPerms");
                return false;
            }
            case 17000: {
                final UIMessage mess2 = (UIMessage)message;
                final String characterName = mess2.getStringValue();
                final CharacterInfo character = CharacterInfoManager.getInstance().getPlayerCharacterByName(characterName);
                if (character == null) {
                    ChatManager.getInstance().pushMessage(WakfuTranslator.getInstance().getString("error.playerUnknownInDimensionalBag", characterName), 3);
                    return false;
                }
                this.m_dimBagRightsView.addIndividualRight(new DimBagIndividualRight(character.getId(), characterName));
                return false;
            }
            case 17001: {
                final UIMessage mess2 = (UIMessage)message;
                this.m_dimBagRightsView.removeIndividualRight(mess2.getLongValue());
                return false;
            }
            case 17005: {
                final UIDimensionalBagChangeRoomPermissionMessage mess = (UIDimensionalBagChangeRoomPermissionMessage)message;
                final DimBagIndividualRight individualRight = this.m_dimBagRightsView.getBag().getIndividualRight(mess.getLongValue());
                final GemType gemType = GemType.getFromItemReferenceId(mess.getIntValue());
                if (mess.getBooleanValue()) {
                    individualRight.allow(gemType);
                }
                else {
                    individualRight.disallow(gemType);
                }
                mess.getRoomView().updateIndividualPerms();
                return false;
            }
            default: {
                return true;
            }
        }
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
        if (!isAboutToBeAdded) {
            this.m_dialogUnloadListener = new DialogUnloadListener() {
                @Override
                public void dialogUnloaded(final String id) {
                    if (id.equals("dimensionalBagRoomAdministrationDialog")) {
                        WakfuGameEntity.getInstance().removeFrame(UIDimensionalBagRoomAdministrationFrame.getInstance());
                    }
                }
            };
            Xulor.getInstance().addDialogUnloadListener(this.m_dialogUnloadListener);
            final DimBagRights bagPermissions = WakfuGameEntity.getInstance().getLocalPlayer().getVisitingDimentionalBag().getBagPermissions();
            if (bagPermissions != null) {
                this.m_dimBagRightsView = new DimBagRightsView(bagPermissions);
                PropertiesProvider.getInstance().setPropertyValue("roomPermissions", this.m_dimBagRightsView);
            }
            PropertiesProvider.getInstance().setPropertyValue("roomViewDisplayed", null);
            Xulor.getInstance().load("dimensionalBagRoomAdministrationDialog", Dialogs.getDialogPath("dimensionalBagRoomAdministrationDialog"), 32769L, (short)10000);
            Xulor.getInstance().putActionClass("wakfu.roomAdministration", RoomAdministrationDialogActions.class);
            WakfuSoundManager.getInstance().windowFadeIn();
        }
    }
    
    @Override
    public void onFrameRemove(final FrameHandler frameHandler, final boolean isAboutToBeRemoved) {
        if (!isAboutToBeRemoved) {
            Xulor.getInstance().unload("dimensionalBagRoomAdministrationDialog");
            Xulor.getInstance().unload("dimensionalBagRoomIndividualRightsDialog");
            final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
            final DimensionalBagView visitingDimentionalBag = localPlayer.getVisitingDimentionalBag();
            if (visitingDimentionalBag != null) {
                visitingDimentionalBag.setBagPermissions(this.m_dimBagRightsView.getBag());
            }
            this.m_dimBagRightsView = null;
            Xulor.getInstance().removeDialogUnloadListener(this.m_dialogUnloadListener);
            PropertiesProvider.getInstance().removeProperty("roomPermissions");
            PropertiesProvider.getInstance().removeProperty("roomViewDisplayed");
            Xulor.getInstance().removeActionClass("wakfu.roomAdministration");
            WakfuSoundManager.getInstance().windowFadeOut();
        }
    }
    
    @Override
    public String toString() {
        return "UIDimensionalBagRoomAdministrationFrame{m_dimBagRightsView=" + this.m_dimBagRightsView + ", m_dialogUnloadListener=" + this.m_dialogUnloadListener + '}';
    }
    
    static {
        m_logger = Logger.getLogger((Class)UIDimensionalBagRoomAdministrationFrame.class);
        m_instance = new UIDimensionalBagRoomAdministrationFrame();
    }
}
