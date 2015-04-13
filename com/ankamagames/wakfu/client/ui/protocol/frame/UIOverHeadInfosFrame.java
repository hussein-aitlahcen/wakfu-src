package com.ankamagames.wakfu.client.ui.protocol.frame;

import com.ankamagames.wakfu.client.ui.component.*;
import com.ankamagames.framework.kernel.events.*;
import org.apache.log4j.*;
import com.ankamagames.xulor2.component.*;
import com.ankamagames.wakfu.client.core.game.actor.*;
import com.ankamagames.wakfu.client.core.game.actor.characterActorHelpers.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.framework.reflect.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.framework.kernel.core.common.message.scheduler.clock.*;
import com.ankamagames.wakfu.client.ui.protocol.message.overHeadInfos.*;
import com.ankamagames.wakfu.client.*;
import com.ankamagames.wakfu.client.core.preferences.*;
import com.ankamagames.baseImpl.graphics.core.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.baseImpl.graphics.*;
import com.ankamagames.framework.graphics.image.*;
import com.ankamagames.framework.kernel.*;
import com.ankamagames.wakfu.client.ui.*;
import com.ankamagames.wakfu.client.console.command.display.*;
import com.ankamagames.wakfu.common.game.nation.*;
import com.ankamagames.wakfu.common.game.pvp.*;
import gnu.trove.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.chat.*;
import com.ankamagames.framework.ai.targetfinder.*;
import com.ankamagames.baseImpl.graphics.alea.adviser.*;
import com.ankamagames.baseImpl.graphics.alea.adviser.text.flying.*;
import com.ankamagames.framework.graphics.engine.text.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import java.util.*;

public class UIOverHeadInfosFrame extends ImmutableFieldProvider implements MessageFrame, VisibleChangedListener, CharacterInfoManagerListener
{
    private static final Logger m_logger;
    private static UIOverHeadInfosFrame m_instance;
    public static final String PVP_RANK_ICON_URL = "pvpRankIconUrl";
    public static final String PVP_RANK_ENABLED = "pvpRankEnabled";
    public static final String OVER_HEAD_INFOS_FIELD = "overHeadInfos";
    public static final String BREED_ICON_URL_FIELD = "breedIconUrl";
    public static final String BREED_ICON_COLOR_FIELD = "breedIconColor";
    public static final String TITLE_FIELD = "title";
    public static final String BREED_ICON_PREFIX = "breed";
    public static final String MONSTERS_ICON_PREFIX = "monsters";
    public static final String HOODED_ICON_PREFIX = "hooded";
    public static final String RESOURCE_ICON_PREFIX = "resource";
    public static final String[] FIELDS;
    private WatcherContainer m_overheadDialog;
    private WatcherContainer m_overheadPlayerDialog;
    private List<UIShowOverHeadInfosMessage.OverHeadInfo> m_overHeadInfos;
    private OverHeadTarget m_currentTarget;
    private UIShowOverHeadInfosMessage m_msg;
    private String m_breedIconUrl;
    private String m_breedIconColor;
    private String m_title;
    private final TLongObjectHashMap<OverheadNameData> m_overheadNameDataMap;
    
    public UIOverHeadInfosFrame() {
        super();
        this.m_overHeadInfos = new ArrayList<UIShowOverHeadInfosMessage.OverHeadInfo>();
        this.m_overheadNameDataMap = new TLongObjectHashMap<OverheadNameData>();
    }
    
    public static UIOverHeadInfosFrame getInstance() {
        return UIOverHeadInfosFrame.m_instance;
    }
    
    public OverHeadTarget getCurrentTarget() {
        return this.m_currentTarget;
    }
    
    public void hideOverHead(final OverHeadTarget target) {
        if (target != null) {
            target.removeVisibleChangedListener(this);
        }
        if (target != this.m_currentTarget) {
            return;
        }
        this.m_currentTarget = null;
        if (this.m_overheadDialog != null) {
            this.m_overheadDialog.setVisible(false);
        }
        if (this.m_overheadPlayerDialog != null) {
            this.m_overheadPlayerDialog.setVisible(false);
        }
        MessageScheduler.getInstance().removeAllClocks(this);
    }
    
    public void updatePlayerCharacter(final long guildId) {
        if (this.m_currentTarget == null) {
            return;
        }
        if (!(this.m_currentTarget instanceof CharacterActor)) {
            return;
        }
        final CharacterInfo characterInfo = ((CharacterActor)this.m_currentTarget).getCharacterInfo();
        if (!(characterInfo instanceof PlayerCharacter)) {
            return;
        }
        if (((PlayerCharacter)characterInfo).getGuildId() != guildId) {
            return;
        }
        this.m_msg.getOverHeadInfos().clear();
        CharacterActorSelectionChangeListener.prepareMessage(characterInfo, this.m_msg);
        this.m_overHeadInfos = this.m_msg.getOverHeadInfos();
        PropertiesProvider.getInstance().firePropertyValueChanged(this, UIOverHeadInfosFrame.FIELDS);
    }
    
    @Override
    public boolean onMessage(final Message message) {
        if (message instanceof ClockMessage) {
            final ClockMessage msg = (ClockMessage)message;
            return this.displayOverhead() && false;
        }
        switch (message.getId()) {
            case 16591: {
                final UIHideOverHeadInfosMessage msg2 = (UIHideOverHeadInfosMessage)message;
                this.hideOverHead(msg2.getTarget());
                return false;
            }
            case 16590: {
                final UIShowOverHeadInfosMessage infosMessage = (UIShowOverHeadInfosMessage)message;
                MessageScheduler.getInstance().removeAllClocks(this);
                this.m_msg = infosMessage;
                long clockDelay;
                if (infosMessage.hasOverheadDelayPreference()) {
                    clockDelay = infosMessage.getOverheadDelayPreference();
                }
                else {
                    clockDelay = ((WakfuGameEntity.getInstance().getLocalPlayer().getCurrentFight() != null) ? 0L : ((long)(WakfuClientInstance.getInstance().getGamePreferences().getFloatValue(WakfuKeyPreferenceStoreEnum.OVER_HEAD_DELAY_KEY) * 1000.0f)));
                }
                (this.m_currentTarget = this.m_msg.getTarget()).addVisibleChangedListener(this);
                if (clockDelay != 0L) {
                    MessageScheduler.getInstance().addClock(this, clockDelay, 0, 1);
                }
                else {
                    this.displayOverhead();
                }
                return false;
            }
            default: {
                return true;
            }
        }
    }
    
    private boolean displayOverhead() {
        this.m_overheadDialog.setVisible(false);
        this.m_overheadPlayerDialog.setVisible(false);
        final OverHeadTarget target = this.m_msg.getTarget();
        WatcherContainer dialog = this.m_overheadDialog;
        if (target instanceof CharacterActor) {
            final CharacterActor actor = (CharacterActor)target;
            if (actor.getCharacterInfo().getType() == 0) {
                dialog = this.m_overheadPlayerDialog;
            }
        }
        Xulor.getInstance().setWatcherTarget("overheadDialog", target);
        Xulor.getInstance().setWatcherTarget("overheadPlayerDialog", target);
        dialog.getAppearance().setModulationColor(new Color(Color.WHITE.get()));
        this.m_overHeadInfos = this.m_msg.getOverHeadInfos();
        this.m_breedIconUrl = this.m_msg.getBreedIconUrl();
        this.m_breedIconColor = this.m_msg.getBreedIconColor().toString();
        this.m_title = this.m_msg.getTitle();
        PropertiesProvider.getInstance().firePropertyValueChanged(this, UIOverHeadInfosFrame.FIELDS);
        final Color c = new Color(target.getOverHeadborderColor());
        c.setAlpha(0.9f);
        this.m_overheadDialog.getAppearance().setModulationColor(c);
        if (!this.m_overHeadInfos.isEmpty()) {
            dialog.setVisible(true);
        }
        return false;
    }
    
    @Override
    public void onFrameAdd(final FrameHandler frameHandler, final boolean isAboutToBeAdded) {
        if (!isAboutToBeAdded) {
            (this.m_overheadDialog = (WatcherContainer)Xulor.getInstance().load("overheadDialog", Dialogs.getDialogPath("overheadDialog"), 73729L, (short)10000)).setVisible(false);
            (this.m_overheadPlayerDialog = (WatcherContainer)Xulor.getInstance().load("overheadPlayerDialog", Dialogs.getDialogPath("overheadPlayerDialog"), 73729L, (short)10000)).setVisible(false);
            PropertiesProvider.getInstance().setPropertyValue("overHead", this);
            CharacterInfoManager.getInstance().addListener(this);
        }
    }
    
    @Override
    public void onFrameRemove(final FrameHandler frameHandler, final boolean isAboutToBeRemoved) {
        if (!isAboutToBeRemoved) {
            this.m_overheadNameDataMap.clear();
            Xulor.getInstance().unload("overheadDialog");
            this.m_overheadDialog = null;
            Xulor.getInstance().unload("overheadPlayerDialog");
            this.m_overheadPlayerDialog = null;
            MessageScheduler.getInstance().removeAllClocks(this);
            PropertiesProvider.getInstance().removeProperty("overHead");
            CharacterInfoManager.getInstance().removeListener(this);
            ShowHideNameOverheadsCommand.setHide(false);
        }
    }
    
    @Override
    public long getId() {
        return 88L;
    }
    
    @Override
    public void setId(final long id) {
    }
    
    @Override
    public String[] getFields() {
        return UIOverHeadInfosFrame.FIELDS;
    }
    
    @Override
    public Object getFieldValue(final String fieldName) {
        if (fieldName.equals("overHeadInfos")) {
            return this.m_overHeadInfos;
        }
        if (fieldName.equals("pvpRankIconUrl")) {
            if (this.m_currentTarget instanceof CharacterActor) {
                final CharacterInfo info = ((CharacterActor)this.m_currentTarget).getCharacterInfo();
                if (info.getType() == 0) {
                    final int nationId = info.getCitizenComportment().getNationId();
                    final NationPvpRanks rank = info.getCitizenComportment().getPvpRank();
                    return WakfuConfiguration.getInstance().getIconUrl("pvpRankIconsPath", "defaultIconPath", nationId, rank.getId());
                }
            }
            return null;
        }
        if (fieldName.equals("pvpRankEnabled")) {
            if (!(this.m_currentTarget instanceof CharacterActor)) {
                return false;
            }
            final CharacterInfo info = ((CharacterActor)this.m_currentTarget).getCharacterInfo();
            if (info.getType() != 0) {
                return false;
            }
            return info.getCitizenComportment().getPvpState() != NationPvpState.PVP_OFF && info.getCitizenComportment().getNation() != Nation.VOID_NATION;
        }
        else {
            if (fieldName.equals("breedIconUrl")) {
                return this.m_breedIconUrl;
            }
            if (fieldName.equals("breedIconColor")) {
                return this.m_breedIconColor;
            }
            if (fieldName.equals("title")) {
                return this.m_title;
            }
            return null;
        }
    }
    
    @Override
    public void onVisibleChanged(final boolean visible, final VisibleChangedCause cause) {
        if (!visible) {
            this.hideOverHead(this.m_currentTarget);
        }
    }
    
    public void displayNameOverheads() {
        CharacterInfoManager.getInstance().forEachPlayerCharacter(new TObjectProcedure<CharacterInfo>() {
            @Override
            public boolean execute(final CharacterInfo ci) {
                UIOverHeadInfosFrame.this.displayNameOverhead((PlayerCharacter)ci);
                return true;
            }
        });
    }
    
    public void hideNameOverheads() {
        this.m_overheadNameDataMap.forEachValue(new TObjectProcedure<OverheadNameData>() {
            @Override
            public boolean execute(final OverheadNameData object) {
                object.clean();
                return true;
            }
        });
    }
    
    private void displayNameOverhead(final PlayerCharacter ci) {
        final OverheadNameData removed = this.m_overheadNameDataMap.remove(ci.getId());
        if (removed != null) {
            removed.clean();
        }
        final OverheadNameData overheadNameData = new OverheadNameData(ci);
        this.m_overheadNameDataMap.put(ci.getId(), overheadNameData);
    }
    
    @Override
    public void onCharacterAdded(final CharacterInfo character) {
        if (!(character instanceof PlayerCharacter)) {
            return;
        }
        if (!ShowHideNameOverheadsCommand.isActive()) {
            return;
        }
        this.displayNameOverhead((PlayerCharacter)character);
    }
    
    static {
        m_logger = Logger.getLogger((Class)UIOverHeadInfosFrame.class);
        UIOverHeadInfosFrame.m_instance = new UIOverHeadInfosFrame();
        FIELDS = new String[] { "pvpRankEnabled", "pvpRankIconUrl", "overHeadInfos", "breedIconUrl", "breedIconColor", "title" };
    }
    
    private class OverheadNameData implements VisibleChangedListener
    {
        private final ArrayList<FlyingText> m_flyingTexts;
        private final PlayerCharacter m_characterInfo;
        
        private OverheadNameData(final PlayerCharacter characterInfo) {
            super();
            this.m_flyingTexts = new ArrayList<FlyingText>();
            this.m_characterInfo = characterInfo;
            this.m_characterInfo.getActor().addVisibleChangedListener(this);
            this.displayNameText(this.m_characterInfo);
        }
        
        private void displayNameText(final PlayerCharacter ci) {
            final String name = ci.getName();
            String title;
            if (ci.getCurrentTitle() != -1) {
                title = WakfuTranslator.getInstance().getString(34, ci.getCurrentTitle(), new Object[0]);
            }
            else {
                title = null;
            }
            if (title == null) {
                this.displayOverhead(ci, name, "narrowFont14Bordered", 80);
            }
            else {
                this.displayOverhead(ci, name, "narrowFont14Bordered", 90);
                this.displayOverhead(ci, title, "styledOverheadFont", 75);
            }
        }
        
        private void displayOverhead(final PlayerCharacter ci, final String text, final String fontName, final int offset) {
            final int duration = -1;
            final FlyingTextDeformer deformer = new FlyingText.StaticFlyingTextDeformer(0, offset);
            final Font font = Xulor.getInstance().getDocumentParser().getFont(fontName).getFont();
            final FlyingText flyingText = new FlyingText(font, text, deformer, -1);
            final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
            Color color;
            if (localPlayer.getPartyComportment().isInParty() && localPlayer.getPartyComportment().getParty().getMember(ci.getId()) != null) {
                final float[] chatGroupColorValue = ChatConstants.CHAT_GROUP_COLOR_VALUE;
                color = new Color(chatGroupColorValue[0], chatGroupColorValue[1], chatGroupColorValue[2], 1.0f);
            }
            else if (localPlayer.isInGuild() && localPlayer.getGuildHandler().getMember(ci.getId()) != null) {
                final float[] chatGuildColorValue = ChatConstants.CHAT_GUILD_COLOR_VALUE;
                color = new Color(chatGuildColorValue[0], chatGuildColorValue[1], chatGuildColorValue[2], 1.0f);
            }
            else {
                color = Color.WHITE;
            }
            flyingText.setColor(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
            final CharacterActor actor = ci.getActor();
            flyingText.setTarget(actor);
            AdviserManager.getInstance().addAdviser(flyingText);
            this.m_flyingTexts.add(flyingText);
        }
        
        @Override
        public void onVisibleChanged(final boolean visible, final VisibleChangedCause cause) {
            if (!visible) {
                this.clean();
            }
        }
        
        public void clean() {
            for (final FlyingText flyingText : this.m_flyingTexts) {
                AdviserManager.getInstance().removeAdviser(flyingText);
            }
            this.m_characterInfo.getActor().removeVisibleChangedListener(this);
            UIOverHeadInfosFrame.this.m_overheadNameDataMap.remove(this.m_characterInfo.getId());
        }
    }
}
