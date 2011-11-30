/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.milanvit.iforum.controllers;

import java.io.IOException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import net.milanvit.iforum.helpers.Hash;
import net.milanvit.iforum.models.User;
import net.milanvit.iforum.models.ValidationErrors;

/**
 *
 * @author Milan
 */
@WebServlet (name = "LoginUser", urlPatterns = {"/loginUser"})
public class LoginUser extends HttpServlet {
	private String username;
	private String password;
	private User user;
	private UserController userController = new UserController (null, null);
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
		HttpSession httpSession = request.getSession ();

		username = request.getParameter ("username");
		password = Hash.toSHA256 (request.getParameter ("password"));
		user = userController.findUser (username);

		validateValues ();

		if (!validationErrors.isEmpty ()) {
			request.setAttribute ("validationErrors", validationErrors);
			request.getRequestDispatcher ("indexerror.jsp").forward (request, response);
		} else {
			updateLoginCount (user);
			httpSession.setAttribute ("username", username);
			response.sendRedirect ("secure/index.jsp");
		}
	}

	/**
	 * Validates values entered in login form.
	 */
	private void validateValues () {
		if (username.isEmpty ()) {
			validationErrors.insertNewErrorMessage ("Username is empty!");
		}

		if (password != null && user != null) {
			if (!password.equals (user.getPassword ())) {
				validationErrors.insertNewErrorMessage ("Password is incorrect!");
			}
		} else {
			validationErrors.insertNewErrorMessage ("Password is empty!");
		}

		if (user == null) {
			validationErrors.insertNewErrorMessage ("User does not exist!");
		}
	}

	/**
	 * Adds number one to login count and updates last login date/time.
	 */
	private void updateLoginCount (User user) {
		Integer loginCount = user.getLoginCount ();
		Date loginLast = new Date ();

		loginCount = (loginCount == null) ? 1 : ++loginCount;
		user.setLoginCount (loginCount);
		user.setLoginLast (loginLast);
		
		try {
			userController.edit (user);
		} catch (Exception e) {
			Logger.getLogger (LoginUser.class.getName()).log (Level.SEVERE, null, e);
		}
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
