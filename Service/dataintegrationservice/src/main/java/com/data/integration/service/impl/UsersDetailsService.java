package com.data.integration.service.impl;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.data.integration.data.User;
import com.data.integration.repository.UserRepository;

@Service
public class UsersDetailsService implements UserDetailsService {
	
	@Autowired
	private UserRepository userRepository;


	@Override
	public UserDetails loadUserByUsername(String username)
			throws UsernameNotFoundException {
		//Subscriber client = clientRepository.findByLogin(username);
		User user = userRepository.findByLogin(username);
		if (user == null) {
			throw new UsernameNotFoundException(String.format(
					"User %s does not exist!", username));
		}
		return new UsereRepositoryUserDetails(user);
	}

	private final static class UsereRepositoryUserDetails extends User
			implements UserDetails {

		private static final long serialVersionUID = 1L;

		private UsereRepositoryUserDetails(User user) {
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

		public String getPassword() {
			return getUserPassword();
		}

	}
}
