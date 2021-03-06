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
import net.milanvit.iforum.models.Thread;
import net.milanvit.iforum.models.User;
import net.milanvit.iforum.models.ValidationErrors;

/**
 *
 * @author Milan
 */
@WebServlet (name = "CreateThread", urlPatterns = {"/secure/createThread"})
public class CreateThread extends HttpServlet {
	private String title;
	private String post;
	private User author;
	private Date created;
	private boolean locked;
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
		Thread thread = null;
		ThreadController threadController = new ThreadController ();

		parseValues (request);
		validateValues ();

		thread = new Thread (null, created, locked, post, title);
		thread.setAuthor (author);

		if (!validationErrors.isEmpty ()) {
			request.setAttribute ("validationErrors", validationErrors);
			request.getRequestDispatcher ("submissionerror.jsp").forward (request, response);
		} else {
			try {
				threadController.create (thread);

				response.sendRedirect ("index.jsp");
			} catch (Exception e) {
				Logger.getLogger (CreateThread.class.getName ()).log (Level.SEVERE, null, e);
			}
		}
	}

	private void parseValues (HttpServletRequest request) {
		title = request.getParameter ("title");
		post = request.getParameter ("post");
		author = (User) request.getSession ().getAttribute ("user");
		created = new Date ();
		locked = false;
	}

	private void validateValues () {
		if ((title.equals ("")) || (title == null)) {
			validationErrors.insertNewErrorMessage ("Title is empty!");
		}

		if ((post.equals ("")) || (post == null)) {
			validationErrors.insertNewErrorMessage ("Post is empty!");
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
		return "Short description";
	}// </editor-fold>
}
