package com.ankamagames.wakfu.client.console.command;

import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.common.game.group.member.*;
import com.ankamagames.wakfu.client.chat.*;
import com.ankamagames.wakfu.common.game.group.party.*;
import java.util.*;
import gnu.trove.*;

public class AutoCompletionHelper
{
    private String m_stringToAutoComplete;
    private String m_lastFoundCompletion;
    private int m_currentIndex;
    private ArrayList<String> m_possibilities;
    private static final ArrayList<String> CHARACTER_INFO_NAMES;
    private static final AutoCompletionHelper m_instance;
    
    public AutoCompletionHelper() {
        super();
        this.m_lastFoundCompletion = null;
        this.m_possibilities = new ArrayList<String>();
    }
    
    public static final AutoCompletionHelper getInstance() {
        return AutoCompletionHelper.m_instance;
    }
    
    public void initialise(final String stringToAutoComplete) {
        this.m_stringToAutoComplete = stringToAutoComplete;
        this.m_lastFoundCompletion = null;
        this.initializeContactBank();
        this.generatePossibilities();
        this.m_currentIndex = 0;
    }
    
    private void initializeContactBank() {
        AutoCompletionHelper.CHARACTER_INFO_NAMES.clear();
        CharacterInfoManager.getInstance().forEachPlayerCharacter(new TObjectProcedure<CharacterInfo>() {
            @Override
            public boolean execute(final CharacterInfo player) {
                AutoCompletionHelper.CHARACTER_INFO_NAMES.add(player.getName());
                return true;
            }
        });
        final PartyModelInterface party = WakfuGameEntity.getInstance().getLocalPlayer().getPartyComportment().getParty();
        if (party != null) {
            final TLongObjectIterator<PartyMemberInterface> it = party.getMembers().iterator();
            while (it.hasNext()) {
                it.advance();
                final String s = it.value().getName();
                if (!AutoCompletionHelper.CHARACTER_INFO_NAMES.contains(s)) {
                    AutoCompletionHelper.CHARACTER_INFO_NAMES.add(s);
                }
            }
        }
        for (final WakfuUser wakfuUser : WakfuUserGroupManager.getInstance().getFriendGroup()) {
            if (wakfuUser.isOnline()) {
                final String s2 = wakfuUser.getCharacterName();
                if (AutoCompletionHelper.CHARACTER_INFO_NAMES.contains(s2)) {
                    continue;
                }
                AutoCompletionHelper.CHARACTER_INFO_NAMES.add(s2);
            }
        }
    }
    
    private void generatePossibilities() {
        this.m_possibilities.clear();
        for (final String characterInfoName : AutoCompletionHelper.CHARACTER_INFO_NAMES) {
            if (characterInfoName.toLowerCase().startsWith(this.m_stringToAutoComplete.toLowerCase()) && !this.m_possibilities.contains(characterInfoName)) {
                this.m_possibilities.add(characterInfoName);
            }
        }
        Collections.sort(this.m_possibilities);
        if (!this.m_possibilities.contains(this.m_stringToAutoComplete)) {
            this.m_possibilities.add(this.m_stringToAutoComplete);
        }
    }
    
    public String getNextPossibility() {
        if (this.m_possibilities.size() == 0) {
            this.m_lastFoundCompletion = "";
        }
        else {
            this.m_lastFoundCompletion = this.m_possibilities.get(this.m_currentIndex);
            this.m_currentIndex = (this.m_currentIndex + 1) % this.m_possibilities.size();
        }
        return this.m_lastFoundCompletion;
    }
    
    public String getStringToAutoComplete() {
        return this.m_stringToAutoComplete;
    }
    
    public void setStringToAutoComplete(final String stringToAutoComplete) {
        this.m_stringToAutoComplete = stringToAutoComplete;
    }
    
    public boolean isMatchingWithStringToAutoComplete(final String stringToMatch) {
        return stringToMatch != null && (stringToMatch.equalsIgnoreCase(this.m_stringToAutoComplete.toLowerCase()) || stringToMatch.equals(this.m_lastFoundCompletion));
    }
    
    static {
        CHARACTER_INFO_NAMES = new ArrayList<String>();
        m_instance = new AutoCompletionHelper();
    }
}
