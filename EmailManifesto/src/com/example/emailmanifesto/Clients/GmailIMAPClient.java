package com.example.emailmanifesto.Clients;

import java.security.Provider;
import java.security.Security;
import java.util.Properties;

import javax.mail.Session;
import javax.mail.URLName;

import com.example.emailmanifesto.SupportClasses.OAuth2SaslClientFactory;
import com.sun.mail.imap.IMAPSSLStore;
import com.sun.mail.imap.IMAPStore;

public class GmailIMAPClient {

	public static final GmailIMAPClient instance = new GmailIMAPClient();
	public static final String MAIL_SERVER = "imap.gmail.com";
	public static final int PORT = 993;

	private GmailIMAPClient() {
		Security.addProvider(new OAuth2Provider());
	}

	/**
	 * Connects to the IMAP server with given credentials via OAUTH2
	 * 
	 * @param email
	 * 		user's email
	 * @param token
	 * 		OAUTH2 token to authenticate with
	 * @param debug
	 * 		enable debugging the connection if true
	 * @return
	 * 		IMAP store authenticated with passed in credentials
	 */
	public IMAPStore connectToImap(String email, String token, boolean debug)
			throws Exception {
		Properties props = new Properties();
		props.put("mail.imaps.sasl.enable", "true");
		props.put("mail.imaps.sasl.mechanisms", "XOAUTH2");
		props.put(OAuth2SaslClientFactory.OAUTH_TOKEN_PROP, token);
		Session session = Session.getInstance(props);
		session.setDebug(debug);

		final URLName unusedUrlName = null;
		IMAPSSLStore store = new IMAPSSLStore(session, unusedUrlName);
		// empty password forces OAUTH2 login
		store.connect(MAIL_SERVER, PORT, email, "");
		return store;
	}

	public static final class OAuth2Provider extends Provider {
		private static final long serialVersionUID = -1860612702191596794L;

		public OAuth2Provider() {
			super("Google OAuth2 Provider", 1.0,
					"Provides the XOAUTH2 SASL Mechanism");
			put("SaslClientFactory.XOAUTH2",
					"com.example.emailmanifesto.SupportClasses.OAuth2SaslClientFactory");
		}
	}
}
