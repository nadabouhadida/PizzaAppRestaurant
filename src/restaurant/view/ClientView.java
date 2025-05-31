package restaurant.view;

import restaurant.RestaurantApp;
import restaurant.model.EtatCommande;
import restaurant.service.*;
import restaurant.model.Commande;
import restaurant.model.Client;
import restaurant.model.Article;
import restaurant.model.ModeRecuperation;
import restaurant.view.components.ArticleCard;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.border.*;

public class ClientView extends JPanel {
    private RestaurantApp app;
    private Client client;
    private JPanel navbarPanel;
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private final Color NAVBAR_BG_COLOR = new Color(25, 25, 25);
    private final Color BUTTON_COLOR = new Color(140, 30, 30);
    private final Color BUTTON_HOVER_COLOR = new Color(180, 40, 40);
    
    private JPanel homePanel;
    private JPanel menuPanel;
    private JPanel commandesPanel;
    private JPanel comptePanel;
    
    
    private JPasswordField oldPassField;
    private JPasswordField newPassField;
    private JPasswordField confirmPassField;
    
    private List<Article> panier = new ArrayList<>();
    private List<Commande> historiqueCommandes = new ArrayList<>();
    private MenuService menuService;
    private List<Article> articles;

public ClientView(RestaurantApp app, Client client) {
        this.app = app;
        this.client = client;
        this.menuService = new MenuService();
        this.articles = menuService.loadArticles();
        
        // Charger les commandes du client
        CommandeService commandeService = new CommandeService();
        this.historiqueCommandes = commandeService.getCommandesByClient(client);
        
        setLayout(new BorderLayout());
        createNavbar();
        createMainPanel();
        
        add(navbarPanel, BorderLayout.WEST);
        add(mainPanel, BorderLayout.CENTER);
    }
    private void createNavbar() {
        navbarPanel = new JPanel();
        navbarPanel.setLayout(new BoxLayout(navbarPanel, BoxLayout.Y_AXIS));
        navbarPanel.setBackground(NAVBAR_BG_COLOR);
        navbarPanel.setPreferredSize(new Dimension(180, getHeight()));
        
        JPanel logoPanel = new JPanel(new BorderLayout());
        logoPanel.setPreferredSize(new Dimension(180, 120));
        logoPanel.setMaximumSize(new Dimension(180, 120));
        logoPanel.setBackground(NAVBAR_BG_COLOR);
        
        try {
            ImageIcon logoIcon = new ImageIcon("src/ressources/logo.png");
            Image logoImage = logoIcon.getImage();
            Image resizedLogo = logoImage.getScaledInstance(90, 70, Image.SCALE_SMOOTH);
            JLabel logoLabel = new JLabel(new ImageIcon(resizedLogo));
            logoLabel.setHorizontalAlignment(JLabel.CENTER);
            logoPanel.add(logoLabel, BorderLayout.CENTER);
        } catch (Exception e) {
            JLabel logoLabel = new JLabel("PIZZA");
            logoLabel.setFont(new Font("Arial", Font.BOLD, 24));
            logoLabel.setForeground(Color.WHITE);
            logoLabel.setHorizontalAlignment(JLabel.CENTER);
            logoPanel.add(logoLabel, BorderLayout.CENTER);
            System.err.println("Erreur logo: " + e.getMessage());
        }
        
        Font buttonFont = new Font("Arial", Font.BOLD, 14);
        Dimension buttonSize = new Dimension(170, 45);
        
        JButton homeBtn = createNavButton("Accueil", buttonFont, buttonSize);
        homeBtn.addActionListener(e -> cardLayout.show(mainPanel, "HOME"));
        
        JButton menuBtn = createNavButton("Menu", buttonFont, buttonSize);
        menuBtn.addActionListener(e -> cardLayout.show(mainPanel, "MENU"));
        
        JButton commandesBtn = createNavButton("Commandes", buttonFont, buttonSize);
        commandesBtn.addActionListener(e -> cardLayout.show(mainPanel, "COMMANDES"));
        
        JButton compteBtn = createNavButton("Compte", buttonFont, buttonSize);
        compteBtn.addActionListener(e -> cardLayout.show(mainPanel, "COMPTE"));
        
     
        JButton logoutBtn = createNavButton("Déconnexion", buttonFont, buttonSize);
        logoutBtn.addActionListener(e -> {
            // Vider le panier avant de se déconnecter
            panier.clear();
            // Retourner à la vue de login
            app.showLoginView();
        });
        
        navbarPanel.add(logoPanel);
        navbarPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        navbarPanel.add(homeBtn);
        navbarPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        navbarPanel.add(menuBtn);
        navbarPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        navbarPanel.add(commandesBtn);
        navbarPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        navbarPanel.add(compteBtn);
        navbarPanel.add(Box.createVerticalGlue());
        navbarPanel.add(logoutBtn);
        navbarPanel.add(Box.createRigidArea(new Dimension(0, 20)));
    }
    
    private JButton createNavButton(String text, Font font, Dimension size) {
        JButton button = new JButton(text);
        button.setFont(font);
        button.setPreferredSize(size);
        button.setMaximumSize(size);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setForeground(Color.WHITE);
        button.setBackground(BUTTON_COLOR);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setBackground(BUTTON_HOVER_COLOR);
            }
            public void mouseExited(MouseEvent e) {
                button.setBackground(BUTTON_COLOR);
            }
        });
        
        return button;
    }
    
    private void createMainPanel() {
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        
        createHomePanel();
        createMenuPanel();
        createCommandesPanel();
        createComptePanel();
        
        mainPanel.add(homePanel, "HOME");
        mainPanel.add(menuPanel, "MENU");
        mainPanel.add(commandesPanel, "COMMANDES");
        mainPanel.add(comptePanel, "COMPTE");
    }
    
    private void createHomePanel() {
        homePanel = new JBackgroundPanel("src/ressources/home page.png");
        homePanel.setLayout(new BorderLayout());
        
        JLabel welcomeLabel = new JLabel("Bienvenue Client! ");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 28));
        welcomeLabel.setForeground(Color.WHITE);
        welcomeLabel.setHorizontalAlignment(JLabel.CENTER);
        
        JPanel textBgPanel = new JPanel();
        textBgPanel.setBackground(new Color(0, 0, 0, 150));
        textBgPanel.setLayout(new BorderLayout());
        textBgPanel.add(welcomeLabel);
        
        JPanel welcomePanel = new JPanel(new BorderLayout());
        welcomePanel.setOpaque(false);
        welcomePanel.add(textBgPanel, BorderLayout.CENTER);
        
        JButton orderButton = new JButton("Commander maintenant");
        orderButton.setFont(new Font("Arial", Font.BOLD, 16));
        orderButton.setForeground(Color.WHITE);
        orderButton.setBackground(BUTTON_COLOR);
        orderButton.setBorderPainted(false);
        orderButton.setFocusPainted(false);
        orderButton.addActionListener(e -> cardLayout.show(mainPanel, "MENU"));
        
        JPanel centerPanel = new JPanel();
        centerPanel.setOpaque(false);
        centerPanel.add(orderButton);
        
        homePanel.add(welcomePanel, BorderLayout.SOUTH);
        homePanel.add(centerPanel, BorderLayout.CENTER);
    }
    
    private void createMenuPanel() {
        menuPanel = new JPanel(new BorderLayout());
        menuPanel.setBackground(new Color(140, 50, 60));
        
        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(new Color(120, 30, 40));
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));
        
        JLabel titleLabel = new JLabel("Nos Pizzas", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 36));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        
        titlePanel.add(titleLabel);
        
        JPanel cardsContainer = new JPanel();
        cardsContainer.setLayout(new FlowLayout(FlowLayout.CENTER, 30, 20));
        cardsContainer.setBackground(new Color(140, 50, 60));
        cardsContainer.setBorder(BorderFactory.createEmptyBorder(10, 10, 20, 10));

        // Utilisation des articles chargés depuis le fichier
        for (Article article : articles) {
            ArticleCard card = new ArticleCard(article, e -> {
                int quantity = article.getQuantity();
                double prixTotal = article.getPrixDT() * quantity;
                
                for (int i = 0; i < quantity; i++) {
                    Article articleToAdd = new Article(
                        article.getId(), 
                        article.getNom(), 
                        article.getDescription(), 
                        article.getPrixDT()
                    );
                    panier.add(articleToAdd);
                }
                
                String message = String.format(
                    "%d x %s ajouté au panier - Total panier: %.3f DT",
                    quantity, article.getNom(), calculerTotalPanier()
                );
                JOptionPane.showMessageDialog(this, message, "Ajout au panier", JOptionPane.INFORMATION_MESSAGE);
            });
            cardsContainer.add(card);
        }
        
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.setBackground(new Color(120, 30, 40));
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        
        JButton nextButton = new JButton("Valider le panier");
        nextButton.setFont(new Font("Arial", Font.BOLD, 16));
        nextButton.setForeground(Color.WHITE);
        nextButton.setBackground(new Color(30, 30, 30));
        nextButton.setBorderPainted(false);
        nextButton.setFocusPainted(false);
        nextButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        nextButton.addActionListener(e -> {
            if (panier.isEmpty()) {
                JOptionPane.showMessageDialog(this, 
                    "Votre panier est vide. Veuillez ajouter des articles.", 
                    "Panier vide", 
                    JOptionPane.WARNING_MESSAGE);
            } else {
                afficherDialogRecuperation();
            }
        });
        
        JButton viewCartButton = new JButton("Voir le panier");
        viewCartButton.setFont(new Font("Arial", Font.BOLD, 16));
        viewCartButton.setForeground(Color.WHITE);
        viewCartButton.setBackground(new Color(30, 30, 30));
        viewCartButton.setBorderPainted(false);
        viewCartButton.setFocusPainted(false);
        viewCartButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        viewCartButton.addActionListener(e -> afficherPanier());
        
        bottomPanel.add(viewCartButton);
        bottomPanel.add(Box.createRigidArea(new Dimension(20, 0)));
        bottomPanel.add(nextButton);
        viewCartButton.setPreferredSize(nextButton.getPreferredSize()); 
        
        JScrollPane scrollPane = new JScrollPane(cardsContainer);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        
        menuPanel.add(titlePanel, BorderLayout.NORTH);
        menuPanel.add(scrollPane, BorderLayout.CENTER);
        menuPanel.add(bottomPanel, BorderLayout.SOUTH);
    }
    
    private double calculerTotalPanier() {
        double total = 0.0;
        for (Article article : panier) {
            total += article.getPrixDT();
        }
        return total;
    }
    
    private void afficherPanier() {
        if (panier.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Votre panier est vide.", 
                "Panier", 
                JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        StringBuilder panierContent = new StringBuilder("<html><body>");
        panierContent.append("<h2>Votre Panier</h2>");
        panierContent.append("<table>");
        
        java.util.Map<String, Integer> articlesCount = new java.util.HashMap<>();
        java.util.Map<String, Double> articlesPrix = new java.util.HashMap<>();
        
        for (Article article : panier) {
            String key = article.getNom();
            articlesCount.put(key, articlesCount.getOrDefault(key, 0) + 1);
            articlesPrix.put(key, article.getPrixDT());
        }
        
        for (String articleName : articlesCount.keySet()) {
            int count = articlesCount.get(articleName);
            double prixUnitaire = articlesPrix.get(articleName);
            double prixTotal = count * prixUnitaire;
            
            panierContent.append("<tr>");
            panierContent.append("<td>").append(count).append(" x ").append(articleName).append("</td>");
            panierContent.append("<td align='right'>").append(String.format("%.2f DT", prixTotal)).append("</td>");
            panierContent.append("</tr>");
        }
        
        panierContent.append("<tr><td colspan='2'><hr></td></tr>");
        panierContent.append("<tr>");
        panierContent.append("<td><b>TOTAL</b></td>");
        panierContent.append("<td align='right'><b>").append(String.format("%.2f DT", calculerTotalPanier())).append("</b></td>");
        panierContent.append("</tr>");
        panierContent.append("</table>");
        panierContent.append("</body></html>");
        
        Object[] options = {"Fermer", "Vider le panier"};
        int choice = JOptionPane.showOptionDialog(
            this,
            new JLabel(panierContent.toString()),
            "Votre Panier",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.INFORMATION_MESSAGE,
            null,
            options,
            options[0]
        );
        
        if (choice == 1) {
            panier.clear();
            JOptionPane.showMessageDialog(this, 
                "Votre panier a été vidé.", 
                "Panier vidé", 
                JOptionPane.INFORMATION_MESSAGE);
        }
    }
    private void afficherDialogRecuperation() {
        // Création de la boîte de dialogue avec style moderne et thème bordeaux
        JDialog dialog = new JDialog();
        dialog.setTitle("Mode de récupération");
        dialog.setSize(550, 450);
        dialog.setLocationRelativeTo(this);
        dialog.setModal(true);
        dialog.setLayout(new BorderLayout());
        
        // Définition des couleurs du thème bordeaux
        Color bordeauxFonce = new Color(120, 30, 40);    // Bordeaux foncé pour les titres
        Color bordeauxMoyen = new Color(140, 50, 60);    // Bordeaux moyen pour les panels
        Color bordeauxClair = new Color(160, 70, 80);    // Bordeaux clair pour les accents
        Color beigeCreme = new Color(250, 245, 240);     // Beige crème pour le fond
        Color orDore = new Color(212, 175, 55);          // Or doré pour les éléments de mise en valeur
        
        // Panel de titre avec gradient et effet de biseautage
        JPanel titlePanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Gradient diagonal pour un effet plus élégant
                GradientPaint gp = new GradientPaint(
                    0, 0, bordeauxFonce, 
                    getWidth(), getHeight(), bordeauxMoyen
                );
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
                
                // Ligne dorée fine en bas du titre pour un effet luxueux
                g2d.setColor(orDore);
                g2d.fillRect(20, getHeight() - 2, getWidth() - 40, 2);
            }
        };
        titlePanel.setPreferredSize(new Dimension(550, 80));
        titlePanel.setLayout(new GridBagLayout()); // Pour centrer parfaitement
        
        // Titre élégant
        JLabel titleLabel = new JLabel("Choisissez votre mode de récupération");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        
       
        
        titlePanel.add(titleLabel);
        
        // Panel principal avec design élégant
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(beigeCreme);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(25, 35, 25, 35));
        
        ButtonGroup group = new ButtonGroup();
        
        // Style commun pour les options avec bords arrondis et effet d'ombre
        Border optionBorder = BorderFactory.createCompoundBorder(
            new SoftBevelBorder(SoftBevelBorder.RAISED),
            BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(bordeauxClair, 1, true),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
            )
        );
        
        // Option Livraison - Carte stylisée
        JPanel livPanel = createOptionPanel("Livraison à domicile", optionBorder, beigeCreme, bordeauxMoyen);
        JRadioButton optionLivraison = new JRadioButton();
        optionLivraison.setBackground(beigeCreme);
        group.add(optionLivraison);
        
        // Panel pour l'adresse
        JPanel adressePanel = new JPanel();
        adressePanel.setLayout(new BoxLayout(adressePanel, BoxLayout.Y_AXIS));
        adressePanel.setBackground(beigeCreme);
        adressePanel.setBorder(BorderFactory.createEmptyBorder(10, 25, 0, 0));
        adressePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel adresseLabel = new JLabel("Adresse de livraison:");
        adresseLabel.setFont(new Font("Arial", Font.BOLD, 14));
        adresseLabel.setForeground(bordeauxFonce);
        adresseLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JTextArea adresseArea = new JTextArea(3, 20);
        adresseArea.setLineWrap(true);
        adresseArea.setWrapStyleWord(true);
        adresseArea.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(bordeauxClair, 1, true),
            BorderFactory.createEmptyBorder(8, 8, 8, 8)
        ));
        adresseArea.setFont(new Font("Arial", Font.PLAIN, 14));
        adresseArea.setBackground(new Color(253, 250, 247)); // Beige très clair pour le champ texte
        adresseArea.setText(client != null ? client.getAdresse() : "");
        
        // Wrap dans un JScrollPane avec style
        JScrollPane scrollPane = new JScrollPane(adresseArea);
        scrollPane.setAlignmentX(Component.LEFT_ALIGNMENT);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        
        adressePanel.add(adresseLabel);
        adressePanel.add(Box.createVerticalStrut(5));
        adressePanel.add(scrollPane);
        
        // Ajout à l'option de livraison
        JPanel livContentPanel = new JPanel();
        livContentPanel.setLayout(new BoxLayout(livContentPanel, BoxLayout.X_AXIS));
        livContentPanel.setBackground(beigeCreme);
        livContentPanel.add(optionLivraison);
        livContentPanel.add(Box.createHorizontalStrut(10));
        
        JLabel livLabel = new JLabel("Livraison à domicile");
        livLabel.setFont(new Font("Arial", Font.BOLD, 16));
        livLabel.setForeground(bordeauxFonce);
        livContentPanel.add(livLabel);
        
        livPanel.add(livContentPanel, BorderLayout.NORTH);
        livPanel.add(adressePanel, BorderLayout.CENTER);
        
        // Option Sur place - Carte stylisée
        JPanel surPlacePanel = createOptionPanel("Sur place", optionBorder, beigeCreme, bordeauxMoyen);
        JRadioButton optionSurPlace = new JRadioButton();
        optionSurPlace.setBackground(beigeCreme);
        group.add(optionSurPlace);
        
        JPanel surPlaceContentPanel = new JPanel();
        surPlaceContentPanel.setLayout(new BoxLayout(surPlaceContentPanel, BoxLayout.X_AXIS));
        surPlaceContentPanel.setBackground(beigeCreme);
        surPlaceContentPanel.add(optionSurPlace);
        surPlaceContentPanel.add(Box.createHorizontalStrut(10));
        
        JLabel surPlaceLabel = new JLabel("Sur place (consommation au restaurant)");
        surPlaceLabel.setFont(new Font("Arial", Font.BOLD, 16));
        surPlaceLabel.setForeground(bordeauxFonce);
        surPlaceContentPanel.add(surPlaceLabel);
        
        surPlacePanel.add(surPlaceContentPanel, BorderLayout.CENTER);
        
        // Option À emporter - Carte stylisée
        JPanel emporterPanel = createOptionPanel("À emporter", optionBorder, beigeCreme, bordeauxMoyen);
        JRadioButton optionEmporter = new JRadioButton();
        optionEmporter.setBackground(beigeCreme);
        group.add(optionEmporter);
        
        JPanel emporterContentPanel = new JPanel();
        emporterContentPanel.setLayout(new BoxLayout(emporterContentPanel, BoxLayout.X_AXIS));
        emporterContentPanel.setBackground(beigeCreme);
        emporterContentPanel.add(optionEmporter);
        emporterContentPanel.add(Box.createHorizontalStrut(10));
        
        JLabel emporterLabel = new JLabel("À emporter (récupération au restaurant)");
        emporterLabel.setFont(new Font("Arial", Font.BOLD, 16));
        emporterLabel.setForeground(bordeauxFonce);
        emporterContentPanel.add(emporterLabel);
        
        emporterPanel.add(emporterContentPanel, BorderLayout.CENTER);
        
        // Sélection par défaut
        optionLivraison.setSelected(true);
        
        // Boutons stylisés
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(beigeCreme);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 0));
        
        JButton cancelButton = createStyledButton("Annuler", new Color(220, 220, 220), bordeauxFonce);
        cancelButton.addActionListener(e -> dialog.dispose());
        
        JButton confirmButton = createStyledButton("Valider", bordeauxMoyen, Color.WHITE);
        confirmButton.addActionListener(e -> {
            ModeRecuperation mode;
            String adresseLivraison = null;
            
            if (optionLivraison.isSelected()) {
                mode = ModeRecuperation.LIVRAISON;
                adresseLivraison = adresseArea.getText().trim();
                if (adresseLivraison.isEmpty()) {
                    showErrorDialog(dialog, "Veuillez saisir votre adresse de livraison.", "Adresse manquante");
                    return;
                }
            } else if (optionSurPlace.isSelected()) {
                mode = ModeRecuperation.SUR_PLACE;
            } else {
                mode = ModeRecuperation.A_EMPORTER;
            }
            
            dialog.dispose();
            confirmerCommande(mode, adresseLivraison);
        });
        
        buttonPanel.add(cancelButton);
        buttonPanel.add(confirmButton);
        
        // Ajout des composants au panel principal avec espacement
        mainPanel.add(livPanel);
        mainPanel.add(Box.createVerticalStrut(15));
        mainPanel.add(surPlacePanel);
        mainPanel.add(Box.createVerticalStrut(15));
        mainPanel.add(emporterPanel);
        mainPanel.add(Box.createVerticalGlue());
        mainPanel.add(buttonPanel);
        
        // Ajout des panels à la dialog
        dialog.add(titlePanel, BorderLayout.NORTH);
        dialog.add(mainPanel, BorderLayout.CENTER);
        
        // Rendre la boîte de dialogue visible
        dialog.setVisible(true);
    }

    // Méthode utilitaire pour créer un panel d'option stylisé comme une carte
    private JPanel createOptionPanel(String title, Border border, Color bgColor, Color hoverColor) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(bgColor);
        panel.setBorder(border);
        panel.setMaximumSize(new Dimension(2000, 150));
        
        // Animation de survol avec effet élégant
        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                // Subtile teinte dorée sur survol
                panel.setBackground(new Color(253, 248, 240));
                panel.setBorder(BorderFactory.createCompoundBorder(
                    new SoftBevelBorder(SoftBevelBorder.RAISED),
                    BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(hoverColor, 2, true),
                        BorderFactory.createEmptyBorder(14, 14, 14, 14)
                    )
                ));
                panel.repaint();
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                panel.setBackground(bgColor);
                panel.setBorder(border);
                panel.repaint();
            }
        });
        
        return panel;
    }

    // Méthode utilitaire pour créer un bouton stylisé dans le thème
    private JButton createStyledButton(String text, Color bgColor, Color fgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setBackground(bgColor);
        button.setForeground(fgColor);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
            new SoftBevelBorder(SoftBevelBorder.RAISED),
            BorderFactory.createEmptyBorder(8, 20, 8, 20)
        ));
        
        // Animation de survol élégante
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                // Transition subtile sur survol
                if (text.equals("Valider")) {
                    // Pour le bouton Valider: effet plus brillant
                    button.setBackground(new Color(
                        Math.min(bgColor.getRed() + 20, 255),
                        Math.min(bgColor.getGreen() + 15, 255),
                        Math.min(bgColor.getBlue() + 15, 255)
                    ));
                } else {
                    // Pour Annuler: effet plus subtil
                    button.setBackground(new Color(230, 230, 230));
                }
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(bgColor);
            }
        });
        
        return button;
    }

    // Méthode pour afficher une boîte de dialogue d'erreur stylisée
    private void showErrorDialog(Component parent, String message, String title) {
        // Création d'un panel personnalisé pour le message
        JPanel messagePanel = new JPanel(new BorderLayout(10, 10));
        messagePanel.setBackground(new Color(250, 245, 240));
        messagePanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        
        // Icône d'avertissement personnalisée
        JLabel iconLabel = new JLabel(UIManager.getIcon("OptionPane.warningIcon"));
        messagePanel.add(iconLabel, BorderLayout.WEST);
        
        // Message avec style
        JLabel messageLabel = new JLabel("<html><body><p style='font-family:Garamond;font-size:14pt'>" + message + "</p></body></html>");
        messagePanel.add(messageLabel, BorderLayout.CENTER);
        
        // Configuration du JOptionPane avec notre panel personnalisé
        JOptionPane optionPane = new JOptionPane(
            messagePanel,
            JOptionPane.PLAIN_MESSAGE
        );
        
        // Configuration du bouton OK
        JButton okButton = new JButton("OK");
        okButton.setFont(new Font("Arial", Font.BOLD, 14));
        okButton.setBackground(new Color(140, 50, 60)); // bordeauxMoyen
        okButton.setForeground(Color.WHITE);
        okButton.setFocusPainted(false);
        okButton.setBorder(BorderFactory.createCompoundBorder(
            new SoftBevelBorder(SoftBevelBorder.RAISED),
            BorderFactory.createEmptyBorder(5, 15, 5, 15)
        ));
        
        optionPane.setOptions(new Object[]{okButton});
        
        // Création et configuration de la boîte de dialogue
        JDialog dialog = optionPane.createDialog(parent, title);
        dialog.setLocationRelativeTo(parent);
        dialog.setVisible(true);
    }
    
  
    private void refreshCommandesList() {
        // Recharger les commandes depuis le fichier
        CommandeService commandeService = new CommandeService();
        this.historiqueCommandes = commandeService.getCommandesByClient(client);
        
        // Recrée le panel avec les commandes mises à jour
        createCommandesPanel();
        
        // Remplace l'ancien panel dans le mainPanel
        mainPanel.remove(commandesPanel);
        mainPanel.add(commandesPanel, "COMMANDES");
        
        // Rafraîchit l'affichage
        mainPanel.revalidate();
        mainPanel.repaint();
        
        // Affiche le panel des commandes
        cardLayout.show(mainPanel, "COMMANDES");
    }
    
    private void createCommandesPanel() {
        // Recharger les commandes depuis le fichier
        CommandeService commandeService = new CommandeService();
        this.historiqueCommandes = commandeService.getCommandesByClient(client);
        
        commandesPanel = new JPanel(new BorderLayout());
        commandesPanel.setBackground(new Color(140, 50, 60));
        
        // Panel de titre
        JPanel titlePanel = new JPanel();
        titlePanel.setPreferredSize(new Dimension(Integer.MAX_VALUE, 80));
        titlePanel.setBackground(new Color(120, 30, 40));
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        
        JLabel titleLabel = new JLabel("MES COMMANDES", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        
        titlePanel.add(titleLabel);
        commandesPanel.add(titlePanel, BorderLayout.NORTH);
        
        // Panel des commandes
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        contentPanel.setBackground(new Color(140, 50, 60));
        
        if (historiqueCommandes.isEmpty()) {
            JLabel emptyLabel = new JLabel("Vous n'avez aucune commande", JLabel.CENTER);
            emptyLabel.setFont(new Font("Arial", Font.ITALIC, 18));
            emptyLabel.setForeground(Color.WHITE);
            contentPanel.add(emptyLabel);
        } else {
            for (Commande commande : historiqueCommandes) {
                contentPanel.add(createCommandeCard(commande));
                contentPanel.add(Box.createRigidArea(new Dimension(0, 20)));
            }
        }
        
        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        commandesPanel.add(scrollPane, BorderLayout.CENTER);
    }

    private JPanel createCommandeCard(Commande commande) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(new Color(240, 240, 240, 230));
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200, 100)), 
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        
        // En-tête de commande
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(240, 240, 240, 0));
        
        JLabel idLabel = new JLabel("Commande #" + commande.getId());
        idLabel.setFont(new Font("Arial", Font.BOLD, 16));
        idLabel.setForeground(new Color(120, 30, 40));
        
        JLabel statusLabel = new JLabel("Statut: " + commande.getEtat().toString());
        statusLabel.setFont(new Font("Arial", Font.BOLD, 14));
        statusLabel.setForeground(commande.getEtat() == EtatCommande.LIVREE ? 
            new Color(0, 128, 0) : new Color(200, 100, 0));
        
        headerPanel.add(idLabel, BorderLayout.WEST);
        headerPanel.add(statusLabel, BorderLayout.EAST);
        card.add(headerPanel);
        card.add(Box.createRigidArea(new Dimension(0, 10)));
        
        // Liste des articles
        JPanel articlesPanel = new JPanel();
        articlesPanel.setLayout(new BoxLayout(articlesPanel, BoxLayout.Y_AXIS));
        
        for (Article article : commande.getArticles()) {
            JPanel articlePanel = new JPanel(new BorderLayout());
            articlePanel.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
            
            JLabel nameLabel = new JLabel("- " + article.getNom());
            nameLabel.setFont(new Font("Arial", Font.PLAIN, 14));
            
            JLabel priceLabel = new JLabel(String.format("%.2f DT", article.getPrixDT()));
            priceLabel.setFont(new Font("Arial", Font.BOLD, 14));
            
            articlePanel.add(nameLabel, BorderLayout.WEST);
            articlePanel.add(priceLabel, BorderLayout.EAST);
            articlesPanel.add(articlePanel);
        }
        
        card.add(articlesPanel);
        card.add(Box.createRigidArea(new Dimension(0, 15)));
        
        // Total et boutons
        JPanel footerPanel = new JPanel(new BorderLayout());
        footerPanel.setBackground(new Color(240, 240, 240, 0));
        
        JLabel totalLabel = new JLabel("Total: " + String.format("%.2f DT", commande.getTotal()));
        totalLabel.setFont(new Font("Arial", Font.BOLD, 16));
        totalLabel.setForeground(new Color(120, 30, 40));
        
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonsPanel.setBackground(new Color(240, 240, 240, 0));
        
        // Bouton Modifier (visible seulement si commande n'est pas livrée)
        if (commande.getEtat() != EtatCommande.LIVREE) {
            JButton editButton = new JButton("Modifier");
            styleButton(editButton, new Color(70, 130, 180)); // Bleu
            editButton.addActionListener(e -> {
                ouvrirDialogModification(commande);
            });
            buttonsPanel.add(editButton);
        }
        
        // Bouton Supprimer
        JButton deleteButton = new JButton("Supprimer");
        styleButton(deleteButton, new Color(220, 60, 60)); // Rouge
        deleteButton.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this,
                "Voulez-vous vraiment supprimer cette commande?",
                "Confirmation",
                JOptionPane.YES_NO_OPTION);
            
            if (confirm == JOptionPane.YES_OPTION) {
                // Supprimer de la liste locale
                historiqueCommandes.remove(commande);
                
                // Supprimer du fichier
                CommandeService commandeService = new CommandeService();
                List<Commande> toutesCommandes = commandeService.loadCommandes();
                toutesCommandes.removeIf(c -> c.getId().equals(commande.getId()));
                commandeService.saveCommandes(toutesCommandes);
                
                refreshCommandesList();
                JOptionPane.showMessageDialog(this, 
                    "Commande #" + commande.getId() + " supprimée", 
                    "Succès", 
                    JOptionPane.INFORMATION_MESSAGE);
            }
        });
        buttonsPanel.add(deleteButton);
        
        footerPanel.add(totalLabel, BorderLayout.WEST);
        footerPanel.add(buttonsPanel, BorderLayout.EAST);
        card.add(footerPanel);
        
        return card;
    }

    private void confirmerCommande(ModeRecuperation mode, String adresseLivraison) {
        String commandeId = "CMD" + System.currentTimeMillis() % 10000;
        Commande nouvelleCommande = new Commande(commandeId, client);
        
        for (Article article : panier) {
            nouvelleCommande.addArticle(article);
        }
        
        nouvelleCommande.setModeRecuperation(mode);
        
        if (mode == ModeRecuperation.LIVRAISON && adresseLivraison != null) {
            nouvelleCommande.setAdresseLivraison(adresseLivraison);
        }
        
        // Sauvegarde de la commande
        CommandeService commandeService = new CommandeService();
        commandeService.saveCommande(nouvelleCommande);
        
        // Ajout à l'historique local
        historiqueCommandes.add(nouvelleCommande);
        panier.clear();
        
        String modeText = mode == ModeRecuperation.LIVRAISON ? "Livraison à domicile" :
                         mode == ModeRecuperation.SUR_PLACE ? "Sur place" : "À emporter";
        
        StringBuilder message = new StringBuilder();
        message.append("<html><body>");
        message.append("<h2>Commande confirmée!</h2>");
        message.append("<p>Référence de commande: <b>").append(commandeId).append("</b></p>");
        message.append("<p>Client: <b>").append(client.getNom()).append(" ").append(client.getPrenom()).append("</b></p>");
        message.append("<p>Mode de récupération: <b>").append(modeText).append("</b></p>");
        if (mode == ModeRecuperation.LIVRAISON) {
            message.append("<p>Adresse de livraison: <b>").append(adresseLivraison).append("</b></p>");
        }
        message.append("<p>Total: <b>").append(String.format("%.2f DT", nouvelleCommande.getTotal())).append("</b></p>");
        message.append("</body></html>");
        
        JOptionPane.showMessageDialog(this,
            new JLabel(message.toString()),
            "Commande confirmée",
            JOptionPane.INFORMATION_MESSAGE);
        
        refreshCommandesList();
    }
 

    private void styleButton(JButton button, Color bgColor) {
        button.setFont(new Font("Arial", Font.BOLD, 12));
        button.setForeground(Color.WHITE);
        button.setBackground(bgColor);
        button.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        button.setFocusPainted(false);
        
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setBackground(brighter(bgColor, 1.2f));
            }
            public void mouseExited(MouseEvent e) {
                button.setBackground(bgColor);
            }
        });}
        
    private void ouvrirDialogModification(Commande commande) {
        JDialog dialog = new JDialog();
        dialog.setTitle("Modifier la commande #" + commande.getId());
        dialog.setSize(500, 450);
        dialog.setLocationRelativeTo(this);
        dialog.setModal(true);
        dialog.setLayout(new BorderLayout());
        
        // Panel de titre amélioré avec style bordeaux
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(new Color(120, 30, 40)); // Bordeaux foncé
        titlePanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        
        JLabel titleIcon = new JLabel(new ImageIcon("icons/edit_icon.png")); // Ajouter une icône ou utiliser une image placeholder
        titleIcon.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 10));
        
        JLabel titleLabel = new JLabel("Modifier la commande #" + commande.getId());
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(Color.WHITE);
        
        titlePanel.add(titleIcon, BorderLayout.WEST);
        titlePanel.add(titleLabel, BorderLayout.CENTER);
        dialog.add(titlePanel, BorderLayout.NORTH);
        
        // Panel principal avec fond légèrement teinté
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(new Color(250, 245, 245)); // Fond légèrement rosé
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        
        // Entête de liste avec style
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(240, 230, 230));
        headerPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(120, 30, 40)),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        
        JLabel articleHeader = new JLabel("Article");
        articleHeader.setFont(new Font("Segoe UI", Font.BOLD, 14));
        articleHeader.setForeground(new Color(120, 30, 40));
        
        JLabel qteHeader = new JLabel("Quantité");
        qteHeader.setFont(new Font("Segoe UI", Font.BOLD, 14));
        qteHeader.setForeground(new Color(120, 30, 40));
        
        headerPanel.add(articleHeader, BorderLayout.WEST);
        headerPanel.add(qteHeader, BorderLayout.EAST);
        mainPanel.add(headerPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        
        // Conteneur pour les articles avec alternance de couleurs
        JPanel articlesContainer = new JPanel();
        articlesContainer.setLayout(new BoxLayout(articlesContainer, BoxLayout.Y_AXIS));
        articlesContainer.setBackground(new Color(250, 245, 245));
        
        boolean alternate = true;
        for (Article article : commande.getArticles()) {
            JPanel articlePanel = new JPanel(new BorderLayout());
            articlePanel.setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 10));
            
            // Alternance de couleurs pour meilleure lisibilité
            if (alternate) {
                articlePanel.setBackground(new Color(245, 240, 240));
            } else {
                articlePanel.setBackground(new Color(255, 250, 250));
            }
            alternate = !alternate;
            
            // Partie gauche - Nom et prix avec style
            JPanel leftPanel = new JPanel(new BorderLayout());
            leftPanel.setOpaque(false);
            
            JLabel nameLabel = new JLabel(article.getNom());
            nameLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            nameLabel.setForeground(new Color(60, 60, 60));
            
            JLabel priceLabel = new JLabel(String.format("%.2f DT", article.getPrixDT()));
            priceLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
            priceLabel.setForeground(new Color(120, 30, 40));
            priceLabel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
            
            leftPanel.add(nameLabel, BorderLayout.WEST);
            leftPanel.add(priceLabel, BorderLayout.EAST);
            
            // Partie droite - Quantité et bouton
            JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
            rightPanel.setOpaque(false);
            
            JLabel qtyLabel = new JLabel("Qté:");
            qtyLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
            qtyLabel.setForeground(new Color(80, 80, 80));
            
            SpinnerNumberModel spinnerModel = new SpinnerNumberModel(1, 1, 10, 1);
            JSpinner quantitySpinner = new JSpinner(spinnerModel);
            quantitySpinner.setPreferredSize(new Dimension(60, 25));
            ((JSpinner.DefaultEditor) quantitySpinner.getEditor()).getTextField().setFont(new Font("Segoe UI", Font.PLAIN, 14));
            
            JButton removeButton = new JButton("Supprimer");
            removeButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
            removeButton.setForeground(Color.WHITE);
            removeButton.setBackground(new Color(180, 50, 60));
            removeButton.setBorder(BorderFactory.createEmptyBorder(6, 12, 6, 12));
            removeButton.setFocusPainted(false);
            removeButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
            
            removeButton.addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent e) {
                    removeButton.setBackground(new Color(200, 60, 70));
                }
                public void mouseExited(MouseEvent e) {
                    removeButton.setBackground(new Color(180, 50, 60));
                }
            });
            
            removeButton.addActionListener(e -> {
                commande.removeArticle(article);
                dialog.dispose();
                ouvrirDialogModification(commande);
            });
            
            rightPanel.add(qtyLabel);
            rightPanel.add(quantitySpinner);
            rightPanel.add(removeButton);
            
            articlePanel.add(leftPanel, BorderLayout.WEST);
            articlePanel.add(rightPanel, BorderLayout.EAST);
            articlesContainer.add(articlePanel);
            
            // Ajout d'un séparateur subtil entre les articles
            if (commande.getArticles().indexOf(article) < commande.getArticles().size() - 1) {
                JSeparator separator = new JSeparator(JSeparator.HORIZONTAL);
                separator.setForeground(new Color(220, 200, 200));
                separator.setBackground(new Color(250, 245, 245));
                articlesContainer.add(separator);
            }
        }
        
        JScrollPane scrollPane = new JScrollPane(articlesContainer);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        
        mainPanel.add(scrollPane);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        
        // Bouton ajout articles avec style amélioré
        JButton addButton = new JButton("+ Ajouter des articles");
        addButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        addButton.setForeground(Color.WHITE);
        addButton.setBackground(new Color(140, 50, 60));
        addButton.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        addButton.setFocusPainted(false);
        addButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        addButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        addButton.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                addButton.setBackground(new Color(160, 60, 70));
            }
            public void mouseExited(MouseEvent e) {
                addButton.setBackground(new Color(140, 50, 60));
            }
        });
        
        addButton.addActionListener(e -> ouvrirDialogAjoutArticles(commande, dialog));
        
        mainPanel.add(addButton);
        dialog.add(mainPanel, BorderLayout.CENTER);
        
        // Panel des boutons avec style bordeaux
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 10));
        buttonPanel.setBackground(new Color(240, 235, 235));
        buttonPanel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(200, 200, 200)));
        
        JButton cancelButton = new JButton("Annuler");
        cancelButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        cancelButton.setForeground(new Color(80, 80, 80));
        cancelButton.setBackground(new Color(230, 230, 230));
        cancelButton.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
        cancelButton.setFocusPainted(false);
        cancelButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        cancelButton.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                cancelButton.setBackground(new Color(220, 220, 220));
            }
            public void mouseExited(MouseEvent e) {
                cancelButton.setBackground(new Color(230, 230, 230));
            }
        });
        
        cancelButton.addActionListener(e -> dialog.dispose());
        
        JButton saveButton = new JButton("Enregistrer");
        saveButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        saveButton.setForeground(Color.WHITE);
        saveButton.setBackground(new Color(120, 30, 40));
        saveButton.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
        saveButton.setFocusPainted(false);
        saveButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        saveButton.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                saveButton.setBackground(new Color(140, 40, 50));
            }
            public void mouseExited(MouseEvent e) {
                saveButton.setBackground(new Color(120, 30, 40));
            }
        });
        
        saveButton.addActionListener(e -> {
            // Sauvegarder les modifications dans la liste locale
            historiqueCommandes.remove(commande);
            historiqueCommandes.add(commande);
            
            // Sauvegarder dans le fichier
            CommandeService commandeService = new CommandeService();
            List<Commande> toutesCommandes = commandeService.loadCommandes();
            
            // Retirer l'ancienne version de la commande
            toutesCommandes.removeIf(c -> c.getId().equals(commande.getId()));
            // Ajouter la version modifiée
            toutesCommandes.add(commande);
            
            // Sauvegarder
            commandeService.saveCommandes(toutesCommandes);
            
            JOptionPane.showMessageDialog(dialog, 
                "Modifications enregistrées avec succès!", 
                "Modifications enregistrées", 
                JOptionPane.INFORMATION_MESSAGE);
            dialog.dispose();
            refreshCommandesList();
        });
        
        buttonPanel.add(cancelButton);
        buttonPanel.add(saveButton);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        
        dialog.setVisible(true);
    }
    private void ouvrirDialogAjoutArticles(Commande commande, JDialog parentDialog) {
        JDialog dialog = new JDialog(parentDialog, "Ajouter des articles", true);
        dialog.setSize(550, 450);
        dialog.setLocationRelativeTo(parentDialog);
        dialog.setLayout(new BorderLayout());
        
        // Panel de titre amélioré
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(new Color(120, 30, 40));
        titlePanel.setBorder(BorderFactory.createEmptyBorder(12, 15, 12, 15));
        
        JLabel titleIcon = new JLabel(new ImageIcon("icons/add_icon.png"));
        titleIcon.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 10));
        
        JLabel titleLabel = new JLabel("Ajouter des articles à la commande");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(Color.WHITE);
        
        titlePanel.add(titleIcon, BorderLayout.WEST);
        titlePanel.add(titleLabel, BorderLayout.CENTER);
        dialog.add(titlePanel, BorderLayout.NORTH);
        
        // Panel principal avec style bordeaux
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBackground(new Color(250, 245, 245));
        
        // Entête de catalogue
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(240, 230, 230));
        headerPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(120, 30, 40)),
            BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
        
        JLabel catalogueLabel = new JLabel("Notre catalogue de pizzas");
        catalogueLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        catalogueLabel.setForeground(new Color(120, 30, 40));
        
        headerPanel.add(catalogueLabel, BorderLayout.WEST);
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        
        // Liste des articles disponibles avec style amélioré
        JPanel articlesPanel = new JPanel();
        articlesPanel.setLayout(new BoxLayout(articlesPanel, BoxLayout.Y_AXIS));
        articlesPanel.setBackground(new Color(250, 245, 245));
        articlesPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        // Utilisation des articles chargés depuis le fichier
        boolean alternate = true;
        for (Article article : articles) {
            JPanel articlePanel = new JPanel(new BorderLayout());
            articlePanel.setBorder(BorderFactory.createEmptyBorder(10, 5, 10, 5));
            
            // Alternance de couleurs pour meilleure lisibilité
            if (alternate) {
                articlePanel.setBackground(new Color(245, 240, 240));
            } else {
                articlePanel.setBackground(new Color(255, 250, 250));
            }
            alternate = !alternate;
            
            // Information sur l'article (gauche)
            JPanel infoPanel = new JPanel();
            infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
            infoPanel.setOpaque(false);
            infoPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
            
            JLabel nameLabel = new JLabel(article.getNom());
            nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
            nameLabel.setForeground(new Color(120, 30, 40));
            nameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            
            JLabel descLabel = new JLabel(article.getDescription());
            descLabel.setFont(new Font("Segoe UI", Font.ITALIC, 12));
            descLabel.setForeground(new Color(100, 100, 100));
            descLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            
            JLabel priceLabel = new JLabel(String.format("%.2f DT", article.getPrixDT()));
            priceLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
            priceLabel.setForeground(new Color(180, 50, 60));
            priceLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            priceLabel.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));
            
            infoPanel.add(nameLabel);
            infoPanel.add(Box.createRigidArea(new Dimension(0, 3)));
            infoPanel.add(descLabel);
            infoPanel.add(Box.createRigidArea(new Dimension(0, 3)));
            infoPanel.add(priceLabel);
            
            // Contrôles d'ajout (droite)
            JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
            controlPanel.setOpaque(false);
            
            JLabel qtyLabel = new JLabel("Quantité:");
            qtyLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
            qtyLabel.setForeground(new Color(80, 80, 80));
            
            SpinnerNumberModel spinnerModel = new SpinnerNumberModel(1, 1, 10, 1);
            JSpinner quantitySpinner = new JSpinner(spinnerModel);
            quantitySpinner.setPreferredSize(new Dimension(60, 30));
            ((JSpinner.DefaultEditor) quantitySpinner.getEditor()).getTextField().setFont(new Font("Segoe UI", Font.PLAIN, 14));
            
            JButton addButton = new JButton("Ajouter");
            addButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
            addButton.setForeground(Color.WHITE);
            addButton.setBackground(new Color(140, 50, 60));
            addButton.setBorder(BorderFactory.createEmptyBorder(6, 15, 6, 15));
            addButton.setFocusPainted(false);
            addButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
            
            addButton.addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent e) {
                    addButton.setBackground(new Color(160, 60, 70));
                }
                public void mouseExited(MouseEvent e) {
                    addButton.setBackground(new Color(140, 50, 60));
                }
            });
            
            addButton.addActionListener(e -> {
                int quantity = (int) quantitySpinner.getValue();
                for (int i = 0; i < quantity; i++) {
                    Article articleToAdd = new Article(
                        article.getId(), 
                        article.getNom(), 
                        article.getDescription(), 
                        article.getPrixDT()
                    );
                    commande.addArticle(articleToAdd);
                }
                
                // Sauvegarder immédiatement les modifications
                CommandeService commandeService = new CommandeService();
                List<Commande> toutesCommandes = commandeService.loadCommandes();
                
                // Retirer l'ancienne version de la commande
                toutesCommandes.removeIf(c -> c.getId().equals(commande.getId()));
                // Ajouter la version modifiée
                toutesCommandes.add(commande);
                
                // Sauvegarder
                commandeService.saveCommandes(toutesCommandes);
                
                // Confirmation plus élégante
                JPanel confirmPanel = new JPanel();
                confirmPanel.setLayout(new BoxLayout(confirmPanel, BoxLayout.Y_AXIS));
                confirmPanel.setBackground(new Color(250, 245, 245));
                confirmPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
                
                JLabel confirmLabel = new JLabel(quantity + " x " + article.getNom() + " ajouté(s) à la commande");
                confirmLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
                confirmLabel.setForeground(new Color(120, 30, 40));
                confirmLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                
                confirmPanel.add(confirmLabel);
                
                JOptionPane.showOptionDialog(
                    dialog,
                    confirmPanel,
                    "Article ajouté",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.INFORMATION_MESSAGE,
                    null,
                    new Object[]{"OK"},
                    "OK"
                );
            });
            
            controlPanel.add(qtyLabel);
            controlPanel.add(quantitySpinner);
            controlPanel.add(addButton);
            
            articlePanel.add(infoPanel, BorderLayout.WEST);
            articlePanel.add(controlPanel, BorderLayout.EAST);
            
            articlesPanel.add(articlePanel);
            
            // Ajout d'un séparateur subtil entre les articles
            if (articles.indexOf(article) < articles.size() - 1) {
                JSeparator separator = new JSeparator(JSeparator.HORIZONTAL);
                separator.setForeground(new Color(220, 200, 200));
                separator.setBackground(new Color(250, 245, 245));
                articlesPanel.add(separator);
            }
        }
        
        JScrollPane scrollPane = new JScrollPane(articlesPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        dialog.add(mainPanel, BorderLayout.CENTER);
        
        // Bouton Fermer avec style bordeaux
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 10));
        buttonPanel.setBackground(new Color(240, 235, 235));
        buttonPanel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(200, 200, 200)));
        
        JButton closeButton = new JButton("Fermer");
        closeButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        closeButton.setForeground(Color.WHITE);
        closeButton.setBackground(new Color(120, 30, 40));
        closeButton.setBorder(BorderFactory.createEmptyBorder(8, 25, 8, 25));
        closeButton.setFocusPainted(false);
        closeButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        closeButton.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                closeButton.setBackground(new Color(140, 40, 50));
            }
            public void mouseExited(MouseEvent e) {
                closeButton.setBackground(new Color(120, 30, 40));
            }
        });
        
        closeButton.addActionListener(e -> {
            dialog.dispose();
            parentDialog.dispose();
            ouvrirDialogModification(commande); // Recharge le dialog principal
        });
        
        buttonPanel.add(closeButton);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        
        dialog.setVisible(true);
    }
    private void createComptePanel() {
        comptePanel = new JPanel(new BorderLayout());
        comptePanel.setBackground(new Color(140, 50, 60)); // Fond bordeaux clair
        
        // Panel de titre
        JPanel titlePanel = new JPanel();
        titlePanel.setPreferredSize(new Dimension(Integer.MAX_VALUE, 80));
        titlePanel.setBackground(new Color(120, 30, 40)); // Bordeaux foncé
        JLabel titleLabel = new JLabel("MON COMPTE", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        titlePanel.add(titleLabel);
        comptePanel.add(titlePanel, BorderLayout.NORTH);
        
        // Panel principal avec GridBagLayout pour un contrôle précis
        JPanel mainContentPanel = new JPanel(new GridBagLayout());
        mainContentPanel.setBackground(new Color(140, 50, 60));
        mainContentPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Section Informations personnelles
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        JLabel infoTitle = createSectionLabel("Informations personnelles");
        mainContentPanel.add(infoTitle, gbc);
        
        // Vérifier que le client n'est pas null
        if (client != null) {
            // Labels et champs alignés
            String[] labels = {"ID :", "Login :", "Nom :", "Prénom :", "Adresse :", "Téléphone :"};
            String[] values = {
                String.valueOf(client.getId()),
                client.getLogin(),
                client.getNom(),
                client.getPrenom(),
                client.getAdresse(),
                client.getTelephone()
            };
            
            for (int i = 0; i < labels.length; i++) {
                gbc.gridy++;
                gbc.gridwidth = 1;
                gbc.weightx = 0;
                mainContentPanel.add(createInfoLabel1(labels[i]), gbc);
                
                gbc.gridx++;
                gbc.weightx = 1;
                JTextField field = createInfoField(values[i]);
                mainContentPanel.add(field, gbc);
                gbc.gridx = 0;
            }
        } else {
            gbc.gridy++;
            gbc.gridwidth = 2;
            JLabel errorLabel = new JLabel("Aucune information client disponible", JLabel.CENTER);
            errorLabel.setForeground(Color.WHITE);
            mainContentPanel.add(errorLabel, gbc);
        }
        
        // Séparateur
        gbc.gridy++;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mainContentPanel.add(createSeparator(), gbc);
        
        // Section Changer le mot de passe
        gbc.gridy++;
        gbc.gridwidth = 2;
        mainContentPanel.add(createSectionLabel("Changer le mot de passe"), gbc);
        
        // Champs mot de passe
        String[] passLabels = {"Ancien mot de passe :", "Nouveau mot de passe :", "Confirmation :"};
        oldPassField = new JPasswordField(20);
        newPassField = new JPasswordField(20);
        confirmPassField = new JPasswordField(20);

        gbc.gridy++;
        gbc.gridwidth = 1;
        gbc.weightx = 0;
        mainContentPanel.add(createInfoLabel1("Ancien mot de passe :"), gbc);
        gbc.gridx++;
        gbc.weightx = 1;
        mainContentPanel.add(oldPassField, gbc);
        gbc.gridx = 0;

        gbc.gridy++;
        gbc.gridwidth = 1;
        gbc.weightx = 0;
        mainContentPanel.add(createInfoLabel1("Nouveau mot de passe :"), gbc);
        gbc.gridx++;
        gbc.weightx = 1;
        mainContentPanel.add(newPassField, gbc);
        gbc.gridx = 0;

        gbc.gridy++;
        gbc.gridwidth = 1;
        gbc.weightx = 0;
        mainContentPanel.add(createInfoLabel1("Confirmation :"), gbc);
        gbc.gridx++;
        gbc.weightx = 1;
        mainContentPanel.add(confirmPassField, gbc);
        gbc.gridx = 0;
        
        // Bouton Changer le mot de passe
        gbc.gridy++;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.CENTER;
        JButton changePassBtn = createActionButton("Changer le mot de passe");
        changePassBtn.addActionListener(e -> {
           
            
            String currentPassword = new String(oldPassField.getPassword());
            String newPassword = new String(newPassField.getPassword());
            String confirmPassword = new String(confirmPassField.getPassword());
            
            // Validation
            if (currentPassword.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
                JOptionPane.showMessageDialog(this, 
                    "Tous les champs doivent être remplis", 
                    "Erreur", 
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (!newPassword.equals(confirmPassword)) {
                JOptionPane.showMessageDialog(this, 
                    "Les nouveaux mots de passe ne correspondent pas", 
                    "Erreur", 
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (newPassword.equals(currentPassword)) {
                JOptionPane.showMessageDialog(this, 
                    "Le nouveau mot de passe doit être différent de l'ancien", 
                    "Erreur", 
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Appel au service
            ClientService clientService = new ClientService();
            boolean success = clientService.updateClientPassword(client, currentPassword, newPassword);
            
            if (success) {
                client.setPassword(newPassword); // Mettre à jour le client en mémoire
                JOptionPane.showMessageDialog(this, 
                    "Mot de passe changé avec succès", 
                    "Succès", 
                    JOptionPane.INFORMATION_MESSAGE);
                
                // Réinitialiser les champs
                oldPassField.setText("");
                newPassField.setText("");
                confirmPassField.setText("");
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Ancien mot de passe incorrect", 
                    "Erreur", 
                    JOptionPane.ERROR_MESSAGE);
            }
        });
        mainContentPanel.add(changePassBtn, gbc);
        
        comptePanel.add(mainContentPanel, BorderLayout.CENTER);
    }

    // Méthodes utilitaires pour créer des composants cohérents
    private JLabel createSectionLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.BOLD, 18));
        label.setForeground(new Color(240, 240, 240));
        label.setBorder(BorderFactory.createEmptyBorder(5, 0, 15, 0));
        return label;
    }

    private JLabel createInfoLabel1(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.BOLD, 14));
        label.setForeground(Color.WHITE);
        label.setPreferredSize(new Dimension(150, 30));
        return label;
    }

    private JTextField createInfoField(String text) {
        JTextField field = new JTextField(text);
        field.setFont(new Font("Arial", Font.PLAIN, 14));
        field.setBackground(Color.WHITE);
        field.setEditable(false);
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        field.setPreferredSize(new Dimension(250, 35));
        return field;
    }

    private JSeparator createSeparator() {
        JSeparator separator = new JSeparator();
        separator.setForeground(new Color(200, 200, 200, 100));
        separator.setPreferredSize(new Dimension(0, 2));
        return separator;
    }

    private JButton createActionButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(120, 30, 40));
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(220, 40));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setBackground(new Color(150, 40, 50));
            }
            public void mouseExited(MouseEvent e) {
                button.setBackground(new Color(120, 30, 40));
            }
        });
        
        return button;
    }

    
    private JLabel createInfoLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.PLAIN, 16));
        return label;
    }
    
     
    private Color brighter(Color color, float factor) {
        int r = Math.min((int)(color.getRed() * factor), 255);
        int g = Math.min((int)(color.getGreen() * factor), 255);
        int b = Math.min((int)(color.getBlue() * factor), 255);
        return new Color(r, g, b);
    } 
    
}

class JBackgroundPanel extends JPanel {
    private Image backgroundImage;
    
    public JBackgroundPanel(String imagePath) {
        try {
            backgroundImage = ImageIO.read(new File(imagePath));
            backgroundImage = backgroundImage.getScaledInstance(800, 600, Image.SCALE_SMOOTH);
        } catch (Exception e) {
            System.err.println("Erreur image fond: " + e.getMessage());
        }
        setOpaque(false);
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        } else {
            // Fond alternatif si image non chargée
            Graphics2D g2d = (Graphics2D)g;
            GradientPaint gradient = new GradientPaint(
                0, 0, new Color(140, 30, 30),
                getWidth(), getHeight(), new Color(80, 10, 10));
            g2d.setPaint(gradient);
            g2d.fillRect(0, 0, getWidth(), getHeight());
        }
        
        // Overlay semi-transparent
        g.setColor(new Color(0, 0, 0, 50));
        g.fillRect(0, 0, getWidth(), getHeight());
    }
    

}