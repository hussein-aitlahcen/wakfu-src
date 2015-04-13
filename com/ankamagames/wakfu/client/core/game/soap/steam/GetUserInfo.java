package com.ankamagames.wakfu.client.core.game.soap.steam;

import com.ankamagames.steam.wrapper.*;
import com.ankamagames.steam.common.*;
import java.net.*;
import java.nio.charset.*;
import com.google.gson.*;
import java.io.*;

public class GetUserInfo
{
    protected static final String CONTENT_TYPE = "Content-Type";
    protected static final String CONTENT_TYPE_VALUE = "application/x-www-form-urlencoded";
    public static final String WEB_API_PUBLISHER_KEY = "20FE8C14BCF6AF6E106969EE3A02B718";
    private static final String GET_USER_INFO_API_URL = "https://api.steampowered.com/ISteamMicroTxn/GetUserInfo/V0001/";
    private static final int CONNECT_TIMEOUT = 15000;
    private static final int READ_TIMEOUT = 15000;
    
    private static URL getUrl(final long steamId) {
        final StringBuilder sb = new StringBuilder();
        sb.append("https://api.steampowered.com/ISteamMicroTxn/GetUserInfo/V0001/");
        sb.append("?key=").append("20FE8C14BCF6AF6E106969EE3A02B718");
        sb.append("&steamid=").append(steamId);
        try {
            return new URL(sb.toString());
        }
        catch (MalformedURLException e) {
            return null;
        }
    }
    
    public static void sendRequest(final CSteamID steamID, final GetUserInfoListener listener) {
        final long serializedSteamId = SteamUtils.serializeSteamId(steamID);
        final URL url = getUrl(serializedSteamId);
        if (url == null) {
            listener.onError();
            return;
        }
        try {
            final HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setConnectTimeout(15000);
            connection.setReadTimeout(15000);
            connection.connect();
            final ListeningThread listening = new ListeningThread();
            listening.setConnection(connection, listener);
            listening.start();
        }
        catch (IOException e) {
            listener.onError();
        }
    }
    
    private static class ListeningThread extends Thread
    {
        private HttpURLConnection m_connection;
        private GetUserInfoListener m_listener;
        
        public void setConnection(final HttpURLConnection connection, final GetUserInfoListener listener) {
            this.m_connection = connection;
            this.m_listener = listener;
            this.setName("GetUserInfo-AnswerListener");
        }
        
        @Override
        public void run() {
            try {
                final InputStream is = this.m_connection.getInputStream();
                final SteamUserInfo steamUserInfo = getSteamUserInfo(is);
                this.m_listener.onSteamUserInfo(steamUserInfo);
            }
            catch (Exception e) {
                this.m_listener.onError();
            }
            finally {
                this.m_connection.disconnect();
            }
        }
        
        private static SteamUserInfo getSteamUserInfo(final InputStream is) throws IOException {
            final Reader reader = new InputStreamReader(is, Charset.forName("UTF-8"));
            try {
                final Gson gson = new Gson();
                return (SteamUserInfo)gson.fromJson(reader, (Class)SteamUserInfo.class);
            }
            finally {
                reader.close();
            }
        }
        
        @Override
        public String toString() {
            return "ListeningThread{m_connection=" + this.m_connection + ", m_listener=" + this.m_listener + '}';
        }
    }
}
