package com.data.integration.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.data.integration.data.ApplicationVariable;

@Repository
public interface ApplicationVariableRepository  extends CrudRepository<ApplicationVariable,Long>{

    
    /**
     * get Application Level variables
     * @return
     */
   public List<ApplicationVariable> findBySubscriberIDIsNull();
    
   /**
    * get Subscriber Level variables
    * @param subscriberID
    * @return
    */
   public List<ApplicationVariable> findBySubscriberID(Long subscriberID);
   
   /**
	 * get distinct keywords
	 * @param subscriberID
	 * @return
	 */
	@Query("SELECT DISTINCT av.keyword FROM ApplicationVariable av WHERE av.subscriberID=null OR av.subscriberID =:subscriberID")
	public List<String> getKeywordsBySubscriberID(@Param("subscriberID")Long subscriberID);
}
