package com.nrt.quiz.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "userQuizHistory_details")
public class UserPlayedQuizHistory {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	private int correctAnswers;
	
	private int wrongAnswers;
	
	private int  attemptQuestions;
	 
	private int score;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JsonIgnore
	private Quiz  attemptQuiz;

	@ManyToOne(fetch = FetchType.EAGER)
	@JsonIgnore
	private User user;
}
