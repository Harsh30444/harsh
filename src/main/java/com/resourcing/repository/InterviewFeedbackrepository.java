package com.resourcing.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.resourcing.beans.InterviewFeedback;

@Repository
public interface InterviewFeedbackrepository extends JpaRepository<InterviewFeedback, Integer> {

}
