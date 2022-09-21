/**
  *	
  *@author:Praveen Gudimalla
  *
  *
  **/

package com.resourcing.beans;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "Skill")
@Data
public class Skill {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private int skillId;
	private String skillName;


}
