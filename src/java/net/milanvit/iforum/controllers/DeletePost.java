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
import net.milanvit.iforum.models.Post;
import net.milanvit.iforum.models.User;

/**
 *
 * @author Milan
 */
@WebServlet (name = "DeletePost", urlPatterns = {"/secure/deletePost"})
public class DeletePost extends HttpServlet {
	/** 
	 * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
	 * @param request servlet request
	 * @param response servlet response
	 * @throws ServletException if a servlet-specific error occurs
	 * @throws IOException if an I/O error occurs
	 */
	protected void processRequest (HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		PostController postController = new PostController ();
		Post post = postController.findPost (Integer.parseInt (request.getParameter ("id")));
		User user = (User) request.getSession ().getAttribute ("user");
		int threadId = post.getThread ().getId ();

		if (post.getAuthor ().equals (user)) {
			try {
				postController.destroy (post.getId ());

				response.sendRedirect ("showthread.jsp?id=" + threadId);
			} catch (Exception e) {
				Logger.getLogger (DeletePost.class.getName ()).log (Level.SEVERE, null, e);
			}
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
		protected void doGet (HttpServletRequest request, HttpServletResponse response
		)
			throws ServletException
		, IOException {
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
		protected void doPost (HttpServletRequest request, HttpServletResponse response
		)
			throws ServletException
		, IOException {
			processRequest (request, response);
		}

		/** 
		 * Returns a short description of the servlet.
		 * @return a String containing servlet description
		 */
		@Override
		public String getServletInfo 
			() {
		return "Short description";
		}// </editor-fold>
	}
