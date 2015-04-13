package com.ankamagames.wakfu.client.core.game.secret;

import com.ankamagames.wakfu.client.ui.component.*;
import gnu.trove.*;
import java.util.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.framework.reflect.*;
import org.jetbrains.annotations.*;
import com.ankamagames.wakfu.client.core.*;

public class SecretsView extends ImmutableFieldProvider
{
    public static final int NUM_ELEMENTS_PER_PAGE = 6;
    public static final String PAGE_DESCRIPTION = "pageDescription";
    public static final String HAS_PREVIOUS_PAGE = "hasPreviousPage";
    public static final String HAS_NEXT_PAGE = "hasNextPage";
    public static final String SECRETS = "secrets";
    private byte m_currentPage;
    private byte m_pageCount;
    private final ArrayList<SecretView> m_allSecrets;
    private final ArrayList<SecretView> m_secrets;
    
    public SecretsView() {
        super();
        this.m_allSecrets = new ArrayList<SecretView>();
        this.m_secrets = new ArrayList<SecretView>();
        SecretManager.INSTANCE.forEachSecret(new TObjectProcedure<SecretData>() {
            @Override
            public boolean execute(final SecretData object) {
                SecretsView.this.m_allSecrets.add(new SecretView(object));
                return true;
            }
        });
        Collections.sort(this.m_allSecrets, new Comparator<SecretView>() {
            @Override
            public int compare(final SecretView o1, final SecretView o2) {
                if (o1.getData().getLevel() < o2.getData().getLevel()) {
                    return -1;
                }
                if (o1.getData().getLevel() > o2.getData().getLevel()) {
                    return 1;
                }
                return o1.getData().getId() - o2.getData().getId();
            }
        });
        this.m_currentPage = 0;
        this.m_pageCount = (byte)Math.ceil(this.m_allSecrets.size() / 6.0f);
        this.updateSecretList();
    }
    
    private void updateSecretList() {
        final int startIndex = MathHelper.clamp(this.m_currentPage * 6, 0, this.m_allSecrets.size() - 1);
        final int endIndex = MathHelper.clamp(startIndex + 6, 0, this.m_allSecrets.size());
        this.m_secrets.clear();
        for (int i = startIndex; i < endIndex; ++i) {
            this.m_secrets.add(this.m_allSecrets.get(i));
        }
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "secrets");
    }
    
    @Override
    public String[] getFields() {
        return SecretsView.NO_FIELDS;
    }
    
    public void previousPage() {
        if (this.hasPreviousPage()) {
            --this.m_currentPage;
            this.updateSecretList();
            this.firePageFields();
        }
    }
    
    public void nextPage() {
        if (this.hasNextPage()) {
            ++this.m_currentPage;
            this.updateSecretList();
            this.firePageFields();
        }
    }
    
    private void firePageFields() {
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "hasPreviousPage", "hasNextPage", "pageDescription");
    }
    
    @Nullable
    @Override
    public Object getFieldValue(final String fieldName) {
        if (fieldName.equals("pageDescription")) {
            return this.getPageDescription();
        }
        if (fieldName.equals("hasNextPage")) {
            return this.hasNextPage();
        }
        if (fieldName.equals("hasPreviousPage")) {
            return this.hasPreviousPage();
        }
        if (fieldName.equals("secrets")) {
            return this.m_secrets;
        }
        return null;
    }
    
    private boolean hasPreviousPage() {
        return this.m_currentPage > 0;
    }
    
    private boolean hasNextPage() {
        return this.m_currentPage + 1 < this.m_pageCount;
    }
    
    private String getPageDescription() {
        return WakfuTranslator.getInstance().getString("pageNumber", this.m_currentPage + 1, this.m_pageCount);
    }
}
