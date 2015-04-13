package com.ankamagames.wakfu.client.service.updater;

import org.apache.log4j.*;
import io.netty.channel.*;
import com.google.gson.*;
import com.ankamagames.wakfu.client.updater.*;
import java.util.*;
import com.ankamagames.wakfu.client.service.updater.message.*;

final class MessageCompleteHandler extends SimpleChannelInboundHandler<String>
{
    private final Logger m_logger;
    private final IComponentManager m_componentManager;
    private final ITextureManager m_textureManager;
    
    MessageCompleteHandler(final IComponentManager componentManager, final ITextureManager textureManager) {
        super(true);
        this.m_logger = Logger.getLogger((Class)MessageCompleteHandler.class);
        this.m_componentManager = componentManager;
        this.m_textureManager = textureManager;
    }
    
    protected void channelRead0(final ChannelHandlerContext ctx, final String msg) throws Exception {
        final Gson gson = new Gson();
        final MessageIdMessage idMessage = (MessageIdMessage)gson.fromJson(msg, (Class)MessageIdMessage.class);
        switch (idMessage.getMessageId()) {
            case UPDATE_ENGINE_STATE: {
                final UpdateEngineStateMessage engineStateMessage = (UpdateEngineStateMessage)gson.fromJson(msg, (Class)UpdateEngineStateMessage.class);
                this.handlerUpdateEngineStateMessage(engineStateMessage);
                break;
            }
            case PROGRESS: {
                final ProgressMessage progressMessage = (ProgressMessage)gson.fromJson(msg, (Class)ProgressMessage.class);
                this.handleProgressMessage(progressMessage);
                break;
            }
            case ERROR: {
                final ErrorMessage errorMessage = (ErrorMessage)gson.fromJson(msg, (Class)ErrorMessage.class);
                this.handleErrorMessage(errorMessage, ctx);
                break;
            }
            case NEW_GAME_CLIENT_CONNECTED: {
                final NewGameClientConnectedMessage message = (NewGameClientConnectedMessage)gson.fromJson(msg, (Class)NewGameClientConnectedMessage.class);
                this.handleNewGameClientMessage(message, ctx);
                break;
            }
            case UNKNOWN: {
                this.m_logger.warn((Object)("Received message with id " + idMessage.getId() + " but it was ignored"));
                break;
            }
        }
    }
    
    private void handleNewGameClientMessage(final NewGameClientConnectedMessage message, final ChannelHandlerContext ctx) {
        this.m_textureManager.setNumClients(message.getNumberOfClients());
    }
    
    private void handlerUpdateEngineStateMessage(final UpdateEngineStateMessage engineStateMessage) {
        if (engineStateMessage.isUpToDate()) {
            this.m_componentManager.changeState(State.UP_TO_DATE);
            return;
        }
        if (engineStateMessage.isUpdateFailed()) {
            this.m_componentManager.changeState(State.FAILURE);
            return;
        }
        if (engineStateMessage.isUpdating()) {
            this.m_componentManager.changeState(State.UPDATING);
            return;
        }
        this.m_componentManager.changeState(State.UNKNOWN);
    }
    
    private void handleProgressMessage(final ProgressMessage progressMessage) {
        if (progressMessage.getParts().isEmpty()) {
            return;
        }
        for (final ProgressComponent component : progressMessage.getParts()) {
            this.m_componentManager.updateComponentInformation(component.getName(), component.getPriority(), component.isCompleted());
        }
        this.m_componentManager.updateProgessInformation(progressMessage.getProgress(), (int)progressMessage.getEstimatedTime());
    }
    
    private void handleErrorMessage(final ErrorMessage errorMessage, final ChannelHandlerContext ctx) {
        this.m_logger.error((Object)("Error message received from updater: " + errorMessage.getMessage() + " (type: " + errorMessage.getType() + ")"));
        ctx.close();
    }
}
