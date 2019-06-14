package com.data.integration.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.data.integration.data.User;

/**
 * User Repository
 * 
 * @author Kalyani
 *
 */

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

	User findByUserId(Long userId);

	User findByLogin(String userName);

	@Query("SELECT user FROM User user WHERE user.login = :userName AND user.userId <> :userId")
	User findByLoginNotInUserId(@Param(value = "userName") String userName,
			@Param(value = "userId") Long userId);
}
