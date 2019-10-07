package handler;

import java.util.HashMap;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

public class SessionCounterListener implements HttpSessionListener {

	private static int totalActiveSessions;
	private static HashMap<String, HttpSession> httpSessions;
	private static final Object lock = new Object();

	@Override
	public void sessionCreated(HttpSessionEvent httpSessionEvent) {

		synchronized (lock) {
			if (httpSessions == null)
				httpSessions = new HashMap<>();
			httpSessions.put(httpSessionEvent.getSession().getId(), httpSessionEvent.getSession());
			totalActiveSessions++;
		}

	}

	@Override
	public void sessionDestroyed(HttpSessionEvent httpSessionEvent) {

		synchronized (lock) {
			httpSessions.remove(httpSessionEvent.getSession().getId());
			totalActiveSessions--;
		}

	}

	public static int getTotalActiveSessions() {
		return totalActiveSessions;
	}

	public static HashMap<String, HttpSession> getHttpSessions() {
		return httpSessions;
	}

}
