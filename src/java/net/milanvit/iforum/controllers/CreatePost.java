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
import net.milanvit.iforum.models.Post;
import net.milanvit.iforum.models.Thread;
import net.milanvit.iforum.models.User;

/**
 *
 * @author Milan
 */
@WebServlet (name = "CreatePost", urlPatterns = {"/secure/createPost"})
public class CreatePost extends HttpServlet {
	private Date created;
	private String postText;
	private User author;
	private int threadId;
	
	/** 
	 * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
	 * @param request servlet request
	 * @param response servlet response
	 * @throws ServletException if a servlet-specific error occurs
	 * @throws IOException if an I/O error occurs
	 */
	protected void processRequest (HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Post post = null;
		PostController postController = new PostController ();
		Thread thread = null;
		ThreadController threadController = new ThreadController ();
		User user = null;
		UserController userController = new UserController ();
		
		parseVariables (request);
		
		thread = threadController.findThread (threadId);
		
		post = new Post ();
		post.setAuthor (author);
		post.setCreated (created);
		post.setPost (postText);
		post.setThread (thread);
		
		if (!thread.getLocked ()) {		
			try {
				postController.create (post);
			
				response.sendRedirect ("showthread.jsp?id=" + threadId);
			} catch (Exception e) {
				Logger.getLogger (CreatePost.class.getName()).log (Level.SEVERE, null, e);
			}
		} else {
			System.out.println ("Someone's hacking us! Halp!");
		}
	}

	private void parseVariables (HttpServletRequest request) throws NumberFormatException {
		postText = request.getParameter ("post");
		threadId = Integer.parseInt (request.getParameter ("thread"));
		author = (User) request.getSession ().getAttribute ("user");
		created = new Date ();
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
