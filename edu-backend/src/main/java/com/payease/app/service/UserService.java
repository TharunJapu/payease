package com.payease.app.service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.payease.app.IDao.IGenericDao;
import com.payease.app.dao.UserDao;
import com.payease.app.helper.RequestObject;
import com.payease.app.helper.ResponseObject;
import com.payease.app.model.User;

@Service("userService")
public class UserService {

	@Autowired
	IGenericDao<User> genericDao;

	@Autowired
	UserDao userDao;
	
	@Autowired
	EmailService emailService;

	public User create(User user) {
		try {
			String randomPass = this.getAlphaNumericString(9);
			System.out.println("-----" + randomPass);
			user.setPassword(this.computeSHA512(randomPass));
			user.setUserName(this.getAlphaNumericString(12));
			user.setId(user.getUserName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return genericDao.create(user);
	}

	public User findOne(String id) {
		return genericDao.fineOne(id);
	}

	public List<User> getAll() {
		return genericDao.getAll();
	}

	public User update(User data) {
		return genericDao.update(data);
	}

	public List<User> findByData(RequestObject data) {
		return userDao.findByData(data);
	}

	private String getAlphaNumericString(int n) {
		String alphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ" + "0123456789" + "abcdefghijklmnopqrstuvwxyz";
		StringBuilder sb = new StringBuilder(n);
		SecureRandom random = new SecureRandom();

		for (int i = 0; i < n; i++) {
			int index = random.nextInt(alphaNumericString.length());
			sb.append(alphaNumericString.charAt(index));
		}
		return sb.toString();
	}

	private String computeSHA512(String data) throws Exception {
		MessageDigest digest = MessageDigest.getInstance("SHA-512");
		byte[] hash = digest.digest(data.getBytes(StandardCharsets.UTF_8));
		StringBuilder sb = new StringBuilder();
		for (byte b : hash) {
			sb.append(String.format("%02x", b));
		}
		return sb.toString();
	}

	public ResponseObject signupUser(User user) {
		ResponseObject responseObject = new ResponseObject();
		if (Boolean.TRUE.equals(user.getDistributeUser())) {
//			user.setDistributeId(this.getAlphaNumericString(8));
		}

		try {
			String randomPass = this.getAlphaNumericString(9);
			System.out.println("randompass :"+randomPass);
			user.setPlainPassword(randomPass);
			user.setUserName(this.getAlphaNumericString(12));
			user.setPassword(this.computeSHA512(randomPass));
			user.setDistributeUser(true);
			user.setId(user.getUserName());
			String body = "User Name : " +user.getUserName() + "\n" + "Password : "+randomPass;
			emailService.sendSimpleEmail(user.getEmailId(), "Distribute User Credintials", body);
		} catch (Exception e) {
			responseObject.setStatus(false);
			responseObject.setErrorMsg("Password encryption failed.");
			return responseObject;
		}
		User result = userDao.create(user);

		if (result != null) {
			responseObject.setObject(result);
			responseObject.setStatus(true);
		} else {
			responseObject.setObject(null);
			responseObject.setStatus(false);
			responseObject.setErrorMsg("User creation failed in database.");
		}

		return responseObject;
	}

	public ResponseObject signinUser(User user) {
		ResponseObject response = new ResponseObject();

		if (user.getUserName()==null || user.getPassword()==null) {
			return buildErrorResponse("Username and password must not be empty");
		}

		User existingUser = userDao.findByUserName(user.getUserName());
		if (existingUser == null) {
			return buildErrorResponse("User not found with the provided username");
		}

		try {
//			String encryptedInputPassword = computeSHA512(user.getPassword());
			if (!existingUser.getPassword().equals(user.getPassword())) {
				return buildErrorResponse("Incorrect username or password");
			}
		} catch (Exception e) {
			return buildErrorResponse("Password processing failed");
		}

		response.setStatus(true);
		response.setObject(existingUser);
		return response;
	}

	private ResponseObject buildErrorResponse(String message) {
		ResponseObject response = new ResponseObject();
		response.setStatus(false);
		response.setErrorMsg(message);
		response.setObject(null);
		return response;
	}
}
