//*author: HarshKashyap

package com.resourcing.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.resourcing.beans.SkillInterviewerAssosiation;
@Repository
public interface SkillInterviewerAssociationRepository extends JpaRepository<SkillInterviewerAssosiation, Integer> {

//	@Query("select a from Skill a WHERE a.skillId=?1")
//	public List<Skill_Interviewer_Assosiation> findBySkill(int skillId);
//	
//	@Query("select a from InterviewPanel a WHERE a.interviewerId=?1")
//	public List<Skill_Interviewer_Assosiation> findInterviewerIdBySkillId(int interviewerId);
	
	@Query("select a from Skill_Interviewer_Assosiation a WHERE a.interviewerId=?1")
	public List<SkillInterviewerAssosiation> findSkillListByInterviewerId(int interviewerId);
	
	@Query("select a from Skill_Interviewer_Assosiation a WHERE a.interviewerId=?1 AND a.skillId=?2")
	public SkillInterviewerAssosiation checkPairOfSkillAndInterviewer(int interviewerId,int skillId);

}
