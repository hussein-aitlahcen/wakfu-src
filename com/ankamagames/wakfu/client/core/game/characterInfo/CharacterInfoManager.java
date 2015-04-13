package com.ankamagames.wakfu.client.core.game.characterInfo;

import com.ankamagames.framework.ai.dataProvider.*;
import org.apache.log4j.*;
import com.ankamagames.wakfu.client.core.game.IsoWorldTarget.*;
import com.ankamagames.baseImpl.graphics.alea.mobile.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.*;
import com.ankamagames.wakfu.client.core.game.miniMap.*;
import com.ankamagames.wakfu.client.core.game.actor.*;
import java.util.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.group.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.monsters.*;
import com.ankamagames.wakfu.client.core.action.*;
import com.ankamagames.wakfu.client.chat.bubble.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.helpers.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;
import com.ankamagames.framework.kernel.core.common.collections.iterators.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.framework.ai.*;
import gnu.trove.*;

public class CharacterInfoManager implements EffectUserInformationProvider, TargetInformationProvider
{
    protected static final Logger m_logger;
    private static final CharacterInfoManager m_instance;
    private final TLongObjectHashMap<CharacterInfo> m_characters;
    private final List<CharacterInfo> m_playerCharacters;
    private final List<NonPlayerCharacter> m_aggressiveCharacters;
    private long m_lastDetectedAggression;
    private TLongArrayList m_lastAgressiveCharacterIds;
    private final List<CharacterInfoManagerListener> m_listeners;
    private final List<CharacterInfoManagerListener> m_listenersToRemove;
    
    public CharacterInfoManager() {
        super();
        this.m_characters = new TLongObjectHashMap<CharacterInfo>();
        this.m_playerCharacters = new ArrayList<CharacterInfo>();
        this.m_aggressiveCharacters = new ArrayList<NonPlayerCharacter>();
        this.m_lastDetectedAggression = 0L;
        this.m_lastAgressiveCharacterIds = null;
        this.m_listeners = new ArrayList<CharacterInfoManagerListener>();
        this.m_listenersToRemove = new ArrayList<CharacterInfoManagerListener>();
    }
    
    public static CharacterInfoManager getInstance() {
        return CharacterInfoManager.m_instance;
    }
    
    public boolean addAndSpawnCharacter(final CharacterInfo character) {
        if (this.addCharacter(character)) {
            return false;
        }
        this.spawnCharacter(character);
        return true;
    }
    
    public void spawnCharacter(final CharacterInfo character) {
        final CharacterActor characterActor = character.getActor();
        WakfuGameEntity.checkMob(characterActor);
        MobileManager.getInstance().addMobile(characterActor);
        InteractiveIsoWorldTargetManager.getInstance().addInteractiveIsoWorldTarget(character.getId(), characterActor);
        if (character instanceof NonPlayerCharacter && ((NonPlayerCharacter)character).isAgressive()) {
            synchronized (this.m_aggressiveCharacters) {
                this.m_aggressiveCharacters.add((NonPlayerCharacter)character);
            }
        }
        if (character instanceof PlayerCharacter) {
            synchronized (this.m_playerCharacters) {
                this.m_playerCharacters.add(character);
            }
        }
        characterActor.addPositionListener(MapManager.getInstance());
        characterActor.addPositionListener(WorldPositionMarkerManager.getInstance());
        WorldPositionMarkerManager.getInstance().onCharacterSpawn(characterActor);
        if (character.isLocalPlayer()) {
            MapManagerHelper.addPoint(character.getId(), 0, character.getWorldCellX(), character.getWorldCellY(), character.getWorldCellAltitude(), character.getInstanceId(), character, MapManager.getInstance().getDefaultMapPoint(), WakfuConfiguration.getInstance().getParticlePath(800237), character.getName(), WakfuClientConstants.MINI_MAP_POINT_COLOR_COMPASS_DEFAULT, MapManager.getInstance());
        }
        this.notifyAddToListeners(character);
    }
    
    public boolean addCharacter(final CharacterInfo character) {
        if (character == null) {
            return true;
        }
        synchronized (this.m_characters) {
            if (this.m_characters.containsKey(character.getId())) {
                CharacterInfoManager.m_logger.error((Object)("Ajout de ce CharacterInfo impossible : il existe d\u00e9j\u00e0 : " + character.getId() + " - " + character + " - " + this.m_characters.get(character.getId())));
                return true;
            }
            this.m_characters.put(character.getId(), character);
        }
        return false;
    }
    
    private void notifyAddToListeners(final CharacterInfo character) {
        for (final CharacterInfoManagerListener listener : this.m_listeners) {
            try {
                listener.onCharacterAdded(character);
            }
            catch (Exception e) {
                CharacterInfoManager.m_logger.error((Object)"Exception levee", (Throwable)e);
            }
        }
        this.m_listeners.removeAll(this.m_listenersToRemove);
        this.m_listenersToRemove.clear();
    }
    
    public void removeCharacter(final CharacterInfo character) {
        if (character == null) {
            return;
        }
        if (character.getCurrentExternalFightInfo() != null) {
            character.getCurrentExternalFightInfo().removeFighter(character);
        }
        this.removeCharacter(character.getId());
        if (character instanceof NonPlayerCharacter) {
            final NonPlayerCharacter npc = (NonPlayerCharacter)character;
            NPCGroupInformationManager.getInstance().onNonPlayerCharacterRemoved(npc, false);
            MonsterTimedPropertyManager.getInstance().removeEntry(character.getId());
            final MonsterBehaviourAction currentBehaviourAction = npc.getCurrentBehaviourAction();
            if (currentBehaviourAction != null) {
                currentBehaviourAction.forceActionEnd();
            }
        }
        character.release();
    }
    
    public boolean forEachCharacter(final TLongObjectProcedure<CharacterInfo> proc) {
        final boolean res;
        synchronized (this.m_characters) {
            res = this.m_characters.forEachEntry(proc);
        }
        return res;
    }
    
    public boolean forEachPlayerCharacter(final TObjectProcedure<CharacterInfo> proc) {
        boolean res = true;
        synchronized (this.m_playerCharacters) {
            for (int i = 0, size = this.m_playerCharacters.size(); i < size; ++i) {
                res = proc.execute(this.m_playerCharacters.get(i));
                if (!res) {
                    break;
                }
            }
        }
        return res;
    }
    
    public int getNumPlayerCharacters() {
        synchronized (this.m_playerCharacters) {
            return this.m_playerCharacters.size();
        }
    }
    
    public CharacterInfo getPlayerCharacterByName(final String characterName) {
        if (characterName == null) {
            return null;
        }
        synchronized (this.m_playerCharacters) {
            for (int i = this.m_playerCharacters.size() - 1; i >= 0; --i) {
                final CharacterInfo characterInfo = this.m_playerCharacters.get(i);
                if (characterInfo != null && characterName.equalsIgnoreCase(characterInfo.getName())) {
                    return characterInfo;
                }
            }
        }
        return null;
    }
    
    public void removeCharacter(final long characterId) {
        final CharacterInfo character;
        synchronized (this.m_characters) {
            character = this.m_characters.remove(characterId);
        }
        if (character == null) {
            CharacterInfoManager.m_logger.error((Object)("Tentative de suppression d'un CharacterInfo " + characterId + " inexistant."));
            return;
        }
        if (character instanceof NonPlayerCharacter) {
            synchronized (this.m_aggressiveCharacters) {
                this.m_aggressiveCharacters.remove(character);
            }
        }
        synchronized (this.m_playerCharacters) {
            this.m_playerCharacters.remove(character);
        }
        if (character.hasActor()) {
            this.cleanActor(characterId, character);
        }
        WorldPositionMarkerManager.getInstance().onCharacterDespawn(character.getActor());
        character.cleanUp();
    }
    
    private void cleanActor(final long characterId, final CharacterInfo character) {
        if (character.isLocalPlayer()) {
            MobileManager.getInstance().detachMobile(character.getActor());
        }
        else {
            MobileManager.getInstance().removeMobile(characterId);
        }
        InteractiveIsoWorldTargetManager.getInstance().removeInteractiveIsoWorldTarget(character.getId());
        ChatBubbleManager.cleanBubble(character.getId());
        EmoteIconHelper.cleanSmiley(character.getId());
        MapManagerHelper.removePoint(0, characterId, MapManager.getInstance());
        character.getActor().removePositionListener(MapManager.getInstance());
        character.getActor().removePositionListener(WorldPositionMarkerManager.getInstance());
    }
    
    public CharacterInfo getCharacter(final long characterId) {
        final CharacterInfo characterInfo;
        synchronized (this.m_characters) {
            characterInfo = this.m_characters.get(characterId);
        }
        return characterInfo;
    }
    
    public CharacterInfo getCharacterByName(final String characterName) {
        if (characterName == null) {
            return null;
        }
        synchronized (this.m_characters) {
            final TLongObjectIterator<CharacterInfo> it = this.m_characters.iterator();
            while (it.hasNext()) {
                it.advance();
                final CharacterInfo characterInfo = it.value();
                if (characterInfo != null && characterName.equals(characterInfo.getName())) {
                    return characterInfo;
                }
            }
        }
        return null;
    }
    
    public void dumpStats() {
        int noneSpawned = 0;
        int worldOnlySpawned = 0;
        int myFightOnlySpawned = 0;
        int bothSpawned = 0;
        synchronized (this.m_characters) {
            final TLongObjectIterator<CharacterInfo> it = this.m_characters.iterator();
            while (it.hasNext()) {
                it.advance();
                final CharacterInfo character = it.value();
                if (character.isSpawnInWorld() && character.isSpawnInMyFight()) {
                    ++bothSpawned;
                }
                else if (!character.isSpawnInWorld() && character.isSpawnInMyFight()) {
                    ++myFightOnlySpawned;
                }
                else if (character.isSpawnInWorld() && !character.isSpawnInMyFight()) {
                    ++worldOnlySpawned;
                }
                else {
                    ++noneSpawned;
                }
            }
        }
    }
    
    public void removeAllCharacters() {
        final int len;
        synchronized (this.m_characters) {
            len = this.m_characters.size();
            final TLongObjectIterator<CharacterInfo> it = this.m_characters.iterator();
            while (it.hasNext()) {
                it.advance();
                it.value().release();
            }
            this.m_characters.clear();
        }
        synchronized (this.m_aggressiveCharacters) {
            this.m_aggressiveCharacters.clear();
        }
        synchronized (this.m_playerCharacters) {
            this.m_playerCharacters.clear();
        }
        NPCGroupInformationManager.getInstance().removeAll();
        MonsterTimedPropertyManager.getInstance().removeAll();
        CharacterInfoManager.m_logger.info((Object)("Nettoyage des CharacterInfo contenus dans le CharacterInfoManager (" + this.m_characters.size() + " restants sur " + len + ")"));
    }
    
    @Override
    public EffectUser getEffectUserFromId(final long effectId) {
        final CharacterInfo characterInfo;
        synchronized (this.m_characters) {
            characterInfo = this.m_characters.get(effectId);
        }
        return characterInfo;
    }
    
    @Override
    public long getNextFreeEffectUserId(final byte effectUserType) {
        return 0L;
    }
    
    @Override
    public Iterator<CharacterInfo> getAllPossibleTargets() {
        return new TroveLongHashMapValueIterator<CharacterInfo>(this.m_characters);
    }
    
    @Override
    public List<EffectUser> getPossibleTargetsAtPosition(final Point3 pos) {
        return this.getPossibleTargetsAtPosition(pos.getX(), pos.getY(), pos.getZ());
    }
    
    @Override
    public List<EffectUser> getPossibleTargetsAtPosition(final int x, final int y, final int z) {
        final ArrayList<EffectUser> list = new ArrayList<EffectUser>();
        synchronized (this.m_characters) {
            final TLongObjectIterator<CharacterInfo> it = this.m_characters.iterator();
            while (it.hasNext()) {
                it.advance();
                if (DistanceUtils.getIntersectionDistance(it.value(), x, y) == 0) {
                    list.add(it.value());
                }
            }
        }
        return list;
    }
    
    public void addListener(final CharacterInfoManagerListener listener) {
        if (!this.m_listeners.contains(listener)) {
            this.m_listeners.add(listener);
        }
    }
    
    public void removeListenerAfterExecution(final CharacterInfoManagerListener listener) {
        if (listener != null) {
            this.m_listenersToRemove.add(listener);
        }
    }
    
    public void removeListener(final CharacterInfoManagerListener listener) {
        this.m_listeners.remove(listener);
    }
    
    static {
        m_logger = Logger.getLogger((Class)CharacterInfoManager.class);
        m_instance = new CharacterInfoManager();
    }
}
