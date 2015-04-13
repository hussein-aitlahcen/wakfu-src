package com.ankamagames.wakfu.client.core.game.group.guild;

import com.ankamagames.wakfu.client.ui.component.*;
import org.apache.log4j.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.framework.reflect.*;
import com.ankamagames.wakfu.client.core.game.notificationSystem.*;
import com.ankamagames.wakfu.client.ui.protocol.message.notificationMessage.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.wakfu.client.ui.actions.*;
import com.ankamagames.wakfu.common.game.guild.definition.*;
import com.ankamagames.wakfu.common.datas.guild.agt_like.*;
import com.ankamagames.baseImpl.client.proxyclient.base.chat.*;
import com.ankamagames.wakfu.common.datas.guild.bonus.*;
import com.ankamagames.wakfu.common.game.guild.bonus.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.guild.*;
import com.ankamagames.baseImpl.common.clientAndServer.utils.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.xulor2.component.*;
import com.ankamagames.xulor2.core.*;
import com.ankamagames.wakfu.common.game.guild.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.*;
import com.ankamagames.baseImpl.client.proxyclient.base.network.*;
import com.ankamagames.wakfu.common.game.havenWorld.definition.entry.*;
import com.ankamagames.wakfu.common.game.havenWorld.definition.*;
import com.ankamagames.wakfu.client.core.nation.*;
import com.ankamagames.wakfu.common.datas.group.*;
import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.common.datas.guild.level.*;
import com.ankamagames.wakfu.common.game.time.calendar.*;
import com.ankamagames.wakfu.common.game.effect.*;
import com.ankamagames.wakfu.client.core.effect.manager.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.characteristics.*;
import com.ankamagames.baseImpl.common.clientAndServer.core.*;
import java.util.*;
import com.ankamagames.framework.graphics.engine.texture.*;
import com.ankamagames.wakfu.common.game.item.referenceItem.*;
import com.ankamagames.wakfu.common.game.guild.bonus.effect.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.time.calendar.*;
import com.ankamagames.wakfu.client.core.utils.*;
import com.ankamagames.wakfu.client.chat.*;
import com.ankamagames.wakfu.client.network.protocol.message.global.clientToServer.guild.*;
import com.ankamagames.wakfu.common.datas.guild.*;
import com.ankamagames.wakfu.client.core.game.achievements.ui.*;
import gnu.trove.*;

public class WakfuGuildView extends ImmutableFieldProvider
{
    private static final Logger m_logger;
    private static final WakfuGuildView m_instance;
    private final GuildListener m_listener;
    private Guild m_guild;
    public static final String NAME_FIELD = "name";
    public static final String BLAZON_FIELD = "blazon";
    public static final String NUM_MEMBERS_FIELD = "numMembers";
    public static final String MEMBERS_LEVEL_AVERAGE_FIELD = "membersLevelAverage";
    public static final String GUILD_LEVEL_TEXT_FIELD = "guildLevelText";
    public static final String CURRENT_GUILD_POINTS_FIELD = "currentGuildPoints";
    public static final String TOTAL_GUILD_POINTS_FIELD = "totalGuildPoints";
    public static final String CONQUEST_POINTS = "conquestPoints";
    public static final String MEMBERS_LIST_FIELD = "membersList";
    public static final String DISPLAY_DISCONNECTED_MEMBERS_FIELD = "displayDisconnectedMembers";
    public static final String CAN_INVITE_FIELD = "canInvite";
    public static final String I_AM_LEADER_FIELD = "iAmLeader";
    public static final String NATION = "nation";
    public static final String CAN_CHANGE_NATION = "canChangeNation";
    public static final String GUILD_DESCRIPTION_FIELD = "guildDescription";
    public static final String GUILD_MESSAGE_FIELD = "guildMessage";
    public static final String HAS_RIGHT_TO_EDIT_GUILD_DESCRIPTION_FIELD = "hasRightToEditGuildDescription";
    public static final String HAS_RIGHT_TO_EDIT_GUILD_MESSAGE_FIELD = "hasRightToEditGuildMessage";
    public static final String HAS_RIGHT_TO_EDIT_RANKS_FIELD = "hasRightToEditRanks";
    public static final String HAS_RIGHT_TO_CREATE_RANKS_FIELD = "hasRightToCreateRanks";
    public static final String HAS_RIGHT_TO_BUY_BONUS_FIELD = "hasRightToBuyBonus";
    public static final String HAS_HAVEN_WORLD = "hasHavenWorld";
    public static final String HAVEN_WORLD_AUCTION_BOOK_ICON_URL = "havenWorld.auctionBookIconUrl";
    public static final String HAVEN_WORLD_PAGE_WARNING_FIELD = "havenWorldPageWarning";
    public static final String LOCAL_PLAYER_FIELD = "localPlayer";
    public static final String RANKS_FIELD = "ranks";
    public static final String SMILEYS_FIELD = "smileys";
    public static final String QUESTS = "quests";
    public static final String LEVEL = "level";
    public static final String NEXT_LEVEL = "nextLevel";
    public static final String NEXT_LEVEL_COST = "nextLevelCost";
    public static final String NEXT_LEVEL_DURATION = "nextLevelDuration";
    public static final String CAN_LEVEL_UP = "canLevelUp";
    public static final String AVAILABLE_BONUSES = "availableBonuses";
    public static final String LOADING_BONUSES = "loadingBonuses";
    public static final String PERMANENT_BONUSES = "permanentBonuses";
    public static final String TEMPORARY_BONUSES = "temporaryBonuses";
    public static final String SIMULTANEOUS_BONUSES_TEXT = "simultaneousBonusesText";
    public static final String CURRENT_WEEK_GUILD_POINTS = "currentWeekGuildPoints";
    public static final String[] FIELDS;
    private boolean m_displayDisconnectedMembers;
    private final ArrayList<WakfuGuildMemberView> m_guildMembers;
    private final ArrayList<GuildRankView> m_guildRankViews;
    private Comparator<WakfuGuildMemberView> m_currentComparator;
    private boolean m_direct;
    private int m_comparatorIndex;
    private final ArrayList<AvatarSmileyView> m_smileyViews;
    
    public WakfuGuildView() {
        super();
        this.m_listener = new GuildListener() {
            @Override
            public void nameChanged() {
                PropertiesProvider.getInstance().firePropertyValueChanged(WakfuGuildView.this, "name");
            }
            
            @Override
            public void blazonChanged() {
                PropertiesProvider.getInstance().firePropertyValueChanged(WakfuGuildView.this, "blazon");
            }
            
            @Override
            public void descriptionChanged() {
                PropertiesProvider.getInstance().firePropertyValueChanged(WakfuGuildView.this, "guildDescription");
            }
            
            @Override
            public void nationIdChanged(final int nationId) {
                final String nationName = WakfuTranslator.getInstance().getString(39, nationId, new Object[0]);
                final String title = WakfuTranslator.getInstance().getString("notification.guildChangeNationTitle");
                final String text = WakfuTranslator.getInstance().getString("notification.guildChangeNationText", nationName);
                final Message uiNotificationMessage = new UINotificationMessage(title, text, NotificationMessageType.SOCIAL);
                Worker.getInstance().pushMessage(uiNotificationMessage);
                PropertiesProvider.getInstance().firePropertyValueChanged(WakfuGuildView.this, "canChangeNation", "nation");
            }
            
            @Override
            public void messageChanged() {
                WakfuGuildView.this.displayGuildMessage();
                PropertiesProvider.getInstance().firePropertyValueChanged(WakfuGuildView.this, "guildMessage");
            }
            
            @Override
            public void levelChanged(final short level) {
                this.displayGuildLevelGainMessage();
                GuildViewManager.INSTANCE.forEachBonusView(new TObjectProcedure<GuildBonusView>() {
                    @Override
                    public boolean execute(final GuildBonusView object) {
                        object.updatePurchaseCapability();
                        return true;
                    }
                });
                PropertiesProvider.getInstance().firePropertyValueChanged(WakfuGuildView.this, "canLevelUp", "nextLevelCost", "nextLevelDuration", "guildLevelText");
            }
            
            @Override
            public void currentGuildPointsChanged(final int deltaPoints) {
                PropertiesProvider.getInstance().firePropertyValueChanged(WakfuGuildView.this, "currentGuildPoints");
            }
            
            @Override
            public void totalGuildPointsChanged(final int deltaPoints) {
                PropertiesProvider.getInstance().firePropertyValueChanged(WakfuGuildView.this, "totalGuildPoints");
            }
            
            @Override
            public void rankAdded(final GuildRank rank) {
                WakfuGuildView.this.updateRanksField(true);
            }
            
            @Override
            public void rankMoved(final GuildRank rank) {
                WakfuGuildView.this.updateRanksField(true);
            }
            
            @Override
            public void rankRemoved(final GuildRank rank) {
                WakfuGuildView.this.updateRanksField(true);
            }
            
            @Override
            public void memberAdded(final GuildMember member) {
                String notifTitleTranslatorKey = null;
                String notifText = null;
                if (member.getId() == WakfuGameEntity.getInstance().getLocalPlayer().getId()) {
                    notifTitleTranslatorKey = "notification.guildJoinTitle";
                    notifText = WakfuTranslator.getInstance().getString("notification.guildJoinText");
                }
                else {
                    notifTitleTranslatorKey = "notification.guildRecrueTitle";
                    notifText = WakfuTranslator.getInstance().getString("notification.guildRecrueText", member.getName());
                }
                final String title = WakfuTranslator.getInstance().getString(notifTitleTranslatorKey);
                final String text = NotificationPanelDialogActions.createLink(notifText, NotificationMessageType.SOCIAL);
                final UINotificationMessage uiNotificationMessage = new UINotificationMessage(title, text, NotificationMessageType.SOCIAL);
                Worker.getInstance().pushMessage(uiNotificationMessage);
                WakfuGuildView.this.updateMembersField(true);
            }
            
            @Override
            public void memberRemoved(final GuildMember member) {
                if (member.getId() != WakfuGameEntity.getInstance().getLocalPlayer().getId()) {
                    String notifTitleTranslatorKey = null;
                    String notifText = null;
                    notifTitleTranslatorKey = "notification.guildQuitTitle";
                    notifText = WakfuTranslator.getInstance().getString("notification.guildQuitText", member.getName());
                    final String title = WakfuTranslator.getInstance().getString(notifTitleTranslatorKey);
                    final String text = NotificationPanelDialogActions.createLink(notifText, NotificationMessageType.SOCIAL);
                    final UINotificationMessage uiNotificationMessage = new UINotificationMessage(title, text, NotificationMessageType.SOCIAL);
                    Worker.getInstance().pushMessage(uiNotificationMessage);
                }
                WakfuGuildView.this.updateMembersField(true);
            }
            
            @Override
            public void bonusAdded(final GuildBonus bonus) {
                this.updateBonusAndGuild(bonus);
                WakfuGuildView.this.addTimedBonus(bonus);
            }
            
            @Override
            public void bonusRemoved(final GuildBonus bonus) {
                this.updateBonusAndGuild(bonus);
                this.removeTimedBonus(bonus);
                if (bonus.getBonusId() == GuildBonusDataAGT.BONUS_84.get().getId()) {
                    PropertiesProvider.getInstance().firePropertyValueChanged(WakfuGuildView.this, "canChangeNation");
                }
            }
            
            @Override
            public void rankChanged(final GuildRank rank) {
                WakfuGuildView.this.updateRanksField(true);
            }
            
            @Override
            public void memberChanged(final GuildMember member) {
                WakfuGuildView.this.updateMemberField(member);
                WakfuGuildView.this.updateRanksField(true);
            }
            
            @Override
            public void bonusActivated(final GuildBonus bonus) {
                this.updateBonusAndGuild(bonus);
                WakfuGuildView.this.addTimedBonus(bonus);
                final GuildBonusDataAGT bonusData = GuildBonusDataAGT.getFromId(bonus.getBonusId());
                if (GuildHelper.isLevelBonus(bonusData)) {
                    return;
                }
                if (bonus.getBonusId() == GuildBonusDataAGT.BONUS_84.get().getId()) {
                    PropertiesProvider.getInstance().firePropertyValueChanged(WakfuGuildView.this, "canChangeNation");
                }
                final String notifTitleTranslatorKey = "notification.guildBonusActivatedTitle";
                final String bonusName = WakfuTranslator.getInstance().getString(138, bonus.getBonusId(), new Object[0]);
                final String notifText = WakfuTranslator.getInstance().getString("notification.guildBonusActivatedText", bonusName);
                final String title = WakfuTranslator.getInstance().getString("notification.guildBonusActivatedTitle");
                final String text = NotificationPanelDialogActions.createLink(notifText, NotificationMessageType.SOCIAL);
                final UINotificationMessage uiNotificationMessage = new UINotificationMessage(title, text, NotificationMessageType.SOCIAL);
                Worker.getInstance().pushMessage(uiNotificationMessage);
                final String mess = WakfuTranslator.getInstance().getString("guild.chatBonusActivated", bonusName);
                final ChatMessage chatMessage = new ChatMessage(WakfuTranslator.getInstance().getString("chat.pipeName.guild"), mess);
                chatMessage.setPipeDestination(4);
                ChatManager.getInstance().pushMessage(chatMessage);
            }
            
            @Override
            public void earnedPointsWeeklyChanged(final int earnedPoints) {
                PropertiesProvider.getInstance().firePropertyValueChanged(WakfuGuildView.this, "currentWeekGuildPoints");
            }
            
            @Override
            public void lastEarningPointWeekChanged(final int week) {
                PropertiesProvider.getInstance().firePropertyValueChanged(WakfuGuildView.this, "currentWeekGuildPoints");
            }
            
            private void updateBonusAndGuild(final GuildBonus bonus) {
                final GuildBonusView view = GuildViewManager.INSTANCE.getBonusView(bonus.getBonusId());
                view.updatePurchaseCapability();
                PropertiesProvider.getInstance().firePropertyValueChanged(WakfuGuildView.this, "canLevelUp", "availableBonuses", "loadingBonuses", "permanentBonuses", "temporaryBonuses", "simultaneousBonusesText");
            }
            
            private void removeTimedBonus(final GuildBonus bonus) {
                final GuildBonusDefinition bonusDefinition = GuildBonusManager.INSTANCE.getBonus(bonus.getBonusId());
                if (!GuildBonusHelper.isBonusTimed(bonusDefinition)) {
                    return;
                }
                final GuildBonusView view = GuildViewManager.INSTANCE.getBonusView(bonus.getBonusId());
                view.removeFromTimeManager();
                this.displayGuildTimedBonusEndMessage(bonusDefinition);
            }
            
            private void displayGuildTimedBonusEndMessage(final GuildBonusDefinition bonus) {
                final String notifTitleTranslatorKey = "notification.guildBonusEndTitle";
                final String bonusName = WakfuTranslator.getInstance().getString(138, bonus.getId(), new Object[0]);
                final String notifText = WakfuTranslator.getInstance().getString("notification.guildBonusEndText", bonusName);
                final String title = WakfuTranslator.getInstance().getString("notification.guildBonusEndTitle");
                final String text = NotificationPanelDialogActions.createLink(notifText, NotificationMessageType.SOCIAL);
                final UINotificationMessage uiNotificationMessage = new UINotificationMessage(title, text, NotificationMessageType.SOCIAL);
                Worker.getInstance().pushMessage(uiNotificationMessage);
                final String mess = WakfuTranslator.getInstance().getString("guild.chatBonusEnd", bonusName);
                final ChatMessage chatMessage = new ChatMessage(WakfuTranslator.getInstance().getString("chat.pipeName.guild"), mess);
                chatMessage.setPipeDestination(4);
                ChatManager.getInstance().pushMessage(chatMessage);
            }
            
            private void displayGuildLevelGainMessage() {
                PropertiesProvider.getInstance().firePropertyValueChanged(WakfuGuildView.this, "level", "nextLevel", "nextLevelCost", "canLevelUp");
                final String notifTitleTranslatorKey = "notification.guildLevelTitle";
                final String notifText = WakfuTranslator.getInstance().getString("notification.guildLevelText", WakfuGuildView.this.getGuildLevel());
                final String title = WakfuTranslator.getInstance().getString("notification.guildLevelTitle");
                final String text = NotificationPanelDialogActions.createLink(notifText, NotificationMessageType.SOCIAL);
                final UINotificationMessage uiNotificationMessage = new UINotificationMessage(title, text, NotificationMessageType.SOCIAL);
                Worker.getInstance().pushMessage(uiNotificationMessage);
                final String mess = WakfuTranslator.getInstance().getString("guild.chatLevelGain", WakfuGuildView.this.getGuildLevel());
                final ChatMessage chatMessage = new ChatMessage(WakfuTranslator.getInstance().getString("chat.pipeName.guild"), mess);
                chatMessage.setPipeDestination(4);
                ChatManager.getInstance().pushMessage(chatMessage);
            }
        };
        this.m_displayDisconnectedMembers = true;
        this.m_guildMembers = new ArrayList<WakfuGuildMemberView>();
        this.m_guildRankViews = new ArrayList<GuildRankView>();
        this.m_currentComparator = NameComparator.DIRECT;
        this.m_smileyViews = new ArrayList<AvatarSmileyView>();
    }
    
    public static WakfuGuildView getInstance() {
        return WakfuGuildView.m_instance;
    }
    
    public void init() {
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        this.m_guild = ((GuildLocalInformationHandler)localPlayer.getGuildHandler()).getGuild();
        this.initTimedBonuses();
        this.addListeners();
        this.updateRanksField(false);
        this.m_currentComparator = NameComparator.DIRECT;
        this.m_direct = true;
        this.m_comparatorIndex = 0;
        this.m_smileyViews.clear();
        for (final SmileyEnum smileyEnum : SmileyEnum.values()) {
            if (smileyEnum.getId() > 14) {
                break;
            }
            this.m_smileyViews.add(AvatarSmileyView.getView(localPlayer.getBreedId(), localPlayer.getSex(), smileyEnum.getId()));
        }
        this.updateMembersField(false);
        PropertiesProvider.getInstance().firePropertyValueChanged(this, WakfuGuildView.FIELDS);
    }
    
    private void initTimedBonuses() {
        this.m_guild.forEachBonus(new TObjectProcedure<GuildBonus>() {
            @Override
            public boolean execute(final GuildBonus bonus) {
                WakfuGuildView.this.addTimedBonus(bonus);
                return true;
            }
        });
    }
    
    private void addTimedBonus(final GuildBonus bonus) {
        if (!GuildBonusHelper.isBonusTimed(GuildBonusManager.INSTANCE.getBonus(bonus.getBonusId()))) {
            return;
        }
        final GuildBonusView view = GuildViewManager.INSTANCE.getBonusView(bonus.getBonusId());
        view.addToTimeManager();
    }
    
    public void initSelectedRank() {
        final GuildRankView rankView = this.getRankView(this.m_guild.getBestRank()).getCopy();
        PropertiesProvider.getInstance().setPropertyValue("selectedGuildRank", rankView);
        final ElementMap elementMap = Xulor.getInstance().getEnvironment().getElementMap("guildManagementDialog");
        if (elementMap == null) {
            return;
        }
        final TextEditor te = (TextEditor)elementMap.getElement("guildName");
        te.setText(rankView.getName());
    }
    
    public void addListeners() {
        this.m_guild.addListener(new GuildBonusApplierController(this.m_guild));
        this.m_guild.addListener(this.m_listener);
    }
    
    public AvatarSmileyView getSmileyFromId(final byte smiley) {
        for (final AvatarSmileyView avatarSmileyView : this.m_smileyViews) {
            if (avatarSmileyView.getSmileyEnumId() == smiley) {
                return avatarSmileyView;
            }
        }
        return null;
    }
    
    public ArrayList<GuildRankView> getGuildRankViews() {
        return this.m_guildRankViews;
    }
    
    public GuildRankView getRankView(final long rank) {
        for (final GuildRankView guildRankView : this.m_guildRankViews) {
            if (guildRankView.getId() == rank) {
                return guildRankView;
            }
        }
        return null;
    }
    
    public GuildMember getMember(final long longValue) {
        return this.m_guild.getMember(longValue);
    }
    
    public boolean isLoaded() {
        return this.m_guild != null;
    }
    
    public Guild getGuild() {
        return this.m_guild;
    }
    
    public void createNewRank() {
        final String rankName = WakfuTranslator.getInstance().getString("guild.newRank");
        final long authorisations = GuildRankAuthorisation.longValueOf(new ArrayList<GuildRankAuthorisation>());
        final GuildCreateRankRequestMessage guildCreateRankRequestMessage = new GuildCreateRankRequestMessage(rankName, authorisations);
        final NetworkEntity networkEntity = WakfuGameEntity.getInstance().getNetworkEntity();
        if (networkEntity != null) {
            networkEntity.sendMessage(guildCreateRankRequestMessage);
        }
        else {
            ConsoleManager.getInstance().err("Pour acc\u00e9der \u00e0 ces commandes il faut \u00eatre connect\u00e9 !");
        }
    }
    
    public void deleteRank(final long rankId) {
        this.initSelectedRank();
        final GuildDeleteRankRequestMessage guildDeleteRankRequestMessage = new GuildDeleteRankRequestMessage(rankId);
        final NetworkEntity networkEntity = WakfuGameEntity.getInstance().getNetworkEntity();
        if (networkEntity != null) {
            networkEntity.sendMessage(guildDeleteRankRequestMessage);
        }
        else {
            ConsoleManager.getInstance().err("Pour acc\u00e9der \u00e0 ces commandes il faut \u00eatre connect\u00e9 !");
        }
    }
    
    public void moveRank(final long rankId, final short position) {
        final GuildMoveRankRequestMessage guildMoveRankRequestMessage = new GuildMoveRankRequestMessage(rankId, position);
        final NetworkEntity networkEntity = WakfuGameEntity.getInstance().getNetworkEntity();
        if (networkEntity != null) {
            networkEntity.sendMessage(guildMoveRankRequestMessage);
        }
        else {
            ConsoleManager.getInstance().err("Pour acc\u00e9der \u00e0 ces commandes il faut \u00eatre connect\u00e9 !");
        }
    }
    
    public void applyRankModifications(final GuildRankView guildRankView) {
        final GuildEditRankRequestMessage guildEditRankRequestMessage = new GuildEditRankRequestMessage(guildRankView.getId(), guildRankView.getModifiedAuthorisationLong(), guildRankView.getName());
        final NetworkEntity networkEntity = WakfuGameEntity.getInstance().getNetworkEntity();
        if (networkEntity != null) {
            networkEntity.sendMessage(guildEditRankRequestMessage);
        }
        else {
            ConsoleManager.getInstance().err("Pour acc\u00e9der \u00e0 ces commandes il faut \u00eatre connect\u00e9 !");
        }
    }
    
    public void modifyGuildDescriptionRequest(final String desc) {
        final GuildChangeDescriptionRequestMessage guildChangeDescriptionRequestMessage = new GuildChangeDescriptionRequestMessage(desc);
        final NetworkEntity networkEntity = WakfuGameEntity.getInstance().getNetworkEntity();
        if (networkEntity != null) {
            networkEntity.sendMessage(guildChangeDescriptionRequestMessage);
        }
        else {
            ConsoleManager.getInstance().err("Pour acc\u00e9der \u00e0 ces commandes il faut \u00eatre connect\u00e9 !");
        }
    }
    
    public void modifyGuildMessageRequest(final String desc) {
        final GuildChangeMessageRequestMessage guildChangeMessageRequestMessage = new GuildChangeMessageRequestMessage(desc);
        final NetworkEntity networkEntity = WakfuGameEntity.getInstance().getNetworkEntity();
        if (networkEntity != null) {
            networkEntity.sendMessage(guildChangeMessageRequestMessage);
        }
        else {
            ConsoleManager.getInstance().err("Pour acc\u00e9der \u00e0 ces commandes il faut \u00eatre connect\u00e9 !");
        }
    }
    
    public void modifyGuildPersonnalDescriptionRequest(final String desc) {
        final GuildPersonalDescriptionRequestMessage guildPersonalDescriptionRequestMessage = new GuildPersonalDescriptionRequestMessage(desc);
        final NetworkEntity networkEntity = WakfuGameEntity.getInstance().getNetworkEntity();
        if (networkEntity != null) {
            networkEntity.sendMessage(guildPersonalDescriptionRequestMessage);
        }
        else {
            ConsoleManager.getInstance().err("Pour acc\u00e9der \u00e0 ces commandes il faut \u00eatre connect\u00e9 !");
        }
    }
    
    public void selectSmiley(final byte smileyId) {
        final GuildSelectSmileyRequestMessage guildSelectSmileyRequestMessage = new GuildSelectSmileyRequestMessage(smileyId);
        final NetworkEntity networkEntity = WakfuGameEntity.getInstance().getNetworkEntity();
        if (networkEntity != null) {
            networkEntity.sendMessage(guildSelectSmileyRequestMessage);
        }
        else {
            ConsoleManager.getInstance().err("Pour acc\u00e9der \u00e0 ces commandes il faut \u00eatre connect\u00e9 !");
        }
    }
    
    public int size() {
        return this.m_guildMembers.size();
    }
    
    public void displayGuildMessage() {
        final ChatMessage chatMessage = new ChatMessage(WakfuTranslator.getInstance().getString("chat.pipeName.guild"), this.m_guild.getMessage());
        chatMessage.setPipeDestination(6);
        if (!ChatConfigurator.isChatLoaded()) {
            ChatConfigurator.addPendingChatMessage(chatMessage);
        }
        else {
            ChatManager.getInstance().pushMessage(chatMessage);
        }
    }
    
    public void notifyHavenWorldBuildngAdded(final int buildingId) {
        final BuildingCatalogEntry entryForBuilding = BuildingDefinitionHelper.getEntryForBuilding((short)buildingId);
        final AbstractBuildingDefinition building = BuildingDefinitionHelper.getLastBuildingFor(entryForBuilding);
        final String notifTitleTranslatorKey = "notification.havenWorldBuildingAddedTitle";
        final String buildingName = WakfuTranslator.getInstance().getString(126, building.getId(), new Object[0]);
        final String notifText = WakfuTranslator.getInstance().getString("notification.havenWorldBuildingAddedText", buildingName);
        final String title = WakfuTranslator.getInstance().getString("notification.havenWorldBuildingAddedTitle");
        final String text = NotificationPanelDialogActions.createLink(notifText, NotificationMessageType.SOCIAL);
        final UINotificationMessage uiNotificationMessage = new UINotificationMessage(title, text, NotificationMessageType.SOCIAL);
        Worker.getInstance().pushMessage(uiNotificationMessage);
        final String mess = WakfuTranslator.getInstance().getString("guild.chatBuildingAdded", buildingName);
        final ChatMessage chatMessage = new ChatMessage(WakfuTranslator.getInstance().getString("chat.pipeName.guild"), mess);
        chatMessage.setPipeDestination(4);
        ChatManager.getInstance().pushMessage(chatMessage);
    }
    
    public void notifyHavenWorldBuildngRemoved(final int buildingId) {
        final BuildingCatalogEntry entryForBuilding = BuildingDefinitionHelper.getEntryForBuilding((short)buildingId);
        final AbstractBuildingDefinition buildingFor = BuildingDefinitionHelper.getLastBuildingFor(entryForBuilding);
        final String notifTitleTranslatorKey = "notification.havenWorldBuildingRemovedTitle";
        final String buildingName = WakfuTranslator.getInstance().getString(126, buildingFor.getId(), new Object[0]);
        final String notifText = WakfuTranslator.getInstance().getString("notification.havenWorldBuildingRemovedText", buildingName);
        final String title = WakfuTranslator.getInstance().getString("notification.havenWorldBuildingRemovedTitle");
        final String text = NotificationPanelDialogActions.createLink(notifText, NotificationMessageType.SOCIAL);
        final UINotificationMessage uiNotificationMessage = new UINotificationMessage(title, text, NotificationMessageType.SOCIAL);
        Worker.getInstance().pushMessage(uiNotificationMessage);
        final String mess = WakfuTranslator.getInstance().getString("guild.chatBuildingRemoved", buildingName);
        final ChatMessage chatMessage = new ChatMessage(WakfuTranslator.getInstance().getString("chat.pipeName.guild"), mess);
        chatMessage.setPipeDestination(4);
        ChatManager.getInstance().pushMessage(chatMessage);
    }
    
    public void notifyHavenWorldBuildngEvolved(final int buildingId, final int buildingIdTo) {
        final BuildingCatalogEntry entryForBuilding = BuildingDefinitionHelper.getEntryForBuilding((short)buildingIdTo);
        final AbstractBuildingDefinition building = BuildingDefinitionHelper.getLastBuildingFor(entryForBuilding);
        if (building.getId() != buildingIdTo) {
            this.notifyHavenWorldBuildngAdded(building.getId());
            return;
        }
        final String notifTitleTranslatorKey = "notification.havenWorldBuildingEvolvedTitle";
        final String buildingNameFrom = WakfuTranslator.getInstance().getString(126, buildingId, new Object[0]);
        final String buildingNameTo = WakfuTranslator.getInstance().getString(126, buildingIdTo, new Object[0]);
        final String notifText = WakfuTranslator.getInstance().getString("notification.havenWorldBuildingEvolvedText", buildingNameFrom, buildingNameTo);
        final String title = WakfuTranslator.getInstance().getString("notification.havenWorldBuildingEvolvedTitle");
        final String text = NotificationPanelDialogActions.createLink(notifText, NotificationMessageType.SOCIAL);
        final UINotificationMessage uiNotificationMessage = new UINotificationMessage(title, text, NotificationMessageType.SOCIAL);
        Worker.getInstance().pushMessage(uiNotificationMessage);
        final String mess = WakfuTranslator.getInstance().getString("guild.chatBuildingEvolved", buildingNameFrom, buildingNameTo);
        final ChatMessage chatMessage = new ChatMessage(WakfuTranslator.getInstance().getString("chat.pipeName.guild"), mess);
        chatMessage.setPipeDestination(4);
        ChatManager.getInstance().pushMessage(chatMessage);
    }
    
    public void setComparator(final int index) {
        if (this.m_comparatorIndex == index) {
            this.m_direct = !this.m_direct;
        }
        else {
            this.m_direct = true;
            this.m_comparatorIndex = index;
        }
        Comparator<WakfuGuildMemberView> c = null;
        switch (this.m_comparatorIndex) {
            case 0: {
                if (this.m_direct) {
                    c = NameComparator.DIRECT;
                    break;
                }
                c = NameComparator.INDIRECT;
                break;
            }
            case 1: {
                if (this.m_direct) {
                    c = LevelComparator.DIRECT;
                    break;
                }
                c = LevelComparator.INDIRECT;
                break;
            }
            case 2: {
                if (this.m_direct) {
                    c = GuildPointsComparator.DIRECT;
                    break;
                }
                c = GuildPointsComparator.INDIRECT;
                break;
            }
            case 3: {
                if (this.m_direct) {
                    c = GuildPointsComparator.DIRECT;
                    break;
                }
                c = GuildPointsComparator.INDIRECT;
                break;
            }
            case 5: {
                if (this.m_direct) {
                    c = RankComparator.DIRECT;
                    break;
                }
                c = RankComparator.INDIRECT;
                break;
            }
        }
        this.m_currentComparator = c;
        this.sortMembersField(true);
    }
    
    @Override
    public String[] getFields() {
        return WakfuGuildView.FIELDS;
    }
    
    @Override
    public Object getFieldValue(final String fieldName) {
        if (fieldName.equals("name")) {
            if (this.m_guild == null) {
                return null;
            }
            return this.m_guild.getName();
        }
        else {
            if (fieldName.equals("canChangeNation")) {
                return (this.m_guild.getNationId() == 0 || this.m_guild.getConstantManager().canChangeNation()) && this.localPlayerHasRight(GuildRankAuthorisation.BUY_BONUS);
            }
            if (fieldName.equals("nation")) {
                return NationsView.INSTANCE.getNation(this.m_guild.getNationId());
            }
            if (fieldName.equals("blazon")) {
                if (this.m_guild == null) {
                    return null;
                }
                final GuildBlazon blazon = new GuildBlazon(this.m_guild.getBlazon());
                if (blazon == null) {
                    return null;
                }
                final GuildBannerData data = new GuildBannerData(blazon.getShapeId(), blazon.getSymbolId(), GuildBannerColor.getInstance().getColor(blazon.getSymbolColor()), GuildBannerColor.getInstance().getColor(blazon.getShapeColor()));
                final Texture guildBannerTexture = GuildBannerGenerator.getInstance().getGuildBannerTexture(data);
                data.cleanUp();
                return guildBannerTexture;
            }
            else if (fieldName.equals("numMembers")) {
                if (this.m_guild == null) {
                    return 0;
                }
                return this.getGuildMembers(false).size() + " / " + this.m_guild.memberSize();
            }
            else if (fieldName.equals("membersLevelAverage")) {
                if (this.m_guild == null) {
                    return 0;
                }
                long count = 0L;
                for (final WakfuGuildMemberView wakfuGuildMemberView : this.m_guildMembers) {
                    count += wakfuGuildMemberView.getLevel();
                }
                return Math.round(count / this.m_guild.memberSize());
            }
            else {
                if (fieldName.equals("currentGuildPoints")) {
                    return this.m_guild.getCurrentGuildPoints();
                }
                if (fieldName.equals("conquestPoints")) {
                    return 0;
                }
                if (fieldName.equals("totalGuildPoints")) {
                    return this.m_guild.getTotalGuildPoints();
                }
                if (fieldName.equals("localPlayer")) {
                    final ArrayList<WakfuGuildMemberView> mbrs = new ArrayList<WakfuGuildMemberView>();
                    mbrs.add(this.getMyMemberView());
                    return mbrs;
                }
                if (fieldName.equals("membersList")) {
                    return this.getGuildMembers(this.m_displayDisconnectedMembers);
                }
                if (fieldName.equals("canInvite")) {
                    return this.localPlayerHasRight(GuildRankAuthorisation.ADD_MEMBER);
                }
                if (fieldName.equals("displayDisconnectedMembers")) {
                    return this.m_displayDisconnectedMembers;
                }
                if (fieldName.equals("hasRightToEditRanks")) {
                    return this.localPlayerHasRight(GuildRankAuthorisation.EDIT_RANK);
                }
                if (fieldName.equals("hasRightToCreateRanks")) {
                    return this.localPlayerHasRight(GuildRankAuthorisation.EDIT_RANK, this.m_guild.getRank(this.getWorstRank()).getPosition());
                }
                if (fieldName.equals("hasRightToEditGuildDescription")) {
                    return this.localPlayerHasRight(GuildRankAuthorisation.CHANGE_GUILD_DESCRIPTION);
                }
                if (fieldName.equals("hasRightToEditGuildMessage")) {
                    return this.localPlayerHasRight(GuildRankAuthorisation.CHANGE_GUILD_MESSAGE);
                }
                if (fieldName.equals("hasRightToBuyBonus")) {
                    return this.canBuyBonusRegardingSimultaneousMax() && this.localPlayerHasRight(GuildRankAuthorisation.BUY_BONUS);
                }
                if (fieldName.equals("hasHavenWorld")) {
                    if (this.m_guild == null) {
                        return null;
                    }
                    return PropertiesProvider.getInstance().getObjectProperty("havenWorld") != null;
                }
                else {
                    if (fieldName.equals("havenWorld.auctionBookIconUrl")) {
                        final AbstractReferenceItem item = ReferenceItemManager.getInstance().getReferenceItem(17426);
                        return WakfuConfiguration.getInstance().getItemBigIconUrl(item.getGfxId());
                    }
                    if (fieldName.equals("havenWorldPageWarning")) {
                        if (this.m_guild == null) {
                            return null;
                        }
                        final Object o = PropertiesProvider.getInstance().getObjectProperty("havenWorld");
                        final boolean incorrectLevel = this.m_guild.getLevel() < 3;
                        if (incorrectLevel || o == null) {
                            return WakfuTranslator.getInstance().getString("guild.havenWorldPageWarning", 3);
                        }
                        return null;
                    }
                    else if (fieldName.equals("guildDescription")) {
                        if (this.m_guild == null) {
                            return null;
                        }
                        return this.m_guild.getDescription();
                    }
                    else if (fieldName.equals("guildMessage")) {
                        if (this.m_guild == null) {
                            return null;
                        }
                        return this.m_guild.getMessage();
                    }
                    else {
                        if (fieldName.equals("ranks")) {
                            Collections.sort(this.m_guildRankViews, new Comparator<GuildRankView>() {
                                @Override
                                public int compare(final GuildRankView o1, final GuildRankView o2) {
                                    return o1.getRank().getPosition() - o2.getRank().getPosition();
                                }
                            });
                            return this.m_guildRankViews;
                        }
                        if (fieldName.equals("iAmLeader")) {
                            return this.isLocalPlayerLeader();
                        }
                        if (fieldName.equals("smileys")) {
                            return this.m_smileyViews;
                        }
                        if (fieldName.equals("quests")) {
                            return this.getQuests();
                        }
                        if (fieldName.equals("level")) {
                            return this.getGuildLevel();
                        }
                        if (fieldName.equals("nextLevel")) {
                            if (this.m_guild.getLevel() == 10) {
                                return null;
                            }
                            return this.getGuildLevel() + 1;
                        }
                        else if (fieldName.equals("nextLevelCost")) {
                            if (this.m_guild.getLevel() == 10) {
                                return null;
                            }
                            return GuildLevelManager.INSTANCE.getCost(this.getGuildLevel() + 1);
                        }
                        else if (fieldName.equals("nextLevelDuration")) {
                            if (this.m_guild.getLevel() == 10) {
                                return null;
                            }
                            final GuildBonusDataAGT bonusFor = GuildHelper.getLevelBonusFor(this.getGuildLevel() + 1);
                            final GuildBonusDefinition bonusDefinition = bonusFor.get();
                            return TimeUtils.getShortDescription(bonusDefinition.getLearningDuration());
                        }
                        else if (fieldName.equals("canLevelUp")) {
                            if (this.m_guild.getLevel() == 10) {
                                return false;
                            }
                            final int cost = GuildLevelManager.INSTANCE.getCost(this.getGuildLevel() + 1);
                            if (cost > this.m_guild.getCurrentGuildPoints()) {
                                return false;
                            }
                            final GuildBonusDataAGT bonusFor2 = GuildHelper.getLevelBonusFor(this.getGuildLevel() + 1);
                            final GuildBonusDefinition bonusDefinition2 = bonusFor2.get();
                            final GuildBonus bonus = this.m_guild.getBonus(bonusDefinition2.getId());
                            return bonus == null || !GuildBonusHelper.isLoading(bonus, bonusDefinition2, this.m_guild);
                        }
                        else {
                            if (fieldName.equals("availableBonuses")) {
                                return this.getAvailableBonuses();
                            }
                            if (fieldName.equals("loadingBonuses")) {
                                return this.getLoadingBonuses();
                            }
                            if (fieldName.equals("temporaryBonuses")) {
                                return this.getCurrentBonuses(false);
                            }
                            if (fieldName.equals("simultaneousBonusesText")) {
                                if (this.canBuyBonusRegardingSimultaneousMax()) {
                                    return null;
                                }
                                final int maxSimultaneous = this.m_guild.getConstantManager().getMaxSimultaneous();
                                return WakfuTranslator.getInstance().getString("guildBonus.maxSimultaneous", maxSimultaneous);
                            }
                            else {
                                if (fieldName.equals("currentWeekGuildPoints")) {
                                    final int currentWeek = WakfuGameCalendar.getInstance().get(3);
                                    int earnedPointsWeekly;
                                    if (this.m_guild.getLastEarningPointWeek() != currentWeek) {
                                        earnedPointsWeekly = 0;
                                    }
                                    else {
                                        earnedPointsWeekly = this.m_guild.getEarnedPointsWeekly();
                                    }
                                    return earnedPointsWeekly + "/" + this.m_guild.getWeeklyPointsLimit();
                                }
                                if (fieldName.equals("permanentBonuses")) {
                                    final ArrayList<String> descs = new ArrayList<String>();
                                    final ArrayList<WakfuEffect> cumulableEffects = new ArrayList<WakfuEffect>();
                                    for (final GuildBonusView guildBonusView : this.getCurrentBonuses(true)) {
                                        final GuildBuffEffect effect = guildBonusView.getEffect();
                                        if (effect.getType() == EffectTypeId.MEMBER_EFFECT) {
                                            final MemberEffect memberEffect = (MemberEffect)effect;
                                            final int effectId = memberEffect.getEffectId();
                                            final WakfuEffect wakfuEffect = EffectManager.getInstance().getEffect(effectId);
                                            cumulableEffects.add(wakfuEffect);
                                        }
                                        else {
                                            descs.add(guildBonusView.getEffectDesc());
                                        }
                                    }
                                    final EffectDescription effectDescr = new EffectDescription(WakfuTranslator.getInstance(), cumulableEffects, (short)0, true);
                                    descs.addAll(effectDescr.getEffectsDescription());
                                    return descs;
                                }
                                return null;
                            }
                        }
                    }
                }
            }
        }
    }
    
    private boolean canBuyBonusRegardingSimultaneousMax() {
        final int maxSimultaneous = this.m_guild.getConstantManager().getMaxSimultaneous();
        return this.getLoadingBonuses().size() < maxSimultaneous;
    }
    
    public ArrayList<GuildBonusSubCategoryView> getAvailableBonuses() {
        final TIntObjectHashMap<ArrayList<GuildBonusView>> sortedBonuses = new TIntObjectHashMap<ArrayList<GuildBonusView>>();
        for (final GuildBonusDataAGT data : GuildBonusDataAGT.values()) {
            if (!GuildHelper.isLevelBonus(data)) {
                final GuildBonusDefinition guildBonus = data.get();
                final int bonusId = guildBonus.getId();
                final GuildBonus bonus = this.m_guild.getBonus(bonusId);
                final int level = GuildLevelManager.INSTANCE.getBonusUnlockLevel(bonusId);
                if (!sortedBonuses.containsKey(level)) {
                    sortedBonuses.put(level, new ArrayList<GuildBonusView>());
                }
                final ArrayList<GuildBonusView> guildBonusViews = sortedBonuses.get(level);
                if (bonus == null) {
                    guildBonusViews.add(GuildViewManager.INSTANCE.getBonusView(bonusId));
                }
            }
        }
        final ArrayList<GuildBonusSubCategoryView> guildBonusSubCategoryViews = new ArrayList<GuildBonusSubCategoryView>(sortedBonuses.size());
        GuildBonusSubCategoryView currentGuildBonusSubCategoryView = null;
        final TIntObjectIterator<ArrayList<GuildBonusView>> it = sortedBonuses.iterator();
        while (it.hasNext()) {
            it.advance();
            currentGuildBonusSubCategoryView = new GuildBonusSubCategoryView(it.key(), it.value());
            guildBonusSubCategoryViews.add(currentGuildBonusSubCategoryView);
        }
        Collections.sort(guildBonusSubCategoryViews, new Comparator<GuildBonusSubCategoryView>() {
            @Override
            public int compare(final GuildBonusSubCategoryView o1, final GuildBonusSubCategoryView o2) {
                final int level1 = o1.getLevel();
                final int level2 = o2.getLevel();
                if (level1 > level2) {
                    return 1;
                }
                if (level1 < level2) {
                    return -1;
                }
                return 0;
            }
        });
        return guildBonusSubCategoryViews;
    }
    
    public ArrayList<GuildBonusView> getLoadingBonuses() {
        final ArrayList<GuildBonusView> bonuses = new ArrayList<GuildBonusView>();
        this.m_guild.forEachBonus(new TObjectProcedure<GuildBonus>() {
            @Override
            public boolean execute(final GuildBonus bonus) {
                final GuildBonusDefinition def = GuildBonusManager.INSTANCE.getBonus(bonus.getBonusId());
                final boolean bonusIsLoading = GuildBonusHelper.isLoading(bonus, def, WakfuGuildView.this.m_guild);
                if (bonusIsLoading) {
                    final GameDate endDate = GuildBonusHelper.getStartActivationDate(bonus, def, WakfuGuildView.this.m_guild);
                    if (endDate.after(WakfuGameCalendar.getInstance().getDate())) {
                        bonuses.add(GuildViewManager.INSTANCE.getBonusView(bonus.getBonusId()));
                    }
                }
                return true;
            }
        });
        return bonuses;
    }
    
    public ArrayList<GuildBonusView> getCurrentBonuses(final boolean permanent) {
        final ArrayList<GuildBonusView> views = new ArrayList<GuildBonusView>();
        for (final GuildBonusDataAGT data : GuildBonusDataAGT.values()) {
            if (!GuildHelper.isLevelBonus(data)) {
                final GuildBonusDefinition guildBonus = data.get();
                final GuildBonus bonus = this.m_guild.getBonus(guildBonus.getId());
                if (bonus != null) {
                    if (!(!guildBonus.hasDuration() ^ permanent)) {
                        if (!GuildBonusHelper.isLoading(bonus, guildBonus, this.m_guild)) {
                            views.add(GuildViewManager.INSTANCE.getBonusView(guildBonus.getId()));
                        }
                    }
                }
            }
        }
        return views;
    }
    
    public ArrayList<WakfuGuildMemberView> getGuildMembers(final boolean displayDisconnected) {
        final ArrayList<WakfuGuildMemberView> members = new ArrayList<WakfuGuildMemberView>();
        for (final WakfuGuildMemberView wakfuGuildMemberView : this.m_guildMembers) {
            if (displayDisconnected || wakfuGuildMemberView.isConnected()) {
                members.add(wakfuGuildMemberView);
            }
        }
        return members;
    }
    
    public short getGuildLevel() {
        return this.m_guild.getLevel();
    }
    
    public boolean localPlayerHasRight(final GuildRankAuthorisation authorisation) {
        return this.localPlayerHasRight(authorisation, (short)(-1));
    }
    
    public boolean localPlayerHasRight(final GuildRankAuthorisation authorisation, final short position) {
        if (this.m_guild == null) {
            return false;
        }
        final long myRank = this.getMyRankId();
        if (myRank == -1L) {
            return false;
        }
        final GuildRank rank = this.m_guild.getRank(myRank);
        return rank != null && rank.hasAuthorisation(authorisation, position);
    }
    
    public long getMyRankId() {
        final WakfuGuildMemberView myMemberView = this.getMyMemberView();
        if (myMemberView == null) {
            return -1L;
        }
        return myMemberView.getRank();
    }
    
    public GuildRank getMyRank() {
        if (this.m_guild == null) {
            return null;
        }
        return this.m_guild.getRank(this.getMyRankId());
    }
    
    public boolean isLocalPlayerLeader() {
        if (this.m_guild == null) {
            return false;
        }
        final WakfuGuildMemberView myMemberView = this.getMyMemberView();
        return myMemberView != null && this.m_guild.getBestRank() == myMemberView.getRank();
    }
    
    public WakfuGuildMemberView getMyMemberView() {
        return this.getMemberView(WakfuGameEntity.getInstance().getLocalPlayer().getId());
    }
    
    public WakfuGuildMemberView getMemberView(final long characterId) {
        for (final WakfuGuildMemberView memberView : this.m_guildMembers) {
            if (memberView.getId() == characterId) {
                return memberView;
            }
        }
        return null;
    }
    
    public void updateRanksField(final boolean refresh) {
        this.m_guildRankViews.clear();
        this.m_guild.forEachRank(new TObjectProcedure<GuildRank>() {
            @Override
            public boolean execute(final GuildRank guildRank) {
                WakfuGuildView.this.m_guildRankViews.add(new GuildRankView(guildRank));
                return true;
            }
        });
        this.sortMembersField(refresh);
        this.sortRanksField();
        if (refresh) {
            PropertiesProvider.getInstance().firePropertyValueChanged(this, "ranks", "hasRightToEditGuildDescription", "hasRightToEditGuildMessage", "hasRightToEditRanks", "hasRightToCreateRanks", "hasRightToBuyBonus");
        }
    }
    
    private void sortRanksField() {
        Collections.sort(this.m_guildRankViews, new Comparator<GuildRankView>() {
            @Override
            public int compare(final GuildRankView o1, final GuildRankView o2) {
                if (o1.getAuthorisationsLong() < o2.getAuthorisationsLong()) {
                    return 1;
                }
                if (o2.getAuthorisationsLong() < o1.getAuthorisationsLong()) {
                    return -1;
                }
                return 0;
            }
        });
    }
    
    private void updateMemberField(final GuildMember member) {
        for (final WakfuGuildMemberView wakfuGuildMemberView : this.m_guildMembers) {
            if (wakfuGuildMemberView.getId() == member.getId()) {
                PropertiesProvider.getInstance().firePropertyValueChanged(wakfuGuildMemberView, WakfuGuildMemberView.FIELDS);
            }
        }
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "hasRightToEditGuildDescription", "hasRightToEditGuildMessage", "hasRightToEditRanks", "hasRightToCreateRanks", "hasRightToBuyBonus");
    }
    
    private void updateRankField(final GuildRank rank) {
        for (final GuildRankView guildRankView : this.m_guildRankViews) {
            if (guildRankView.getId() == rank.getId()) {
                PropertiesProvider.getInstance().firePropertyValueChanged(guildRankView, GuildRankView.FIELDS);
            }
        }
    }
    
    public void updateMembersField(final boolean refresh) {
        this.m_guildMembers.clear();
        this.m_guild.forEachMember(new TObjectProcedure<GuildMember>() {
            @Override
            public boolean execute(final GuildMember guildMember) {
                WakfuGuildView.this.m_guildMembers.add(new WakfuGuildMemberView(guildMember));
                return true;
            }
        });
        this.sortMembersField(refresh);
        if (refresh) {
            PropertiesProvider.getInstance().firePropertyValueChanged(this, "numMembers", "membersLevelAverage", "numMembers", "membersList", "localPlayer", "hasRightToEditRanks", "hasRightToCreateRanks", "hasRightToEditGuildDescription", "hasRightToEditGuildMessage", "hasRightToBuyBonus");
        }
    }
    
    private void sortMembersField(final boolean refresh) {
        Collections.sort(this.m_guildMembers, this.m_currentComparator);
        if (refresh) {
            PropertiesProvider.getInstance().firePropertyValueChanged(this, "iAmLeader", "canInvite", "membersLevelAverage", "localPlayer", "membersList");
        }
    }
    
    public boolean isDisplayDisconnectedMembers() {
        return this.m_displayDisconnectedMembers;
    }
    
    public void setDisplayDisconnectedMembers(final boolean displayDisconnectedMembers) {
        if (this.m_displayDisconnectedMembers == displayDisconnectedMembers) {
            return;
        }
        this.m_displayDisconnectedMembers = displayDisconnectedMembers;
        this.updateMembersField(true);
    }
    
    public void requestMemberRankChange(final long characterId, final long rank) {
        final GuildChangeMemberRankRequestMessage message = new GuildChangeMemberRankRequestMessage(characterId, rank);
        final NetworkEntity networkEntity = WakfuGameEntity.getInstance().getNetworkEntity();
        if (networkEntity != null) {
            networkEntity.sendMessage(message);
        }
        else {
            ConsoleManager.getInstance().err("Pour acc\u00e9der \u00e0 ces commandes il faut \u00eatre connect\u00e9 !");
        }
    }
    
    public void requestRemoveById(final long id) {
        final GuildRemoveMemberRequestMessage message = new GuildRemoveMemberRequestMessage(id);
        final NetworkEntity networkEntity = WakfuGameEntity.getInstance().getNetworkEntity();
        if (networkEntity != null) {
            networkEntity.sendMessage(message);
        }
        else {
            ConsoleManager.getInstance().err("Pour acc\u00e9der \u00e0 ces commandes il faut \u00eatre connect\u00e9 !");
        }
    }
    
    public void inviteByName(final String name) {
        if (!ChatHelper.controlAction(new Action(name, 3))) {
            return;
        }
        final String chatMessage = WakfuTranslator.getInstance().getString("guild.inviting.character", name);
        ChatManager.getInstance().pushMessage(chatMessage, 4);
        final GuildAddMemberRequestMessage message = new GuildAddMemberRequestMessage(name);
        final NetworkEntity networkEntity = WakfuGameEntity.getInstance().getNetworkEntity();
        if (networkEntity != null) {
            networkEntity.sendMessage(message);
        }
        else {
            ConsoleManager.getInstance().err("Pour acc\u00e9der \u00e0 ces commandes il faut \u00eatre connect\u00e9 !");
        }
    }
    
    public void disband() {
        final GuildDisbandRequestMessage message = new GuildDisbandRequestMessage();
        final NetworkEntity networkEntity = WakfuGameEntity.getInstance().getNetworkEntity();
        if (networkEntity != null) {
            networkEntity.sendMessage(message);
        }
        else {
            ConsoleManager.getInstance().err("Pour acc\u00e9der \u00e0 ces commandes il faut \u00eatre connect\u00e9 !");
        }
    }
    
    public long getBestRank() {
        if (this.m_guild == null) {
            return -1L;
        }
        return this.m_guild.getBestRank();
    }
    
    public long getWorstRank() {
        if (this.m_guild == null) {
            return -1L;
        }
        return this.m_guild.getWorstRank();
    }
    
    private ArrayList<AchievementView> getQuests() {
        final ArrayList<AchievementView> quests = new ArrayList<AchievementView>();
        GuildDataManager.INSTANCE.forEachQuest(new TIntProcedure() {
            @Override
            public boolean execute(final int questId) {
                quests.add(AchievementsViewManager.INSTANCE.getAchievement(WakfuGameEntity.getInstance().getLocalPlayer().getId(), questId));
                return true;
            }
        });
        return quests;
    }
    
    public void clean() {
        if (this.m_guild != null) {
            this.m_guild.removeListener(this.m_listener);
        }
        this.m_guild = null;
        this.m_guildMembers.clear();
        this.m_guildRankViews.clear();
        this.m_smileyViews.clear();
    }
    
    static {
        m_logger = Logger.getLogger((Class)WakfuGuildView.class);
        m_instance = new WakfuGuildView();
        FIELDS = new String[] { "nation", "name", "blazon", "numMembers", "membersLevelAverage", "guildLevelText", "currentGuildPoints", "totalGuildPoints", "membersList", "displayDisconnectedMembers", "canInvite", "iAmLeader", "guildDescription", "guildMessage", "hasRightToEditGuildDescription", "hasRightToEditGuildMessage", "hasRightToEditRanks", "hasRightToCreateRanks", "hasRightToBuyBonus", "havenWorldPageWarning", "localPlayer", "ranks", "quests", "level", "nextLevel", "nextLevelCost", "availableBonuses", "loadingBonuses", "temporaryBonuses", "permanentBonuses", "simultaneousBonusesText", "conquestPoints", "currentWeekGuildPoints" };
    }
    
    private static class NameComparator implements Comparator<WakfuGuildMemberView>
    {
        private static final NameComparator DIRECT;
        private static final NameComparator INDIRECT;
        private boolean m_direct;
        
        private NameComparator(final boolean direct) {
            super();
            this.m_direct = true;
            this.m_direct = direct;
        }
        
        @Override
        public int compare(final WakfuGuildMemberView o1, final WakfuGuildMemberView o2) {
            return this.m_direct ? o1.getName().compareTo(o2.getName()) : o2.getName().compareTo(o1.getName());
        }
        
        static {
            DIRECT = new NameComparator(true);
            INDIRECT = new NameComparator(false);
        }
    }
    
    private static class LevelComparator implements Comparator<WakfuGuildMemberView>
    {
        private static final LevelComparator DIRECT;
        private static final LevelComparator INDIRECT;
        private boolean m_direct;
        
        private LevelComparator(final boolean direct) {
            super();
            this.m_direct = true;
            this.m_direct = direct;
        }
        
        @Override
        public int compare(final WakfuGuildMemberView o1, final WakfuGuildMemberView o2) {
            return this.m_direct ? (o1.getLevel() - o2.getLevel()) : (o2.getLevel() - o1.getLevel());
        }
        
        static {
            DIRECT = new LevelComparator(true);
            INDIRECT = new LevelComparator(false);
        }
    }
    
    private static class GuildPointsComparator implements Comparator<WakfuGuildMemberView>
    {
        private static final GuildPointsComparator DIRECT;
        private static final GuildPointsComparator INDIRECT;
        private boolean m_direct;
        
        private GuildPointsComparator(final boolean direct) {
            super();
            this.m_direct = true;
            this.m_direct = direct;
        }
        
        @Override
        public int compare(final WakfuGuildMemberView o1, final WakfuGuildMemberView o2) {
            if (o1.getGuildPoints() > o2.getGuildPoints()) {
                return this.m_direct ? 1 : -1;
            }
            if (o1.getGuildPoints() < o2.getGuildPoints()) {
                return this.m_direct ? -1 : 1;
            }
            return 0;
        }
        
        static {
            DIRECT = new GuildPointsComparator(true);
            INDIRECT = new GuildPointsComparator(false);
        }
    }
    
    private static class RankComparator implements Comparator<WakfuGuildMemberView>
    {
        private static final RankComparator DIRECT;
        private static final RankComparator INDIRECT;
        private boolean m_direct;
        
        private RankComparator(final boolean direct) {
            super();
            this.m_direct = true;
            this.m_direct = direct;
        }
        
        @Override
        public int compare(final WakfuGuildMemberView o1, final WakfuGuildMemberView o2) {
            if (o1.getRank() > o2.getRank()) {
                return this.m_direct ? 1 : -1;
            }
            if (o1.getRank() < o2.getRank()) {
                return this.m_direct ? -1 : 1;
            }
            return 0;
        }
        
        static {
            DIRECT = new RankComparator(true);
            INDIRECT = new RankComparator(false);
        }
    }
}
