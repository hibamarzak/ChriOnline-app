package service;

import dao.UserDAO;
import model.User;

// AuthService - Gestion complète de l'authentification, 
// ce service est utilisé côté SERVEUR pour traiter les requête d'authentification 
// et d'enregistrement envoyées par le client via TCP
  
 
public class AuthService {

    private UserDAO userDAO;
  
    public AuthService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

   
    public String traiterRequete(String requete) {
        if (requete == null || requete.isEmpty()) {
            return "ERREUR:Requête vide";
        }
// cette ligne sert a decouper la requete en morcaux en utilisant [:] pour separer 
        String[] parts = requete.split(":");

        switch (parts[0].toUpperCase()) {
            case "LOGIN":
                return traiterLogin(parts);
            case "REGISTER":
                return traiterRegister(parts);
            case "LOGOUT":
                return "LOGOUT_OK";
            default:
                return "ERREUR:Commande inconnue";
        }
    }

    // Traite une demande de connexion Format: LOGIN:email:password
    
    private String traiterLogin(String[] parts) {
        if (parts.length < 3) {
            return "ERREUR:Format invalide. Attendu: LOGIN:email:password";
        }

        String email = parts[1];
        String password = parts[2];

        // Validation basique
        if (email.isEmpty() || password.isEmpty()) {
            return "ERREUR:Email ou mot de passe vide";
        }
// Demande à UserDAO de vérifier dans la BD si cet email et le mot de passe existent. Retourne un User si trouvé, null sinon.
        User user = userDAO.authenticate(email, password);

        if (user != null) {
            
          
          // Retourner les infos de l'utilisateur au client
            return "LOGIN_OK:" + user.getId() + ":" + user.getUsername() + ":" + user.getRole();
        } else {
            return "ERREUR:Email ou mot de passe incorrect";
        }
    }

    // Traite une demande d'enregistrement Format: REGISTER:username:email:password
     
    private String traiterRegister(String[] parts) {
        if (parts.length < 4) {
            return "ERREUR:Format invalide. Attendu: REGISTER:username:email:password";
        }
// Extrait les 3 données de l'inscription depuis le tableau.
        String username = parts[1];
        String email    = parts[2];
        String password = parts[3];

        // Validations
        String erreur = validerInscription(username, email, password);
        if (erreur != null) {
            return "ERREUR:" + erreur;
        }

        // Vérifier si l'email existe déjà
        if (userDAO.findByEmail(email) != null) {
            return "ERREUR:Cet email est déjà utilisé";
        }
        // Crée un nouvel objet User avec le rôle CLIENT par défaut (id=0 car la BD va générer l'id automatiquement), 
        // puis le sauvegarde dans la BD.
      
        User nouvelUser = new User(0, username, email, password, "CLIENT");
        boolean succes = userDAO.save(nouvelUser);

        if (succes) {
            return "REGISTER_OK:" + nouvelUser.getId() + ":" + username;
        } else {
            return "ERREUR:Impossible de créer le compte";
        }
    }

    // validation ou les messages d'erreur 
  
    private String validerInscription(String username, String email, String password) {
        if (username == null || username.trim().isEmpty()) {
            return "Le nom d'utilisateur est obligatoire";
        }
        if (username.length() < 3) {
            return "Le nom d'utilisateur doit avoir au moins 3 caractères";
        }
        if (email == null || !email.contains("@") || !email.contains(".")) {
            return "Adresse email invalide";
        }
        if (password == null || password.length() < 4) {
            return "Le mot de passe doit avoir au moins 4 caractères";
        }
        return null; // Tout est valide
    }
}
