package modele;

public class Schema {

    public static String tUser(){ return AppConfig.get("table.user"); }
    public static String cUserId(){ return AppConfig.get("col.user.id"); }
    public static String cUserNom(){ return AppConfig.get("col.user.nom"); }
    public static String cUserPrenom(){ return AppConfig.get("col.user.prenom"); }
    public static String cUserEmail(){ return AppConfig.get("col.user.email"); }
    public static String cUserMdp(){ return AppConfig.get("col.user.mdp"); }
    public static String cUserRole(){ return AppConfig.get("col.user.role"); }
    public static String cUserDiscord(){ return AppConfig.getOrDefault("col.user.discord", "DiscordPseudo"); }

    public static String tCategorie(){ return AppConfig.get("table.categorie"); }
    public static String cCategorieId(){ return AppConfig.get("col.categorie.id"); }
    public static String cCategorieLibelle(){ return AppConfig.get("col.categorie.libelle"); }

    public static String tMarque(){ return AppConfig.get("table.marque"); }
    public static String cMarqueId(){ return AppConfig.get("col.marque.id"); }
    public static String cMarqueLibelle(){ return AppConfig.get("col.marque.libelle"); }

    public static String tVehicule(){ return AppConfig.get("table.vehicule"); }
    public static String cVehiculeId(){ return AppConfig.get("col.vehicule.id"); }
    public static String cVehiculeNom(){ return AppConfig.get("col.vehicule.nom"); }
    public static String cVehiculePrix(){ return AppConfig.get("col.vehicule.prix"); }
    public static String cVehiculeIdMarque(){ return AppConfig.get("col.vehicule.idmarque"); }
    public static String cVehiculeIdCategorie(){ return AppConfig.get("col.vehicule.idcategorie"); }

    public static String tVente(){ return AppConfig.get("table.vente"); }
    public static String cVenteId(){ return AppConfig.get("col.vente.id"); }
    public static String cVenteDate(){ return AppConfig.get("col.vente.date"); }
    public static String cVenteTotal(){ return AppConfig.get("col.vente.total"); }
    public static String cVenteIdEmploye(){ return AppConfig.get("col.vente.id_employe"); }

    public static String tConfig(){ return AppConfig.get("table.config"); }
    public static String cConfigId(){ return AppConfig.get("col.config.id"); }
    public static String cConfigValeur(){ return AppConfig.get("col.config.valeur"); }

    public static int configRowId(){
        return Integer.parseInt(AppConfig.getOrDefault("config.row.id", "1"));
    }

    public static double margeDefault(){
        return Double.parseDouble(AppConfig.getOrDefault("config.marge.default", "40"));
    }
}
