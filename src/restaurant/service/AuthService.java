package restaurant.service;

import restaurant.model.Client;
import restaurant.model.Gerant;
import restaurant.model.Utilisateur;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class AuthService {
    private static final String USERS_FILE = "src/restaurant/data/utilisateurs.dat";
    private List<Utilisateur> utilisateurs;

    public AuthService() {
        this.utilisateurs = new ArrayList<>();
        chargerUtilisateurs();
        creerGerantParDefaut();
    }

    private void chargerUtilisateurs() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(USERS_FILE))) {
            utilisateurs = (List<Utilisateur>) ois.readObject();
        } catch (FileNotFoundException e) {
            // Fichier non trouvé, on le créera plus tard
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void sauvegarderUtilisateurs() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(USERS_FILE))) {
            oos.writeObject(utilisateurs);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void creerGerantParDefaut() {
        // Vérifier si un gérant existe déjà
        boolean gerantExiste = utilisateurs.stream()
                .anyMatch(u -> u instanceof Gerant);
        
        if (!gerantExiste) {
            // Créer le gérant par défaut
            Gerant admin = new Gerant(1, "admin", "admin123", "Admin", "System");
            utilisateurs.add(admin);
            sauvegarderUtilisateurs();
        }
    }

    public Utilisateur authentifier(String login, String password) {
        return utilisateurs.stream()
                .filter(u -> u.getLogin().equals(login) && u.getPassword().equals(password))
                .findFirst()
                .orElse(null);
    }

    public List<Utilisateur> getUtilisateurs() {
        return new ArrayList<>(utilisateurs);
    }
}