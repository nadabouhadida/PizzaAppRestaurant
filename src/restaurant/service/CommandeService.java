package restaurant.service;

import restaurant.model.Commande;
import restaurant.model.Client;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class CommandeService {
    private static final String COMMANDES_FILE = "src/restaurant/data/commande.dat";
    
    // Sauvegarder toutes les commandes
    public void saveCommandes(List<Commande> commandes) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(COMMANDES_FILE))) {
            oos.writeObject(commandes);
        } catch (IOException e) {
            System.err.println("Erreur lors de la sauvegarde des commandes: " + e.getMessage());
        }
    }
    
    // Charger toutes les commandes
    @SuppressWarnings("unchecked")
    public List<Commande> loadCommandes() {
        File file = new File(COMMANDES_FILE);
        if (!file.exists()) {
            return new ArrayList<>();
        }
        
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (List<Commande>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Erreur lors du chargement des commandes: " + e.getMessage());
            return new ArrayList<>();
        }
    }
    
    // Sauvegarder une nouvelle commande (ajoute à la liste existante)
    public void saveCommande(Commande nouvelleCommande) {
        List<Commande> commandesExistantes = loadCommandes();
        commandesExistantes.add(nouvelleCommande);
        saveCommandes(commandesExistantes);
    }
    
    // Récupérer les commandes d'un client spécifique
    public List<Commande> getCommandesByClient(Client client) {
        List<Commande> toutesCommandes = loadCommandes();
        List<Commande> commandesClient = new ArrayList<>();
        
        for (Commande cmd : toutesCommandes) {
            if (cmd.getClient().getId() == client.getId()) {
                commandesClient.add(cmd);
            }
        }
        
        return commandesClient;
    }
    
 // Dans CommandeService.java, ajoutez cette méthode
    public void deleteCommande(String commandeId) {
        List<Commande> commandes = loadCommandes();
        commandes.removeIf(c -> c.getId().equals(commandeId));
        saveCommandes(commandes);
    }
}