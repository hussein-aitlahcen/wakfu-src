package com.ankamagames.wakfu.client.ui.protocol.frame;

import com.ankamagames.framework.kernel.events.*;
import org.apache.log4j.*;
import com.ankamagames.wakfu.client.core.game.interactiveElement.*;
import com.ankamagames.baseImpl.graphics.ui.event.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.wakfu.client.ui.protocol.message.*;
import com.ankamagames.wakfu.client.ui.protocol.message.guild.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer.group.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.common.game.characterInfo.*;
import com.ankamagames.xulor2.core.messagebox.*;
import com.ankamagames.framework.graphics.engine.texture.*;
import com.ankamagames.wakfu.common.datas.guild.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.wakfu.common.game.item.referenceItem.*;
import com.ankamagames.framework.kernel.*;
import com.ankamagames.wakfu.client.ui.*;
import com.ankamagames.wakfu.client.ui.actions.*;
import com.ankamagames.wakfu.client.core.game.group.guild.*;

public class UIGuildCreatorFrame implements MessageFrame
{
    protected static final Logger m_logger;
    private static UIGuildCreatorFrame m_instance;
    private GuildBannerGenerator.GuildBannerColorType m_currentType;
    private GuildBannerData m_guildBannerData;
    private GuildMachine m_machine;
    private DialogUnloadListener m_dialogUnloadListener;
    
    public UIGuildCreatorFrame() {
        super();
        this.m_currentType = GuildBannerGenerator.GuildBannerColorType.FOREGROUND;
    }
    
    public static UIGuildCreatorFrame getInstance() {
        return UIGuildCreatorFrame.m_instance;
    }
    
    @Override
    public boolean onMessage(final Message message) {
        switch (message.getId()) {
            case 18200: {
                final UIGuildBannerSetColorMessage msg = (UIGuildBannerSetColorMessage)message;
                switch (this.m_currentType) {
                    case BACKGROUND: {
                        this.m_guildBannerData.setBackground(msg.getColor());
                        PropertiesProvider.getInstance().setPropertyValue("guildBannerPrimaryColor", this.m_guildBannerData.getBackground().toString());
                        break;
                    }
                    case FOREGROUND: {
                        this.m_guildBannerData.setForeground(msg.getColor());
                        PropertiesProvider.getInstance().setPropertyValue("guildBannerSecondaryColor", this.m_guildBannerData.getForeground().toString());
                        break;
                    }
                }
                this.regenerateTextureProperty();
                return false;
            }
            case 18203: {
                final UIGuildBannerSetColorMessage msg = (UIGuildBannerSetColorMessage)message;
                this.m_currentType = msg.getType();
                return false;
            }
            case 18201: {
                final UIMessage msg2 = (UIMessage)message;
                if (msg2.getIntValue() < 0) {
                    GuildBannerGenerator.getInstance().applyPreviousBackground(this.m_guildBannerData);
                }
                else {
                    GuildBannerGenerator.getInstance().applyNextBackground(this.m_guildBannerData);
                }
                this.regenerateTextureProperty();
                return false;
            }
            case 18202: {
                final UIMessage msg2 = (UIMessage)message;
                if (msg2.getIntValue() < 0) {
                    GuildBannerGenerator.getInstance().applyPreviousForeground(this.m_guildBannerData);
                }
                else {
                    GuildBannerGenerator.getInstance().applyNextForeground(this.m_guildBannerData);
                }
                this.regenerateTextureProperty();
                return false;
            }
            case 18204: {
                final UIGuildCreateMessage msg3 = (UIGuildCreateMessage)message;
                final String name = NameChecker.doNameCorrection(msg3.getName());
                final NameCheckerResult result = this.isNameValid(name);
                final NameChecker.NameResult validity = result.getResult();
                if (validity != NameChecker.NameResult.OK) {
                    String errorText = null;
                    switch (validity) {
                        case ERROR_FORBIDDEN_NAME: {
                            errorText = WakfuTranslator.getInstance().getString("error.guild.creation.forbiddenName");
                            break;
                        }
                        case ERROR_INVALID_DASH_POSITION: {
                            errorText = WakfuTranslator.getInstance().getString("error.guild.creation.invalidDashPosition");
                            break;
                        }
                        case ERROR_TOO_MANY_CONSECUTIVE_CONSONANT: {
                            errorText = WakfuTranslator.getInstance().getString("error.characterCreation.tooManyConsonant");
                            break;
                        }
                        case ERROR_TOO_MANY_CONSECUTIVE_VOWEL: {
                            errorText = WakfuTranslator.getInstance().getString("error.characterCreation.tooManyVowel");
                            break;
                        }
                        case ERROR_NAME_TOO_LONG: {
                            errorText = WakfuTranslator.getInstance().getString("error.characterCreation.nameTooLong");
                            break;
                        }
                        case ERROR_NAME_TOO_SHORT: {
                            errorText = WakfuTranslator.getInstance().getString("error.characterCreation.nameTooShort");
                            break;
                        }
                        case ERROR_BAD_CHAR: {
                            errorText = WakfuTranslator.getInstance().getString("error.characterCreation.badChar");
                            break;
                        }
                        case ERROR_TOO_MANY_CONSECUTIVE_IDENTICAL: {
                            errorText = WakfuTranslator.getInstance().getString("error.characterCreation.tooManyConsecutiveIdentical");
                            break;
                        }
                        case ERROR_TOO_FEW_VOWEL_IN_PART: {
                            errorText = WakfuTranslator.getInstance().getString("error.characterCreation.tooFewVowelInPart");
                            break;
                        }
                        case ERROR_TOO_FEW_CONSONANT_IN_PART: {
                            errorText = WakfuTranslator.getInstance().getString("error.characterCreation.tooFewConsonantInPart");
                            break;
                        }
                        case ERROR_TOO_MANY_SPECIAL_IN_PART: {
                            errorText = WakfuTranslator.getInstance().getString("error.characterCreation.tooManySpecialInPart");
                            break;
                        }
                        case ERROR_PART_TOO_LONG: {
                            errorText = WakfuTranslator.getInstance().getString("error.characterCreation.parTooLong");
                            break;
                        }
                        case ERROR_TOO_MANY_SPECIAL: {
                            errorText = WakfuTranslator.getInstance().getString("error.characterCreation.tooManySpecial");
                            break;
                        }
                        case ERROR_NAME_WITH_BAD_CASE: {
                            errorText = WakfuTranslator.getInstance().getString("error.guild.creation.nameWithBadCase");
                            break;
                        }
                        default: {
                            errorText = WakfuTranslator.getInstance().getString("error.guild.creation.invalidName");
                            break;
                        }
                    }
                    Xulor.getInstance().msgBox(errorText, WakfuMessageBoxConstants.getMessageBoxIconUrl(1), 2L, 102, 3);
                    return false;
                }
                final String checkedName = name;
                final String guildCreationMessage = WakfuTranslator.getInstance().getString("guild.creation.validate", checkedName);
                final MessageBoxControler controler = Xulor.getInstance().msgBox(guildCreationMessage, WakfuMessageBoxConstants.getMessageBoxIconUrl(4), 6L, 102, 3);
                controler.addEventListener(new MessageBoxEventListener() {
                    @Override
                    public void messageBoxClosed(final int type, final String userEntry) {
                        if (type != 2) {
                            return;
                        }
                        final CreateGuildRequestMessage netmsg = new CreateGuildRequestMessage(checkedName, UIGuildCreatorFrame.this.m_guildBannerData.getBlazon(), msg3.getBooleanValue());
                        WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(netmsg);
                    }
                });
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
    
    private NameCheckerResult isNameValid(final String name) {
        return NameChecker.guildNameValidity(name);
    }
    
    private void regenerateTextureProperty() {
        final Texture tex = GuildBannerGenerator.getInstance().getGuildBannerTexture(this.m_guildBannerData);
        PropertiesProvider.getInstance().setPropertyValue("guildBannerTexture", tex);
    }
    
    private void initProperties() {
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        final int cash = localPlayer.getWallet().getAmountOfCash();
        final int guildogemId = GuildDataManager.INSTANCE.getCreationItemId();
        final int guildCreationFee = GuildDataManager.INSTANCE.getCreationKamaCost();
        final Item item = localPlayer.getBags().getFirstItemFromInventoryFromRefId(guildogemId);
        final AbstractReferenceItem guildogem = ReferenceItemManager.getInstance().getReferenceItem(guildogemId);
        PropertiesProvider.getInstance().setPropertyValue("guildCreation.hasItem", item != null);
        PropertiesProvider.getInstance().setPropertyValue("guildCreation.itemDescription", guildogem);
        PropertiesProvider.getInstance().setPropertyValue("guildCreation.hasMoney", cash >= guildCreationFee);
        PropertiesProvider.getInstance().setPropertyValue("guildCreation.feeDescription", guildCreationFee);
    }
    
    @Override
    public void onFrameAdd(final FrameHandler frameHandler, final boolean isAboutToBeAdded) {
        if (!isAboutToBeAdded) {
            this.m_dialogUnloadListener = new DialogUnloadListener() {
                @Override
                public void dialogUnloaded(final String id) {
                    if (id.equals("guildCreatorDialog")) {
                        WakfuGameEntity.getInstance().removeFrame(UIGuildCreatorFrame.getInstance());
                    }
                }
            };
            this.m_machine.notifyViews();
            this.initProperties();
            Xulor.getInstance().addDialogUnloadListener(this.m_dialogUnloadListener);
            Xulor.getInstance().load("guildCreatorDialog", Dialogs.getDialogPath("guildCreatorDialog"), 33024L, (short)10000);
            Xulor.getInstance().putActionClass("wakfu.guildBannerCreator", GuildCreatorDialogActions.class);
            PropertiesProvider.getInstance().setPropertyValue("guildBannerColors", GuildBannerColor.getInstance().getColors());
            this.m_guildBannerData = GuildBannerGenerator.getInstance().getRandomBannerPart();
            PropertiesProvider.getInstance().setPropertyValue("guildBannerPrimaryColor", this.m_guildBannerData.getBackground().toString());
            PropertiesProvider.getInstance().setPropertyValue("guildBannerSecondaryColor", this.m_guildBannerData.getForeground().toString());
            this.regenerateTextureProperty();
        }
    }
    
    @Override
    public void onFrameRemove(final FrameHandler frameHandler, final boolean isAboutToBeRemoved) {
        if (!isAboutToBeRemoved) {
            this.m_machine.notifyViews();
            Xulor.getInstance().removeDialogUnloadListener(this.m_dialogUnloadListener);
            Xulor.getInstance().unload("guildCreatorDialog");
            Xulor.getInstance().removeActionClass("wakfu.guildBannerCreator");
            PropertiesProvider.getInstance().removeProperty("guildBannerColors");
            PropertiesProvider.getInstance().removeProperty("guildBannerPrimaryColor");
            PropertiesProvider.getInstance().removeProperty("guildBannerSecondaryColor");
            this.m_guildBannerData.cleanUp();
            this.m_guildBannerData = null;
        }
    }
    
    public void setMachine(final GuildMachine machine) {
        this.m_machine = machine;
    }
    
    static {
        m_logger = Logger.getLogger((Class)UIGuildCreatorFrame.class);
        UIGuildCreatorFrame.m_instance = new UIGuildCreatorFrame();
    }
}
