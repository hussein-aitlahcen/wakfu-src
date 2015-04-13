package com.ankamagames.wakfu.client.core.game.companion;

import com.ankamagames.wakfu.client.ui.component.*;
import gnu.trove.*;
import com.ankamagames.wakfu.common.game.group.member.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;
import com.ankamagames.wakfu.common.game.group.party.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.client.core.game.group.party.*;
import org.jetbrains.annotations.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.framework.reflect.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.xulor2.component.*;
import com.ankamagames.baseImpl.graphics.alea.animatedElement.*;
import com.ankamagames.xulor2.core.*;

public class HeroesDisplayer extends ImmutableFieldProvider
{
    public static final String HERO_SLOT_0 = "heroSlot0";
    public static final String HERO_SLOT_1 = "heroSlot1";
    public static final TLongObjectProcedure<CharacterView> REFRESH_HEROES_LIST_PROCEDURE;
    private HeroSlot m_heroSlot0;
    private HeroSlot m_heroSlot1;
    public static final HeroesDisplayer INSTANCE;
    final PartyModelListener m_partyModelListener;
    
    public HeroesDisplayer() {
        super();
        this.m_partyModelListener = new PartyModelListener() {
            @Override
            public void onMemberAdded(final PartyModelInterface party, final PartyMemberInterface member) {
                final long ownerId = WakfuGameEntity.getInstance().getLocalPlayer().getOwnerId();
                if ((member.isHero() || member.isCompanion()) && member.getClientId() == ownerId) {
                    HeroesDisplayer.this.addCharacter(this.getCharacterFromHeroList(member));
                }
            }
            
            @Override
            public void onMemberRemoved(final PartyModelInterface party, final PartyMemberInterface member) {
                final long ownerId = WakfuGameEntity.getInstance().getLocalPlayer().getOwnerId();
                if ((member.isHero() || member.isCompanion()) && member.getClientId() == ownerId) {
                    HeroesDisplayer.this.removeCharacter(this.getCharacterFromHeroList(member));
                }
            }
            
            @Override
            public void onLeaderChange(final PartyModelInterface party, final long previousLeader, final long newLeader) {
            }
            
            public CharacterView getCharacterFromHeroList(final PartyMemberInterface member) {
                final UICompanionsManagementFrame companionsManagementFrame = UICompanionsManagementFrame.INSTANCE;
                if (!member.isCompanion()) {
                    return companionsManagementFrame.getCharacterFromHeroList(member.getCharacterId());
                }
                return companionsManagementFrame.getCharacterFromHeroList(-member.getBreedId());
            }
        };
    }
    
    public void init() {
        this.m_heroSlot0 = new HeroSlot((byte)0);
        this.m_heroSlot1 = new HeroSlot((byte)1);
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        final PartyComportment partyComportment = localPlayer.getPartyComportment();
        partyComportment.addPartyComportmentListener(new PartyComportmentListener() {
            @Override
            public void onJoinParty(final PartyModel partyModel) {
                partyModel.addListener(HeroesDisplayer.this.m_partyModelListener);
            }
            
            @Override
            public void onLeaveParty(final PartyModel partyModel) {
                partyModel.removeListener(HeroesDisplayer.this.m_partyModelListener);
            }
        });
    }
    
    public void clear() {
        this.m_heroSlot0 = null;
        this.m_heroSlot1 = null;
    }
    
    @Override
    public String[] getFields() {
        return new String[0];
    }
    
    @Nullable
    @Override
    public Object getFieldValue(final String fieldName) {
        if (fieldName.equals("heroSlot0")) {
            return this.m_heroSlot0;
        }
        if (fieldName.equals("heroSlot1")) {
            return this.m_heroSlot1;
        }
        return null;
    }
    
    public void addCharacter(final CharacterView characterView) {
        if (this.m_heroSlot0.getHero() != null && this.m_heroSlot1.getHero() != null) {
            return;
        }
        HeroSlot slot;
        if (!characterView.isCompanion()) {
            slot = this.addhero(characterView);
        }
        else {
            slot = this.addCompanion(characterView);
        }
        this.refreshHeroesList(characterView);
        if (slot != null) {
            this.onHeroAddedOnSlot(slot);
        }
    }
    
    public void refreshHeroesList(final CharacterView characterView) {
        PropertiesProvider.getInstance().firePropertyValueChanged(characterView, "isInParty");
        PropertiesProvider.getInstance().setPropertyValue("heroesDisplayer", null);
        PropertiesProvider.getInstance().setPropertyValue("heroesDisplayer", this);
        UICompanionsManagementFrame.INSTANCE.forEachHeroView(HeroesDisplayer.REFRESH_HEROES_LIST_PROCEDURE);
    }
    
    private HeroSlot addCompanion(final CharacterView characterView) {
        if (this.m_heroSlot0.getHero() == null && (!this.m_heroSlot0.isUpgraded() || this.m_heroSlot1.getHero() != null || this.m_heroSlot1.isUpgraded())) {
            this.m_heroSlot0.setHero(characterView);
            return this.m_heroSlot0;
        }
        if (this.m_heroSlot1.getHero() == null) {
            this.m_heroSlot1.setHero(characterView);
            return this.m_heroSlot1;
        }
        return null;
    }
    
    private HeroSlot addhero(final CharacterView characterView) {
        if (this.m_heroSlot0.isUpgraded() && this.m_heroSlot0.getHero() == null) {
            this.m_heroSlot0.setHero(characterView);
            return this.m_heroSlot0;
        }
        if (this.m_heroSlot1.isUpgraded() && this.m_heroSlot1.getHero() == null) {
            this.m_heroSlot1.setHero(characterView);
            return this.m_heroSlot1;
        }
        return null;
    }
    
    public void onHeroAddedOnSlot(final HeroSlot slot) {
        final ElementMap map = Xulor.getInstance().getEnvironment().getElementMap("companionsManagementDialog");
        if (map == null) {
            return;
        }
        final AnimatedElementViewer viewer = (AnimatedElementViewer)map.getElement(slot.getAevId());
        final AnimatedElementWithDirection animatedElement = (AnimatedElementWithDirection)viewer.getAnimatedElement();
        if (!animatedElement.containsAnimation("AnimApparition")) {
            return;
        }
        viewer.setAnimName("AnimApparition");
        animatedElement.addAnimationEndedListener(new AnimationEndedListener() {
            @Override
            public void animationEnded(final AnimatedElement element) {
                viewer.setAnimName("AnimStatique");
                animatedElement.removeAnimationEndedListener(this);
            }
        });
    }
    
    public void removeCharacter(final CharacterView characterView) {
        if (characterView.equals(this.m_heroSlot0.getHero())) {
            this.m_heroSlot0.setHero(null);
        }
        else if (characterView.equals(this.m_heroSlot1.getHero())) {
            this.m_heroSlot1.setHero(null);
        }
        this.refreshHeroesList(characterView);
    }
    
    public boolean hasFreeUpgradedSlot() {
        return (this.m_heroSlot0.isUpgraded() && this.m_heroSlot0.getHero() == null) || (this.m_heroSlot1.isUpgraded() && this.m_heroSlot1.getHero() == null);
    }
    
    static {
        REFRESH_HEROES_LIST_PROCEDURE = new TLongObjectProcedure<CharacterView>() {
            @Override
            public boolean execute(final long a, final CharacterView b) {
                PropertiesProvider.getInstance().firePropertyValueChanged(b, "addRemovePartyEnabled");
                return true;
            }
        };
        INSTANCE = new HeroesDisplayer();
    }
}
