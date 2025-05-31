package restaurant.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Commande implements Serializable {
    private String id;
    private Client client;
    private Date date;
    private List<Article> articles;
    private double total;
    private EtatCommande etat;
    private ModeRecuperation modeRecuperation;
    private String adresseLivraison;

    public Commande(String id, Client client) {
        this.id = id;
        this.client = client;
        this.date = new Date();
        this.articles = new ArrayList<>();
        this.etat = EtatCommande.EN_PREPARATION;
        this.modeRecuperation = ModeRecuperation.LIVRAISON;
    }
    
    public String getId() { return id; }
    public Client getClient() { return client; }
    public Date getDate() { return date; }
    public List<Article> getArticles() { return articles; }
    public double getTotal() { return total; }
    public EtatCommande getEtat() { return etat; }
    
    public void setEtat(EtatCommande etat) { this.etat = etat; }
    
    public ModeRecuperation getModeRecuperation() { return modeRecuperation; }
    public void setModeRecuperation(ModeRecuperation modeRecuperation) { 
        this.modeRecuperation = modeRecuperation; 
    }
    
    public String getAdresseLivraison() { return adresseLivraison; }
    public void setAdresseLivraison(String adresseLivraison) { 
        this.adresseLivraison = adresseLivraison; 
    }
    
    public void addArticle(Article article) {
        articles.add(article);
        total += article.getPrixDT();
    }
    
    public void removeArticle(Article article) {
        if (articles.remove(article)) {
            total -= article.getPrixDT();
        }
    }
    public String getClientName() {
        return client.getNom() + " " + client.getPrenom();
    }
}