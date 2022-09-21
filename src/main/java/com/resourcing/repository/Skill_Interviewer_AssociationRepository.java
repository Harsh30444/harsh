//*author: HarshKashyap

package com.resourcing.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.resourcing.beans.SkillInterviewerAssosiation;

public interface Skill_Interviewer_AssociationRepository extends JpaRepository<SkillInterviewerAssosiation, Integer> {

	@Query("select a from Skill_Interviewer_Assosiation a WHERE a.interviewerId=?1")
	public List<SkillInterviewerAssosiation> findSkillListByInterviewerId(int interviewerId);

	@Query("select a from Skill_Interviewer_Assosiation a WHERE a.interviewerId=?1 AND a.skillId=?2")
	public SkillInterviewerAssosiation checkPairOfSkillAndInterviewer(int interviewerId, int skillId);

}
