/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.milanvit.iforum.models;

import java.util.ArrayList;

/**
 *
 * @author Milan
 */
public class ValidationErrors {
	public ArrayList<String> errorMessages;

	public ValidationErrors () {
		errorMessages = new ArrayList<String> ();
	}

	public ArrayList<String> getErrorMessages () {
		return (errorMessages);
	}

	public void setErrorMessages (ArrayList<String> errorMessages) {
		this.errorMessages = errorMessages;
	}
	
	public void insertNewErrorMessage (String message) {
		errorMessages.add (message);
	}
	
	public boolean isEmpty () {
		return (errorMessages.isEmpty ());
	}
	
	public void emptyMessages () {
		errorMessages.clear ();
	}
}
