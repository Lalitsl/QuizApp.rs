package com.nrt.quiz.controller;

import java.sql.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.nrt.quiz.request.LoginRequest;
import com.nrt.quiz.request.UserRequest;
import com.nrt.quiz.response.ApiResponse;
import com.nrt.quiz.response.LoginResponse;
import com.nrt.quiz.response.UserResponse;
import com.nrt.quiz.service.UserService;

import jakarta.servlet.http.HttpSession;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Controller
@RequestMapping("/user")
@CrossOrigin(origins = "http://localhost:9090")
public class UserController {

	@Autowired
	UserService userService;

	// for index page
	@RequestMapping("/")
	public String homePage() {
		return "index";
	}

	// for user profile page
	@GetMapping("/page/login")
	public ModelAndView getLoginPage(ModelAndView modelAndView) {
		modelAndView.setViewName("html/logins/login");
		return modelAndView;

	}

	@GetMapping("/page/usersList")
	public ModelAndView getUsersListPage(ModelAndView modelAndView) {
		modelAndView.setViewName("html/logins/usersList");
		return modelAndView;

	}

	@GetMapping("/page/updateUser/{userId}")
	public ModelAndView getUserUpdatePage(ModelAndView modelAndView, @PathVariable("userId") long userId) {

		UserResponse currentUserDetails = userService.getUserDetails(userId);

		modelAndView.addObject("user", currentUserDetails);
		modelAndView.setViewName("html/logins/updateUser");

		return modelAndView;
	}

	// for user login page
	@GetMapping("/page/profile")
	public ModelAndView getProfilePage(ModelAndView modelAndView) {
		log.info("getProfilePage controller called ..");
		modelAndView.setViewName("html/logins/profile");
		return modelAndView;

	}

	// for user registration page
	@GetMapping("/page/registration")
	public ModelAndView getRegistrationPage(ModelAndView modelAndView) {
		log.info("registration controller invoked ..");
		modelAndView.setViewName("html/logins/registration");
		return modelAndView;

	}

	@PostMapping("/register")
	public ResponseEntity<ApiResponse<UserResponse>> createNewUser(@RequestBody UserRequest userRequest) {
		UserResponse userResponse = userService.createUser(userRequest);
		log.info("register controller invoked ..");
		if (userResponse != null) {
			return new ResponseEntity<ApiResponse<UserResponse>>(
					new ApiResponse<UserResponse>("success", "User Registered successfully", userResponse, 201),
					HttpStatus.CREATED);
		} else {
			return new ResponseEntity<ApiResponse<UserResponse>>(
					new ApiResponse<UserResponse>("Failed", "User Registeration Failed", userResponse, 400),
					HttpStatus.BAD_REQUEST);
		}

	}

	// get user login
	@SuppressWarnings("deprecation")
	@PostMapping("/login")
	public ResponseEntity<ApiResponse<LoginResponse>> userLogin(@RequestBody LoginRequest loginRequest, HttpSession session) {
		log.info("login controller invoked ..");

		 session.setAttribute("email", loginRequest.getEmail());
	        session.setAttribute("password", loginRequest.getPassword());
		
		Boolean isLoggedIn = userService.login(loginRequest.getEmail(), loginRequest.getPassword());
		if (isLoggedIn) {
			LoginResponse loginResponse = new LoginResponse("jwtToken will be passed",
					new Date(new java.util.Date().getDate()));
			log.info("login controller true.");
			return new ResponseEntity<ApiResponse<LoginResponse>>(
					new ApiResponse<>("success", "user logged successfully", loginResponse, 200), HttpStatus.OK);
		}
		log.info("login controller False.");
		return new ResponseEntity<ApiResponse<LoginResponse>>(
				new ApiResponse<>("Failed", "user login failed", null, 403), HttpStatus.FORBIDDEN);

	}

	// get all users list
	@GetMapping("/usersList")
	public ResponseEntity<ApiResponse<List<UserResponse>>> getAllUsers() {
		List<UserResponse> userResponse = userService.getAllUesrsList();
		if (userResponse != null) {
			return new ResponseEntity<ApiResponse<List<UserResponse>>>(
					new ApiResponse<List<UserResponse>>("success", "data fatched successfully", userResponse, 200),
					HttpStatus.OK);
		} else {
			return new ResponseEntity<ApiResponse<List<UserResponse>>>(
					new ApiResponse<List<UserResponse>>("Failed", "user records not found", userResponse, 404),
					HttpStatus.NO_CONTENT);
		}

	}

	// update the user details
	@PostMapping("/updateUser")
	public ResponseEntity<ApiResponse<UserResponse>> updateUser(@RequestBody UserRequest userRequest) {
		log.info("user request"+userRequest.toString());
		UserResponse udpatedUser = userService.updateUserDetails(userRequest);
		if (udpatedUser != null) {
			return new ResponseEntity<ApiResponse<UserResponse>>(
					new ApiResponse<UserResponse>("success", "user datails is updated successfully", udpatedUser, 200),
					HttpStatus.OK);
		} else {
			return new ResponseEntity<ApiResponse<UserResponse>>(new ApiResponse<UserResponse>("Failed",
					"failed to update or user does not eixts", udpatedUser, 404), HttpStatus.NO_CONTENT);
		}

	}

	// get single user details
	@GetMapping("/user")
	public ResponseEntity<ApiResponse<UserResponse>> getUser(@RequestParam("userId") String userId) {
		log.info("getUser controller invoked .." + userId);
		UserResponse userResponse = userService.getUserDetails(userId);

		if (userResponse.getEmailAddress() != null) {
			ApiResponse<UserResponse> apiResponse = new ApiResponse<UserResponse>("success",
					"user details fetched successfully", userResponse, 200);
			return new ResponseEntity<>(apiResponse, null, HttpStatus.OK);
		}
		ApiResponse<UserResponse> apiResponse = new ApiResponse<UserResponse>("Failed",
				" user details  failed to fetch~", null, 200);

		return new ResponseEntity<>(apiResponse, null, HttpStatus.NOT_FOUND);

	}
	
	
	@DeleteMapping("/deleteUser/{userId}")
	public ResponseEntity<ApiResponse<UserResponse>> deleteUser(@PathVariable("userId") long userId){
		log.info("userId deleteUser invoked .." + userId);
		Boolean isDeleted =  userService.deleteUserRecord(userId);
		if (isDeleted) {
			return new ResponseEntity<ApiResponse<UserResponse>>(
					new ApiResponse<UserResponse>("success", "user is deleted  successfully",null, 200),
					HttpStatus.OK);
		} else {
			return new ResponseEntity<ApiResponse<UserResponse>>(new ApiResponse<UserResponse>("Failed",
					"failed to delete the user", null, 404), HttpStatus.NO_CONTENT);
		}

	}

}
