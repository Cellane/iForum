/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.milanvit.iforum.controllers;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.milanvit.iforum.controllers.exceptions.IllegalOrphanException;
import net.milanvit.iforum.controllers.exceptions.NonexistentEntityException;
import net.milanvit.iforum.controllers.exceptions.RollbackFailureException;
import net.milanvit.iforum.models.Thread;
import net.milanvit.iforum.models.User;

/**
 *
 * @author Milan
 */
@WebServlet (name = "LockThread", urlPatterns = {"/secure/lockThread"})
public class LockThread extends HttpServlet {	
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
		ThreadController threadController = new ThreadController (null, null);
		UserController userController = new UserController (null, null);
		User user = null;
		int id = Integer.parseInt (request.getParameter ("id"));
		
		thread = threadController.findThread (id);
		user = userController.findUser ((String) request.getSession ().getAttribute ("username"));
		
		if (user.getUsername ().equals (thread.getAuthor ().getUsername ())) {
			thread.setLocked (!thread.getLocked ());
			
			try {
				threadController.edit (thread);
			} catch (Exception e) {
				Logger.getLogger (LockThread.class.getName()).log (Level.SEVERE, null, e);
			}
			
			response.sendRedirect ("showthread.jsp?id=" + id);
		} else {
			System.out.println ("Someone's hacking us! Halp!");
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
