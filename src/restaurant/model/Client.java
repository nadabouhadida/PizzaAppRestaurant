
package restaurant.model;

import java.io.Serializable;

public class Client extends Utilisateur implements Serializable {
    private String adresse;
    private String telephone;

    public Client(int id ,String login, String password, String nom, String prenom, String adresse, String telephone) {
        super(id,login, password, nom, prenom);
        this.adresse = adresse;
        this.telephone = telephone;
    }

    // Getters et setters spécifiques à Client
    public String getAdresse() { return adresse; }
    public void setAdresse(String adresse) { this.adresse = adresse; }
    public String getTelephone() { return telephone; }
    public void setTelephone(String telephone) { this.telephone = telephone; }
}
