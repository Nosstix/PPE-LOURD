package controleur;

public class User {
    private int id;
    private String nom;
    private String prenom;
    private String email;
    private String mdp;
    private String role;
    private String discord;

    public User(int id, String nom, String prenom, String email, String mdp, String role, String discord) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.mdp = mdp;
        this.role = role;
        this.discord = discord;
    }

    // compat ancien constructeur
    public User(int id, String nom, String prenom, String email, String mdp, String role) {
        this(id, nom, prenom, email, mdp, role, null);
    }

    public int getId() { return id; }
    public String getNom() { return nom; }
    public String getPrenom() { return prenom; }
    public String getEmail() { return email; }
    public String getMdp() { return mdp; }
    public String getRole() { return role; }
    public String getDiscord() { return discord; }

    public void setId(int id) { this.id = id; }
    public void setNom(String nom) { this.nom = nom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }
    public void setEmail(String email) { this.email = email; }
    public void setMdp(String mdp) { this.mdp = mdp; }
    public void setRole(String role) { this.role = role; }
    public void setDiscord(String discord) { this.discord = discord; }
}
