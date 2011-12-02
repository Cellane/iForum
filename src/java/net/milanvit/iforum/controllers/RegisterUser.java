/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.milanvit.iforum.controllers;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.milanvit.iforum.helpers.Hash;
import net.milanvit.iforum.models.User;
import net.milanvit.iforum.models.ValidationErrors;

/**
 *
 * @author Milan
 */
@WebServlet (name = "RegisterUser", urlPatterns = {"/registerUser"})
public class RegisterUser extends HttpServlet {
	private String username;
	private String password;
	private String passwordAgain;
	private int age;
	private String avatar;
	private boolean sex;
	private ValidationErrors validationErrors = new ValidationErrors ();

	/** 
	 * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
	 * @param request servlet request
	 * @param response servlet response
	 * @throws ServletException if a servlet-specific error occurs
	 * @throws IOException if an I/O error occurs
	 */
	protected void processRequest (HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		User user = null;
		UserController userController = new UserController ();

		parseValues (request);
		validateValues ();

		if (!validationErrors.isEmpty ()) {
			request.setAttribute ("validationErrors", validationErrors);
			request.getRequestDispatcher ("registererror.jsp").forward (request, response);
		} else {
			user = new User (username, age, avatar, Hash.toSHA256 (password), sex);

			try {
				userController.create (user);

				response.sendRedirect ("index.jsp");
			} catch (Exception e) {
				validationErrors.insertNewErrorMessage ("There is disturbance in the force.");

				request.setAttribute ("validationErrors", validationErrors);
				request.getRequestDispatcher ("registererror.jsp").forward (request, response);
			}
		}
	}

	/**
	 * Validates values entered in registration form.
	 */
	private void validateValues () {
		if (username.isEmpty ()) {
			validationErrors.insertNewErrorMessage ("Username is empty!");
		}

		if (password.isEmpty ()) {
			validationErrors.insertNewErrorMessage ("Password is empty!");
		}

		if (!password.equals (passwordAgain)) {
			validationErrors.insertNewErrorMessage ("Passwords are not equal!");
		}

		Pattern pattern = Pattern.compile ("^https?://(?:[a-z\\-]+\\.)+[a-z]{2,6}(?:/[^/#?]+)+\\.(?:jpe?g|gif|png)$");
		Matcher matcher = pattern.matcher (avatar);

		if (!matcher.matches ()) {
			validationErrors.insertNewErrorMessage ("Avatar URL is not valid!");
		}
	}

	/**
	 * Parses the values from registration form.
	 * @param request servlet request
	 */
	private void parseValues (HttpServletRequest request) {
		username = request.getParameter ("username");
		password = request.getParameter ("password");
		passwordAgain = request.getParameter ("passwordagain");

		try {
			age = Integer.parseInt (request.getParameter ("age"));
		} catch (NumberFormatException nfe) {
			age = 0;
			validationErrors.insertNewErrorMessage ("Age is not a number!");
		}

		avatar = request.getParameter ("avatar");
		sex = (Integer.parseInt (request.getParameter ("sex")) == 1);
	}

	// <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
	/** 
	 * Handles the HTTP <code>GET</code> method.
	 * @param request servlet request
	 * @param response servlet response
	 * @throws ServletException if a servlet-specific error occurs
	 * @throws IOException if an I/O error occurs
	 */
	@Override
	protected void doGet (HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		processRequest (request, response);
	}

	/** 
	 * Handles the HTTP <code>POST</code> method.
	 * @param request servlet request
	 * @param response servlet response
	 * @throws ServletException if a servlet-specific error occurs
	 * @throws IOException if an I/O error occurs
	 */
	@Override
	protected void doPost (HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		processRequest (request, response);
	}

	/** 
	 * Returns a short description of the servlet.
	 * @return a String containing servlet description
	 */
	@Override
	public String getServletInfo () {
		return ("Short description");
	}// </editor-fold>
}
