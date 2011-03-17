package samples.restlet;

public class User {

	private String _nome;
	private int _idade;

	public User(String nome, int idade) {
		this.setNome(nome);
		this.setIdade(idade);
	}

	public void setNome(String nome) {
		this._nome = nome;
	}

	public String getNome() {
		return _nome;
	}

	public void setIdade(int idade) {
		this._idade = idade;
	}

	public int getIdade() {
		return _idade;
	}
	
}
