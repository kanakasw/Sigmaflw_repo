package com.data.integration.service.impl;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;
import org.springframework.stereotype.Service;

import com.data.integration.config.OAuth2ServerConfig;
import com.data.integration.data.Subscriber;
import com.data.integration.repository.SubscriberRepository;
import com.data.integration.service.enums.GrantTypeEnum;

@Service
public class CustomClientDetailsService implements ClientDetailsService {

	@Autowired
	private SubscriberRepository clientRepository;

	
	@Override
	public ClientDetails loadClientByClientId(String clientKey)
			throws ClientRegistrationException {
		Subscriber client = clientRepository.findByClientID(clientKey);
		if (client == null) {
			throw new ClientRegistrationException(String.format("Client Not Found %s", clientKey));
		}
		Set<String> authorizedGrantTypes = new HashSet<String>();
		authorizedGrantTypes.add(GrantTypeEnum.PASSWORD.getKey());
		authorizedGrantTypes.add(GrantTypeEnum.REFRESH_TOKEN.getKey());
		authorizedGrantTypes.add(GrantTypeEnum.CLIENT_CREDENTIALS.getKey());
		authorizedGrantTypes.add(GrantTypeEnum.IMPLICIT.getKey());

		BaseClientDetails clientDetails = new BaseClientDetails();
		
		clientDetails.setClientId(client.getClientID());
		clientDetails.setClientSecret(client.getClientSecret());
		clientDetails.setAuthorizedGrantTypes(authorizedGrantTypes);
		clientDetails.setResourceIds(Arrays.asList(new String[] {OAuth2ServerConfig.RESOURCEID}));
		clientDetails.setScope(Arrays.asList(new String[] {"read","write"}));
		return clientDetails;

	}

}
