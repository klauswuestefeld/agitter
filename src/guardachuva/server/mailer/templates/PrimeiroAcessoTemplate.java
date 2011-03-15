package guardachuva.server.mailer.templates;

public class PrimeiroAcessoTemplate extends MailTemplate {

	@Override
	public String getSubject() {
		return "Link de Acesso";
	}

	@Override
	protected String getBodyTemplate() {
		return "Seja bem vindo!<br /><br />"
				+ "Agora você já pode visualizar e criar seus próprios agitos acessando o link:<br />"
				+ "<a href=\"[app_link]/?token=[hash]\">[app_link]/?token=[hash]</a><br /><br />"
				+ "Bons agitos,<br />Equipe Vagaloom.";
	}

}
