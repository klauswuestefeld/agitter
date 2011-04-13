package agitter.server.mailer.templates;

public class ConviteAcessoTemplate extends MailTemplate {

	@Override
	public String getSubject() {
		return "Convite";
	}

	@Override
	protected String getBodyTemplate() {
		return "Seja um Agitador!<br /><br />"
				+ "[from_mail] convidou você para acompanhar os agitos dele."
				+ "Você pode visualizar os agitos de [from_mail] e criar seus próprios agitos acessando o link:<br />"
				+ "<a href=\"[app_link]/?token=[hash]\">[app_link]/?token=[hash]</a><br /><br />"
				+ "Bons agitos,<br />Equipe Vagaloom.";
	}

}
