package restaurant.view;

import java.util.*;



import restaurant.RestaurantApp;
import restaurant.model.*;
import restaurant.view.components.ArticleCardGerant;
import restaurant.service.*;
import javax.swing.*;
import javax.swing.table.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class GerantView extends JPanel {
    // CONSTANTES
    private static final Color NAVBAR_BG_COLOR = new Color(25, 25, 25);
    private static final Color BUTTON_COLOR = new Color(140, 30, 30);
    private static final Color BUTTON_HOVER_COLOR = new Color(180, 40, 40);
    private static final Color FORM_BG_COLOR = new Color(245, 245, 245);
    
    // COMPOSANTS PRINCIPAUX
    private final RestaurantApp app;
    private Gerant gerant;
    private JPanel navbarPanel;
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private ClientService clientService;
    private MenuService menuService;
    
    // DONNEES
    private List<Article> articles;
    private List<Commande> commandes;
    private List<Client> clients;
    
    // PANELS
    private JPanel homePanel;
    private JPanel menuPanel;
    private JPanel clientsPanel;
    private JPanel commandesPanel;
    private JPanel menuCardsPanel;
    private JPanel addArticleFormPanel;
    private JPanel articlesListPanel;
    private JScrollPane articlesScrollPane;
    
    // COMPOSANTS TABLEAUX
    private DefaultTableModel clientsTableModel;
    private DefaultTableModel commandesTableModel;
    private Object[][] originalCommandesData;
    
    // CHAMPS DE FORMULAIRE
    private JTextField nomArticleField;
    private JTextField prixArticleField;
    private JTextArea descriptionArticleArea;

    public GerantView(RestaurantApp app) {
    	this.app = app;
        this.clientService = new ClientService();
        this.menuService = new MenuService();
        this.articles = new ArrayList<>();
        this.articles = menuService.loadArticles();
        setupUI();
    }
    
    


    // INITIALISATION ===========================================================
    private void setupUI() {
        setLayout(new BorderLayout());
        createNavbar();
        createMainPanel();
        add(navbarPanel, BorderLayout.WEST);
        add(mainPanel, BorderLayout.CENTER);
    }
    
   

    // NAVBAR ===================================================================
    private void createNavbar() {
        navbarPanel = new JPanel();
        navbarPanel.setLayout(new BoxLayout(navbarPanel, BoxLayout.Y_AXIS));
        navbarPanel.setBackground(NAVBAR_BG_COLOR);
        navbarPanel.setPreferredSize(new Dimension(180, getHeight()));
        
        addLogoToNavbar();
        addNavButtons();
    }
    
    private void addLogoToNavbar() {
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
        
        navbarPanel.add(logoPanel);
        navbarPanel.add(Box.createRigidArea(new Dimension(0, 20)));
    }
    
    private void addNavButtons() {
        Font buttonFont = new Font("Arial", Font.BOLD, 14);
        Dimension buttonSize = new Dimension(170, 45);
        
        JButton homeBtn = createNavButton("Accueil", buttonFont, buttonSize);
        homeBtn.addActionListener(e -> cardLayout.show(mainPanel, "HOME"));
        
        JButton menuBtn = createNavButton("Menu", buttonFont, buttonSize);
        menuBtn.addActionListener(e -> {
            refreshArticlesList();  // Or loadArticles() depending on what you want to do
            cardLayout.show(mainPanel, "MENU");
        });
        
        JButton clientsBtn = createNavButton("Clients", buttonFont, buttonSize);
        clientsBtn.addActionListener(e -> cardLayout.show(mainPanel, "CLIENTS"));
        
        JButton commandesBtn = createNavButton("Commandes", buttonFont, buttonSize);
        commandesBtn.addActionListener(e -> cardLayout.show(mainPanel, "COMMANDES"));
        
        JButton logoutBtn = createNavButton("Déconnexion", buttonFont, buttonSize);
        logoutBtn.addActionListener(e -> app.showLoginView());
        
        navbarPanel.add(homeBtn);
        navbarPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        navbarPanel.add(menuBtn);
        navbarPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        navbarPanel.add(clientsBtn);
        navbarPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        navbarPanel.add(commandesBtn);
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

    // MAIN PANEL ===============================================================
    private void createMainPanel() {
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        
        createHomePanel();
        createMenuPanel();
        createClientsPanel();
        createCommandesPanel();
        
        mainPanel.add(homePanel, "HOME");
        mainPanel.add(menuPanel, "MENU");
        mainPanel.add(clientsPanel, "CLIENTS");
        mainPanel.add(commandesPanel, "COMMANDES");
    }

    // HOME PANEL ===============================================================
    private void createHomePanel() {
        homePanel = new JBackgroundPanel("src/ressources/home page.png");
        homePanel.setLayout(new BorderLayout());
        
        JLabel welcomeLabel = new JLabel("Bienvenue Gérant " + (gerant != null ? gerant.getPrenom() : "") + "!", JLabel.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 28));
        welcomeLabel.setForeground(Color.WHITE);
        
        JPanel textBgPanel = new JPanel();
        textBgPanel.setBackground(new Color(0, 0, 0, 150));
        textBgPanel.setLayout(new BorderLayout());
        textBgPanel.add(welcomeLabel);
        
        JPanel welcomePanel = new JPanel(new BorderLayout());
        welcomePanel.setOpaque(false);
        welcomePanel.add(textBgPanel, BorderLayout.CENTER);
        
        homePanel.add(welcomePanel, BorderLayout.SOUTH);
    }

    // MENU PANEL ===============================================================
   

    private void createMenuPanel() {
        menuPanel = new JPanel(new BorderLayout(10, 10));
        menuPanel.setBackground(Color.WHITE);
        menuPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        // Panel titre
        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(new Color(120, 30, 40));
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));
        
        JLabel titleLabel = new JLabel("Gestion des Articles", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 30));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        titlePanel.add(titleLabel);
        
        // Panel contenu principal
        JPanel contentPanel = new JPanel(new BorderLayout(0, 15));
        contentPanel.setBackground(Color.WHITE);
        
        // Panel liste d'articles
        articlesListPanel = new JPanel();
        articlesListPanel.setLayout(new BoxLayout(articlesListPanel, BoxLayout.Y_AXIS));
        articlesListPanel.setBackground(Color.WHITE);
        
        // Création du JScrollPane pour contenir la liste des articles
        articlesScrollPane = new JScrollPane(articlesListPanel);
        articlesScrollPane.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));
        articlesScrollPane.getVerticalScrollBar().setUnitIncrement(16);
        
        // Formulaire ajout d'article
        JPanel formPanel = createArticleFormPanel();
        
        // Ajout au panel contenu
        contentPanel.add(articlesScrollPane, BorderLayout.CENTER);
        contentPanel.add(formPanel, BorderLayout.SOUTH);
        
        // Ajout au panel principal
        menuPanel.add(titlePanel, BorderLayout.NORTH);
        menuPanel.add(contentPanel, BorderLayout.CENTER);
        
        // Charger les articles initiaux
        loadArticles(articlesListPanel);
    }
    private String generateNewArticleId() {
        if (articles.isEmpty()) {
            return "P001"; // Premier ID si la liste est vide
        }
        
        // Trouver le plus grand ID existant et incrémenter
        int maxId = articles.stream()
            .mapToInt(a -> Integer.parseInt(a.getId().substring(1)))
            .max()
            .orElse(0);
        
        return String.format("P%03d", maxId + 1);
    }
    private JPanel createArticleFormPanel() {
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBackground(new Color(245, 245, 245));
        formPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(220, 220, 220)),
            BorderFactory.createEmptyBorder(15, 10, 15, 10)
        ));
        
        // Titre du formulaire
        JLabel formTitle = new JLabel("Ajouter un nouvel article");
        formTitle.setFont(new Font("Arial", Font.BOLD, 20));
        formTitle.setForeground(new Color(140, 30, 30));
        formTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Panneaux pour chaque ligne du formulaire
        JPanel row1 = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        JPanel row2 = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        JPanel row3 = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        
        row1.setOpaque(false);
        row2.setOpaque(false);
        row3.setOpaque(false);
        
        // Champs de formulaire (supprimer quantityField)
    
        JTextField nomField = new JTextField(15);
        JTextField prixField = new JTextField(8);
        JTextField descriptionField = new JTextField(30);
        
        
        

        row1.add(new JLabel("Nom:"));
        row1.add(nomField);
        
        // Ligne 2 (supprimer la partie quantité)
        row2.add(new JLabel("Prix (DT):"));
        row2.add(prixField);
        
        // Ligne 3
        row3.add(new JLabel("Description:"));
        row3.add(descriptionField);
        
        // Bouton d'ajout
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setOpaque(false);
        
        JButton addButton = createActionButton("Ajouter Article", new Color(120, 30, 40));
        addButton.addActionListener(e -> {
            try {
                String nom = nomField.getText().trim();
                String description = descriptionField.getText().trim();
                double prix = Double.parseDouble(prixField.getText().trim());
                
                // Générer un nouvel ID automatiquement
                String newId = generateNewArticleId();
                
                Article newArticle = new Article(newId, nom, description, prix);
                addNewArticle(newArticle);
                
                // Réinitialiser les champs
                nomField.setText("");
                descriptionField.setText("");
                prixField.setText("");
                
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this,
                    "Veuillez entrer une valeur numérique valide pour le prix.",
                    "Erreur de format", JOptionPane.ERROR_MESSAGE);
            }
        });
        buttonPanel.add(addButton);
        
        // Assembler le formulaire
        formPanel.add(formTitle);
        formPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        formPanel.add(row1);
        formPanel.add(row2);
        formPanel.add(row3);
        formPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        formPanel.add(buttonPanel);
        
        return formPanel;
    }

    private JButton createActionButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 12));
        button.setForeground(Color.WHITE);
        button.setBackground(bgColor);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(120, 30));
       
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setBackground(bgColor.brighter());
            }
            public void mouseExited(MouseEvent e) {
                button.setBackground(bgColor);
            }
        });
       
        return button;
    }

    private void loadArticles(JPanel panel) {
        panel.removeAll();
        
        if (articles == null || articles.isEmpty()) {
            JLabel emptyLabel = new JLabel("Aucun article disponible");
            emptyLabel.setHorizontalAlignment(JLabel.CENTER);
            emptyLabel.setFont(new Font("Arial", Font.ITALIC, 16));
            panel.add(emptyLabel);
        } else {
            for (Article article : articles) {
                ArticleCardGerant card = new ArticleCardGerant(
                    article,
                    this::editArticle,
                    this::deleteArticle
                );
                panel.add(card);
                panel.add(Box.createRigidArea(new Dimension(0, 10)));
            }
        }
        
        panel.revalidate();
        panel.repaint();
    }

    private void refreshArticlesList() {
        loadArticles(articlesListPanel);
        articlesScrollPane.getViewport().setView(articlesListPanel);
        menuPanel.revalidate();
        menuPanel.repaint();
    }
    
    private void editArticle(Article article) {
        // Ouvrir une boîte de dialogue pour modifier l'article
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Modifier Article", true);
        dialog.setLayout(new BorderLayout());
        dialog.setSize(400, 250);
        dialog.setLocationRelativeTo(this);
        
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        // Champ ID (non modifiable)
        JTextField idField = new JTextField(article.getId());
        idField.setEditable(false);
        
        // Champs modifiables
        JTextField nomField = new JTextField(article.getNom());
        JTextField prixField = new JTextField(String.valueOf(article.getPrixDT()));
        JTextArea descriptionArea = new JTextArea(article.getDescription());
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        
        // Création des lignes du formulaire
        JPanel idPanel = createFormRow("ID:", idField);
        JPanel nomPanel = createFormRow("Nom:", nomField);
        JPanel prixPanel = createFormRow("Prix (DT):", prixField);
        
        JPanel descPanel = new JPanel(new BorderLayout());
        descPanel.add(new JLabel("Description:"), BorderLayout.NORTH);
        descPanel.add(new JScrollPane(descriptionArea), BorderLayout.CENTER);
        
        // Boutons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton saveButton = createActionButton("Enregistrer", new Color(40, 167, 69));
        JButton cancelButton = createActionButton("Annuler", new Color(108, 117, 125));
        
        saveButton.addActionListener(e -> {
            try {
                String nom = nomField.getText().trim();
                double prix = Double.parseDouble(prixField.getText().trim());
                String description = descriptionArea.getText().trim();
                
                if (nom.isEmpty()) {
                    JOptionPane.showMessageDialog(dialog,
                        "Le nom de l'article ne peut pas être vide.",
                        "Erreur de validation", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                
                // Mettre à jour l'article
                article.setNom(nom);
                article.setPrixDT(prix);
                article.setDescription(description);
                
                // Sauvegarder les modifications
                menuService.saveArticles(articles);
                
                // Rafraîchir l'affichage
                refreshArticlesList();
                dialog.dispose();
                
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog,
                    "Veuillez entrer une valeur numérique valide pour le prix.",
                    "Erreur de format", JOptionPane.ERROR_MESSAGE);
            }
        });
        cancelButton.addActionListener(e -> dialog.dispose());
        
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);
        
        // Assemblage du formulaire
        formPanel.add(idPanel);
        formPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        formPanel.add(nomPanel);
        formPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        formPanel.add(prixPanel);
        formPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        formPanel.add(descPanel);
        formPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        
        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        
        dialog.setVisible(true);
    }

    private JPanel createFormRow(String labelText, JComponent field) {
        JPanel panel = new JPanel(new BorderLayout(10, 0));
        JLabel label = new JLabel(labelText);
        label.setPreferredSize(new Dimension(100, 25));
        panel.add(label, BorderLayout.WEST);
        panel.add(field, BorderLayout.CENTER);
        return panel;
    }

    private void deleteArticle(Article article) {
        int confirm = JOptionPane.showConfirmDialog(
            this,
            "Êtes-vous sûr de vouloir supprimer l'article '" + article.getNom() + "' ?",
            "Confirmation de suppression",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE
        );
        
        if (confirm == JOptionPane.YES_OPTION) {
            articles.remove(article); 
            menuService.saveArticles(articles); // Sauvegarde après suppression
            refreshArticlesList();
            
            JOptionPane.showMessageDialog(
                this,
                "Article supprimé avec succès.",
                "Information",
                JOptionPane.INFORMATION_MESSAGE
            );
        }
    }
    private void addNewArticle(Article article) {
        articles.add(article);
        menuService.saveArticles(articles); // Sauvegarde après ajout
        refreshArticlesList();
        
        JOptionPane.showMessageDialog(
            this,
            "L'article a été ajouté avec succès.",
            "Information",
            JOptionPane.INFORMATION_MESSAGE
        );
    }


 // CLIENTS PANEL ============================================================
    private void createClientsPanel() {
        clients = clientService.getAllClients(); // Charger les clients depuis le fichier
        
        clientsPanel = new JPanel(new BorderLayout());
        clientsPanel.setBackground(Color.WHITE);
        
        clientsPanel.add(createClientsHeader(), BorderLayout.NORTH);
        clientsPanel.add(createClientsTablePanel(), BorderLayout.CENTER);
        clientsPanel.add(createClientFormPanel(), BorderLayout.SOUTH);}
    
    private JPanel createClientsHeader() {
    	  JPanel headerPanel = new JPanel(new BorderLayout());
          headerPanel.setBackground(new Color(120, 30, 40));
          headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
          JPanel titlePanel = new JPanel();
          titlePanel.setBackground(new Color(120, 30, 40));
          titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
          titlePanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));
          
          JLabel titleLabel = new JLabel("Gestion des Clients", JLabel.CENTER);
          titleLabel.setFont(new Font("Arial", Font.BOLD, 30));
          titleLabel.setForeground(Color.WHITE);
          titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
          titleLabel.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
          
          titlePanel.add(titleLabel);
          
          // Suppression du panneau de recherche
          headerPanel.add(titleLabel, BorderLayout.CENTER);
        
        return headerPanel;
    }
    
    private JPanel createClientsTablePanel() {
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(Color.WHITE);
        tablePanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        
        // Added ID column to match the Utilisateur/Client classes
        String[] columnNames = {"ID", "Login", "Nom", "Prénom", "Adresse", "Téléphone", "Actions"};
        Object[][] data = createSampleClientData();
        
        clientsTableModel = new DefaultTableModel(data, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 6; // Changed to 6 to match the new column index for Actions
            }
        };
        
        JTable clientsTable = configureClientsTable(clientsTableModel);
        tablePanel.add(new JScrollPane(clientsTable), BorderLayout.CENTER);
        
        return tablePanel;
    }
    
    private Object[][] createSampleClientData() {
        // Safety check for null list
        if (clients == null) {
            clients = new ArrayList<>();
        }
        
        Object[][] data = new Object[clients.size()][7]; // Changed to 7 columns to include ID
        for (int i = 0; i < clients.size(); i++) {
            Client client = clients.get(i);
            data[i] = new Object[]{
                client.getId(),        // New ID field
                client.getLogin(),
                client.getNom(),
                client.getPrenom(),
                client.getAdresse(),
                client.getTelephone(),
                ""
            };
        }
        return data;
    }
    
    private JTable configureClientsTable(DefaultTableModel tableModel) {
        JTable clientsTable = new JTable(tableModel);
        clientsTable.setRowHeight(40);
        clientsTable.setSelectionBackground(new Color(240, 240, 240));
        clientsTable.setGridColor(new Color(230, 230, 230));
        clientsTable.setShowVerticalLines(true);
        clientsTable.setShowHorizontalLines(true);
        
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        
        for (int i = 0; i < clientsTable.getColumnCount() - 1; i++) {
            clientsTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
        
        setColumnWidths(clientsTable);
        configureTableHeader(clientsTable);
        configureActionsColumn(clientsTable, tableModel);
        
        return clientsTable;
    }
    
    private void setColumnWidths(JTable table) {
        table.getColumnModel().getColumn(0).setPreferredWidth(50);   // ID
        table.getColumnModel().getColumn(1).setPreferredWidth(80);   // Login
        table.getColumnModel().getColumn(2).setPreferredWidth(100);  // Nom
        table.getColumnModel().getColumn(3).setPreferredWidth(100);  // Prénom
        table.getColumnModel().getColumn(4).setPreferredWidth(200);  // Adresse
        table.getColumnModel().getColumn(5).setPreferredWidth(100);  // Téléphone
        table.getColumnModel().getColumn(6).setPreferredWidth(150);  // Actions
    }
    
    private void configureTableHeader(JTable table) {
        JTableHeader header = table.getTableHeader();
        header.setBackground(new Color(220, 220, 220));
        header.setFont(new Font("Arial", Font.BOLD, 12));
        header.setReorderingAllowed(false);
    }
    
    private void configureActionsColumn(JTable table, DefaultTableModel tableModel) {
        table.getColumnModel().getColumn(6).setCellRenderer(new ActionCellRenderer()); // Changed index to 6
        table.getColumnModel().getColumn(6).setCellEditor(new ActionCellEditor(tableModel)); // Changed index to 6
    }
    
    private JPanel createClientFormPanel() {
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBackground(FORM_BG_COLOR);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(220, 220, 220)),
            BorderFactory.createEmptyBorder(15, 20, 15, 20)
        ));
        
        JLabel formTitle = new JLabel("Ajouter un nouveau client");
        formTitle.setFont(new Font("Arial", Font.BOLD, 16));
        formTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JPanel fieldsPanel = new JPanel(new GridLayout(0, 4, 10, 10));
        fieldsPanel.setOpaque(false);
        fieldsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        fieldsPanel.setMaximumSize(new Dimension(1200, 120)); // Réduit la hauteur car on a supprimé un champ
        
        // Suppression du champ ID
        JTextField loginField = new JTextField(15);
        JPasswordField passwordField = new JPasswordField(15);
        JTextField nomField = new JTextField(15);
        JTextField prenomField = new JTextField(15);
        JTextField adresseField = new JTextField(15);
        JTextField telephoneField = new JTextField(15);
        
        applyDocumentFilters(nomField, prenomField, telephoneField);
        
        addFormFields(fieldsPanel, loginField, passwordField, nomField, prenomField, adresseField, telephoneField);
        
        JButton addButton = createFormButton("Ajouter le client");
        addButton.addActionListener(e -> handleAddClient(
            loginField, passwordField, nomField, prenomField, adresseField, telephoneField
        ));
        
        formPanel.add(formTitle);
        formPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        formPanel.add(fieldsPanel);
        formPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        formPanel.add(addButton);
        
        return formPanel;
    }
    
    private void applyDocumentFilters(JTextField nomField, JTextField prenomField, JTextField telephoneField) {
        ((AbstractDocument) nomField.getDocument()).setDocumentFilter(new AlphabeticFilter());
        ((AbstractDocument) prenomField.getDocument()).setDocumentFilter(new AlphabeticFilter());
        ((AbstractDocument) telephoneField.getDocument()).setDocumentFilter(new NumericFilter(8));
    }
    
    private void addFormFields(JPanel panel, JTextField... fields) {
        // Labels mis à jour sans ID
        String[] labels = {"Login:", "Mot de passe:", "Nom:", "Prénom:", "Adresse:", "Téléphone (8 chiffres):"};
        
        for (int i = 0; i < labels.length; i++) {
            panel.add(new JLabel(labels[i]));
            panel.add(fields[i]);
        }
    }
    
    private void handleAddClient(JTextField... fields) {
        try {
            // Génération automatique de l'ID
            List<Client> existingClients = clientService.getAllClients();
            int newId = existingClients.isEmpty() ? 1 : 
                existingClients.stream().mapToInt(Client::getId).max().getAsInt() + 1;
            
            String login = fields[0].getText().trim();
            String password = new String(((JPasswordField)fields[1]).getPassword());
            String nom = fields[2].getText().trim();
            String prenom = fields[3].getText().trim();
            String adresse = fields[4].getText().trim();
            String telephone = fields[5].getText().trim();
            
            // Validation des champs
            if (login.isEmpty() || password.isEmpty() || nom.isEmpty() || 
                prenom.isEmpty() || telephone.isEmpty()) {
                showWarningMessage("Veuillez remplir tous les champs obligatoires", "Champs incomplets");
                return;
            }
            
            if (telephone.length() != 8) {
                showWarningMessage("Le numéro de téléphone doit contenir exactement 8 chiffres", "Format incorrect");
                return;
            }
            
            // Vérification si le login existe déjà
            if (clientService.loginExists(login)) {
                showWarningMessage("Ce login est déjà utilisé", "Erreur");
                return;
            }
            
            // Création du nouveau client avec ID auto-généré
            Client newClient = new Client(
                newId,
                login,
                password,
                nom,
                prenom,
                adresse,
                telephone
            );
            
            // Ajout dans la base de données
            clientService.addClient(newClient);
            
            // Mise à jour du tableau
            clients = clientService.getAllClients(); // Recharger les clients depuis le fichier
            refreshClientsTable();
            
            // Réinitialisation des champs
            for (JTextField field : fields) {
                field.setText("");
            }
            ((JPasswordField)fields[1]).setText(""); // Effacer le mot de passe
            
            showSuccessMessage("Client ajouté avec succès!", "Succès");
            
        } catch (Exception e) {
            showErrorMessage("Erreur lors de l'ajout du client: " + e.getMessage(), "Erreur");
            }
    }
    
    private void refreshClientsTable() {
        clientsTableModel.setRowCount(0); // Vider le tableau
        
        for (Client client : clients) {
            clientsTableModel.addRow(new Object[]{
                client.getId(),
                client.getLogin(),
                client.getNom(),
                client.getPrenom(),
                client.getAdresse(),
                client.getTelephone(),
                ""
            });
        }
    }
    
    private void editClient(String clientLogin, DefaultTableModel tableModel) {
        // Trouver l'index de la ligne correspondant au client
        int foundRow = -1;
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            if (tableModel.getValueAt(i, 1).toString().equals(clientLogin)) {
                foundRow = i;
                break;
            }
        }

        if (foundRow == -1) {
            JOptionPane.showMessageDialog(this, 
                "Client introuvable dans le tableau", 
                "Erreur", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Créer une copie finale pour utiliser dans les listeners
        final int rowIndex = foundRow;

        // Créer la boîte de dialogue
        JDialog editDialog = new JDialog((Frame)SwingUtilities.getWindowAncestor(this), 
                                       "Modifier le client", 
                                       true);
        editDialog.setLayout(new BorderLayout());
        editDialog.setSize(400, 300);
        editDialog.setLocationRelativeTo(this);

        // Panel pour le formulaire
        JPanel formPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Récupérer les valeurs actuelles
        String currentNom = tableModel.getValueAt(rowIndex, 2).toString();
        String currentPrenom = tableModel.getValueAt(rowIndex, 3).toString();
        String currentAdresse = tableModel.getValueAt(rowIndex, 4).toString();
        String currentTelephone = tableModel.getValueAt(rowIndex, 5).toString();

        // Créer les champs
        JTextField nomField = new JTextField(currentNom);
        JTextField prenomField = new JTextField(currentPrenom);
        JTextField adresseField = new JTextField(currentAdresse);
        JTextField telephoneField = new JTextField(currentTelephone);

        // Ajouter les champs au formulaire
        formPanel.add(new JLabel("Nom:"));
        formPanel.add(nomField);
        formPanel.add(new JLabel("Prénom:"));
        formPanel.add(prenomField);
        formPanel.add(new JLabel("Adresse:"));
        formPanel.add(adresseField);
        formPanel.add(new JLabel("Téléphone:"));
        formPanel.add(telephoneField);

        // Panel pour les boutons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton saveButton = new JButton("Enregistrer");
        JButton cancelButton = new JButton("Annuler");

        // Style des boutons
        saveButton.setBackground(new Color(70, 130, 180));
        saveButton.setForeground(Color.WHITE);
        cancelButton.setBackground(new Color(220, 220, 220));

        // Actions des boutons
        saveButton.addActionListener(e -> {
            // Validation
            if (nomField.getText().trim().isEmpty() || 
                prenomField.getText().trim().isEmpty() ||
                telephoneField.getText().trim().isEmpty()) {
                
                JOptionPane.showMessageDialog(editDialog, 
                    "Tous les champs sont obligatoires", 
                    "Erreur", 
                    JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Mettre à jour le tableau
            tableModel.setValueAt(nomField.getText().trim(), rowIndex, 2);
            tableModel.setValueAt(prenomField.getText().trim(), rowIndex, 3);
            tableModel.setValueAt(adresseField.getText().trim(), rowIndex, 4);
            tableModel.setValueAt(telephoneField.getText().trim(), rowIndex, 5);

            // Mettre à jour la liste des clients
            for (Client client : clients) {
                if (client.getLogin().equals(clientLogin)) {
                    client.setNom(nomField.getText().trim());
                    client.setPrenom(prenomField.getText().trim());
                    client.setAdresse(adresseField.getText().trim());
                    client.setTelephone(telephoneField.getText().trim());
                    break;
                }
            }
            clientService.saveUsers(new ArrayList<>(clients));

            editDialog.dispose();
        });

        cancelButton.addActionListener(e -> editDialog.dispose());

        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);

        editDialog.add(formPanel, BorderLayout.CENTER);
        editDialog.add(buttonPanel, BorderLayout.SOUTH);
        editDialog.setVisible(true);
    }

    private void deleteClient(String clientLogin, DefaultTableModel tableModel) {
        int confirm = showConfirmDialog(
            "Êtes-vous sûr de vouloir supprimer le client " + clientLogin + " ?",
            "Confirmation de suppression"
        );

        if (confirm == JOptionPane.YES_OPTION) {
            // Supprimer de la liste
            clients.removeIf(c -> c.getLogin().equals(clientLogin));
            
            // Sauvegarder la liste mise à jour
            clientService.saveUsers(new ArrayList<>(clients));
            
            // Mettre à jour le tableau
            refreshClientsTable();
            
            showSuccessMessage("Client supprimé avec succès", "Succès");
        }
    }
    // COMMANDES PANEL ==========================================================
 // COMMANDES PANEL ==========================================================
    private void createCommandesPanel() {
        commandesPanel = new JPanel(new BorderLayout());
        commandesPanel.setBackground(new Color(140, 50, 60));
        
        // Créer des exemples de commandes statiques pour la démo
        createSampleCommandes();
        
        commandesPanel.add(createCommandesHeader(), BorderLayout.NORTH);
        commandesPanel.add(createCommandesTablePanel(), BorderLayout.CENTER);
        commandesPanel.add(createCommandesFilterPanel(), BorderLayout.SOUTH);
    }

    private void createSampleCommandes() {
        CommandeService commandeService = new CommandeService();
        this.commandes = commandeService.loadCommandes(); // Charge les commandes depuis le fichier
        
        // Convertir les commandes en données pour le tableau
        originalCommandesData = convertCommandesToTableData();
    }
    
    private Object[][] convertCommandesToTableData() {
        // Vérification si la liste des commandes est vide
        if (commandes == null || commandes.isEmpty()) {
            return new Object[0][7]; // Retourne un tableau vide si aucune commande
        }

        Object[][] data = new Object[commandes.size()][7];
        
        for (int i = 0; i < commandes.size(); i++) {
            Commande cmd = commandes.get(i);
            data[i] = new Object[]{
                cmd.getId(),
                getClientName(cmd.getClient()), // Nom complet du client
                formatDate(cmd.getDate()),      // Date formatée
                String.format("%.2f DT", cmd.getTotal()),
                getArticlesListAsString(cmd.getArticles()),
                cmd.getEtat().getLibelle(),
                "" // Colonne actions (vide car gérée par les renderers/editors)
            };
        }
        
        return data;
    }

    // Méthodes auxiliaires utilisées par convertCommandesToTableData()

    /**
     * Formatte le nom complet du client
     */
    private String getClientName(Client client) {
        if (client == null) {
            return "Client inconnu";
        }
        return client.getNom() + " " + client.getPrenom();
    }

    /**
     * Formatte la date pour l'affichage
     */
    private String formatDate(Date date) {
        if (date == null) {
            return "Date inconnue";
        }
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        return sdf.format(date);
    }

    /**
     * Convertit la liste d'articles en une chaîne lisible
     */
    private String getArticlesListAsString(List<Article> articles) {
        if (articles == null || articles.isEmpty()) {
            return "Aucun article";
        }

        // Compte le nombre de chaque article
        Map<String, Integer> articleCounts = new HashMap<>();
        Map<String, Double> articlePrices = new HashMap<>();
        
        for (Article article : articles) {
            String nom = article.getNom();
            articleCounts.put(nom, articleCounts.getOrDefault(nom, 0) + 1);
            articlePrices.put(nom, article.getPrixDT());
        }

        // Construit la chaîne de résultat
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, Integer> entry : articleCounts.entrySet()) {
            String nom = entry.getKey();
            int count = entry.getValue();
            double prix = articlePrices.get(nom);
            sb.append(count).append("x ").append(nom)
              .append(" (").append(String.format("%.2f", prix)).append(" DT), ");
        }

        // Supprime la dernière virgule et espace
        if (sb.length() > 0) {
            sb.setLength(sb.length() - 2);
        }

        return sb.toString();
    }



    private JPanel createCommandesHeader() {
  	  JPanel headerPanel = new JPanel(new BorderLayout());
      headerPanel.setBackground(new Color(120, 30, 40));
      headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
      JPanel titlePanel = new JPanel();
      titlePanel.setBackground(new Color(120, 30, 40));
      titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
      titlePanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));
      
      JLabel titleLabel = new JLabel("Gestion Commandes", JLabel.CENTER);
      titleLabel.setFont(new Font("Arial", Font.BOLD, 30));
      titleLabel.setForeground(Color.WHITE);
      titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
      titleLabel.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
      
      titlePanel.add(titleLabel);
      
      // Suppression du panneau de recherche
      headerPanel.add(titleLabel, BorderLayout.CENTER);
        
        return headerPanel;
    }

    private void filterCommandes(String searchText) {
        commandesTableModel.setRowCount(0);
        
        for (Object[] row : originalCommandesData) {
            boolean matches = false;
            // Vérifier chaque colonne sauf les colonnes d'actions
            for (int i = 0; i < row.length - 1; i++) {
                if (row[i].toString().toLowerCase().contains(searchText)) {
                    matches = true;
                    break;
                }
            }
            
            if (matches || searchText.isEmpty()) {
                commandesTableModel.addRow(row);
            }
        }
    }

    private JPanel createCommandesTablePanel() {
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(Color.WHITE);
        tablePanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        
        String[] columnNames = {"ID", "Client", "Date", "Total", "Articles", "État", "Actions"};
        commandesTableModel = new DefaultTableModel(originalCommandesData, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 6; // Seule la colonne Actions est éditable
            }
        };
        
        JTable commandesTable = configureCommandesTable(commandesTableModel);
        tablePanel.add(new JScrollPane(commandesTable), BorderLayout.CENTER);
        
        return tablePanel;
    }

    private JTable configureCommandesTable(DefaultTableModel tableModel) {
        JTable commandesTable = new JTable(tableModel);
        commandesTable.setRowHeight(40);
        commandesTable.setSelectionBackground(new Color(240, 240, 240));
        commandesTable.setGridColor(new Color(230, 230, 230));
        commandesTable.setShowVerticalLines(true);
        commandesTable.setShowHorizontalLines(true);
        
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        
        // Centrer le texte pour toutes les colonnes sauf Articles et Actions
        for (int i = 0; i < commandesTable.getColumnCount(); i++) {
            if (i != 4) { // La colonne Articles n'est pas centrée
                commandesTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
            }
        }
        
        // Renderer spécial pour la colonne État
        commandesTable.getColumnModel().getColumn(5).setCellRenderer(new EtatCellRenderer());
        
        // Configurer la colonne Actions
        configureCommandesActionsColumn(commandesTable, tableModel);
        
        // Définir les largeurs de colonnes
        commandesTable.getColumnModel().getColumn(0).setPreferredWidth(80);  // ID
        commandesTable.getColumnModel().getColumn(1).setPreferredWidth(120); // Client
        commandesTable.getColumnModel().getColumn(2).setPreferredWidth(150); // Date
        commandesTable.getColumnModel().getColumn(3).setPreferredWidth(80);  // Total
        commandesTable.getColumnModel().getColumn(4).setPreferredWidth(200); // Articles
        commandesTable.getColumnModel().getColumn(5).setPreferredWidth(150); // État
        commandesTable.getColumnModel().getColumn(6).setPreferredWidth(150); // Actions
        
        return commandesTable;
    }

    private void configureCommandesActionsColumn(JTable table, DefaultTableModel tableModel) {
        table.getColumnModel().getColumn(6).setCellRenderer(new CommandeActionCellRenderer());
        table.getColumnModel().getColumn(6).setCellEditor(new CommandeActionCellEditor(tableModel));
    }

    private JPanel createCommandesFilterPanel() {
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        filterPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        filterPanel.setBackground(new Color(245, 245, 245));
        
        JLabel filterLabel = new JLabel("Filtrer par état:");
        filterLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        
        JComboBox<String> filterCombo = new JComboBox<>();
        filterCombo.addItem("Tous");
        for (EtatCommande etat : EtatCommande.values()) {
            filterCombo.addItem(etat.getLibelle());
        }
        
        JButton applyFilterButton = new JButton("Appliquer");
        applyFilterButton.setBackground(BUTTON_COLOR);
        applyFilterButton.setForeground(Color.WHITE);
        applyFilterButton.addActionListener(e -> {
            String selectedState = (String) filterCombo.getSelectedItem();
            filterCommandesByState(selectedState);
        });
        
        JButton refreshButton = new JButton("Actualiser");
        refreshButton.setBackground(new Color(70, 130, 180));
        refreshButton.setForeground(Color.WHITE);
        refreshButton.addActionListener(e -> {
            filterCommandesByState("Tous");
            filterCombo.setSelectedItem("Tous");
        });
        
        filterPanel.add(filterLabel);
        filterPanel.add(filterCombo);
        filterPanel.add(applyFilterButton);
        filterPanel.add(refreshButton);
        
        return filterPanel;
    }

    private void filterCommandesByState(String state) {
        commandesTableModel.setRowCount(0);
        
        if (state.equals("Tous")) {
            for (Object[] row : originalCommandesData) {
                commandesTableModel.addRow(row);
            }
            return;
        }
        
        for (Object[] row : originalCommandesData) {
            if (row[5].toString().equals(state)) {
                commandesTableModel.addRow(row);
            }
        }
    }

    private void changeCommandeState(String commandeId, DefaultTableModel tableModel) {
        // Utiliser un tableau pour contourner la restriction "effectively final"
        final Commande[] commandeHolder = new Commande[1];
        for (Commande cmd : commandes) {
            if (cmd.getId().equals(commandeId)) {
                commandeHolder[0] = cmd;
                break;
            }
        }
        
        if (commandeHolder[0] == null) return;
        
        // Créer une boîte de dialogue pour modifier l'état
        JDialog stateDialog = new JDialog((Frame)SwingUtilities.getWindowAncestor(this), 
                                        "Modifier l'état de la commande", 
                                        true);
        stateDialog.setLayout(new BorderLayout());
        stateDialog.setSize(350, 200);
        stateDialog.setLocationRelativeTo(this);
        
        JPanel contentPanel = new JPanel(new BorderLayout(10, 10));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        JLabel titleLabel = new JLabel("Commande " + commandeId, JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        
        JPanel statePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel stateLabel = new JLabel("Nouvel état:");
        JComboBox<EtatCommande> stateCombo = new JComboBox<>(EtatCommande.values());
        stateCombo.setSelectedItem(commandeHolder[0].getEtat());
        
        // Afficher le mode de récupération si pertinent
        JLabel modeLabel = null;
        if (commandeHolder[0].getModeRecuperation() == ModeRecuperation.LIVRAISON) {
            modeLabel = new JLabel("Livraison à: " + commandeHolder[0].getAdresseLivraison());
        } else {
            modeLabel = new JLabel("Mode: " + commandeHolder[0].getModeRecuperation().toString());
        }
        
        statePanel.add(stateLabel);
        statePanel.add(stateCombo);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        JButton saveButton = new JButton("Enregistrer");
        JButton cancelButton = new JButton("Annuler");
        
        saveButton.setBackground(new Color(70, 130, 180));
        saveButton.setForeground(Color.WHITE);
        cancelButton.setBackground(new Color(220, 220, 220));
        
        saveButton.addActionListener(e -> {
            EtatCommande newState = (EtatCommande) stateCombo.getSelectedItem();
            commandeHolder[0].setEtat(newState);
            
            // Mettre à jour le tableau
            for (int i = 0; i < tableModel.getRowCount(); i++) {
                if (tableModel.getValueAt(i, 0).equals(commandeId)) {
                    tableModel.setValueAt(newState.getLibelle(), i, 5);
                    break;
                }
            }
            
            // Mettre à jour les données originales
            for (int i = 0; i < originalCommandesData.length; i++) {
                if (originalCommandesData[i][0].equals(commandeId)) {
                    originalCommandesData[i][5] = newState.getLibelle();
                    break;
                }
            }
            
            // Sauvegarder les modifications dans le fichier
            CommandeService commandeService = new CommandeService();
            commandeService.saveCommandes(commandes);
            
            stateDialog.dispose();
        });
        
        cancelButton.addActionListener(e -> stateDialog.dispose());
        
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);
        
        contentPanel.add(titleLabel, BorderLayout.NORTH);
        contentPanel.add(statePanel, BorderLayout.CENTER);
        contentPanel.add(modeLabel, BorderLayout.SOUTH);
        
        stateDialog.add(contentPanel, BorderLayout.CENTER);
        stateDialog.add(buttonPanel, BorderLayout.SOUTH);
        stateDialog.setVisible(true);
    }

    private void deleteCommande(String commandeId, DefaultTableModel tableModel) {
        int confirm = showConfirmDialog(
            "Êtes-vous sûr de vouloir supprimer la commande " + commandeId + " ?",
            "Confirmation de suppression"
        );

        if (confirm == JOptionPane.YES_OPTION) {
            // Supprimer de la liste des commandes
            commandes.removeIf(c -> c.getId().equals(commandeId));
            
            // Mettre à jour les données originales
            List<Object[]> newData = new ArrayList<>();
            for (Object[] row : originalCommandesData) {
                if (!row[0].equals(commandeId)) {
                    newData.add(row);
                }
            }
            originalCommandesData = newData.toArray(new Object[0][]);
            
            // Mettre à jour le tableau
            refreshCommandesTable();
            
            showSuccessMessage("Commande supprimée avec succès", "Succès");
        }
    }

    private void refreshCommandesTable() {
    	  CommandeService commandeService = new CommandeService();
    	    this.commandes = commandeService.loadCommandes();
    	    this.originalCommandesData = convertCommandesToTableData();
    	    
    	    // Rafraîchir le tableau
    	    commandesTableModel.setRowCount(0);
    	    for (Object[] row : originalCommandesData) {
    	        commandesTableModel.addRow(row);
    	    }
    }

    // UTILITAIRES ==============================================================
    private JButton createFormButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(BUTTON_COLOR);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setAlignmentX(Component.LEFT_ALIGNMENT);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }
    
    private void showWarningMessage(String message, String title) {
        JOptionPane.showMessageDialog(this, message, title, JOptionPane.WARNING_MESSAGE);
    }
    
    private void showWarningMessage(Component parent, String message, String title) {
        JOptionPane.showMessageDialog(parent, message, title, JOptionPane.WARNING_MESSAGE);
    }
    
    private void showSuccessMessage(String message, String title) {
        JOptionPane.showMessageDialog(this, message, title, JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void showErrorMessage(String message, String title) {
        JOptionPane.showMessageDialog(this, message, title, JOptionPane.ERROR_MESSAGE);
    }
    
    private void showInfoMessage(String message, String title) {
        JOptionPane.showMessageDialog(this, message, title, JOptionPane.INFORMATION_MESSAGE);
    }
    
    private int showConfirmDialog(String message, String title) {
        return JOptionPane.showConfirmDialog(
            this,
            message,
            title,
            JOptionPane.YES_NO_OPTION
        );
    }

    // GETTERS/SETTERS ==========================================================
    public void setGerant(Gerant gerant) {
        this.gerant = gerant;
        updateWelcomeMessage();
    }
    
    private void updateWelcomeMessage() {
        if (homePanel != null) {
            for (Component comp : homePanel.getComponents()) {
                if (comp instanceof JPanel) {
                    for (Component subComp : ((JPanel)comp).getComponents()) {
                        if (subComp instanceof JPanel) {
                            for (Component subSubComp : ((JPanel)subComp).getComponents()) {
                                if (subSubComp instanceof JLabel) {
                                    ((JLabel)subSubComp).setText("Bienvenue Gérant " + (gerant != null ? gerant.getPrenom() : "") + "!");
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // CLASSES INTERNES =========================================================
    private class JBackgroundPanel extends JPanel {
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
                Graphics2D g2d = (Graphics2D)g;
                GradientPaint gradient = new GradientPaint(
                    0, 0, new Color(140, 30, 30),
                    getWidth(), getHeight(), new Color(80, 10, 10));
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
            
            g.setColor(new Color(0, 0, 0, 50));
            g.fillRect(0, 0, getWidth(), getHeight());
        }
    }
    
    private class ActionCellRenderer implements TableCellRenderer {
        private final JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
        private final JButton editButton = new JButton("Modifier");
        private final JButton deleteButton = new JButton("Supprimer");
        
        public ActionCellRenderer() {
            editButton.setBackground(new Color(60, 120, 200));
            editButton.setForeground(Color.WHITE);
            editButton.setPreferredSize(new Dimension(80, 30));
            
            deleteButton.setBackground(new Color(200, 60, 60));
            deleteButton.setForeground(Color.WHITE);
            deleteButton.setPreferredSize(new Dimension(80, 30));
            
            panel.add(editButton);
            panel.add(deleteButton);
            panel.setBackground(Color.WHITE);
        }
        
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            panel.setBackground(isSelected ? table.getSelectionBackground() : Color.WHITE);
            return panel;
        }
    }
    
    private class ActionCellEditor extends AbstractCellEditor implements TableCellEditor {
        private final JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
        private final JButton editButton = new JButton("Modifier");
        private final JButton deleteButton = new JButton("Supprimer");
        private String clientLogin;
        
        public ActionCellEditor(DefaultTableModel tableModel) {
            editButton.setBackground(new Color(60, 120, 200));
            editButton.setForeground(Color.WHITE);
            editButton.setPreferredSize(new Dimension(80, 30));
            
            deleteButton.setBackground(new Color(200, 60, 60));
            deleteButton.setForeground(Color.WHITE);
            deleteButton.setPreferredSize(new Dimension(80, 30));
            
            editButton.addActionListener(e -> {
                fireEditingStopped();
                editClient(clientLogin, tableModel);
            });
            
            deleteButton.addActionListener(e -> {
                fireEditingStopped();
                deleteClient(clientLogin, tableModel);
            });
            
            panel.add(editButton);
            panel.add(deleteButton);
        }
        
        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                boolean isSelected, int row, int column) {
            // Récupérer le login depuis la colonne 1
            this.clientLogin = table.getValueAt(row, 1).toString();
            return panel;
        }
        
        @Override
        public Object getCellEditorValue() {
            return "";
        }
    }
    
    private class CommandeActionCellRenderer implements TableCellRenderer {
        private final JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
        private final JButton stateButton = new JButton("État");
        private final JButton deleteButton = new JButton("Supprimer");
        
        public CommandeActionCellRenderer() {
            stateButton.setBackground(new Color(60, 120, 200));
            stateButton.setForeground(Color.WHITE);
            stateButton.setPreferredSize(new Dimension(80, 30));
            
            deleteButton.setBackground(new Color(200, 60, 60));
            deleteButton.setForeground(Color.WHITE);
            deleteButton.setPreferredSize(new Dimension(80, 30));
            
            panel.add(stateButton);
            panel.add(deleteButton);
            panel.setBackground(Color.WHITE);
        }
        
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            panel.setBackground(isSelected ? table.getSelectionBackground() : Color.WHITE);
            return panel;
        }
    }
    
    private class CommandeActionCellEditor extends AbstractCellEditor implements TableCellEditor {
        private final JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
        private final JButton stateButton = new JButton("État");
        private final JButton deleteButton = new JButton("Supprimer");
        private String commandeId;
        
        public CommandeActionCellEditor(DefaultTableModel tableModel) {
            stateButton.setBackground(new Color(60, 120, 200));
            stateButton.setForeground(Color.WHITE);
            stateButton.setPreferredSize(new Dimension(80, 30));
            
            deleteButton.setBackground(new Color(200, 60, 60));
            deleteButton.setForeground(Color.WHITE);
            deleteButton.setPreferredSize(new Dimension(80, 30));
            
            stateButton.addActionListener(e -> {
                fireEditingStopped();
                changeCommandeState(commandeId, tableModel);
            });
            
            deleteButton.addActionListener(e -> {
                fireEditingStopped();
                deleteCommande(commandeId, tableModel);
            });
            
            panel.add(stateButton);
            panel.add(deleteButton);
        }
        
        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                boolean isSelected, int row, int column) {
            commandeId = table.getValueAt(row, 0).toString();
            return panel;
        }
        
        @Override
        public Object getCellEditorValue() {
            return "";
        }
    }
    
    private class EtatCellRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            setHorizontalAlignment(JLabel.CENTER);
            
            String etat = value.toString();
            switch (etat) {
                case "Non encore traitée":
                    setForeground(new Color(150, 0, 0));
                    break;
                case "En cours de préparation":
                    setForeground(new Color(200, 120, 0));
                    break;
                case "Prête":
                    setForeground(new Color(0, 128, 0));
                    break;
                case "En route":
                    setForeground(new Color(0, 0, 200));
                    break;
                default:
                    setForeground(Color.BLACK);
                    break;
            }
            
            return c;
        }
    }
    
    private class AlphabeticFilter extends DocumentFilter {
        @Override
        public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
            if (string.matches("[a-zA-ZÀ-ÿ\\s]*")) {
                super.insertString(fb, offset, string, attr);
            }
        }
        
        @Override
        public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
            if (text.matches("[a-zA-ZÀ-ÿ\\s]*")) {
                super.replace(fb, offset, length, text, attrs);
            }
        }
    }
    
    private class NumericFilter extends DocumentFilter {
        private final int maxLength;
        
        public NumericFilter(int maxLength) {
            this.maxLength = maxLength;
        }
        
        @Override
        public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
            if (string.matches("\\d*") && (fb.getDocument().getLength() + string.length() <= maxLength)) {
                super.insertString(fb, offset, string, attr);
            }
        }
        
        @Override
        public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
            if (text.matches("\\d*") && (fb.getDocument().getLength() - length + text.length() <= maxLength)) {
                super.replace(fb, offset, length, text, attrs);
            }
        }
    }
}





