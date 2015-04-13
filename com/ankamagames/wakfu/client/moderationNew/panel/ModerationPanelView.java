package com.ankamagames.wakfu.client.moderationNew.panel;

import com.ankamagames.wakfu.client.ui.component.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.common.constants.*;
import java.util.*;
import org.jetbrains.annotations.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.framework.reflect.*;

public class ModerationPanelView extends ImmutableFieldProvider
{
    public static final String CURRENT_PAGE = "currentPage";
    public static final String PLAYER_LIST = "playerList";
    public static final String WARNING_LIST = "warningList";
    public static final String MUTE_LIST = "muteList";
    public static final String SANCTION_LIST = "sanctionList";
    public static final String CURRENT_SEARCH = "currentSearch";
    public static final String SEARCH_RESULT = "searchResult";
    public static final String MODERATORS_LIST = "moderatorsList";
    public static final String MODERATOR_GAMESERVER = "moderatorGameServer";
    public static final String MODERATOR_POSITION = "moderatorPosition";
    public static final String MODERATOR_INSTANCE = "moderatorInstance";
    public static final String CURRENT_PLAYER = "currentPlayer";
    public static final String LOGS = "logs";
    private ArrayList<String> m_moderatorList;
    private LinkedList<String> m_logs;
    private LinkedList<String> m_searchResult;
    private String m_gameServer;
    private ModerationPanelPage m_currentPage;
    private Collection<ModeratedPlayerView> m_moderatedPlayerViews;
    private ModeratedPlayerView m_currentPlayer;
    
    public ModerationPanelView() {
        super();
        this.m_moderatorList = new ArrayList<String>();
        this.m_logs = new LinkedList<String>();
        this.m_searchResult = new LinkedList<String>();
        this.m_currentPage = ModerationPanelPage.MAIN;
        this.m_moderatedPlayerViews = new ArrayList<ModeratedPlayerView>();
    }
    
    @Override
    public String[] getFields() {
        return new String[0];
    }
    
    @Nullable
    @Override
    public Object getFieldValue(final String fieldName) {
        if ("currentPage".equals(fieldName)) {
            return this.m_currentPage.getId();
        }
        if ("searchResult".equals(fieldName)) {
            if (!this.m_searchResult.isEmpty()) {
                return this.m_searchResult;
            }
            return null;
        }
        else {
            if ("playerList".equals(fieldName)) {
                return this.m_moderatedPlayerViews;
            }
            if ("moderatorsList".equals(fieldName)) {
                String result = WakfuTranslator.getInstance().getString("moderationPanel.inGameModerators") + " :";
                for (final String moderatorPseudo : this.m_moderatorList) {
                    result = result + "\n - " + moderatorPseudo;
                }
                return result;
            }
            if ("moderatorPosition".equals(fieldName)) {
                final int positionX = WakfuGameEntity.getInstance().getLocalPlayer().getWorldCellX();
                final int positionY = WakfuGameEntity.getInstance().getLocalPlayer().getWorldCellY();
                final int positionZ = WakfuGameEntity.getInstance().getLocalPlayer().getWorldCellAltitude();
                return WakfuTranslator.getInstance().getString("moderationPanel.currentPosition") + " : " + positionX + ", " + positionY + ", " + positionZ;
            }
            if ("moderatorGameServer".equals(fieldName)) {
                return WakfuTranslator.getInstance().getString("moderationPanel.currentGameServer") + " : " + this.m_gameServer;
            }
            if ("moderatorInstance".equals(fieldName)) {
                final short instanceId = WakfuGameEntity.getInstance().getLocalPlayer().getInstanceId();
                return WakfuTranslator.getInstance().getString("moderationPanel.currentInstance") + " : " + instanceId;
            }
            if ("currentPlayer".equals(fieldName)) {
                return this.m_currentPlayer;
            }
            if ("warningList".equals(fieldName)) {
                final Collection<ModerationSanctionView> sanctionList = new ArrayList<ModerationSanctionView>();
                for (final ModerationSanction constant : ModerationSanction.values()) {
                    if (constant.getSanctionType() == ModerationSanctionType.WARNING) {
                        sanctionList.add(new ModerationSanctionView(constant));
                    }
                }
                return sanctionList;
            }
            if ("muteList".equals(fieldName)) {
                final Collection<ModerationSanctionView> sanctionList = new ArrayList<ModerationSanctionView>();
                for (final ModerationSanction constant : ModerationSanction.values()) {
                    if (constant.getSanctionType() == ModerationSanctionType.MUTE) {
                        sanctionList.add(new ModerationSanctionView(constant));
                    }
                }
                return sanctionList;
            }
            if ("sanctionList".equals(fieldName)) {
                final Collection<ModerationSanctionView> sanctionList = new ArrayList<ModerationSanctionView>();
                for (final ModerationSanction constant : ModerationSanction.values()) {
                    if (constant.getSanctionType() == ModerationSanctionType.BAN) {
                        sanctionList.add(new ModerationSanctionView(constant));
                    }
                }
                return sanctionList;
            }
            if ("logs".equals(fieldName)) {
                return Collections.unmodifiableList((List<?>)this.m_logs);
            }
            return null;
        }
    }
    
    public ModeratedPlayerView getCurrentPlayer() {
        return this.m_currentPlayer;
    }
    
    public void addPlayerTab(final String viewName) {
        if (viewName == null) {
            return;
        }
        if (this.isAleadyInPlayerTab(viewName)) {
            return;
        }
        final ModeratedPlayerView moderatedPlayerView = new ModeratedPlayerView(viewName);
        this.m_moderatedPlayerViews.add(moderatedPlayerView);
        this.m_currentPlayer = moderatedPlayerView;
        this.m_currentPage = ModerationPanelPage.PLAYER;
        UIModerationPanelFrame.INSTANCE.request((short)1, (byte)3, viewName);
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "playerList", "currentPage");
    }
    
    public void addPlayerTabWithView(final ModeratedPlayerView view) {
        if (this.isAleadyInPlayerTab(view.getName())) {
            return;
        }
        this.m_moderatedPlayerViews.add(view);
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "playerList");
    }
    
    public void removeModeratedPlayer(final ModeratedPlayerView moderatedPlayerView) {
        this.m_moderatedPlayerViews.remove(moderatedPlayerView);
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "playerList");
    }
    
    public void setCurrentPage(final ModerationPanelPage currentPage) {
        this.m_currentPage = currentPage;
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "currentPage");
    }
    
    public void setCurrentPlayer(final ModeratedPlayerView playerView) {
        this.m_currentPlayer = playerView;
        if (!this.isAleadyInPlayerTab(playerView.getName())) {
            this.addPlayerTabWithView(playerView);
        }
        UIModerationPanelFrame.INSTANCE.request((short)1, (byte)3, playerView.getName());
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "currentPlayer", "playerList");
    }
    
    public void setGameServer(final String gameServer) {
        this.m_gameServer = gameServer;
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "moderatorGameServer");
    }
    
    public void setModeratorsList(final ArrayList<String> moderatorsList) {
        this.m_moderatorList = moderatorsList;
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "moderatorsList");
    }
    
    public void setSearchList(final LinkedList<String> searchResult) {
        this.m_searchResult = searchResult;
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "searchResult");
    }
    
    public void addToLogs(final String logEntry) {
        this.m_logs.addFirst(logEntry);
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "logs");
    }
    
    private boolean isAleadyInPlayerTab(final String viewName) {
        for (final ModeratedPlayerView moderatedPlayerView : this.m_moderatedPlayerViews) {
            if (moderatedPlayerView.getFieldValue("nameWithoutText").equals(viewName)) {
                return true;
            }
        }
        return false;
    }
    
    public ModerationPanelPage getCurrentPage() {
        return this.m_currentPage;
    }
}
