package controleur;

public class Vehicule {
    private int id;
    private String nomModele;
    private double prixCatalogue;
    private int idMarque;
    private int idCategorie;

    public Vehicule(int id, String nomModele, double prixCatalogue, int idMarque, int idCategorie) {
        this.id = id;
        this.nomModele = nomModele;
        this.prixCatalogue = prixCatalogue;
        this.idMarque = idMarque;
        this.idCategorie = idCategorie;
    }

    public int getId() { return id; }
    public String getNomModele() { return nomModele; }
    public double getPrixCatalogue() { return prixCatalogue; }
    public int getIdMarque() { return idMarque; }
    public int getIdCategorie() { return idCategorie; }

    public void setId(int id) { this.id = id; }
    public void setNomModele(String nomModele) { this.nomModele = nomModele; }
    public void setPrixCatalogue(double prixCatalogue) { this.prixCatalogue = prixCatalogue; }
    public void setIdMarque(int idMarque) { this.idMarque = idMarque; }
    public void setIdCategorie(int idCategorie) { this.idCategorie = idCategorie; }
}
