package com.data.integration.service.impl;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.data.integration.data.Subscriber;
import com.data.integration.repository.SubscriberRepository;

@Service
public class ClientUserDetailsService implements UserDetailsService {

	@Autowired
	private SubscriberRepository clientRepository;


	@Override
	public UserDetails loadUserByUsername(String username)
			throws UsernameNotFoundException {
		Subscriber client = clientRepository.findByLogin(username);
		if (client == null) {
			throw new UsernameNotFoundException(String.format(
					"Client %s does not exist!", username));
		}
		return new ClientRepositoryUserDetails(client);
	}

	private final static class ClientRepositoryUserDetails extends Subscriber
			implements UserDetails {

		private static final long serialVersionUID = 1L;

		private ClientRepositoryUserDetails(Subscriber user) {
			super(user);
		}

		public Collection<? extends GrantedAuthority> getAuthorities() {
			return AuthorityUtils.createAuthorityList("ROLE_USER");
		}

		public String getUsername() {
			return getLogin();
		}

		public boolean isAccountNonExpired() {
			return true;
		}

		public boolean isAccountNonLocked() {
			return true;
		}

		public boolean isCredentialsNonExpired() {
			return true;
		}

		public boolean isEnabled() {
			return true;
		}

	}
}
