package restaurant.service;

import restaurant.model.Article;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class MenuService {
    private static final String MENU_FILE = "src/restaurant/data/menu.dat";
    
    public void saveArticles(List<Article> articles) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(MENU_FILE))) {
            oos.writeObject(articles);
        } catch (IOException e) {
            System.err.println("Erreur lors de la sauvegarde du menu: " + e.getMessage());
        }
    }
    
    @SuppressWarnings("unchecked")
    public List<Article> loadArticles() {
        File file = new File(MENU_FILE);
        if (!file.exists()) {
            return new ArrayList<>();
        }
        
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (List<Article>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Erreur lors du chargement du menu: " + e.getMessage());
            return new ArrayList<>();
        }
    }
}