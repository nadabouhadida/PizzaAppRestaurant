package restaurant.model;

import java.io.Serializable;

public abstract class Utilisateur implements Serializable {
    protected int id;  // Nouvel attribut
    protected String login;
    protected String password;
    protected String nom;
    protected String prenom;

    // Constructeur modifié pour inclure l'id
    public Utilisateur(int id, String login, String password, String nom, String prenom) {
        this.id = id;
        this.login = login;
        this.password = password;
        this.nom = nom;
        this.prenom = prenom;
    }

    public void setLogin(String login) {
		this.login = login;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}

	// Getters et setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getLogin() { return login; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getNom() { return nom; }
    public String getPrenom() { return prenom; }
}