package restaurant;

import restaurant.service.AuthService;
import restaurant.model.*;
import restaurant.service.CommandeService;
import restaurant.view.ClientView;
import restaurant.view.GerantView;
import restaurant.view.LoginView;
import javax.swing.*;

public class RestaurantApp extends JFrame {
    private CommandeService commandeService;
    private AuthService authService;
    private Client currentUser; // Added this field to store the currently logged-in user
    
    public RestaurantApp() {
        // Initialisation des services
        this.commandeService = new CommandeService();
        this.authService = new AuthService();
        
        // Configuration de la fenêtre
        setTitle("Restaurant Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null);
        
        // Afficher la vue de login au démarrage
        showLoginView();
    }
    
    public void showLoginView() {
        getContentPane().removeAll();
        add(new LoginView(this));
        revalidate();
        repaint();
    }
    
    public void showClientView() {
        getContentPane().removeAll();
        if (currentUser != null && currentUser instanceof Client) {
            add(new ClientView(this, (Client) currentUser));
        } else {
            JOptionPane.showMessageDialog(this, 
                "Erreur: Aucun client connecté", 
                "Erreur", 
                JOptionPane.ERROR_MESSAGE);
            showLoginView();
            return;
        }
        revalidate();
        repaint();
    }
    
    public void showGerantView() {
        getContentPane().removeAll();
        add(new GerantView(this));
        revalidate();
        repaint();
    }
    
    public AuthService getAuthService() {
        return authService;
    }
    
    public CommandeService getCommandeService() {
        return commandeService;
    }
    
    // Add getter and setter for currentUser
    public Client getCurrentUser() {
        return currentUser;
    }
    
    public void setCurrentUser(Client user) {
        this.currentUser = user;
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            RestaurantApp app = new RestaurantApp();
            app.setVisible(true);
        });
    }
}