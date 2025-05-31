package restaurant.model;

import java.io.Serializable;

public class Article implements Serializable {
    private String id;
    private String nom;
    private String description;
    private double prixDT; // Prix en dinars tunisiens
    private int quantity;

    // Constructeur principal
    public Article(String id, String nom, String description, double prixDT) {
        this.id = id;
        this.nom = nom;
        this.description = description;
        this.prixDT = prixDT;
        this.quantity = 1; // Quantité par défaut
    }

    // Constructeur de copie
    public Article(Article other) {
        this.id = other.id;
        this.nom = other.nom;
        this.description = other.description;
        this.prixDT = other.prixDT;
        this.quantity = other.quantity;
    }

    // Getters
    public String getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public String getDescription() {
        return description;
    }

    public double getPrixDT() {
        return prixDT;
    }

    public int getQuantity() {
        return quantity;
    }

    // Setters
    public void setId(String id) {
        this.id = id;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPrixDT(double prixDT) {
        this.prixDT = prixDT;
    }

    public void setQuantity(int quantity) {
        this.quantity = Math.max(quantity, 0); // Empêche les quantités négatives
    }

    // Méthodes utilitaires
    public String getFormattedPrix() {
        return String.format("%.3f DT", prixDT);
    }

    public double getTotalPrice() {
        return prixDT * quantity;
    }

    public String getFormattedTotalPrice() {
        return String.format("%.3f DT", getTotalPrice());
    }

    // Méthodes pour gérer la quantité
    public void incrementQuantity() {
        quantity++;
    }

    public void decrementQuantity() {
        quantity = Math.max(0, quantity - 1);
    }

    @Override
    public String toString() {
        return String.format("%s (x%d) - %.3f DT", nom, quantity, getTotalPrice());
    }

    // Méthode equals basée sur l'ID
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Article article = (Article) o;
        return id.equals(article.id);
    }

    // Méthode hashCode cohérente avec equals
    @Override
    public int hashCode() {
        return id.hashCode();
    }
}