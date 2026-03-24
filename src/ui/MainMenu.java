package ui;

import java.util.Scanner;

// cette partie represente le point d'entree de toute l'app
public class MainMenu {
    private Scanner scanner;
    private AuthMenu authMenu;
    public MainMenu() {
        this.scanner  = new Scanner(System.in);
        this.authMenu = new AuthMenu(scanner);
    }
  // Lance le menu d'authentification. Bloque ici jusqu'à ce que l'utilisateur se connecte avec succès. 
  // Retourne true si connecté, false sinon.
    public void demarrer() {
        System.out.println("Connexion au serveur...");
        boolean connecte = authMenu.afficherMenu();
        if (!connecte) {
            System.out.println("Impossible de se connecter.");
            return;
        }
        afficherMenuPrincipal();
    }
    
    private void afficherMenuPrincipal() {
        boolean quitter = false;
        while (!quitter) {
        
            System.out.println("\n========================================");
            System.out.println("  MENU PRINCIPAL - " + authMenu.getUsername().toUpperCase());
            System.out.println("========================================");
            System.out.println("  1. Voir les produits");
            System.out.println("  2. Mon panier");
            System.out.println("  3. Mes commandes");
            System.out.println("  4. Mon profil");
            System.out.println("  9. Se déconnecter"); // Le 9 est mis loin des autres chiffres pour éviter que l'user se déconnecte par accident en ratant un chiffre.
            System.out.println("  0. Quitter");
            System.out.println("----------------------------------------");
            System.out.print("Votre choix: ");
            String choix = scanner.nextLine().trim();
            switch (choix) {
                case "1":
                    System.out.println("[→] Chargement de la liste des produits...");
                   
                    break;
                case "2":
                    System.out.println("[→] Chargement du panier...");
                    
                    break;
                case "3":
                    System.out.println("[→] Chargement des commandes...");
                   
                    break;
                case "4":
                    afficherMenuProfil();
                    break;
                case "9":
                    authMenu.seDeconnecter();
                    boolean reconnecte = authMenu.afficherMenu();
                    if (!reconnecte) quitter = true;
                    break;
                case "0":
                    quitter = true;
                    System.out.println("Au revoir, " + authMenu.getUsername() + " !");
                    break;
                default:
                    System.out.println("[!] Choix invalide.");
            }
        }
        scanner.close();
    }
    
    private void afficherMenuProfil() {
        System.out.println("\n--- MON PROFIL ---");
        System.out.println("Utilisateur : " + authMenu.getUsername());
        System.out.println("Rôle        : " + authMenu.getUserRole());
        System.out.println("ID          : " + authMenu.getUserId());
        System.out.println("\n  1. Modifier mes informations");
        System.out.println("  0. Retour");
        System.out.print("Votre choix: ");
        String choix = scanner.nextLine().trim();
        if (choix.equals("1")) {
            afficherFormulaireModificationProfil();
        }
    }

    private void afficherFormulaireModificationProfil() {
        System.out.println("\n--- MODIFIER MON PROFIL ---");
        System.out.print("Nouvel email (laisser vide pour ne pas changer): ");
        String email = scanner.nextLine().trim();
        System.out.print("Téléphone: ");
        String telephone = scanner.nextLine().trim();
        System.out.print("Adresse: ");
        String adresse = scanner.nextLine().trim();
        System.out.println("[✓] Profil mis à jour (à connecter au serveur).");
    }

    public static void main(String[] args) {
        MainMenu menu = new MainMenu();
        menu.demarrer();
    }
}







// les methode utilises et leurs roles :
// MainMenu() : Constructeur — crée le Scanner et l'AuthMenu
// demarrer() : Lance l'authentification puis le menu principal
// afficherMenuPrincipal() : Boucle principale de navigation avec les options
// afficherMenuProfil() : Affiche les infos de l'utilisateur connecté
// afficherFormulaireModificationProfil() : Collecte les nouvelles informations du profil
// main() : Point de démarrage de toute l'application
