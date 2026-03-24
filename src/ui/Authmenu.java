package ui;

import java.util.Scanner;

/**
 * AuthMenu - Interface utilisateur pour la connexion et l'inscription
 * Responsable: Hiba
 *
 * Ce menu est côté CLIENT. Il collecte les données de l'utilisateur,
 * envoie les requêtes au serveur via ClientTCP, et affiche les résultats.
 */
public class AuthMenu {

    private Scanner scanner;
    // Référence vers le ClientTCP (injecté depuis MainMenu)
    // ClientTCP client;  <-- sera utilisé quand ClientTCP sera disponible

    // Session courante
    private int userId = -1;
    private String username = null;
    private String userRole = null;

    public AuthMenu(Scanner scanner) {
        this.scanner = scanner;
    }

    /**
     * Affiche le menu principal d'authentification
     * Retourne true si l'utilisateur est connecté avec succès
     */
    public boolean afficherMenu() {
        while (true) {
            System.out.println("\n========================================");
            System.out.println("      BIENVENUE - MINI E-COMMERCE       ");
            System.out.println("========================================");
            System.out.println("  1. Se connecter");
            System.out.println("  2. Créer un compte");
            System.out.println("  0. Quitter");
            System.out.println("----------------------------------------");
            System.out.print("Votre choix: ");

            String choix = scanner.nextLine().trim();

            switch (choix) {
                case "1":
                    if (afficherMenuConnexion()) {
                        return true; // Connecté avec succès
                    }
                    break;
                case "2":
                    afficherMenuInscription();
                    break;
                case "0":
                    System.out.println("Au revoir !");
                    System.exit(0);
                    break;
                default:
                    System.out.println("[!] Choix invalide. Veuillez entrer 0, 1 ou 2.");
            }
        }
    }

    /**
     * Affiche le formulaire de connexion
     * Retourne true si la connexion réussit
     */
    public boolean afficherMenuConnexion() {
        System.out.println("\n--- CONNEXION ---");

        System.out.print("Email: ");
        String email = scanner.nextLine().trim();

        System.out.print("Mot de passe: ");
        String password = scanner.nextLine().trim();

        if (email.isEmpty() || password.isEmpty()) {
            System.out.println("[!] Email et mot de passe obligatoires.");
            return false;
        }

        // Construire la requête pour le serveur
        String requete = "LOGIN:" + email + ":" + password;

        // TODO: Envoyer via ClientTCP et récupérer la réponse
        // String reponse = client.envoyerRequete(requete);
        // Pour l'instant, simuler l'envoi
        String reponse = envoyerRequete(requete);

        return traiterReponseLogin(reponse);
    }

    /**
     * Affiche le formulaire d'inscription
     */
    public void afficherMenuInscription() {
        System.out.println("\n--- CRÉER UN COMPTE ---");

        System.out.print("Nom d'utilisateur: ");
        String username = scanner.nextLine().trim();

        System.out.print("Email: ");
        String email = scanner.nextLine().trim();

        System.out.print("Mot de passe: ");
        String password = scanner.nextLine().trim();

        System.out.print("Confirmer le mot de passe: ");
        String confirm = scanner.nextLine().trim();

        if (!password.equals(confirm)) {
            System.out.println("[!] Les mots de passe ne correspondent pas.");
            return;
        }

        // Construire la requête pour le serveur
        String requete = "REGISTER:" + username + ":" + email + ":" + password;

        // TODO: Envoyer via ClientTCP et récupérer la réponse
        String reponse = envoyerRequete(requete);

        traiterReponseRegister(reponse);
    }

    /**
     * Traite la réponse du serveur pour le login
     */
    private boolean traiterReponseLogin(String reponse) {
        if (reponse == null) {
            System.out.println("[!] Pas de réponse du serveur.");
            return false;
        }

        if (reponse.startsWith("LOGIN_OK:")) {
            // Format: LOGIN_OK:id:username:role
            String[] parts = reponse.split(":");
            this.userId   = Integer.parseInt(parts[1]);
            this.username = parts[2];
            this.userRole = parts[3];
            System.out.println("\n[✓] Connexion réussie ! Bienvenue, " + username + " !");
            return true;
        } else if (reponse.startsWith("ERREUR:")) {
            System.out.println("[!] " + reponse.substring(7));
            return false;
        }

        System.out.println("[!] Réponse inattendue: " + reponse);
        return false;
    }

    /**
     * Traite la réponse du serveur pour l'inscription
     */
    private void traiterReponseRegister(String reponse) {
        if (reponse == null) {
            System.out.println("[!] Pas de réponse du serveur.");
            return;
        }

        if (reponse.startsWith("REGISTER_OK:")) {
            String[] parts = reponse.split(":");
            System.out.println("\n[✓] Compte créé avec succès ! Bienvenue, " + parts[2] + " !");
            System.out.println("    Vous pouvez maintenant vous connecter.");
        } else if (reponse.startsWith("ERREUR:")) {
            System.out.println("[!] " + reponse.substring(7));
        }
    }

    /**
     * Se déconnecter
     */
    public void seDeconnecter() {
        if (userId != -1) {
            envoyerRequete("LOGOUT:" + userId);
        }
        this.userId   = -1;
        this.username = null;
        this.userRole = null;
        System.out.println("[✓] Déconnexion réussie.");
    }

    /**
     * Stub pour envoyer une requête au serveur.
     * À remplacer par l'appel réel à ClientTCP quand disponible.
     */
    private String envoyerRequete(String requete) {
        // TODO: remplacer par -> return client.envoyerRequete(requete);
        System.out.println("[DEBUG] Requête envoyée: " + requete);
        return "ERREUR:ClientTCP non connecté (à intégrer)";
    }

    // Getters pour les autres menus (ex: MainMenu)
    public int getUserId()       { return userId; }
    public String getUsername()  { return username; }
    public String getUserRole()  { return userRole; }
    public boolean isConnecte()  { return userId != -1; }
}
