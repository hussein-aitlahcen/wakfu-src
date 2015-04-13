package com.ankamagames.wakfu.client.moderationNew.panel;

import com.ankamagames.wakfu.client.ui.component.*;
import com.ankamagames.framework.text.*;
import com.ankamagames.framework.graphics.image.*;
import com.ankamagames.wakfu.client.core.*;
import org.jetbrains.annotations.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.framework.reflect.*;
import com.ankamagames.wakfu.common.datas.Breed.*;

public class ModeratedPlayerView extends ImmutableFieldProvider
{
    public static final String ONLINE = "online";
    public static final String NAME = "name";
    public static final String CLIENT_ID = "accountId";
    public static final String CHARACTER_ID = "characterId";
    public static final String NAME_WITHOUT_TEXT = "nameWithoutText";
    public static final String GAMESERVER = "gameServer";
    public static final String POSITION = "position";
    public static final String INSTANCE = "instance";
    public static final String ANKAMA_NAME = "ankamaName";
    public static final String LEVEL = "level";
    public static final String RACE = "race";
    public static final String GUILD = "guild";
    public static final String RESPAWN_COORDS = "respawnCoords";
    public static final String RESPAWN_GAMESERVER = "respawnGameServer";
    public static final String[] FIELDS;
    private boolean m_online;
    private String m_name;
    private String m_ankamaName;
    private long m_clientId;
    private long m_characterId;
    private short m_level;
    private String m_race;
    private String m_guild;
    private int m_positionX;
    private int m_positionY;
    private int m_positionZ;
    private short m_instanceId;
    private String m_gameServer;
    private String m_respawnCoords;
    private String m_respawnGameServer;
    private long m_guildId;
    
    public ModeratedPlayerView(final String name) {
        super();
        this.m_online = false;
        this.m_name = ((name != null) ? name : "null");
        this.m_ankamaName = "null";
        this.m_characterId = 0L;
        this.m_clientId = 0L;
        this.m_level = 0;
        this.m_guild = "null";
        this.m_race = "null";
        this.m_positionX = 0;
        this.m_positionY = 0;
        this.m_positionZ = 0;
        this.m_instanceId = 0;
        this.m_gameServer = "null";
        this.m_respawnCoords = "0, 0, 0 @0";
        this.m_respawnGameServer = "null";
    }
    
    @Override
    public String[] getFields() {
        return ModeratedPlayerView.FIELDS;
    }
    
    @Nullable
    @Override
    public Object getFieldValue(final String fieldName) {
        if ("online".equals(fieldName)) {
            final String online = this.m_online ? "Online" : "Offline";
            final TextWidgetFormater twf = new TextWidgetFormater();
            twf.openText().addColor(this.m_online ? Color.getRGBAFromHex("00ac04") : Color.RED).append(online).closeText();
            return twf.finishAndToString();
        }
        if ("name".equals(fieldName)) {
            final TextWidgetFormater twf2 = new TextWidgetFormater();
            twf2.openText().b().append(WakfuTranslator.getInstance().getString("moderationPanel.pseudo"))._b().append(" : " + this.m_name).closeText();
            return twf2.finishAndToString();
        }
        if ("nameWithoutText".equals(fieldName)) {
            return this.m_name;
        }
        if ("accountId".equals(fieldName)) {
            final TextWidgetFormater twf2 = new TextWidgetFormater();
            twf2.openText().b().append(WakfuTranslator.getInstance().getString("moderationPanel.clientId"))._b().append(" : " + this.m_clientId).closeText();
            return twf2.finishAndToString();
        }
        if ("characterId".equals(fieldName)) {
            final TextWidgetFormater twf2 = new TextWidgetFormater();
            twf2.openText().b().append(WakfuTranslator.getInstance().getString("moderationPanel.characterId"))._b().append(" : " + this.m_characterId).closeText();
            return twf2.finishAndToString();
        }
        if ("ankamaName".equals(fieldName)) {
            final TextWidgetFormater twf2 = new TextWidgetFormater();
            twf2.openText().b().append(WakfuTranslator.getInstance().getString("moderationPanel.ankamaPseudo"))._b().append(" : " + this.m_ankamaName).closeText();
            return twf2.finishAndToString();
        }
        if ("race".equals(fieldName)) {
            final TextWidgetFormater twf2 = new TextWidgetFormater();
            twf2.openText().b().append(WakfuTranslator.getInstance().getString("moderationPanel.race"))._b().append(" : " + this.m_race).closeText();
            return twf2.finishAndToString();
        }
        if ("level".equals(fieldName)) {
            final TextWidgetFormater twf2 = new TextWidgetFormater();
            twf2.openText().b().append(WakfuTranslator.getInstance().getString("moderationPanel.level"))._b().append(" : " + this.m_level).closeText();
            return twf2.finishAndToString();
        }
        if ("guild".equals(fieldName)) {
            final TextWidgetFormater twf2 = new TextWidgetFormater();
            twf2.openText().b().append(WakfuTranslator.getInstance().getString("moderationPanel.guild"))._b().append(" : " + this.m_guild).closeText();
            return twf2.finishAndToString();
        }
        if ("position".equals(fieldName)) {
            final TextWidgetFormater twf2 = new TextWidgetFormater();
            twf2.openText().b().append(WakfuTranslator.getInstance().getString("moderationPanel.currentPosition"))._b().append(" : " + this.m_positionX + ", " + this.m_positionY + ", " + this.m_positionZ).closeText();
            return twf2.finishAndToString();
        }
        if ("instance".equals(fieldName)) {
            final TextWidgetFormater twf2 = new TextWidgetFormater();
            twf2.openText().b().append(WakfuTranslator.getInstance().getString("moderationPanel.currentInstance"))._b().append(" : " + this.m_instanceId).closeText();
            return twf2.finishAndToString();
        }
        if ("gameServer".equals(fieldName)) {
            final TextWidgetFormater twf2 = new TextWidgetFormater();
            twf2.openText().b().append(WakfuTranslator.getInstance().getString("moderationPanel.currentGameServer"))._b().append(" : " + this.m_gameServer).closeText();
            return twf2.finishAndToString();
        }
        if ("respawnCoords".equals(fieldName)) {
            final TextWidgetFormater twf2 = new TextWidgetFormater();
            twf2.openText().b().append(WakfuTranslator.getInstance().getString("moderationPanel.respawnCoords"))._b().append(" : " + this.m_respawnCoords).closeText();
            return twf2.finishAndToString();
        }
        if ("respawnGameServer".equals(fieldName)) {
            final TextWidgetFormater twf2 = new TextWidgetFormater();
            twf2.openText().b().append(WakfuTranslator.getInstance().getString("moderationPanel.currentGameServer"))._b().append(" : " + this.m_respawnGameServer).closeText();
            return twf2.finishAndToString();
        }
        return null;
    }
    
    public long getGuildId() {
        return this.m_guildId;
    }
    
    public String getName() {
        return this.m_name;
    }
    
    public long getCharacterId() {
        return this.m_characterId;
    }
    
    public long getAccountId() {
        return this.m_clientId;
    }
    
    public void setGuild(final String guild) {
        this.m_guild = guild;
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "guild");
    }
    
    public void setAll(final byte online, final String name, final long characterId, final String ankamaName, final long accountId, final short raceId, final short level, final long guildId, final int positionX, final int positionY, final int positionZ, final short instanceId, final String gameServer, final int respawnX, final int respawnY, final int respawnZ, final short respawnInstance, final String respawnGameServer) {
        this.m_online = (online > 0);
        this.m_name = name;
        this.m_characterId = characterId;
        this.m_ankamaName = ankamaName;
        this.m_clientId = accountId;
        this.m_race = AvatarBreed.getBreedFromId(raceId).toString();
        this.m_level = level;
        this.m_guildId = guildId;
        this.m_positionX = positionX;
        this.m_positionY = positionY;
        this.m_positionZ = positionZ;
        this.m_instanceId = instanceId;
        this.m_gameServer = gameServer;
        this.m_respawnCoords = respawnX + ", " + respawnY + ", " + respawnZ + " @" + respawnInstance;
        this.m_respawnGameServer = respawnGameServer;
        PropertiesProvider.getInstance().firePropertyValueChanged(this, ModeratedPlayerView.FIELDS);
    }
    
    static {
        FIELDS = new String[] { "online", "name", "accountId", "characterId", "nameWithoutText", "gameServer", "position", "instance", "ankamaName", "level", "race", "guild", "respawnCoords", "respawnGameServer" };
    }
}
