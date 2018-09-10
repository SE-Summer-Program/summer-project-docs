package com.sjtubus.model.response;

import com.alibaba.fastjson.JSONObject;
import org.apache.oltu.oauth2.client.response.OAuthAccessTokenResponse;
import org.apache.oltu.oauth2.client.validator.TokenValidator;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.token.BasicOAuthToken;
import org.apache.oltu.oauth2.common.token.OAuthToken;

/**
 * @author SHIYONG
 * @date 2018/7/12 10:11
 */
public class MyAccessTokenResponse extends OAuthAccessTokenResponse {
    private JSONObject params;

    @Override
    protected void setBody(String body) throws OAuthProblemException {
        params = JSONObject.parseObject(body);
        this.body = body;
    }

    protected void setContentType(String var1){
        this.contentType = var1;
    }

    @Override
    public String getParam(String param){
        return this.params.getString(param);
    }

    protected void setResponseCode(int var1){
        this.responseCode = var1;
    }

    @Override
    public String getAccessToken(){
        return this.params.getString("access_token");
    }

    @Override
    public String getTokenType(){
        return this.params.getString("token_type");
    }

    @Override
    public Long getExpiresIn(){
        String value = this.params.getString("expires_in");
        return value == null ? null : Long.valueOf(value);
    }

    @Override
    public String getRefreshToken(){
        return this.params.getString("refresh_token");
    }

    @Override
    public String getScope(){
        return this.params.getString("scope");
    }

    public OAuthToken getOAuthToken(){
        return new BasicOAuthToken(this.getAccessToken(), this.getTokenType(), this.getExpiresIn(), this.getRefreshToken(), this.getScope());
    }
}
