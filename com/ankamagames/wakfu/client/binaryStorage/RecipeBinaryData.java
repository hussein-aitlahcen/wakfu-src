package com.ankamagames.wakfu.client.binaryStorage;

import com.ankamagames.framework.fileFormat.io.binaryStorage2.*;
import com.ankamagames.wakfu.common.binaryStorage.*;

public class RecipeBinaryData implements BinaryData
{
    protected int m_id;
    protected int m_categoryId;
    protected long m_duration;
    protected String m_criteria;
    protected String m_visibilityCriteria;
    protected int m_level;
    protected int[] m_properties;
    protected int m_xpRatio;
    protected int[] m_machinesUsingRecipe;
    protected int m_successRate;
    protected boolean m_contractEnabled;
    protected long m_neededKamas;
    protected long m_xp;
    protected RecipeIngredient[] m_ingredients;
    protected RecipeProduct[] m_products;
    protected RecipeMaterial[] m_materials;
    
    public int getId() {
        return this.m_id;
    }
    
    public int getCategoryId() {
        return this.m_categoryId;
    }
    
    public long getDuration() {
        return this.m_duration;
    }
    
    public String getCriteria() {
        return this.m_criteria;
    }
    
    public String getVisibilityCriteria() {
        return this.m_visibilityCriteria;
    }
    
    public int getLevel() {
        return this.m_level;
    }
    
    public int[] getProperties() {
        return this.m_properties;
    }
    
    public int getXpRatio() {
        return this.m_xpRatio;
    }
    
    public int[] getMachinesUsingRecipe() {
        return this.m_machinesUsingRecipe;
    }
    
    public int getSuccessRate() {
        return this.m_successRate;
    }
    
    public boolean isContractEnabled() {
        return this.m_contractEnabled;
    }
    
    public long getNeededKamas() {
        return this.m_neededKamas;
    }
    
    public long getXp() {
        return this.m_xp;
    }
    
    public RecipeIngredient[] getIngredients() {
        return this.m_ingredients;
    }
    
    public RecipeProduct[] getProducts() {
        return this.m_products;
    }
    
    public RecipeMaterial[] getMaterials() {
        return this.m_materials;
    }
    
    @Override
    public void reset() {
        this.m_id = 0;
        this.m_categoryId = 0;
        this.m_duration = 0L;
        this.m_criteria = null;
        this.m_visibilityCriteria = null;
        this.m_level = 0;
        this.m_properties = null;
        this.m_xpRatio = 0;
        this.m_machinesUsingRecipe = null;
        this.m_successRate = 0;
        this.m_contractEnabled = false;
        this.m_neededKamas = 0L;
        this.m_xp = 0L;
        this.m_ingredients = null;
        this.m_products = null;
        this.m_materials = null;
    }
    
    @Override
    public void read(final RandomByteBufferReader buffer) throws Exception {
        this.m_id = buffer.getInt();
        this.m_categoryId = buffer.getInt();
        this.m_duration = buffer.getLong();
        this.m_criteria = buffer.readUTF8().intern();
        this.m_visibilityCriteria = buffer.readUTF8().intern();
        this.m_level = buffer.getInt();
        this.m_properties = buffer.readIntArray();
        this.m_xpRatio = buffer.getInt();
        this.m_machinesUsingRecipe = buffer.readIntArray();
        this.m_successRate = buffer.getInt();
        this.m_contractEnabled = buffer.readBoolean();
        this.m_neededKamas = buffer.getLong();
        this.m_xp = buffer.getLong();
        final int ingredientCount = buffer.getInt();
        this.m_ingredients = new RecipeIngredient[ingredientCount];
        for (int iIngredient = 0; iIngredient < ingredientCount; ++iIngredient) {
            (this.m_ingredients[iIngredient] = new RecipeIngredient()).read(buffer);
        }
        final int productCount = buffer.getInt();
        this.m_products = new RecipeProduct[productCount];
        for (int iProduct = 0; iProduct < productCount; ++iProduct) {
            (this.m_products[iProduct] = new RecipeProduct()).read(buffer);
        }
        final int materialCount = buffer.getInt();
        this.m_materials = new RecipeMaterial[materialCount];
        for (int iMaterial = 0; iMaterial < materialCount; ++iMaterial) {
            (this.m_materials[iMaterial] = new RecipeMaterial()).read(buffer);
        }
    }
    
    @Override
    public final int getDataTypeId() {
        return WakfuBinaryStorableType.RECIPE.getId();
    }
    
    public static class RecipeIngredient
    {
        protected int m_itemId;
        protected short m_quantity;
        
        public int getItemId() {
            return this.m_itemId;
        }
        
        public short getQuantity() {
            return this.m_quantity;
        }
        
        public void read(final RandomByteBufferReader buffer) throws Exception {
            this.m_itemId = buffer.getInt();
            this.m_quantity = buffer.getShort();
        }
    }
    
    public static class RecipeProduct
    {
        protected int m_itemId;
        protected short m_quantity;
        
        public int getItemId() {
            return this.m_itemId;
        }
        
        public short getQuantity() {
            return this.m_quantity;
        }
        
        public void read(final RandomByteBufferReader buffer) throws Exception {
            this.m_itemId = buffer.getInt();
            this.m_quantity = buffer.getShort();
        }
    }
    
    public static class RecipeMaterial
    {
        protected short m_minLevel;
        protected short m_minRarity;
        protected boolean m_optionnal;
        protected int[] m_materialTypes;
        
        public short getMinLevel() {
            return this.m_minLevel;
        }
        
        public short getMinRarity() {
            return this.m_minRarity;
        }
        
        public boolean isOptionnal() {
            return this.m_optionnal;
        }
        
        public int[] getMaterialTypes() {
            return this.m_materialTypes;
        }
        
        public void read(final RandomByteBufferReader buffer) throws Exception {
            this.m_minLevel = buffer.getShort();
            this.m_minRarity = buffer.getShort();
            this.m_optionnal = buffer.readBoolean();
            this.m_materialTypes = buffer.readIntArray();
        }
    }
}
