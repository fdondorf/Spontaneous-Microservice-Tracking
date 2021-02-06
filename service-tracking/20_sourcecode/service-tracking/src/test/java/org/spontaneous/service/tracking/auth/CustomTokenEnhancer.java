package org.spontaneous.service.tracking.auth;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;

public class CustomTokenEnhancer implements TokenEnhancer {

	private static final String KEY_USER_ID = "userId";

	@Override
	public OAuth2AccessToken enhance(OAuth2AccessToken oAuth2AccessToken, OAuth2Authentication authentication) {
		DefaultOAuth2AccessToken newToken = new DefaultOAuth2AccessToken(oAuth2AccessToken);
		Map<String, Object> addInfo = new HashMap<>();
		addInfo.put(KEY_USER_ID, getUserIdFromAuthentication(authentication));
		newToken.setAdditionalInformation(addInfo);
		return newToken;
	}

	private UUID getUserIdFromAuthentication(OAuth2Authentication authentication) {

		Map<String, Object> obj = (Map<String, Object>) authentication.getUserAuthentication().getDetails();

		return (UUID) obj.getOrDefault(KEY_USER_ID, null);
	}

}