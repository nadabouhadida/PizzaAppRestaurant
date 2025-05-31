package restaurant.model;

public enum EtatCommande {
    EN_PREPARATION("En préparation"),
    PRETE("Prête à être livrée"),
    EN_LIVRAISON("En cours de livraison"),
    LIVREE("Livrée"),
    ANNULEE("Annulée");

    private final String libelle;

    private EtatCommande(String libelle) {
        this.libelle = libelle;
    }

    public String getLibelle() {
        return libelle;
    }

    @Override
    public String toString() {
        return libelle;
    }
}