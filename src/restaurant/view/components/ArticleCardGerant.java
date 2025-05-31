package restaurant.view.components;

import restaurant.model.Article;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.function.Consumer;

public class ArticleCardGerant extends JPanel {
    private Article article;
    private JLabel nameLabel;
    private JLabel priceLabel;
    private JLabel descriptionLabel;
    private JLabel imageLabel;
    private JButton editButton;
    private JButton deleteButton;
    private JLabel idLabel;
  
    
    private Consumer<Article> onEditAction;
    private Consumer<Article> onDeleteAction;
    
    // Style constants remain the same
    private static final Color CARD_BG_COLOR = new Color(255, 255, 255);
    private static final Color CARD_BORDER_COLOR = new Color(220, 220, 220);
    private static final Color TITLE_COLOR = new Color(140, 30, 30);
    private static final Color TEXT_COLOR = new Color(50, 50, 50);
    private static final Font TITLE_FONT = new Font("Arial", Font.BOLD, 16);
    private static final Font PRICE_FONT = new Font("Arial", Font.BOLD, 15);
    private static final Font DESC_FONT = new Font("Arial", Font.PLAIN, 13);
    private static final Font ID_FONT = new Font("Arial", Font.PLAIN, 12);
    private static final Color ID_COLOR = new Color(100, 100, 100);
    
    public ArticleCardGerant(Article article, Consumer<Article> onEdit, Consumer<Article> onDelete) {
        this.article = article;
        this.onEditAction = onEdit;
        this.onDeleteAction = onDelete;
        
        setupUI();
        updateContent();
    }
    
   
	private void setupUI() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(CARD_BORDER_COLOR, 1),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        setBackground(CARD_BG_COLOR);
        
        // Image panel (now includes quantity)
        JPanel imagePanel = new JPanel(new BorderLayout());
        imagePanel.setOpaque(false);
        
        imageLabel = new JLabel();
        imageLabel.setPreferredSize(new Dimension(80, 80));
        imageLabel.setHorizontalAlignment(JLabel.CENTER);
        imageLabel.setBorder(BorderFactory.createLineBorder(CARD_BORDER_COLOR));
     
        
        try {
            ImageIcon defaultIcon = new ImageIcon("src/ressources/pizza_1404945.png");
            Image defaultImg = defaultIcon.getImage().getScaledInstance(70, 70, Image.SCALE_SMOOTH);
            imageLabel.setIcon(new ImageIcon(defaultImg));
        } catch (Exception e) {
            imageLabel.setText("Image");
            imageLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        }
        
        imagePanel.add(imageLabel, BorderLayout.CENTER);
        
        
        // Info panel
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setOpaque(false);
        
        idLabel = new JLabel();
        idLabel.setFont(ID_FONT);
        idLabel.setForeground(ID_COLOR);
        idLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        nameLabel = new JLabel();
        nameLabel.setFont(TITLE_FONT);
        nameLabel.setForeground(TITLE_COLOR);
        nameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        priceLabel = new JLabel();
        priceLabel.setFont(PRICE_FONT);
        priceLabel.setForeground(TEXT_COLOR);
        priceLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        descriptionLabel = new JLabel();
        descriptionLabel.setFont(DESC_FONT);
        descriptionLabel.setForeground(TEXT_COLOR);
        descriptionLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        infoPanel.add(idLabel);
        infoPanel.add(Box.createRigidArea(new Dimension(0, 3)));
        infoPanel.add(nameLabel);
        infoPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        infoPanel.add(priceLabel);
        infoPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        infoPanel.add(descriptionLabel);
        
        // Button panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setOpaque(false);
        
        editButton = createIconButton("src/ressources/edit_4511659.png", "Modifier");
        if (editButton.getIcon() == null) {
            editButton.setText("✏️");
        }
        editButton.addActionListener(e -> onEditAction.accept(article));
        
        deleteButton = createIconButton("src/ressources/trash_9915683.png", "Supprimer");
        if (deleteButton.getIcon() == null) {
            deleteButton.setText("🗑️");
        }
        deleteButton.addActionListener(e -> onDeleteAction.accept(article));
        
        buttonPanel.add(editButton);
        buttonPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        buttonPanel.add(deleteButton);
        
        // Add components to main panel
        add(imagePanel, BorderLayout.WEST);
        add(infoPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.EAST);
        
        setPreferredSize(new Dimension(400, 100));
        setMaximumSize(new Dimension(600, 100));
    }
    
    private JButton createIconButton(String iconPath, String toolTip) {
        // Same implementation as before
        JButton button = new JButton();
        button.setToolTipText(toolTip);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(24, 24));
        button.setMaximumSize(new Dimension(24, 24));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        try {
            ImageIcon icon = new ImageIcon(iconPath);
            Image img = icon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
            button.setIcon(new ImageIcon(img));
        } catch (Exception e) {
            System.err.println("Erreur de chargement d'icône: " + e.getMessage());
        }
        
        return button;
    }
    
    public void updateContent() {
        if (article != null) {
            idLabel.setText("ID: " + article.getId());
            nameLabel.setText(article.getNom());
            priceLabel.setText(article.getFormattedPrix());
            
            String description = article.getDescription();
            if (description != null && description.length() > 50) {
                description = description.substring(0, 47) + "...";
            }
            descriptionLabel.setText(description);
        }
    }
    
    
    public void setArticle(Article article) {
        this.article = article;
        updateContent();
    }
    
    public Article getArticle() {
        return article;
    }
}