package samples.restlet;

public class User {

	private String nome;
	private int idade;

	public User(String nome, int idade) {
		this.setNome(nome);
		this.setIdade(idade);
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getNome() {
		return nome;
	}

	public void setIdade(int idade) {
		this.idade = idade;
	}

	public int getIdade() {
		return idade;
	}
	
}
