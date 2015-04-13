package com.ankamagames.wakfu.client.steam;

import com.ankamagames.wakfu.client.ui.component.*;
import com.ankamagames.wakfu.client.core.game.steam.*;
import org.jetbrains.annotations.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.framework.reflect.*;
import com.ankamagames.steam.client.friends.*;
import com.ankamagames.steam.wrapper.*;
import com.ankamagames.framework.kernel.core.common.message.scheduler.process.*;
import java.awt.*;
import com.ankamagames.framework.graphics.image.*;
import com.ankamagames.framework.graphics.engine.texture.*;
import com.ankamagames.framework.graphics.engine.*;
import java.awt.image.*;

public class SteamDisplayer extends ImmutableFieldProvider
{
    public static final SteamDisplayer INSTANCE;
    public static final String USER_NAME = "userName";
    public static final String AVATAR_ICON_URL = "avatarIconUrl";
    public static final String HINT_ACTIVATED = "hintActivated";
    public static final String CONNECTED = "connected";
    private boolean m_hintActivated;
    private boolean m_connected;
    private Texture m_avatarTexture;
    private boolean m_loadingAvatar;
    
    @Override
    public String[] getFields() {
        return SteamDisplayer.NO_FIELDS;
    }
    
    @Nullable
    @Override
    public Object getFieldValue(final String fieldName) {
        if ("userName".equals(fieldName)) {
            final SteamFriendsHandler friends = SteamClientContext.INSTANCE.getFriendsHandler();
            return (friends != null) ? friends.getPersonaName() : null;
        }
        if ("hintActivated".equals(fieldName)) {
            return this.m_hintActivated;
        }
        if ("connected".equals(fieldName)) {
            return this.m_connected;
        }
        if (fieldName.equals("avatarIconUrl")) {
            return this.getAvatarTexture();
        }
        return null;
    }
    
    public void setHintActivated(final boolean hintActivated) {
        this.m_hintActivated = hintActivated;
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "hintActivated");
    }
    
    public void setConnected(final boolean connected) {
        this.m_connected = connected;
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "connected");
    }
    
    public Texture getAvatarTexture() {
        if (this.m_avatarTexture != null) {
            return this.m_avatarTexture;
        }
        if (!this.m_loadingAvatar) {
            AvatarImageLoader.INSTANCE.getFriendAvatar((AvatarImageLoad)new AvatarImageLoad(SteamClientContext.INSTANCE.getSteamID(), AvatarType.MEDIUM) {
                public void onImageLoaded(final int imageId, final int width, final int height, final short[] data) {
                    ProcessScheduler.getInstance().schedule(new Runnable() {
                        @Override
                        public void run() {
                            final WritableRaster raster = Raster.createInterleavedRaster(1, width, height, 4, null);
                            raster.setDataElements(0, 0, width, height, data);
                            final BufferedImage image = new BufferedImage(width, height, 2);
                            image.setData(raster);
                            final Image img = Image.createImage(image);
                            final Texture result = TextureManager.getInstance().createTexturePowerOfTwo(RendererType.OpenGL.getRenderer(), Engine.getTextureName("SteamAvatar-" + imageId), img, true);
                            img.removeReference();
                            SteamDisplayer.this.m_avatarTexture = result;
                            PropertiesProvider.getInstance().firePropertyValueChanged(SteamDisplayer.this, "avatarIconUrl");
                        }
                    });
                }
            });
            this.m_loadingAvatar = true;
        }
        return null;
    }
    
    static {
        INSTANCE = new SteamDisplayer();
    }
}
