package com.nrt.quiz.serviceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.nrt.quiz.entity.Quiz;
import com.nrt.quiz.entity.User;
import com.nrt.quiz.entity.UserPlayedQuizHistory;
import com.nrt.quiz.repository.QuizRepository;
import com.nrt.quiz.repository.UserPlayedQuizHistoryRepo;
import com.nrt.quiz.repository.UserRepository;
import com.nrt.quiz.request.UserPlayedQuizHistoryReq;
import com.nrt.quiz.response.ApiResponse;
import com.nrt.quiz.service.UserPlayedQuizHistoryService;
import com.nrt.quiz.util.CommonUtil;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class UserPlayedQuizHistoryServiceImpl implements UserPlayedQuizHistoryService {

	@Autowired
	UserPlayedQuizHistoryRepo historyRepo;

	@Autowired
	QuizRepository quizRepository;

	@Autowired
	UserRepository repository;

	@Override
	public ResponseEntity<ApiResponse<UserPlayedQuizHistory>> addUserQuizHistory(
			UserPlayedQuizHistoryReq quizHistoryRequest) {

		User user = repository.findByEmailAddress(CommonUtil.getCurrentUserEmailAddress());

		log.info("user data is here " + user);

		long quizId = Long.parseLong(quizHistoryRequest.getAttemptQuiz());

		try {

			log.info("category id is : " + quizHistoryRequest.getAttemptQuiz());
			Quiz quiz = quizRepository.findById((long) quizId).get();

			quiz.getMaxMarks();
			int markPerQuestions = changeToInt(quiz.getNumberOfQuestions()) / changeToInt(quiz.getMaxMarks());

			int attemptQues = changeToInt(quizHistoryRequest.getAttemptQuestions());

			int correctAnswers = changeToInt(quizHistoryRequest.getCorrectAnswers());

			UserPlayedQuizHistory history = new UserPlayedQuizHistory();
			history.setAttemptQuestions(attemptQues);
			history.setAttemptQuizId(quizId);
			history.setCorrectAnswers(correctAnswers);
			history.setScore(markPerQuestions * correctAnswers);
			history.setWrongAnswers(attemptQues - correctAnswers);
			history.setUserId(user.getId());

			UserPlayedQuizHistory payload = historyRepo.save(history);
			return ResponseEntity.ok(new ApiResponse<>("success", "Data saved successfully", payload, 200));
		} catch (Exception e) {
			// Handle the exception here and log it
			log.error("An error occurred while saving data", e);
			return ResponseEntity.internalServerError().body(new ApiResponse<>("error", e.getMessage(), null, 500));
		}
	}

	public int changeToInt(String str) {
		if (str != null)
			return Integer.parseInt(str);
		return 0;
	}

	@Override
	public ResponseEntity<ApiResponse<List<UserPlayedQuizHistory>>> getUserQuizHistory() {
		try {
			List<UserPlayedQuizHistory> userPlayedQuizHistoryList = historyRepo.findAll();
			log.info("An error occurreduser " + userPlayedQuizHistoryList);
			return ResponseEntity
					.ok(new ApiResponse<>("success", "Data fetched successfully", userPlayedQuizHistoryList, 200));
		} catch (Exception e) {
			log.error("An error occurred while saving data", e);
			return ResponseEntity.internalServerError().body(new ApiResponse<>("error", e.getMessage(), null, 500));
		}
	}

	@Override
	public ResponseEntity<ApiResponse<UserPlayedQuizHistory>> getUserQuizResult(String requestId) {

		try {

			long reqId = Long.parseLong(requestId);
			UserPlayedQuizHistory userPlayedQuizHistoryList = historyRepo.findById(reqId).get();

			return ResponseEntity.ok(
					new ApiResponse<>("success", "result data fetched  successfully", userPlayedQuizHistoryList, 200));
		} catch (Exception e) {
			log.error("An error occurred while saving data", e);
			return ResponseEntity.internalServerError().body(new ApiResponse<>("error", e.getMessage(), null, 500));
		}
	}

	@Override
	public ResponseEntity<ApiResponse<Integer>> addUserRank(long QuizId) {
		return null;
//		try {
//			User user = repository.findByEmailAddress(CommonUtil.getCurrentUserEmailAddress());
//			log.info("userdaa ta is here " + user.toString());
//
//			Quiz quiz = quizRepository.findById(QuizId).get();
//
//			List<UserPlayedQuizHistory> resultList = historyRepo.findAllByAttemptQuiz(QuizId);
//			Collections.sort(resultList, new ResultComparator());
//
//			int rank = 0;
//			
//			log.info(rank + "is the rank of user");
//			return ResponseEntity.ok(new ApiResponse<>("success", "result list  fetched  successfully", rank, 200));
//		} catch (Exception e) {
//			log.error("An error occurred while saving data", e);
//			return ResponseEntity.internalServerError().body(new ApiResponse<>("error", e.getMessage(), null, 500));
//		}
	}
}
