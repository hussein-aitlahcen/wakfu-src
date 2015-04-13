package com.ankamagames.wakfu.client.core.game.group.party;

import com.ankamagames.wakfu.client.ui.component.*;
import org.apache.log4j.*;
import com.ankamagames.wakfu.common.game.group.member.*;
import com.ankamagames.wakfu.client.core.game.group.party.member.*;
import com.ankamagames.wakfu.common.game.companion.*;
import com.ankamagames.baseImpl.client.proxyclient.base.chat.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.wakfu.client.ui.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.framework.reflect.*;
import java.util.*;
import com.ankamagames.wakfu.client.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.framework.graphics.engine.texture.*;
import com.ankamagames.framework.text.*;
import com.ankamagames.framework.fileFormat.properties.*;
import com.ankamagames.wakfu.common.game.fighter.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.characteristic.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import gnu.trove.*;

public class PartyDisplayer extends ImmutableFieldProvider
{
    protected static final Logger m_logger;
    public static final String MEMBERS_FIELD = "members";
    public static final String LEADER_FIELD = "leader";
    public static final String IS_LEADER_FIELD = "isLeader";
    public static final String AVAILABLE_SLOTS_COUNT_FIELD = "availableSlotsCount";
    public static final String PARTY_FULL_FIELD = "partyFull";
    public static final String LOCAL_PLAYER_MEMBER_FIELD = "localPlayerMember";
    public static final String EDITABLE_MEMBER_FIELD = "editableMember";
    public static final String[] FIELDS;
    private PartyComportment m_party;
    TLongObjectHashMap<PartyMemberDisplayer> m_partyMemberDisplayers;
    private PartyMemberDisplayer m_leaderMember;
    private PartyMemberDisplayer m_localPlayerMember;
    private PartyMemberDisplayer m_editableMember;
    
    public PartyDisplayer(final PartyComportment party) {
        super();
        this.m_partyMemberDisplayers = new TLongObjectHashMap<PartyMemberDisplayer>();
        this.initialize(party);
    }
    
    public void setEditableMember(final PartyMemberDisplayer editableMember) {
        this.m_editableMember = editableMember;
    }
    
    public void initialize(final PartyComportment party) {
        this.clear();
        this.m_party = party;
        if (party.getParty() != null) {
            final TLongObjectIterator<PartyMemberInterface> iterator = party.getParty().getMembers().iterator();
            while (iterator.hasNext()) {
                iterator.advance();
                final PartyMemberInterface partyMember = iterator.value();
                final PartyMemberDisplayer displayer = new PartyMemberDisplayer(partyMember);
                displayer.onCharacterSpawn();
                if (partyMember.isCompanion()) {
                    final CompanionModel companionModel = ((CompanionPartyMemberModel)partyMember).getCompanionModel();
                    companionModel.addListener(new CompanionHpModificationListener(displayer));
                }
                this.m_partyMemberDisplayers.put(partyMember.getCharacterId(), displayer);
                if (partyMember.getCharacterId() == WakfuGameEntity.getInstance().getLocalPlayer().getId()) {
                    this.m_localPlayerMember = displayer;
                }
                if (partyMember.getCharacterId() == party.getParty().getLeaderId()) {
                    this.m_leaderMember = displayer;
                }
                if (displayer.isFollowed() && partyMember.getInstanceId() != WakfuGameEntity.getInstance().getLocalPlayer().getInstanceId()) {
                    this.followMember(partyMember.getCharacterId(), false);
                }
            }
        }
    }
    
    public void clear() {
        final TLongObjectIterator<PartyMemberDisplayer> it = this.m_partyMemberDisplayers.iterator();
        while (it.hasNext()) {
            it.advance();
            it.value().onLeaveParty();
        }
        this.m_partyMemberDisplayers.clear();
        this.m_localPlayerMember = null;
        this.m_leaderMember = null;
        this.m_editableMember = null;
    }
    
    public void exclude(final long memberId) {
        this.m_party.exclude(memberId);
        final PartyMemberInterface member = this.m_party.getParty().getMember(memberId);
        if (member == null) {
            PartyDisplayer.m_logger.error((Object)("impossible de retrouver le membre du groupe d'id : " + memberId));
            return;
        }
        final String chatMessage = (memberId == WakfuGameEntity.getInstance().getLocalPlayer().getId()) ? WakfuTranslator.getInstance().getString("group.party.memberDeletedSelf") : WakfuTranslator.getInstance().getString("group.party.memberDeleted", member.getName());
        ChatManager.getInstance().pushMessage(chatMessage, 4);
    }
    
    public void followMember(final long memberId, final boolean activate) {
        if (memberId < 0L) {
            PartyDisplayer.m_logger.error((Object)("on essai d'appliquer la boussole sur un membre invalide d'id : " + memberId));
            return;
        }
        final PartyMemberInterface member = this.m_party.getParty().getMember(memberId);
        if (member == null) {
            PartyDisplayer.m_logger.error((Object)("impossible de retrouver le membre du groupe d'id : " + memberId));
            return;
        }
        if (activate) {
            if (member.getInstanceId() != WakfuGameEntity.getInstance().getLocalPlayer().getInstanceId()) {
                Xulor.getInstance().msgBox(WakfuTranslator.getInstance().getString("error.group.memberTooFar"), WakfuMessageBoxConstants.getMessageBoxIconUrl(1), 3L, 102, 1);
                PropertiesProvider.getInstance().firePropertyValueChanged(this.m_partyMemberDisplayers.get(member.getCharacterId()), "isFollowed");
                return;
            }
            final String chatMessage = WakfuTranslator.getInstance().getString("group.party.memberFollowed", member.getName());
            ChatManager.getInstance().pushMessage(chatMessage, 4);
        }
        else {
            final String chatMessage = WakfuTranslator.getInstance().getString("group.party.stopFollowing", member.getName());
            ChatManager.getInstance().pushMessage(chatMessage, 4);
        }
        member.setFollowed(activate);
        PropertiesProvider.getInstance().firePropertyValueChanged(this.m_partyMemberDisplayers.get(member.getCharacterId()), "isFollowed");
    }
    
    public void addPartyMember(final String memberName) {
        this.m_party.inviteSomeone(memberName);
    }
    
    public PartyMemberDisplayer getPartyMember(final long id) {
        return this.m_partyMemberDisplayers.get(id);
    }
    
    @Override
    public String[] getFields() {
        return PartyDisplayer.FIELDS;
    }
    
    @Override
    public Object getFieldValue(final String fieldName) {
        if (fieldName.equals("members")) {
            final Object[] objects = this.m_partyMemberDisplayers.getValues();
            final ArrayList<PartyMemberDisplayer> memberDisplayers = new ArrayList<PartyMemberDisplayer>();
            for (final Object o : objects) {
                memberDisplayers.add((PartyMemberDisplayer)o);
            }
            Collections.sort(memberDisplayers);
            return (memberDisplayers.size() > 0) ? memberDisplayers : null;
        }
        if (fieldName.equals("leader")) {
            return this.m_leaderMember;
        }
        if (fieldName.equals("isLeader")) {
            return this.m_leaderMember == this.m_localPlayerMember;
        }
        if (fieldName.equals("availableSlotsCount")) {
            final int availableSlots = 6 - this.m_partyMemberDisplayers.size();
            return WakfuTranslator.getInstance().getString("partyList.availableSlots", availableSlots);
        }
        if (fieldName.equals("partyFull")) {
            return this.m_partyMemberDisplayers.size() >= 6;
        }
        if (fieldName.equals("localPlayerMember")) {
            return this.m_localPlayerMember;
        }
        if (fieldName.equals("editableMember")) {
            return this.m_editableMember;
        }
        return null;
    }
    
    static {
        m_logger = Logger.getLogger((Class)PartyDisplayer.class);
        FIELDS = new String[] { "members", "leader", "isLeader", "availableSlotsCount", "partyFull", "localPlayerMember", "editableMember" };
    }
    
    public class PartyMemberDisplayer extends ImmutableFieldProvider implements Comparable, CharacterImageGenerator.Listener
    {
        public static final String NAME_FIELD = "name";
        public static final String ICON_URL = "iconUrl";
        private static final String LEVEL_FIELD = "level";
        private static final String IS_FOLLOWED = "isFollowed";
        private static final String IS_LEADER = "isLeader";
        private static final String STATE_STYLE_FIELD = "stateStyle";
        public static final String HP_DESCRIPTION_FIELD = "hpDescription";
        public static final String HP_PERCENTAGE_FIELD = "hpPercentage";
        private static final String POSITION_ICON_URL_FIELD = "positionIconUrl";
        private static final String OWNER_FIELD = "owner";
        private PartyMemberInterface m_partyMember;
        private CharacteristicUpdateListener m_listener;
        
        private PartyMemberDisplayer(final PartyMemberInterface partyMember) {
            super();
            this.m_listener = new CharacteristicUpdateListener() {
                @Override
                public void onCharacteristicUpdated(final AbstractCharacteristic charac) {
                    final FighterCharacteristic hp = (FighterCharacteristic)charac;
                    PartyMemberDisplayer.this.m_partyMember.setCurrentHp(hp.value());
                    PartyMemberDisplayer.this.m_partyMember.setMaxHp(hp.max());
                    PropertiesProvider.getInstance().firePropertyValueChanged(PartyMemberDisplayer.this, "hpDescription", "hpPercentage");
                }
            };
            this.m_partyMember = partyMember;
        }
        
        @Override
        public String[] getFields() {
            return PartyDisplayer.FIELDS;
        }
        
        private String getPositionIconUrl() {
            final Point3 localPlayerPos = PartyDisplayer.this.m_localPlayerMember.getPartyMember().getPosition();
            final Point3 memberPos = this.m_partyMember.getPosition();
            final int deltaX = memberPos.getX() - localPlayerPos.getX();
            final int deltaY = memberPos.getY() - localPlayerPos.getY();
            if (deltaX == 0 && deltaY == 0) {
                return null;
            }
            final float screenX = WakfuClientInstance.getInstance().getWorldScene().isoToScreenX(deltaX, deltaY);
            final float screenY = WakfuClientInstance.getInstance().getWorldScene().isoToScreenY(deltaX, deltaY);
            final float rayon = Vector2.length(screenX, screenY);
            final int vectX = Math.round(screenX / rayon * MathHelper.SQRT_2);
            final int vectY = Math.round(screenY / rayon * MathHelper.SQRT_2);
            switch (Direction8.getDirectionFromVector(vectX, vectY)) {
                case EAST: {
                    return WakfuConfiguration.getInstance().getIconUrl("compassIconsPath", "defaultIconPath", 1);
                }
                case SOUTH_EAST: {
                    return WakfuConfiguration.getInstance().getIconUrl("compassIconsPath", "defaultIconPath", 0);
                }
                case SOUTH: {
                    return WakfuConfiguration.getInstance().getIconUrl("compassIconsPath", "defaultIconPath", 7);
                }
                case SOUTH_WEST: {
                    return WakfuConfiguration.getInstance().getIconUrl("compassIconsPath", "defaultIconPath", 6);
                }
                case WEST: {
                    return WakfuConfiguration.getInstance().getIconUrl("compassIconsPath", "defaultIconPath", 5);
                }
                case NORTH_WEST: {
                    return WakfuConfiguration.getInstance().getIconUrl("compassIconsPath", "defaultIconPath", 4);
                }
                case NORTH: {
                    return WakfuConfiguration.getInstance().getIconUrl("compassIconsPath", "defaultIconPath", 3);
                }
                case NORTH_EAST: {
                    return WakfuConfiguration.getInstance().getIconUrl("compassIconsPath", "defaultIconPath", 2);
                }
                default: {
                    return null;
                }
            }
        }
        
        public void resetIconUrl() {
            CharacterImageGenerator.getInstance().deleteCharacterImage(this.getId());
            PropertiesProvider.getInstance().firePropertyValueChanged(this, "iconUrl");
        }
        
        @Override
        public void onDone(final Texture texture, final String message) {
            if (texture != null) {
                PropertiesProvider.getInstance().firePropertyValueChanged(this, "iconUrl");
            }
            else {
                PartyDisplayer.m_logger.warn((Object)message);
            }
        }
        
        @Override
        public Object getFieldValue(final String fieldName) {
            if (fieldName.equals("name")) {
                return this.getName();
            }
            if (fieldName.equals("iconUrl")) {
                return this.getCharacterImage();
            }
            if (fieldName.equals("level")) {
                final short level = this.m_partyMember.isCompanion() ? ((short)Math.min(this.getLowestOwnerLevel(), this.m_partyMember.getLevel())) : this.m_partyMember.getLevel();
                return WakfuTranslator.getInstance().getString("partyList.MemberFormatedName", level);
            }
            if (fieldName.equals("isFollowed")) {
                return this.isFollowed();
            }
            if (fieldName.equals("positionIconUrl")) {
                return this.getPositionIconUrl();
            }
            if (fieldName.equals("hpDescription")) {
                final TextWidgetFormater sb = new TextWidgetFormater();
                sb.append(WakfuTranslator.getInstance().getString("HPShort")).append(" : ");
                sb.append(this.m_partyMember.getCurrentHp()).append("/").append(this.m_partyMember.getMaxHp());
                return sb.finishAndToString();
            }
            if (fieldName.equals("hpPercentage")) {
                return this.m_partyMember.getCurrentHp() / this.m_partyMember.getMaxHp();
            }
            if (fieldName.equals("stateStyle")) {
                return this.getStateStyle();
            }
            if (fieldName.equals("isLeader")) {
                return this.isLeader();
            }
            if (!fieldName.equals("owner")) {
                return null;
            }
            if (!this.m_partyMember.isCompanion()) {
                return null;
            }
            final PartyMemberDisplayer owner = this.getOwner();
            if (owner != null) {
                return WakfuTranslator.getInstance().getString("companionOwnedBy", owner.getName());
            }
            return this.m_partyMember.getClientId();
        }
        
        private int getLowestOwnerLevel() {
            final long clientId = this.m_partyMember.getClientId();
            short lowestLevel = 32767;
            final TLongObjectIterator<PartyMemberDisplayer> it = PartyDisplayer.this.m_partyMemberDisplayers.iterator();
            while (it.hasNext()) {
                it.advance();
                final PartyMemberDisplayer owner = it.value();
                if (owner.m_partyMember.isCompanion()) {
                    continue;
                }
                if (owner.getClientId() != clientId) {
                    continue;
                }
                final short ownerLevel = owner.getLevel();
                if (ownerLevel >= lowestLevel) {
                    continue;
                }
                lowestLevel = ownerLevel;
            }
            return lowestLevel;
        }
        
        private PartyMemberDisplayer getOwner() {
            final long clientId = this.m_partyMember.getClientId();
            PartyMemberDisplayer owner = null;
            final TLongObjectIterator<PartyMemberDisplayer> it = PartyDisplayer.this.m_partyMemberDisplayers.iterator();
            while (it.hasNext()) {
                it.advance();
                owner = it.value();
                if (owner.m_partyMember.isCompanion()) {
                    continue;
                }
                if (owner.m_partyMember.isHero()) {
                    continue;
                }
                if (owner.getClientId() == clientId) {
                    break;
                }
            }
            return owner;
        }
        
        private Object getCharacterImage() {
            if (this.m_partyMember.isCompanion()) {
                final CompanionModel companionModel = ((CompanionPartyMemberModel)this.m_partyMember).getCompanionModel();
                if (companionModel == null) {
                    return null;
                }
                final short breedId = companionModel.getBreedId();
                try {
                    return String.format(WakfuConfiguration.getInstance().getString("companionIconsPath"), breedId);
                }
                catch (PropertyException e) {
                    PartyDisplayer.m_logger.error((Object)"Exception", (Throwable)e);
                }
            }
            return CharacterImageGenerator.getInstance().getCharacterImage(this.m_partyMember.getCharacterId());
        }
        
        private String getName() {
            return this.m_partyMember.getName();
        }
        
        public short getLevel() {
            return this.m_partyMember.getLevel();
        }
        
        public boolean isLeader() {
            return PartyDisplayer.this.m_leaderMember == this;
        }
        
        private String getStateStyle() {
            if (this.m_partyMember.isDead()) {
                return "partyStateDead";
            }
            if (this.m_partyMember.isInFight()) {
                return "partyStateInFight";
            }
            return "";
        }
        
        public void fireStateChanged() {
            PropertiesProvider.getInstance().firePropertyValueChanged(this, "stateStyle");
        }
        
        public long getId() {
            return this.m_partyMember.getCharacterId();
        }
        
        public long getClientId() {
            return this.m_partyMember.getClientId();
        }
        
        public short getInstanceId() {
            return this.m_partyMember.getInstanceId();
        }
        
        @Override
        public String toString() {
            return this.getName();
        }
        
        public PartyMemberInterface getPartyMember() {
            return this.m_partyMember;
        }
        
        public boolean isFollowed() {
            return this.m_partyMember.isFollowed();
        }
        
        @Override
        public int compareTo(final Object o) {
            if (!(o instanceof PartyMemberDisplayer)) {
                return 0;
            }
            final PartyMemberDisplayer partyMemberDisplayer = (PartyMemberDisplayer)o;
            if (this.getId() == PartyDisplayer.this.m_leaderMember.getId()) {
                return -1;
            }
            if (partyMemberDisplayer.getId() == PartyDisplayer.this.m_leaderMember.getId()) {
                return 1;
            }
            return this.toString().compareTo(partyMemberDisplayer.toString());
        }
        
        public void onCharacterSpawn() {
            final CharacterInfo character = CharacterInfoManager.getInstance().getCharacter(this.m_partyMember.getCharacterId());
            if (character != null) {
                final FighterCharacteristic hp = character.getCharacteristic((CharacteristicType)FighterCharacteristicType.HP);
                hp.addListener(this.m_listener);
                this.m_listener.onCharacteristicUpdated(hp);
            }
            CharacterImageGenerator.getInstance().addListener(this.m_partyMember.getCharacterId(), this);
            CharacterImageGenerator.getInstance().deleteCharacterImage(this.m_partyMember.getCharacterId());
        }
        
        public void onLeaveParty() {
            final CharacterInfo character = CharacterInfoManager.getInstance().getCharacter(this.m_partyMember.getCharacterId());
            if (character != null) {
                character.getCharacteristic((CharacteristicType)FighterCharacteristicType.HP).removeListener(this.m_listener);
            }
            CharacterImageGenerator.getInstance().removeListener(this.m_partyMember.getCharacterId(), this);
        }
    }
}
