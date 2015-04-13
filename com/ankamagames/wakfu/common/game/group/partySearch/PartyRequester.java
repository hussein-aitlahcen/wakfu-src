package com.ankamagames.wakfu.common.game.group.partySearch;

import org.apache.log4j.*;
import java.nio.*;
import com.ankamagames.framework.kernel.utils.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.time.calendar.*;
import com.ankamagames.wakfu.common.constants.*;
import com.ankamagames.wakfu.common.datas.Breed.*;
import java.util.*;
import com.ankamagames.framework.kernel.core.common.collections.*;

public final class PartyRequester extends AbstractPartyRequester
{
    private static final Logger m_logger;
    private List<PartyPlayerDefinition> m_definitions;
    private short m_minLevel;
    
    public PartyRequester(final ByteBuffer bb) {
        super(bb);
        this.m_definitions = new ArrayList<PartyPlayerDefinition>();
        final int size = bb.getInt();
        PartyPlayerDefinition leaderDef = null;
        for (int i = 0; i < size; ++i) {
            final PartyPlayerDefinition def = new PartyPlayerDefinition(bb);
            this.m_definitions.add(def);
            if (def.getId() == this.getLeaderId()) {
                leaderDef = def;
            }
        }
        if (leaderDef != null) {
            final byte[] leaderName = new byte[bb.getInt()];
            bb.get(leaderName);
            leaderDef.setName(StringUtils.fromUTF8(leaderName));
        }
    }
    
    public PartyRequester(final long id, final PartyMood mood, final String description, final GameDateConst registrationDate, final List<PartyOccupation> occupations, final List<PartyPlayerDefinition> definitions) {
        super(id, mood, description, registrationDate, occupations);
        this.m_definitions = new ArrayList<PartyPlayerDefinition>();
        this.m_definitions = new ArrayList<PartyPlayerDefinition>(definitions);
        this.computeMinLevel();
    }
    
    private void computeMinLevel() {
        this.m_minLevel = XpConstants.getPlayerCharacterLevelCap();
        for (final PartyPlayerDefinition def : this.m_definitions) {
            if (def.isCompanion()) {
                continue;
            }
            if (def.getLevel() >= this.m_minLevel) {
                continue;
            }
            this.m_minLevel = def.getLevel();
        }
    }
    
    public PartyRequester updateRequester(final PartyMood mood, final String description, final List<PartyOccupation> occupations, final List<PartyPlayerDefinition> definitions) {
        this.updateRequester(mood, description, occupations);
        this.m_definitions = new ArrayList<PartyPlayerDefinition>(definitions);
        this.computeMinLevel();
        return this;
    }
    
    @Override
    public boolean isValid(final SearchParameters params) {
        if (!super.isValid(params)) {
            return false;
        }
        for (final PartyPlayerDefinition definition : this.m_definitions) {
            if (definition.isCompanion()) {
                continue;
            }
            if (definition.getLevel() < params.getMinLevel()) {
                continue;
            }
            if (definition.getLevel() > params.getMaxLevel()) {
                continue;
            }
            if (params.getRole() != PartyRole.NONE && definition.getRole() != params.getRole()) {
                continue;
            }
            if (params.getBreed() != AvatarBreed.NONE && definition.getBreedId() != params.getBreed().getBreedId()) {
                continue;
            }
            return true;
        }
        return false;
    }
    
    public PartyPlayerDefinition getDefinition(final long id) {
        for (final PartyPlayerDefinition definition : this.m_definitions) {
            if (definition.getId() == id) {
                return definition;
            }
        }
        return null;
    }
    
    public List<PartyPlayerDefinition> getDefinitions() {
        return Collections.unmodifiableList((List<? extends PartyPlayerDefinition>)this.m_definitions);
    }
    
    public long getLeaderId() {
        return this.getId();
    }
    
    @Override
    protected ByteArray doSerialize(final boolean withOccupations) {
        final ByteArray bb = super.doSerialize(withOccupations);
        bb.putInt(this.m_definitions.size());
        String leaderName = "";
        for (final PartyPlayerDefinition definition : this.m_definitions) {
            bb.put(definition.serialize(true));
            if (definition.getId() == this.getLeaderId()) {
                leaderName = definition.getName();
            }
        }
        final byte[] leader = StringUtils.toUTF8(leaderName);
        bb.putInt(leader.length);
        bb.put(leader);
        return bb;
    }
    
    public void merge(final PartyPlayerDefinition member) {
        for (int i = 0, size = this.m_definitions.size(); i < size; ++i) {
            final PartyPlayerDefinition def = this.m_definitions.get(i);
            if (def.getId() == member.getId()) {
                this.m_definitions.set(i, member);
                return;
            }
        }
        this.m_definitions.add(member);
    }
    
    public void remove(final long characterId) {
        for (int i = 0, size = this.m_definitions.size(); i < size; ++i) {
            if (this.m_definitions.get(i).getId() == characterId) {
                this.m_definitions.remove(i);
                return;
            }
        }
    }
    
    public PartySearchFeedbackEnum isValidOccupationsForRegister() {
        this.computeMinLevel();
        if (this.m_occupations.isEmpty()) {
            PartyRequester.m_logger.error((Object)("[PartySearch] PartyRequester sans occupation " + this.getId()));
            return PartySearchFeedbackEnum.REMOVED_FOR_VOID_OCCUPATION;
        }
        for (int i = this.m_occupations.size() - 1; i >= 0; --i) {
            final PartyOccupation partyOccupation = this.m_occupations.get(i);
            final long id = partyOccupation.getId();
            if (PartyOccupationManager.INSTANCE.getPartyOccupation(id) == null) {
                PartyRequester.m_logger.error((Object)"[PartySearch] Tentative d'enregistrement d'une occupation inconnue du manager");
                return PartySearchFeedbackEnum.REMOVED_FOR_INVALID_OCCUPATION;
            }
            if ((partyOccupation.getOccupationType() == PartyOccupationType.DUNGEON || partyOccupation.getOccupationType() == PartyOccupationType.MONSTER) && ((PvePartyOccupation)partyOccupation).getLevel() > this.m_minLevel) {
                this.m_occupations.remove(partyOccupation);
            }
        }
        if (this.m_occupations.isEmpty()) {
            PartyRequester.m_logger.error((Object)("[PartySearch] PartyRequester sans occupation " + this.getId()));
            return PartySearchFeedbackEnum.REMOVED_FOR_VOID_OCCUPATION;
        }
        if (this.m_occupations.size() > 20) {
            PartyRequester.m_logger.error((Object)("[PartySearch] Le PartyRequester contient trop d'occupations " + this.getId()));
            return PartySearchFeedbackEnum.TOO_MUCH_OCCUPATIONS;
        }
        return PartySearchFeedbackEnum.NO_ERROR;
    }
    
    @Override
    public String toString() {
        return "PartyRequester{m_definitions=" + this.m_definitions + "m_minLevel=" + this.m_minLevel + "} " + super.toString();
    }
    
    public String getLeaderName() {
        return this.getDefinition(this.getLeaderId()).getName();
    }
    
    static {
        m_logger = Logger.getLogger((Class)PartyRequester.class);
    }
}
