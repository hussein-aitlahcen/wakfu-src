package com.ankamagames.wakfu.client.core.game.characterInfo;

import org.apache.log4j.*;
import gnu.trove.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.characterChoiceSlot.*;
import com.ankamagames.wakfu.common.game.hero.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;
import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.xulor2.property.*;
import java.util.*;
import com.ankamagames.wakfu.client.*;
import com.ankamagames.baseImpl.graphics.core.*;
import com.ankamagames.wakfu.client.core.preferences.*;
import com.ankamagames.wakfu.client.core.*;
import java.io.*;
import com.ankamagames.framework.fileFormat.io.*;

public class LocalCharacterInfosManager
{
    protected static final Logger m_logger;
    private static final LocalCharacterInfosManager m_instance;
    private final ArrayList<CharacterInfo> m_characterInfos;
    private byte m_additionalCharacterSlots;
    private final TObjectIntHashMap<CharacterInfo> m_nationInformations;
    private CharacterInfo m_selectedCharacterInfo;
    private final Comparator<CharacterInfo> m_comparator;
    private CharacterInfo m_lastCreatedCharacter;
    
    public LocalCharacterInfosManager() {
        super();
        this.m_characterInfos = new ArrayList<CharacterInfo>();
        this.m_additionalCharacterSlots = 0;
        this.m_nationInformations = new TObjectIntHashMap<CharacterInfo>();
        this.m_comparator = new CharacterComparator();
    }
    
    public static LocalCharacterInfosManager getInstance() {
        return LocalCharacterInfosManager.m_instance;
    }
    
    public void setAdditionalCharacterSlots(final byte additionalCharacterSlots) {
        this.m_additionalCharacterSlots = additionalCharacterSlots;
        CharacterChoiceSlots.INSTANCE.updateCharacters(this.m_additionalCharacterSlots, this.m_characterInfos);
    }
    
    public void setSerializedCharacters(final ArrayList<byte[]> serializedCharacters) {
        this.m_characterInfos.clear();
        for (int i = 0; i < serializedCharacters.size(); ++i) {
            final PlayerCharacter playerCharacter = this.addSerializedCharacter(serializedCharacters.get(i));
            HeroesManager.INSTANCE.addHero(WakfuGameEntity.getInstance().getLocalAccount().getAccountId(), playerCharacter);
            CharacterInfoManager.getInstance().addCharacter(playerCharacter);
        }
        Collections.sort(this.m_characterInfos, this.m_comparator);
        this.updateLocalCharactersListProperty(true);
        UICharacterChoiceFrame.getInstance().setCharacterListLoaded(true);
    }
    
    private PlayerCharacter addSerializedCharacter(final byte[] serializedCharacter) {
        final LocalPlayerCharacter characterInfo = new LocalPlayerCharacter();
        this.m_characterInfos.add(characterInfo);
        try {
            characterInfo.setCharacterChoiceState(true);
            characterInfo.setAddActorToManager(false);
            characterInfo.fromBuild(serializedCharacter);
        }
        catch (Exception e) {
            characterInfo.getSerializer().extractPartFromBuild(characterInfo.getSerializer().getIdPart(), serializedCharacter);
            LocalCharacterInfosManager.m_logger.error((Object)"Erreur durant la d\u00e9Serialization d'un personnage : ", (Throwable)e);
        }
        return characterInfo;
    }
    
    public void addNationInformation(final CharacterInfo character, final int nationId) throws IllegalArgumentException {
        if (!this.m_characterInfos.contains(character)) {
            throw new IllegalArgumentException("Impossible de rajouter des informations de nation sur un character non-existant : " + character);
        }
        this.m_nationInformations.put(character, nationId);
    }
    
    public int getNationInformation(final CharacterInfo characterInfo) {
        return this.m_nationInformations.get(characterInfo);
    }
    
    public void removeCharacterInfo(final long characterId) {
        for (int i = this.m_characterInfos.size() - 1; i >= 0; --i) {
            final CharacterInfo characterInfo = this.m_characterInfos.get(i);
            if (characterInfo.getId() == characterId) {
                final CharacterInfo character = this.m_characterInfos.remove(i);
                this.m_nationInformations.remove(character);
                this.updateLocalCharactersListProperty(true);
                if (this.m_selectedCharacterInfo != null && this.m_selectedCharacterInfo.getId() == characterId) {
                    final int index = (i == 0) ? 0 : (i - 1);
                    this.selectCharacterInfo(this.m_characterInfos.isEmpty() ? null : this.m_characterInfos.get(index));
                }
                CharacterInfoManager.getInstance().removeCharacter(character);
                return;
            }
        }
    }
    
    public final void removeAllCharacterInfos() {
        this.m_characterInfos.clear();
        this.m_nationInformations.clear();
        this.selectCharacterInfo(null);
        this.updateLocalCharactersListProperty(false);
    }
    
    public final void selectCharacterInfo(final CharacterInfo characterInfo) {
        this.m_selectedCharacterInfo = (this.m_characterInfos.contains(characterInfo) ? characterInfo : null);
        PropertiesProvider.getInstance().setPropertyValue("characterChoice.selectedCharacter", this.m_selectedCharacterInfo);
    }
    
    public final CharacterInfo getSelectedCharacterInfo() {
        return this.m_selectedCharacterInfo;
    }
    
    public final int getNumbersOfCharacterInfos() {
        return this.m_characterInfos.size();
    }
    
    public final CharacterInfo getLastCreatedCharacterInfo() {
        if (this.m_lastCreatedCharacter == null) {
            return null;
        }
        for (final CharacterInfo characterInfo : this.m_characterInfos) {
            if (characterInfo.getName().equals(this.m_lastCreatedCharacter.getName())) {
                return characterInfo;
            }
        }
        return null;
    }
    
    public final CharacterInfo getFirstCharacterInfo() {
        if (this.m_characterInfos == null || this.m_characterInfos.size() == 0) {
            return null;
        }
        return this.m_characterInfos.get(0);
    }
    
    public ArrayList<CharacterInfo> getCharacterInfos() {
        return this.m_characterInfos;
    }
    
    private void updateLocalCharactersListProperty(final boolean updatePreferenceStore) {
        if (updatePreferenceStore) {
            final WakfuKeyPreferenceStoreEnum onServerPattern = WakfuKeyPreferenceStoreEnum.CHARACTERS_NUMBER_ON_SERVER_PATTERN;
            final WakfuGamePreferences wakfuGamePreferences = WakfuClientInstance.getInstance().getGamePreferences();
            String currentList = wakfuGamePreferences.getStringValue(onServerPattern);
            final String regex = "(:[0-9]+;)";
            final String newEntry = this.m_characterInfos.size() + ";";
            if (currentList == null) {
                currentList = newEntry;
            }
            else if (currentList.matches("(:[0-9]+;)")) {
                currentList = currentList.replaceAll("(:[0-9]+;)", newEntry);
            }
            else {
                currentList += newEntry;
            }
            wakfuGamePreferences.setValue(onServerPattern, currentList);
        }
        CharacterChoiceSlots.INSTANCE.updateCharacters(this.m_additionalCharacterSlots, this.m_characterInfos);
    }
    
    public void checkCharactersConfigDirectory() {
        final ArrayList<String> names = new ArrayList<String>();
        for (final CharacterInfo characterInfo : this.m_characterInfos) {
            names.add(characterInfo.getName());
        }
        final File dir = new File(WakfuClientConfigurationManager.getInstance().getWorldDirectory());
        if (!dir.exists()) {
            return;
        }
        for (final File file : dir.listFiles(new FileFilter() {
            @Override
            public boolean accept(final File pathname) {
                return pathname.isDirectory();
            }
        })) {
            if (!names.contains(file.getName())) {
                FileHelper.deleteDirectory(file);
            }
        }
    }
    
    public void setLastCreatedCharacter(final CharacterInfo ci) {
        this.m_lastCreatedCharacter = ci;
    }
    
    public CharacterInfo getCharacterInfoById(final long characterId) {
        for (final CharacterInfo characterInfo : this.m_characterInfos) {
            if (characterInfo.getId() == characterId) {
                return characterInfo;
            }
        }
        return null;
    }
    
    public CharacterInfo getCharacterInfoByName(final String lastCharacterName) {
        for (final CharacterInfo characterInfo : this.m_characterInfos) {
            if (characterInfo.getName().equals(lastCharacterName)) {
                return characterInfo;
            }
        }
        return null;
    }
    
    static {
        m_logger = Logger.getLogger((Class)LocalCharacterInfosManager.class);
        m_instance = new LocalCharacterInfosManager();
    }
    
    private static class CharacterComparator implements Comparator<CharacterInfo>
    {
        @Override
        public int compare(final CharacterInfo o1, final CharacterInfo o2) {
            if (o1.getId() - o2.getId() > 0L) {
                return 1;
            }
            if (o1.getId() - o2.getId() < 0L) {
                return -1;
            }
            return 0;
        }
    }
}
