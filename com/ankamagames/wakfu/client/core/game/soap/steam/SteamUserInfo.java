package com.ankamagames.wakfu.client.core.game.soap.steam;

import com.google.gson.annotations.*;

public class SteamUserInfo
{
    @SerializedName("response")
    private Response m_response;
    
    public boolean isOk() {
        return this.m_response != null && "OK".equals(this.m_response.getResult());
    }
    
    public String getResult() {
        return this.m_response.getResult();
    }
    
    public Params getParams() {
        return this.m_response.getParams();
    }
    
    public Error getError() {
        return this.m_response.getError();
    }
    
    private static class Response
    {
        @SerializedName("result")
        private String m_result;
        @SerializedName("params")
        private Params m_params;
        @SerializedName("error")
        private Error m_error;
        
        String getResult() {
            return this.m_result;
        }
        
        Params getParams() {
            return this.m_params;
        }
        
        Error getError() {
            return this.m_error;
        }
    }
    
    public static class Params
    {
        @SerializedName("state")
        private String m_state;
        @SerializedName("country")
        private String m_country;
        @SerializedName("currency")
        private String m_currency;
        @SerializedName("status")
        private String m_status;
        
        public String getState() {
            return this.m_state;
        }
        
        public String getCountry() {
            return this.m_country;
        }
        
        public String getCurrency() {
            return this.m_currency;
        }
        
        public String getStatus() {
            return this.m_status;
        }
    }
    
    public static class Error
    {
        @SerializedName("errorcode")
        private int m_errorCode;
        @SerializedName("errordesc")
        private String m_errorDesc;
        
        public int getErrorCode() {
            return this.m_errorCode;
        }
        
        public String getErrorDesc() {
            return this.m_errorDesc;
        }
    }
}
