package com.ankamagames.wakfu.client.core.game.characterInfo.characterChoiceSlot;

import com.ankamagames.wakfu.client.ui.component.*;
import com.ankamagames.wakfu.common.configuration.*;
import com.ankama.wakfu.utils.injection.*;
import com.ankamagames.wakfu.client.updater.*;
import com.ankamagames.wakfu.client.core.game.soap.shop.*;
import com.ankamagames.wakfu.client.core.game.webShop.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import java.util.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.framework.reflect.*;
import org.jetbrains.annotations.*;

public class CharacterChoiceSlots extends ImmutableFieldProvider
{
    public static final String SLOTS = "slots";
    public static final CharacterChoiceSlots INSTANCE;
    private final List<AbstractCharacterChoiceSlot> m_characterSlots;
    private final List<UnlockableCharacterChoiceSlot> m_additionalSlots;
    private final List<AbstractCharacterChoiceSlot> m_slots;
    private byte m_numAdditionalSlots;
    private byte m_numArticlesResult;
    private Runnable m_onArticlesResultRunnable;
    
    private CharacterChoiceSlots() {
        super();
        this.m_characterSlots = new ArrayList<AbstractCharacterChoiceSlot>();
        this.m_additionalSlots = new ArrayList<UnlockableCharacterChoiceSlot>();
        this.m_slots = new ArrayList<AbstractCharacterChoiceSlot>();
        this.m_numAdditionalSlots = 0;
        this.m_numArticlesResult = 0;
        SystemConfiguration.INSTANCE.addListener(new SystemConfigurationListener() {
            @Override
            public void onLoad() {
                ArticlesCache.INSTANCE.remove("characterSlot1");
                ArticlesCache.INSTANCE.remove("characterSlot2");
                ArticlesCache.INSTANCE.remove("characterSlot3");
            }
        });
    }
    
    public void requestUpdateShop(final Runnable runnable) {
        synchronized (this.m_additionalSlots) {
            this.m_additionalSlots.clear();
        }
        final boolean loading = !Injection.getInstance().getInstance(IComponentManager.class).hasComponentsCompleted(Component.FULL);
        if (loading) {
            if (runnable != null) {
                runnable.run();
            }
            return;
        }
        this.m_numArticlesResult = 0;
        this.m_onArticlesResultRunnable = runnable;
        ArticlesCache.INSTANCE.loadArticleByKey("characterSlot1", new ArticlesKeyListener() {
            @Override
            public void onArticlesKey(final List<Article> articles) {
                if (!articles.isEmpty()) {
                    synchronized (CharacterChoiceSlots.this.m_additionalSlots) {
                        CharacterChoiceSlots.this.m_additionalSlots.add(new UnlockableCharacterChoiceSlot(articles.get(0), 1));
                        Collections.sort((List<Comparable>)CharacterChoiceSlots.this.m_additionalSlots);
                    }
                }
                CharacterChoiceSlots.this.incAndCheckNumArticles();
            }
            
            @Override
            public void onError() {
                CharacterChoiceSlots.this.incAndCheckNumArticles();
            }
        });
        ArticlesCache.INSTANCE.loadArticleByKey("characterSlot2", new ArticlesKeyListener() {
            @Override
            public void onArticlesKey(final List<Article> articles) {
                if (!articles.isEmpty()) {
                    synchronized (CharacterChoiceSlots.this.m_additionalSlots) {
                        CharacterChoiceSlots.this.m_additionalSlots.add(new UnlockableCharacterChoiceSlot(articles.get(0), 2));
                        Collections.sort((List<Comparable>)CharacterChoiceSlots.this.m_additionalSlots);
                    }
                }
                CharacterChoiceSlots.this.incAndCheckNumArticles();
            }
            
            @Override
            public void onError() {
                CharacterChoiceSlots.this.incAndCheckNumArticles();
            }
        });
        ArticlesCache.INSTANCE.loadArticleByKey("characterSlot3", new ArticlesKeyListener() {
            @Override
            public void onArticlesKey(final List<Article> articles) {
                if (!articles.isEmpty()) {
                    synchronized (CharacterChoiceSlots.this.m_additionalSlots) {
                        CharacterChoiceSlots.this.m_additionalSlots.add(new UnlockableCharacterChoiceSlot(articles.get(0), 3));
                        Collections.sort((List<Comparable>)CharacterChoiceSlots.this.m_additionalSlots);
                    }
                }
                CharacterChoiceSlots.this.incAndCheckNumArticles();
            }
            
            @Override
            public void onError() {
                CharacterChoiceSlots.this.incAndCheckNumArticles();
            }
        });
    }
    
    private void incAndCheckNumArticles() {
        ++this.m_numArticlesResult;
        if (this.m_numArticlesResult >= 3) {
            if (this.m_onArticlesResultRunnable != null) {
                this.m_onArticlesResultRunnable.run();
            }
            this.buildSlots();
        }
    }
    
    public void updateCharacters(final byte additionalSlots, final List<CharacterInfo> characters) {
        this.m_characterSlots.clear();
        this.m_numAdditionalSlots = additionalSlots;
        for (int maxAccountCharacterCount = Math.max(5 + additionalSlots, characters.size()), i = 0; i < maxAccountCharacterCount; ++i) {
            final CharacterInfo characterInfo = (i < characters.size()) ? characters.get(i) : null;
            final AbstractCharacterChoiceSlot characterSlot = new CharacterChoiceSlot(characterInfo);
            this.m_characterSlots.add(characterSlot);
        }
        this.buildSlots();
    }
    
    private void buildSlots() {
        this.m_slots.clear();
        this.m_slots.addAll(this.m_characterSlots);
        if (this.m_numAdditionalSlots < this.m_additionalSlots.size()) {
            for (int i = 0; i < this.m_additionalSlots.size() - this.m_numAdditionalSlots; ++i) {
                this.m_slots.add(this.m_additionalSlots.get(this.m_numAdditionalSlots));
            }
        }
        for (int i = 0, size = this.m_slots.size(); i < size; ++i) {
            this.m_slots.get(i).setOffset(i);
        }
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "slots");
    }
    
    @Override
    public String[] getFields() {
        return CharacterChoiceSlots.NO_FIELDS;
    }
    
    @Nullable
    @Override
    public Object getFieldValue(final String fieldName) {
        if (fieldName.equals("slots")) {
            return this.m_slots;
        }
        return null;
    }
    
    public int getCharacterOffset(final CharacterInfo info) {
        if (info != null) {
            for (int i = 0, size = this.m_characterSlots.size(); i < size; ++i) {
                final CharacterChoiceSlot characterChoiceSlot = this.m_characterSlots.get(i);
                if (characterChoiceSlot.getCharacterInfo().getId() == info.getId()) {
                    return i;
                }
            }
        }
        return -1;
    }
    
    static {
        INSTANCE = new CharacterChoiceSlots();
    }
}
