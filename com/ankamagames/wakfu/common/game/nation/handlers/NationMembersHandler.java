package com.ankamagames.wakfu.common.game.nation.handlers;

import org.apache.log4j.*;
import java.util.*;
import org.jetbrains.annotations.*;
import com.ankamagames.wakfu.common.game.nation.government.*;
import com.ankamagames.wakfu.common.game.nation.*;
import gnu.trove.*;
import com.ankamagames.wakfu.common.game.nation.event.*;

public abstract class NationMembersHandler extends NationHandler<NationMembersEventHandler>
{
    private static final Logger m_logger;
    private final TLongObjectHashMap<Citizen> m_members;
    private final List<NationMembersEventHandler> m_membersEventHandlers;
    
    protected NationMembersHandler(final Nation nation) {
        super(nation);
        this.m_members = new TLongObjectHashMap<Citizen>();
        this.m_membersEventHandlers = new ArrayList<NationMembersEventHandler>();
    }
    
    void dispatchMemberAddedEvent(final Citizen character) {
        for (int i = this.m_membersEventHandlers.size() - 1; i >= 0; --i) {
            this.m_membersEventHandlers.get(i).onMemberAdded(character);
        }
    }
    
    void dispatchMemberRemovedEvent(final Citizen character) {
        for (int i = this.m_membersEventHandlers.size() - 1; i >= 0; --i) {
            this.m_membersEventHandlers.get(i).onMemberRemoved(this.getNation(), character);
        }
    }
    
    @Override
    public void finishInitialization() {
    }
    
    @Nullable
    public Citizen getCitizen(final long citizenId) {
        return this.m_members.get(citizenId);
    }
    
    @Override
    public void registerEventHandler(final NationMembersEventHandler handler) {
        if (handler == null) {
            return;
        }
        this.m_membersEventHandlers.add(handler);
    }
    
    @Override
    public void unregisterEventHandler(final NationMembersEventHandler handler) {
        this.m_membersEventHandlers.remove(handler);
    }
    
    public boolean hasCitizen(final Citizen citizen) {
        final Citizen citizenFound = this.m_members.get(citizen.getId());
        assert citizenFound == citizen : "On a trouv\u00e9 2 citoyens diff\u00e9rents mais avec le m\u00eame id dans la nation " + this.getNation() + " : " + citizen + " - " + citizenFound;
        return citizenFound != null;
    }
    
    public abstract void requestAddCitizen(@NotNull final Citizen p0);
    
    public abstract void requestAddCitizen(final long p0);
    
    public void requestAddNationJob(final long characterId, final NationJob job) {
        throw new UnsupportedOperationException();
    }
    
    public void requestRemoveNationJob(final long characterId, final NationJob job) {
        throw new UnsupportedOperationException();
    }
    
    public void registerCitizen(@NotNull final Citizen character) {
        final CitizenComportment comportment = character.getCitizenComportment();
        if (comportment.getNation() != this.getNation()) {
            NationMembersHandler.m_logger.error((Object)("[NATION] On essaye d'enregister le character " + character + " \u00e0 la nation " + this.getNation() + " mais il est semble etre dans la nation " + comportment.getNation()));
            return;
        }
        if (this.m_members.containsKey(character.getId())) {
            NationMembersHandler.m_logger.error((Object)("[NATION] On essaye d'enregistrer le character " + character + " \u00e0 la nation " + this.getNation().getNationId() + " mais il est d\u00e9j\u00e0 dedans"));
            return;
        }
        this.m_members.put(character.getId(), character);
        this.dispatchMemberAddedEvent(character);
    }
    
    public boolean addCitizen(@NotNull final Citizen character) {
        if (this.m_members.containsKey(character.getId())) {
            NationMembersHandler.m_logger.error((Object)("[NATION] On essaye d'ajouter le character " + character + " \u00e0 la nation " + this.getNation().getNationId() + " mais il est d\u00e9j\u00e0 dedans"));
        }
        final CitizenComportment comportment = character.getCitizenComportment();
        final Nation oldNation = comportment.getNation();
        if (oldNation != this.getNation()) {
            final boolean notRemoved = !oldNation.unregisterCitizen(character);
            if (notRemoved) {
                NationMembersHandler.m_logger.error((Object)("[NATION] Impossible d'ajouter le joueur " + character + " \u00e0 la nation " + this.getNation() + " car on ne peut pas l'enlever \u00e0 sa nation courante (" + oldNation + ")"));
                return false;
            }
        }
        this.m_members.put(character.getId(), character);
        comportment.resetToNation(this.getNation());
        this.dispatchMemberAddedEvent(character);
        return true;
    }
    
    public boolean unregisterCitizen(@NotNull final Citizen character) {
        final Citizen citizenRemoved = this.m_members.remove(character.getId());
        if (this.getNation() != Nation.VOID_NATION) {
            if (citizenRemoved == null) {
                NationMembersHandler.m_logger.error((Object)("[NATION] On essaye de supprimer le character " + character + " de la nation " + this.getNation() + " mais il n'est pas dans la liste des membres"));
                return false;
            }
            if (citizenRemoved != character) {
                NationMembersHandler.m_logger.error((Object)("[NATION] On essaye de supprimer le character + " + character + " d'id " + character.getId() + " de la nation " + this.getNation() + " mais l'id donne le perso " + citizenRemoved + ". Conflit d'ID ?"));
                return false;
            }
        }
        this.dispatchMemberRemovedEvent(character);
        return true;
    }
    
    public void executeForEachCitizen(final TObjectProcedure<Citizen> procedure) {
        this.m_members.forEachValue(procedure);
    }
    
    public void onDisconnection(final long characterId) {
        final Citizen citizen = this.m_members.remove(characterId);
        if (citizen == null) {
            NationMembersHandler.m_logger.error((Object)("[NATION] On essaye de retirer le character " + characterId + " \u00e0 la nation " + this.getNation() + " suite \u00e0 une d\u00e9co mais il n'en fait pas partie"));
        }
    }
    
    public int getCitizenCount() {
        return this.m_members.size();
    }
    
    static {
        m_logger = Logger.getLogger((Class)NationMembersHandler.class);
    }
}
