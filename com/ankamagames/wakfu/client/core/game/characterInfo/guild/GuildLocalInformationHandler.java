package com.ankamagames.wakfu.client.core.game.characterInfo.guild;

import org.jetbrains.annotations.*;
import gnu.trove.*;
import com.ankamagames.wakfu.common.game.guild.definition.*;
import com.ankamagames.wakfu.client.core.game.group.guild.*;
import java.nio.*;
import com.ankamagames.wakfu.common.game.guild.*;
import com.ankamagames.framework.kernel.events.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.core.game.notificationSystem.*;
import com.ankamagames.wakfu.client.ui.actions.*;
import com.ankamagames.wakfu.client.ui.protocol.message.notificationMessage.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;
import com.ankamagames.wakfu.client.core.game.storageBox.guild.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.framework.reflect.*;
import com.ankamagames.wakfu.common.game.guild.constant.*;

public class GuildLocalInformationHandler implements ClientGuildInformationHandler
{
    @Nullable
    private Guild m_guild;
    private int m_havenWorldId;
    private float m_moderationBonusLearningFactor;
    
    @Override
    public long getGuildId() {
        return (this.m_guild != null) ? this.m_guild.getId() : 0L;
    }
    
    @Nullable
    @Override
    public GuildMember getMember(final long id) {
        if (this.m_guild == null) {
            return null;
        }
        return this.m_guild.getMember(id);
    }
    
    @Override
    public GuildRank getRank(final long rankId) {
        return (this.m_guild != null) ? this.m_guild.getRank(rankId) : null;
    }
    
    @Override
    public long getBestRank() {
        return (this.m_guild != null) ? this.m_guild.getBestRank() : 0L;
    }
    
    @Override
    public int getHavenWorldId() {
        return this.m_havenWorldId;
    }
    
    @Override
    public int getNationId() {
        return (this.m_guild != null) ? this.m_guild.getNationId() : 0;
    }
    
    @Override
    public TIntHashSet getActiveBonuses() {
        if (this.m_guild == null) {
            return new TIntHashSet();
        }
        final FillActiveBonuses filler = new FillActiveBonuses();
        this.m_guild.forEachBonus(filler);
        return filler.getActiveBonuses();
    }
    
    @Nullable
    public Guild getGuild() {
        return this.m_guild;
    }
    
    @Override
    public String getName() {
        return (this.m_guild != null) ? this.m_guild.getName() : "";
    }
    
    @Override
    public long getBlazon() {
        return (this.m_guild != null) ? this.m_guild.getBlazon() : 0L;
    }
    
    @Override
    public short getLevel() {
        return (short)((this.m_guild != null) ? this.m_guild.getLevel() : 0);
    }
    
    public int getCurrentGuildPoints() {
        return (this.m_guild != null) ? this.m_guild.getCurrentGuildPoints() : 0;
    }
    
    public void setSerializedGuild(final byte... rawGuild) {
        final Guild guild = this.m_guild;
        WakfuGuildView.getInstance().clean();
        GuildViewManager.INSTANCE.clean();
        if (this.m_guild == null) {
            this.m_guild = ((rawGuild != null) ? GuildSerializer.unSerializeGuild(ByteBuffer.wrap(rawGuild)) : null);
        }
        else if (rawGuild == null) {
            this.m_guild = null;
        }
        else {
            GuildSerializer.unSerializeGuild(ByteBuffer.wrap(rawGuild), this.m_guild);
        }
        if (this.m_guild != null) {
            GuildViewManager.INSTANCE.init();
            WakfuGuildView.getInstance().init();
            if (guild == null && this.m_guild.getMessage() != null && this.m_guild.getMessage().length() > 0) {
                WakfuGuildView.getInstance().displayGuildMessage();
            }
            if (WakfuGameEntity.getInstance().hasFrame(UIGuildCreatorFrame.getInstance()) && WakfuGuildView.getInstance().size() == 1) {
                String notifTitleTranslatorKey = null;
                String notifText = null;
                notifTitleTranslatorKey = "notification.guildCreateTitle";
                notifText = WakfuTranslator.getInstance().getString("notification.guildCreateText", this.m_guild.getName());
                final String title = WakfuTranslator.getInstance().getString(notifTitleTranslatorKey);
                final String text = NotificationPanelDialogActions.createLink(notifText, NotificationMessageType.SOCIAL);
                final UINotificationMessage uiNotificationMessage = new UINotificationMessage(title, text, NotificationMessageType.SOCIAL);
                Worker.getInstance().pushMessage(uiNotificationMessage);
                WakfuGameEntity.getInstance().removeFrame(UIGuildCreatorFrame.getInstance());
            }
        }
        else if (guild != null) {
            String notifTitleTranslatorKey = null;
            String notifText = null;
            notifTitleTranslatorKey = "notification.guildQuitTitle";
            notifText = WakfuTranslator.getInstance().getString("notification.guildSelfQuitText", guild.getName());
            final String title = WakfuTranslator.getInstance().getString(notifTitleTranslatorKey);
            final String text = NotificationPanelDialogActions.createLink(notifText, NotificationMessageType.SOCIAL);
            final UINotificationMessage uiNotificationMessage = new UINotificationMessage(title, text, NotificationMessageType.SOCIAL);
            Worker.getInstance().pushMessage(uiNotificationMessage);
            WakfuGuildView.getInstance().clean();
            if (WakfuGameEntity.getInstance().hasFrame(UIGuildManagementFrame.getInstance())) {
                WakfuGameEntity.getInstance().removeFrame(UIGuildManagementFrame.getInstance());
            }
            if (WakfuGameEntity.getInstance().hasFrame(UIStorageBoxFrame.getInstance()) && UIStorageBoxFrame.getInstance().getStorageBoxBoxView() instanceof GuildStorageBoxView) {
                WakfuGameEntity.getInstance().removeFrame(UIStorageBoxFrame.getInstance());
            }
        }
        PropertiesProvider.getInstance().firePropertyValueChanged(WakfuGameEntity.getInstance().getLocalPlayer(), "hasGuild");
    }
    
    public void setHavenWorldId(final int havenWorldId) {
        this.m_havenWorldId = havenWorldId;
    }
    
    public void setModerationBonusLearningFactor(final float moderationBonusLearningFactor) {
        this.m_moderationBonusLearningFactor = moderationBonusLearningFactor;
    }
    
    public float getModerationBonusLearningFactor() {
        return this.m_moderationBonusLearningFactor;
    }
    
    @Override
    public void clear() {
        this.m_guild = null;
        this.m_havenWorldId = 0;
    }
    
    @Override
    public String toString() {
        return "GuildLocalInformationHandler{m_guild=" + this.m_guild + '}';
    }
    
    private static class FillActiveBonuses implements TObjectProcedure<GuildBonus>
    {
        private final TIntHashSet m_activeBonuses;
        
        FillActiveBonuses() {
            super();
            this.m_activeBonuses = new TIntHashSet();
        }
        
        @Override
        public boolean execute(final GuildBonus object) {
            if (GuildTimeHelper.isActive(object)) {
                this.m_activeBonuses.add(object.getBonusId());
            }
            return true;
        }
        
        TIntHashSet getActiveBonuses() {
            return this.m_activeBonuses;
        }
        
        @Override
        public String toString() {
            return "FillActiveBonuses{m_activeBonuses=" + this.m_activeBonuses.size() + '}';
        }
    }
}
