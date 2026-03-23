package model;

public class User {
    private int id;
    private String username;
    private String email;
    private String password;
    private String role;
    private String adresse;
    private String telephone;
    private boolean actif;
    
    // definir les methodes 
    public User() {}

    public User(int id, String username, String email, String password, String role) {
        setId(id);
        setUsername(username);
        setEmail(email);
        setPassword(password);
        setRole(role);
        setActif(true);
    }

    public String getNomComplet() {
        return username;
    }

    public boolean verifierMotDePasse(String password) {
        return this.password != null && this.password.equals(password);
    }

    public void modifierProfil(String email, String telephone, String adresse) {
        setEmail(email);
        setTelephone(telephone);
        setAdresse(adresse);
    }

    // Getters & Setters
    public int getId() { return id; }
    public void setId(int newId) { id = newId; }

    public String getUsername() { return username; }
    public void setUsername(String newUsername) { username = newUsername; }

    public String getEmail() { return email; }
    public void setEmail(String newEmail) { email = newEmail; }

    public String getPassword() { return password; }
    public void setPassword(String newPassword) { password = newPassword; }

    public String getRole() { return role; }
    public void setRole(String newRole) { role = newRole; }

    public String getAdresse() { return adresse; }
    public void setAdresse(String newAdresse) { adresse = newAdresse; }

    public String getTelephone() { return telephone; }
    public void setTelephone(String newTelephone) { telephone = newTelephone; }

    public boolean isActif() { return actif; }
    public void setActif(boolean newActif) { actif = newActif; }

    // afficher les informations de l’objet User
    @Override
    public String toString() {
        return "User{id=" + id + ", username='" + username + "', email='" + email + "', role='" + role + "'}";
    }
}
