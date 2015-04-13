package com.ankamagames.wakfu.client.ui.protocol.frame;

import com.ankamagames.framework.kernel.events.*;
import com.ankamagames.baseImpl.graphics.ui.event.*;
import com.ankamagames.framework.kernel.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.wakfu.common.game.hero.*;
import com.ankamagames.wakfu.common.game.nation.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.wakfu.client.core.game.hero.*;
import com.ankamagames.wakfu.client.ui.actions.*;
import java.util.*;
import com.ankamagames.wakfu.common.configuration.*;
import com.ankamagames.wakfu.client.core.game.companion.*;
import com.ankamagames.wakfu.common.game.companion.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.monsters.*;
import com.ankamagames.wakfu.common.datas.Breed.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;
import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.wakfu.common.game.fighter.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.characteristic.*;
import com.ankamagames.wakfu.client.core.game.item.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.xulor2.component.*;
import com.ankamagames.xulor2.util.*;
import com.ankamagames.xulor2.core.*;
import com.ankamagames.wakfu.client.ui.*;
import gnu.trove.*;

public abstract class UICompanionsEmbeddedFrame implements MessageFrame
{
    protected static final TIntObjectHashMap<CharacterView> m_companionViews;
    protected static final TLongObjectHashMap<CharacterView> m_characterViews;
    public static final ArrayList<UICompanionsEmbeddedFrame> m_loadedFrames;
    protected ArrayList<String> m_loadedDialogs;
    protected TIntObjectHashMap<ShortCharacterView> m_companionsList;
    private DialogUnloadListener m_dialogUnloadListener;
    
    public UICompanionsEmbeddedFrame() {
        super();
        this.m_loadedDialogs = new ArrayList<String>();
        this.m_companionsList = new TIntObjectHashMap<ShortCharacterView>();
    }
    
    @Override
    public void onFrameAdd(final FrameHandler frameHandler, final boolean isAboutToBeAdded) {
        if (isAboutToBeAdded) {
            return;
        }
        this.m_dialogUnloadListener = new DialogUnloadListener() {
            @Override
            public void dialogUnloaded(final String id) {
                if (id.startsWith(UICompanionsEmbeddedFrame.this.getBaseDialogId())) {
                    UICompanionsEmbeddedFrame.this.m_loadedDialogs.remove(id);
                    if (!id.equals(UICompanionsEmbeddedFrame.this.getBaseDialogId())) {
                        final int breedId = Integer.parseInt(id.substring(UICompanionsEmbeddedFrame.this.getBaseDialogId().length()));
                        UICompanionsEmbeddedFrame.this.setCompanionDndcEnabled(UICompanionsEmbeddedFrame.m_companionViews.get(breedId), true);
                    }
                    if (UICompanionsEmbeddedFrame.this.m_loadedDialogs.isEmpty()) {
                        WakfuGameEntity.getInstance().removeFrame(UICompanionsEmbeddedFrame.this);
                    }
                }
            }
        };
        Xulor.getInstance().addDialogUnloadListener(this.m_dialogUnloadListener);
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        final ArrayList<ShortCharacterView> companionsList = new ArrayList<ShortCharacterView>();
        final ArrayList<ShortCharacterView> realCompanionList = new ArrayList<ShortCharacterView>();
        for (final ShortCharacterView shortCharacterView : localPlayer.getCompanionViews()) {
            if (shortCharacterView != null) {
                if (!shortCharacterView.isPlayer()) {
                    final ShortCharacterView copy = shortCharacterView.getCopy();
                    this.m_companionsList.put(shortCharacterView.getBreedId(), copy);
                    companionsList.add(copy);
                }
            }
        }
        final TLongIterator it = HeroesManager.INSTANCE.getHeroesInParty(localPlayer.getOwnerId()).iterator();
        while (it.hasNext()) {
            final PlayerCharacter character = HeroesManager.INSTANCE.getHero(it.next());
            final PlayerCompanionViewShort shortView = new PlayerCompanionViewShort(character);
            final CharacterView view = HeroesCharacterViewManager.INSTANCE.getOrCreateCharacterView(shortView);
            UICompanionsEmbeddedFrame.m_characterViews.put(character.getId(), view);
            realCompanionList.add(shortView);
        }
        Collections.sort(companionsList, new Comparator<ShortCharacterView>() {
            @Override
            public int compare(final ShortCharacterView o1, final ShortCharacterView o2) {
                return o1.getBreedId() - o2.getBreedId();
            }
        });
        reflowViews();
        this.loadMainDialog(UICompanionsEmbeddedFrame.m_characterViews.get(localPlayer.getId()));
        PropertiesProvider.getInstance().setLocalPropertyValue("companionPartyList", companionsList, this.getBaseDialogId());
        recomputeRealCompanionList();
        PropertiesProvider.getInstance().setPropertyValue("companionPartyListFull", !UnlockedCompanionGroupLimitManager.INSTANCE.hasUnlockedCompanionGroupLimit(localPlayer.getClientId()) && !ClientHeroUtils.canInviteHeroOrCompanion((byte)5));
        Xulor.getInstance().putActionClass("wakfu.companionsEmbedded", CompanionsEmbeddedActions.class);
        UICompanionsEmbeddedFrame.m_loadedFrames.add(this);
    }
    
    public static void refreshAllCompanionsLists() {
        for (final UICompanionsEmbeddedFrame frame : UICompanionsEmbeddedFrame.m_loadedFrames) {
            PropertiesProvider.getInstance().setLocalPropertyValue("companionPartyList", frame.reflowCompanionsList(), frame.getBaseDialogId());
            frame.checkLoadedDialogs();
        }
        recomputeRealCompanionList();
        reflowViews();
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        PropertiesProvider.getInstance().setPropertyValue("companionPartyListFull", !UnlockedCompanionGroupLimitManager.INSTANCE.hasUnlockedCompanionGroupLimit(localPlayer.getClientId()) && !ClientHeroUtils.canInviteHeroOrCompanion((byte)5));
    }
    
    private void checkLoadedDialogs() {
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        for (final String id : this.m_loadedDialogs) {
            if (id.equals(this.getBaseDialogId())) {
                final CharacterView characterView = HeroesCharacterViewManager.INSTANCE.getOrCreateCharacterView(new PlayerCompanionViewShort(localPlayer));
                if (UICompanionsEmbeddedFrame.m_characterViews.get(characterView.getCharacterInfo().getId()) != null) {
                    continue;
                }
                PropertiesProvider.getInstance().setLocalPropertyValue("characterSheet", UICompanionsEmbeddedFrame.m_characterViews.get(localPlayer.getId()), id);
            }
        }
    }
    
    public static void addCharacterView(final CharacterView view) {
        UICompanionsEmbeddedFrame.m_characterViews.put(view.getCharacterInfo().getId(), view);
    }
    
    public static void removeCharacterView(final long characterId) {
        UICompanionsEmbeddedFrame.m_characterViews.remove(characterId);
    }
    
    public static void addCompanionView(final CharacterView view) {
        UICompanionsEmbeddedFrame.m_companionViews.put(view.getShortCharacterView().getBreedId(), view);
    }
    
    public static void removeCompanionView(final int breedId) {
        UICompanionsEmbeddedFrame.m_companionViews.remove(breedId);
    }
    
    public static boolean containsCompanionView(final int breedId) {
        return UICompanionsEmbeddedFrame.m_companionViews.containsKey(breedId);
    }
    
    public ArrayList<ShortCharacterView> reflowCompanionsList() {
        this.m_companionsList.clear();
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        final ArrayList<ShortCharacterView> companionsList = new ArrayList<ShortCharacterView>();
        for (final ShortCharacterView shortCharacterView : localPlayer.getCompanionViews()) {
            if (shortCharacterView != null) {
                final int breedId = shortCharacterView.getBreedId();
                final ShortCharacterView shortCharacterView2 = this.m_companionsList.get(breedId);
                if (shortCharacterView2 != null) {
                    companionsList.add(shortCharacterView2);
                }
                else {
                    final ShortCharacterView copy = shortCharacterView.getCopy();
                    this.m_companionsList.put(breedId, copy);
                    companionsList.add(copy);
                }
            }
        }
        Collections.sort(companionsList, new Comparator<ShortCharacterView>() {
            @Override
            public int compare(final ShortCharacterView o1, final ShortCharacterView o2) {
                return o1.getBreedId() - o2.getBreedId();
            }
        });
        return companionsList;
    }
    
    public static void recomputeRealCompanionList() {
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        final List<ShortCharacterView> companionsList = new ArrayList<ShortCharacterView>();
        for (final ShortCharacterView shortCharacterView : localPlayer.getCompanionViews()) {
            if (shortCharacterView != null) {
                if (!shortCharacterView.isPlayer() || !SystemConfiguration.INSTANCE.getBooleanValue(SystemConfigurationType.HEROES_ENABLED)) {
                    final ShortCharacterView copy = shortCharacterView.getCopy();
                    companionsList.add(copy);
                    final CompanionModel companion = CompanionManager.INSTANCE.getCompanion(localPlayer.getOwnerId(), shortCharacterView.getBreedId());
                    if (companion != null) {
                        final NonPlayerCharacter npc = createNonPlayerCharacterCompanion(companion);
                        final CharacterView characterView = new CharacteristicCompanionView(npc, new CompanionViewShort(companion));
                        addCompanionView(characterView);
                    }
                }
            }
        }
        Collections.sort(companionsList, new Comparator<ShortCharacterView>() {
            @Override
            public int compare(final ShortCharacterView o1, final ShortCharacterView o2) {
                return o1.getBreedId() - o2.getBreedId();
            }
        });
        PropertiesProvider.getInstance().setPropertyValue("companionParty", companionsList);
        PropertiesProvider.getInstance().setPropertyValue("companionPartyExists", !companionsList.isEmpty());
    }
    
    @Override
    public void onFrameRemove(final FrameHandler frameHandler, final boolean isAboutToBeRemoved) {
        if (isAboutToBeRemoved) {
            return;
        }
        Xulor.getInstance().removeDialogUnloadListener(this.m_dialogUnloadListener);
        for (final String dialogId : this.m_loadedDialogs) {
            Xulor.getInstance().unload(dialogId);
        }
        this.m_loadedDialogs.clear();
        UICompanionsEmbeddedFrame.m_loadedFrames.remove(this);
        this.m_companionsList.clear();
    }
    
    public static void clearStaticData() {
        UICompanionsEmbeddedFrame.m_companionViews.clear();
        UICompanionsEmbeddedFrame.m_characterViews.clear();
        UICompanionsEmbeddedFrame.m_loadedFrames.clear();
        PropertiesProvider.getInstance().setPropertyValue("companionPartyListFull", false);
    }
    
    public static CharacterView getCharacterSheetView(final int breedId) {
        return UICompanionsEmbeddedFrame.m_companionViews.get(breedId);
    }
    
    public static CharacterView getCharacterSheetView(final long id) {
        return UICompanionsEmbeddedFrame.m_characterViews.get(id);
    }
    
    public static TLongObjectHashMap<CharacterView> getCharactersSheetViews() {
        return UICompanionsEmbeddedFrame.m_characterViews;
    }
    
    private static void reflowViews() {
        UICompanionsEmbeddedFrame.m_companionViews.clear();
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        for (final ShortCharacterView shortCharacterView : localPlayer.getCompanionViews()) {
            if (shortCharacterView != null) {
                CharacterView characterView;
                if (shortCharacterView.isPlayer()) {
                    characterView = new CharacterView(localPlayer, shortCharacterView);
                }
                else {
                    final long companionId = ((CompanionViewShort)shortCharacterView).getCompanionId();
                    final CompanionModel companion = CompanionManager.INSTANCE.getCompanion(companionId);
                    final NonPlayerCharacter npc = createNonPlayerCharacterCompanion(companion);
                    characterView = new CharacteristicCompanionView(npc, shortCharacterView);
                    npc.getCharacteristicViewProvider().loadFromConfiguration();
                }
                UICompanionsEmbeddedFrame.m_companionViews.put(shortCharacterView.getBreedId(), characterView);
            }
        }
    }
    
    public static NonPlayerCharacter createNonPlayerCharacterCompanion(final CompanionModel companion) {
        return createNonPlayerCharacterCompanion(companion, true);
    }
    
    public static NonPlayerCharacter createNonPlayerCharacterCompanion(final CompanionModel companion, final boolean cappedLevel) {
        final NonPlayerCharacter npc = NonPlayerCharacter.createNpc();
        npc.setCompanionId(companion.getId());
        npc.setType((byte)5);
        npc.setBreed(MonsterBreedManager.getInstance().getBreedFromId(companion.getBreedId()));
        npc.setLevel(cappedLevel ? ((short)Math.min(WakfuGameEntity.getInstance().getLocalPlayer().getLevel(), companion.getLevel())) : companion.getLevel());
        npc.setAddActorToManager(false);
        npc.initialize();
        npc.reloadBuffs(npc.getAppropriateContext());
        npc.reloadItemEffectsWithoutCheck();
        return npc;
    }
    
    public static void reloadCompanionItemEffects(final CompanionModel companion, final Item removedItem) {
        if (companion == null) {
            return;
        }
        final CharacterView view = UICompanionsEmbeddedFrame.m_companionViews.get(companion.getBreedId());
        if (view == null) {
            return;
        }
        final CharacterInfo characterInfo = view.getCharacterInfo();
        if (removedItem != null) {
            if (removedItem.getReferenceItem().getSetId() != 0) {
                final ItemSet set = ItemSetManager.getInstance().getItemSet(removedItem.getReferenceItem().getSetId());
                characterInfo.unapplyItemOnEquipEffect(removedItem, set);
            }
            else {
                characterInfo.unapplyItemOnEquipEffect(removedItem);
            }
        }
        characterInfo.reloadItemEffectsWithoutCheck();
        characterInfo.getCharacteristic((CharacteristicType)FighterCharacteristicType.HP).toMax();
    }
    
    @Override
    public boolean onMessage(final Message message) {
        switch (message.getId()) {
            case 19270: {
                return false;
            }
            default: {
                return true;
            }
        }
    }
    
    @Override
    public long getId() {
        return 0L;
    }
    
    @Override
    public void setId(final long id) {
    }
    
    public abstract String getBaseDialogId();
    
    public String loadMainDialog(final CharacterView characterView) {
        final String dialogId = this.getBaseDialogId();
        this.loadDialog(characterView, dialogId);
        PropertiesProvider.getInstance().setLocalPropertyValue("mainCharacterSheet", true, dialogId);
        return dialogId;
    }
    
    public String loadSecondaryDialog(final CharacterView characterView, final String windowId, final int screenX, final int screenY) {
        final String dialogId = this.getBaseDialogId() + characterView.getCharacterInfo().getBreedId();
        if (Xulor.getInstance().isLoaded(dialogId)) {
            return null;
        }
        this.loadDialog(characterView, dialogId);
        final ElementMap elementMap = Xulor.getInstance().getEnvironment().getElementMap(dialogId);
        final Window window = (Window)elementMap.getElement(windowId);
        window.addWindowPostProcessedListener(new WindowPostProcessedListener() {
            @Override
            public void windowPostProcessed() {
                window.setPosition(screenX, screenY + 10 - window.getHeight());
                window.removeWindowPostProcessedListener(this);
            }
        });
        PropertiesProvider.getInstance().setLocalPropertyValue("mainCharacterSheet", false, dialogId);
        this.setCompanionDndcEnabled(characterView, false);
        return dialogId;
    }
    
    public String loadHeroSecondaryDialog(final CharacterView characterView, final String windowId, final int screenX, final int screenY, final Window w) {
        final String dialogId = windowId + characterView.getCharacterInfo().getId();
        if (Xulor.getInstance().isLoaded(dialogId)) {
            return null;
        }
        this.loadHeroDialog(characterView, dialogId);
        PropertiesProvider.getInstance().setLocalPropertyValue("mainCharacterSheet", false, dialogId);
        PropertiesProvider.getInstance().setLocalPropertyValue("inventoryWindow", w, dialogId);
        return dialogId;
    }
    
    private void setCompanionDndcEnabled(final CharacterView characterView, final boolean enabled) {
        if (characterView == null) {
            return;
        }
        final ShortCharacterView shortCharacterView = this.m_companionsList.get(characterView.getCharacterInfo().getBreedId());
        if (shortCharacterView == null) {
            return;
        }
        shortCharacterView.setDragEnabled(enabled);
    }
    
    private void loadDialog(final CharacterView characterView, final String dialogId) {
        Xulor.getInstance().load(dialogId, Dialogs.getDialogPath(this.getBaseDialogId()), 32769L, (short)10000);
        this.m_loadedDialogs.add(dialogId);
        PropertiesProvider.getInstance().setLocalPropertyValue("characterSheet", characterView, dialogId);
    }
    
    private void loadHeroDialog(final CharacterView characterView, final String dialogId) {
        Xulor.getInstance().load(dialogId, Dialogs.getDialogPath("inventoryOnlyDialog"), 32769L, (short)10000);
        this.m_loadedDialogs.add(dialogId);
        PropertiesProvider.getInstance().setLocalPropertyValue("characterSheet", characterView, dialogId);
    }
    
    public ArrayList<String> getLoadedDialogs() {
        return this.m_loadedDialogs;
    }
    
    static {
        m_companionViews = new TIntObjectHashMap<CharacterView>();
        m_characterViews = new TLongObjectHashMap<CharacterView>();
        m_loadedFrames = new ArrayList<UICompanionsEmbeddedFrame>();
    }
}
