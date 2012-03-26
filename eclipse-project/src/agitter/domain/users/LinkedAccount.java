package agitter.domain.users;

class LinkedAccount {
	final String oauthVerifier;
	final String oauthToken;
	final String userName;
	final String portal;
	final String imageProfile;
	final String email;
	
	LinkedAccount(String portal, String username, String oauthToken, String oauthVerifier, String imageProfile, String email) {
		if (portal == null) throw new IllegalArgumentException();
		this.portal = portal;
		this.userName = username;
		this.oauthToken = oauthToken;
		this.oauthVerifier = oauthVerifier;
		this.imageProfile = imageProfile;
		this.email = email;
	}
	
	@Override
	public String toString() {
		return "oVerifier: " + oauthVerifier + "\n"
			+ "oToken: " + oauthToken + "\n"
			+ "User Name: " + userName + "\n"
			+ "Portal: " + portal + "\n"
			+ "IMG: " + imageProfile + "\n"
			+ "E-mail: " + email;
	}
}