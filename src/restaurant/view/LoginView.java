package restaurant.view;

import restaurant.RestaurantApp;
import restaurant.model.Client;
import restaurant.model.Gerant;
import restaurant.model.Utilisateur;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginView extends JPanel {
    private JTextField loginField;
    private JPasswordField passwordField;
    private RestaurantApp app;

    public LoginView(RestaurantApp app) {
        this.app = app;
        setLayout(new BorderLayout());
        setBackground(new Color(240, 240, 240));
        
        // Panel principal avec fond
        JPanel mainPanel = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                Color color1 = new Color(140, 30, 30);
                Color color2 = new Color(80, 10, 10);
                GradientPaint gp = new GradientPaint(0, 0, color1, getWidth(), getHeight(), color2);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        
        // Panel de formulaire
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setOpaque(false);
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Titre
        JLabel titleLabel = new JLabel("Connexion", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        formPanel.add(titleLabel, gbc);
        
        // Champ login
        JLabel loginLabel = new JLabel("Login:");
        loginLabel.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        formPanel.add(loginLabel, gbc);
        
        loginField = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 1;
        formPanel.add(loginField, gbc);
        
        // Champ mot de passe
        JLabel passwordLabel = new JLabel("Mot de passe:");
        passwordLabel.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(passwordLabel, gbc);
        
        passwordField = new JPasswordField(20);
        gbc.gridx = 1;
        gbc.gridy = 2;
        formPanel.add(passwordField, gbc);
        
        // Bouton de connexion
        JButton loginButton = new JButton("Se connecter");
        loginButton.setBackground(new Color(140, 30, 30));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFocusPainted(false);
        
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String login = loginField.getText();
                String password = new String(passwordField.getPassword());
                
                Utilisateur utilisateur = app.getAuthService().authentifier(login, password);
                
                if (utilisateur != null) {
                    // Stocker l'utilisateur connecté
                    if (utilisateur instanceof Client) {
                        app.setCurrentUser((Client) utilisateur);
                    }
                    
                    if (utilisateur instanceof Gerant) {
                        app.showGerantView();
                    } else if (utilisateur instanceof Client) {
                        app.showClientView();
                    }
                } else {
                    JOptionPane.showMessageDialog(LoginView.this, 
                            "Identifiants incorrects", 
                            "Erreur d'authentification", 
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.NONE;
        formPanel.add(loginButton, gbc);
        
        mainPanel.add(formPanel);
        add(mainPanel, BorderLayout.CENTER);
    }
}