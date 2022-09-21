/**
  *	
  *@author:Praveen Gudimalla
  *
  *
  **/

package com.resourcing.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.resourcing.beans.Doc;

@Repository
public interface DocRepository extends JpaRepository<Doc, Integer> {

	@Query(value = "SELECT a1 FROM Doc a1 WHERE a1.candidateId =?1")
	List<Doc> findAllById(int candidateId);

}
