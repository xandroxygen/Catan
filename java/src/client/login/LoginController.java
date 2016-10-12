package client.login;

import client.admin.GameAdministrator;
import client.base.*;
import client.misc.*;

import java.net.*;
import java.io.*;
import java.util.*;
import java.lang.reflect.*;

import client.model.InvalidActionException;
import client.model.Model;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;


/**
 * Implementation for the login controller
 */
public class LoginController extends Controller implements ILoginController, Observer {

	private IMessageView messageView;
	private IAction loginAction;
	
	/**
	 * LoginController constructor
	 * 
	 * @param view Login view
	 * @param messageView Message view (used to display error messages that occur during the login process)
	 */
	public LoginController(ILoginView view, IMessageView messageView) {

		super(view);
		
		this.messageView = messageView;
	}
	
	public ILoginView getLoginView() {
		
		return (ILoginView)super.getView();
	}
	
	public IMessageView getMessageView() {
		
		return messageView;
	}
	
	/**
	 * Sets the action to be executed when the user logs in
	 * 
	 * @param value The action to be executed when the user logs in
	 */
	public void setLoginAction(IAction value) {
		
		loginAction = value;
	}
	
	/**
	 * Returns the action to be executed when the user logs in
	 * 
	 * @return The action to be executed when the user logs in
	 */
	public IAction getLoginAction() {
		
		return loginAction;
	}

	@Override
	public void start() {
		
		getLoginView().showModal();
	}

	@Override
	public void signIn() {
		try {
            String username = getLoginView().getLoginUsername();
            String password = getLoginView().getLoginPassword();

            if (GameAdministrator.getInstance().canLogin(username, password)) {
                GameAdministrator.getInstance().login(username, password);
            }
            getLoginView().closeModal();
            loginAction.execute();
		}
		catch (InvalidActionException e) {
			getMessageView().showModal();
            getMessageView().setTitle("Error");
            getMessageView().setMessage("There was an error logging in:" + e.getMessage());
		}


	}

	@Override
	public void register() {
        try {
            String username = getLoginView().getLoginUsername();
            String password = getLoginView().getLoginPassword();

            if (GameAdministrator.getInstance().canRegister(username, password)) {
                GameAdministrator.getInstance().register(username, password);
            }
        }
        catch (Exception e) {
            getMessageView().showModal();
            getMessageView().setTitle("Error");
            getMessageView().setMessage("There was an error registering:" + e.getMessage());
        }

		getLoginView().closeModal();
		loginAction.execute();
	}

	/**
	 * This method is called whenever the observed object is changed. An
	 * application calls an <tt>Observable</tt> object's
	 * <code>notifyObservers</code> method to have all the object's
	 * observers notified of the change.
	 *
	 * @param o   the observable object.
	 * @param arg an argument passed to the <code>notifyObservers</code>
	 */
	@Override
	public void update(Observable o, Object arg) {
		// not needed
	}
}

