package restaurant.view.components;

import restaurant.model.Article;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ArticleCard extends JPanel {
    private final Color CARD_BG_COLOR = new Color(250, 250, 250);
    private final Color BORDER_COLOR = new Color(200, 200, 200);
    private final Color PRICE_COLOR = new Color(140, 30, 30);
    private final Color BUTTON_COLOR = new Color(120, 30, 40); // Bordeaux
    private final Color BUTTON_HOVER_COLOR = new Color(150, 40, 50); // Bordeaux clair
    private final Color QUANTITY_COLOR = new Color(70, 70, 70);

    private JLabel quantityLabel;
    private Article article;

    public ArticleCard(Article article, ActionListener addToCartListener) {
        this.article = article;
        setupCardLayout();
        add(createImagePanel(), BorderLayout.NORTH);
        add(createInfoPanel(), BorderLayout.CENTER);
        add(createAddButton(addToCartListener), BorderLayout.SOUTH);
    }

    private void setupCardLayout() {
        setLayout(new BorderLayout(5, 5));
        setBackground(CARD_BG_COLOR);
        setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR, 1),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        setMaximumSize(new Dimension(220, 320)); // Dimensions réduites pour une carte plus compacte
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }

    private JPanel createImagePanel() {
        JPanel imagePanel = new JPanel(new BorderLayout());
        imagePanel.setBackground(CARD_BG_COLOR);
        imagePanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        
        // Charger l'image
        ImageIcon originalIcon = new ImageIcon("src/ressources/pizza_1404945.png");
        
        // Redimensionner l'image à une taille appropriée pour la carte
        Image image = originalIcon.getImage();
        Image scaledImage = image.getScaledInstance(150, 120, Image.SCALE_SMOOTH);
        ImageIcon scaledIcon = new ImageIcon(scaledImage);
        
        JLabel imageLabel = new JLabel(scaledIcon);
        imageLabel.setHorizontalAlignment(JLabel.CENTER);
        
        imagePanel.add(imageLabel, BorderLayout.CENTER);
        return imagePanel;
    }

    private JPanel createInfoPanel() {
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(CARD_BG_COLOR);

        // Nom
        JLabel nameLabel = new JLabel(article.getNom());
        nameLabel.setFont(new Font("Arial", Font.BOLD, 16)); // Police réduite
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Description
        JTextArea descArea = new JTextArea(article.getDescription());
        descArea.setEditable(false);
        descArea.setLineWrap(true);
        descArea.setWrapStyleWord(true);
        descArea.setBackground(CARD_BG_COLOR);
        descArea.setFont(new Font("Arial", Font.PLAIN, 11)); // Police réduite
        descArea.setMaximumSize(new Dimension(200, 40)); // Dimensions réduites

        // Prix en DT
        JLabel priceLabel = new JLabel(article.getFormattedPrix());
        priceLabel.setFont(new Font("Arial", Font.BOLD, 18)); // Police réduite
        priceLabel.setForeground(PRICE_COLOR);
        priceLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Sélecteur quantité
        JPanel quantityPanel = createQuantityPanel();

        // Assemblage avec espacement réduit
        infoPanel.add(nameLabel);
        infoPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        infoPanel.add(descArea);
        infoPanel.add(Box.createRigidArea(new Dimension(0, 8)));
        infoPanel.add(priceLabel);
        infoPanel.add(Box.createRigidArea(new Dimension(0, 8)));
        infoPanel.add(quantityPanel);
        
        return infoPanel;
    }

    private JPanel createQuantityPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.setBackground(CARD_BG_COLOR);
        panel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Bouton -
        JButton minusBtn = new JButton("-");
        styleQuantityButton(minusBtn);
        minusBtn.addActionListener(e -> updateQuantity(-1));
        
        // Label quantité
        quantityLabel = new JLabel("  " + article.getQuantity() + "  ");
        quantityLabel.setFont(new Font("Arial", Font.BOLD, 16));
        quantityLabel.setForeground(QUANTITY_COLOR);
        
        // Bouton +
        JButton plusBtn = new JButton("+");
        styleQuantityButton(plusBtn);
        plusBtn.addActionListener(e -> updateQuantity(1));
        
        panel.add(Box.createHorizontalGlue());
        panel.add(minusBtn);
        panel.add(quantityLabel);
        panel.add(plusBtn);
        panel.add(Box.createHorizontalGlue());

        return panel;
    }

    private void styleQuantityButton(JButton button) {
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setForeground(QUANTITY_COLOR);
        button.setBackground(CARD_BG_COLOR);
        button.setBorder(BorderFactory.createLineBorder(QUANTITY_COLOR, 1));
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(30, 25));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setBackground(new Color(240, 240, 240));
            }
            public void mouseExited(MouseEvent e) {
                button.setBackground(CARD_BG_COLOR);
            }
        });
    }

    private JButton createAddButton(ActionListener listener) {
        JButton addButton = new JButton("Ajouter");
        addButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        addButton.setFont(new Font("Arial", Font.BOLD, 13)); // Police réduite
        addButton.setForeground(Color.WHITE);
        addButton.setBackground(BUTTON_COLOR);
        addButton.setBorderPainted(false);
        addButton.setFocusPainted(false);
        addButton.setPreferredSize(new Dimension(120, 30)); // Dimensions réduites
        addButton.addActionListener(listener);
        
        addButton.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                addButton.setBackground(BUTTON_HOVER_COLOR);
            }
            public void mouseExited(MouseEvent e) {
                addButton.setBackground(BUTTON_COLOR);
            }
        });
        
        return addButton;
    }

    private void updateQuantity(int change) {
        int newQuantity = article.getQuantity() + change;
        if (newQuantity >= 1) {
            article.setQuantity(newQuantity);
            quantityLabel.setText("  " + newQuantity + "  ");
        }
    }
}