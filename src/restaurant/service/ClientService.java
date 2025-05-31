package restaurant.service;

import restaurant.model.Client;
import restaurant.model.Utilisateur;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ClientService {
    private static final String DATA_FILE = "src/restaurant/data/utilisateurs.dat";

    // Sauvegarder la liste des utilisateurs
    public void saveUsers(List<Utilisateur> users) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(DATA_FILE))) {
            oos.writeObject(users);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Charger la liste des utilisateurs
    public List<Utilisateur> loadUsers() {
        List<Utilisateur> users = new ArrayList<>();
        File file = new File(DATA_FILE);
        
        if (file.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                users = (List<Utilisateur>) ois.readObject();
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return users;
    }

    // Ajouter un nouveau client
    public void addClient(Client client) {
        List<Utilisateur> users = loadUsers();
        users.add(client);
        saveUsers(users);
    }

    // Vérifier si un login existe déjà
    public boolean loginExists(String login) {
        List<Utilisateur> users = loadUsers();
        return users.stream().anyMatch(u -> u.getLogin().equals(login));
    }

    // Récupérer tous les clients
    public List<Client> getAllClients() {
        List<Utilisateur> users = loadUsers();
        List<Client> clients = new ArrayList<>();
        
        for (Utilisateur user : users) {
            if (user instanceof Client) {
                clients.add((Client) user);
            }
        }
        return clients;
    }
    public boolean updateClientPassword(Client client, String currentPassword, String newPassword) {
        List<Utilisateur> users = loadUsers();
        
        // Vérifier que l'ancien mot de passe est correct
        if (!client.getPassword().equals(currentPassword)) {
            return false;
        }
        
        // Trouver le client dans la liste et mettre à jour son mot de passe
        for (Utilisateur user : users) {
            if (user instanceof Client && user.getId() == client.getId()) {
                user.setPassword(newPassword);
                break;
            }
        }
        
        // Sauvegarder les modifications
        saveUsers(users);
        return true;
    }
    
 
}