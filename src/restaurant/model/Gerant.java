package restaurant.model;

import java.io.Serializable;

public class Gerant extends Utilisateur implements Serializable {
    public Gerant(int id, String login, String password, String nom, String prenom) {
        super(id,login, password, nom, prenom);
    }
}