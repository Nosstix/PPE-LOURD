package modele;

import controleur.SimpleItem;
import controleur.User;
import controleur.Vehicule;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Modele {
    private static final BDD bdd = new BDD();

    // === AUTH (admin only) ===
    public static User selectWhereUser(String email, String mdp) {
        String sql = "SELECT " + Schema.cUserId() + "," + Schema.cUserNom() + "," + Schema.cUserPrenom() + ","
                + Schema.cUserEmail() + "," + Schema.cUserRole() + "," + Schema.cUserDiscord()
                + " FROM " + Schema.tUser()
                + " WHERE " + Schema.cUserEmail() + "=? AND " + Schema.cUserMdp() + "=? LIMIT 1";

        try (Connection cnx = bdd.getConnection();
             PreparedStatement ps = cnx.prepareStatement(sql)) {

            ps.setString(1, email);
            ps.setString(2, mdp);

            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return null;

                String role = rs.getString(5);
                if (role == null || !role.equalsIgnoreCase("admin")) return null;

                return new User(
                        rs.getInt(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getString(4),
                        null,
                        role,
                        rs.getString(6)
                );
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur login: " + e.getMessage(), e);
        }
    }

    // === USERS ===
    public static Object[][] selectAllUsers() {
        String sql = "SELECT " + Schema.cUserId() + "," + Schema.cUserNom() + "," + Schema.cUserPrenom() + ","
                + Schema.cUserEmail() + "," + Schema.cUserRole() + "," + Schema.cUserDiscord()
                + " FROM " + Schema.tUser() + " ORDER BY " + Schema.cUserId() + " DESC";

        List<Object[]> rows = new ArrayList<>();
        try (Connection cnx = bdd.getConnection();
             PreparedStatement ps = cnx.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                rows.add(new Object[]{
                        rs.getInt(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getString(4),
                        rs.getString(5),
                        rs.getString(6)
                });
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur selectAllUsers: " + e.getMessage(), e);
        }
        return rows.toArray(new Object[0][]);
    }

    public static void insertUser(User u) {
        String sql = "INSERT INTO " + Schema.tUser() + "("
                + Schema.cUserNom() + "," + Schema.cUserPrenom() + "," + Schema.cUserEmail() + ","
                + Schema.cUserMdp() + "," + Schema.cUserRole() + "," + Schema.cUserDiscord()
                + ") VALUES (?,?,?,?,?,?)";

        try (Connection cnx = bdd.getConnection();
             PreparedStatement ps = cnx.prepareStatement(sql)) {
            ps.setString(1, u.getNom());
            ps.setString(2, u.getPrenom());
            ps.setString(3, u.getEmail());
            ps.setString(4, u.getMdp());
            ps.setString(5, u.getRole());
            ps.setString(6, u.getDiscord());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erreur insertUser: " + e.getMessage(), e);
        }
    }

    public static void updateUser(User u, boolean updatePassword) {
        String sql;
        if (updatePassword) {
            sql = "UPDATE " + Schema.tUser() + " SET "
                    + Schema.cUserNom() + "=?,"
                    + Schema.cUserPrenom() + "=?,"
                    + Schema.cUserEmail() + "=?,"
                    + Schema.cUserMdp() + "=?,"
                    + Schema.cUserRole() + "=?,"
                    + Schema.cUserDiscord() + "=?"
                    + " WHERE " + Schema.cUserId() + "=?";
        } else {
            sql = "UPDATE " + Schema.tUser() + " SET "
                    + Schema.cUserNom() + "=?,"
                    + Schema.cUserPrenom() + "=?,"
                    + Schema.cUserEmail() + "=?,"
                    + Schema.cUserRole() + "=?,"
                    + Schema.cUserDiscord() + "=?"
                    + " WHERE " + Schema.cUserId() + "=?";
        }

        try (Connection cnx = bdd.getConnection();
             PreparedStatement ps = cnx.prepareStatement(sql)) {
            int i = 1;
            ps.setString(i++, u.getNom());
            ps.setString(i++, u.getPrenom());
            ps.setString(i++, u.getEmail());
            if (updatePassword) ps.setString(i++, u.getMdp());
            ps.setString(i++, u.getRole());
            ps.setString(i++, u.getDiscord());
            ps.setInt(i, u.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erreur updateUser: " + e.getMessage(), e);
        }
    }

    public static void deleteUser(int id) {
        String sql = "DELETE FROM " + Schema.tUser() + " WHERE " + Schema.cUserId() + "=?";
        try (Connection cnx = bdd.getConnection();
             PreparedStatement ps = cnx.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erreur deleteUser: " + e.getMessage(), e);
        }
    }

    // === SIMPLE ITEMS (categorie/marque) ===
    public static Object[][] selectAllCategories() {
        return selectAllSimple(Schema.tCategorie(), Schema.cCategorieId(), Schema.cCategorieLibelle());
    }
    public static void insertCategorie(SimpleItem i) {
        insertSimple(Schema.tCategorie(), Schema.cCategorieLibelle(), i.getLibelle());
    }
    public static void updateCategorie(SimpleItem i) {
        updateSimple(Schema.tCategorie(), Schema.cCategorieId(), Schema.cCategorieLibelle(), i.getId(), i.getLibelle());
    }
    public static void deleteCategorie(int id) {
        deleteSimple(Schema.tCategorie(), Schema.cCategorieId(), id);
    }

    public static Object[][] selectAllMarques() {
        return selectAllSimple(Schema.tMarque(), Schema.cMarqueId(), Schema.cMarqueLibelle());
    }
    public static void insertMarque(SimpleItem i) {
        insertSimple(Schema.tMarque(), Schema.cMarqueLibelle(), i.getLibelle());
    }
    public static void updateMarque(SimpleItem i) {
        updateSimple(Schema.tMarque(), Schema.cMarqueId(), Schema.cMarqueLibelle(), i.getId(), i.getLibelle());
    }
    public static void deleteMarque(int id) {
        deleteSimple(Schema.tMarque(), Schema.cMarqueId(), id);
    }

    public static List<SimpleItem> selectAllCategoriesList() {
        return selectSimpleList(Schema.tCategorie(), Schema.cCategorieId(), Schema.cCategorieLibelle());
    }

    public static List<SimpleItem> selectAllMarquesList() {
        return selectSimpleList(Schema.tMarque(), Schema.cMarqueId(), Schema.cMarqueLibelle());
    }

    private static Object[][] selectAllSimple(String table, String colId, String colLibelle) {
        String sql = "SELECT " + colId + "," + colLibelle + " FROM " + table + " ORDER BY " + colId + " DESC";
        List<Object[]> rows = new ArrayList<>();
        try (Connection cnx = bdd.getConnection();
             PreparedStatement ps = cnx.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) rows.add(new Object[]{ rs.getInt(1), rs.getString(2) });
        } catch (SQLException e) {
            throw new RuntimeException("Erreur selectAllSimple(" + table + "): " + e.getMessage(), e);
        }
        return rows.toArray(new Object[0][]);
    }

    private static List<SimpleItem> selectSimpleList(String table, String colId, String colLibelle) {
        String sql = "SELECT " + colId + "," + colLibelle + " FROM " + table + " ORDER BY " + colLibelle + " ASC";
        List<SimpleItem> out = new ArrayList<>();
        try (Connection cnx = bdd.getConnection();
             PreparedStatement ps = cnx.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) out.add(new SimpleItem(rs.getInt(1), rs.getString(2)));
        } catch (SQLException e) {
            throw new RuntimeException("Erreur selectSimpleList(" + table + "): " + e.getMessage(), e);
        }
        return out;
    }

    private static void insertSimple(String table, String colLibelle, String libelle) {
        String sql = "INSERT INTO " + table + "(" + colLibelle + ") VALUES (?)";
        try (Connection cnx = bdd.getConnection();
             PreparedStatement ps = cnx.prepareStatement(sql)) {
            ps.setString(1, libelle);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erreur insertSimple(" + table + "): " + e.getMessage(), e);
        }
    }

    private static void updateSimple(String table, String colId, String colLibelle, int id, String libelle) {
        String sql = "UPDATE " + table + " SET " + colLibelle + "=? WHERE " + colId + "=?";
        try (Connection cnx = bdd.getConnection();
             PreparedStatement ps = cnx.prepareStatement(sql)) {
            ps.setString(1, libelle);
            ps.setInt(2, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erreur updateSimple(" + table + "): " + e.getMessage(), e);
        }
    }

    private static void deleteSimple(String table, String colId, int id) {
        String sql = "DELETE FROM " + table + " WHERE " + colId + "=?";
        try (Connection cnx = bdd.getConnection();
             PreparedStatement ps = cnx.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erreur deleteSimple(" + table + "): " + e.getMessage(), e);
        }
    }

    // === VEHICULES (affiche marque/catégorie + garde IDs pour édition) ===
    public static Object[][] selectAllVehicules() {
        String sql = "SELECT v." + Schema.cVehiculeId() + ", v." + Schema.cVehiculeNom() + ", v." + Schema.cVehiculePrix() + ","
                + " m." + Schema.cMarqueLibelle() + " AS marqueNom,"
                + " c." + Schema.cCategorieLibelle() + " AS categorieLibelle,"
                + " v." + Schema.cVehiculeIdMarque() + ", v." + Schema.cVehiculeIdCategorie()
                + " FROM " + Schema.tVehicule() + " v"
                + " JOIN " + Schema.tMarque() + " m ON m." + Schema.cMarqueId() + " = v." + Schema.cVehiculeIdMarque()
                + " JOIN " + Schema.tCategorie() + " c ON c." + Schema.cCategorieId() + " = v." + Schema.cVehiculeIdCategorie()
                + " ORDER BY v." + Schema.cVehiculeId() + " DESC";

        List<Object[]> rows = new ArrayList<>();
        try (Connection cnx = bdd.getConnection();
             PreparedStatement ps = cnx.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                rows.add(new Object[]{
                        rs.getInt(1),
                        rs.getString(2),
                        rs.getDouble(3),
                        rs.getString(4),
                        rs.getString(5),
                        rs.getInt(6),
                        rs.getInt(7)
                });
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur selectAllVehicules: " + e.getMessage(), e);
        }
        return rows.toArray(new Object[0][]);
    }

    public static void insertVehicule(Vehicule v) {
        String sql = "INSERT INTO " + Schema.tVehicule() + "("
                + Schema.cVehiculeNom() + "," + Schema.cVehiculeIdMarque() + "," + Schema.cVehiculeIdCategorie() + "," + Schema.cVehiculePrix()
                + ") VALUES (?,?,?,?)";

        try (Connection cnx = bdd.getConnection();
             PreparedStatement ps = cnx.prepareStatement(sql)) {
            ps.setString(1, v.getNomModele());
            ps.setInt(2, v.getIdMarque());
            ps.setInt(3, v.getIdCategorie());
            ps.setDouble(4, v.getPrixCatalogue());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erreur insertVehicule: " + e.getMessage(), e);
        }
    }

    public static void updateVehicule(Vehicule v) {
        String sql = "UPDATE " + Schema.tVehicule() + " SET "
                + Schema.cVehiculeNom() + "=?,"
                + Schema.cVehiculeIdMarque() + "=?,"
                + Schema.cVehiculeIdCategorie() + "=?,"
                + Schema.cVehiculePrix() + "=?"
                + " WHERE " + Schema.cVehiculeId() + "=?";

        try (Connection cnx = bdd.getConnection();
             PreparedStatement ps = cnx.prepareStatement(sql)) {
            ps.setString(1, v.getNomModele());
            ps.setInt(2, v.getIdMarque());
            ps.setInt(3, v.getIdCategorie());
            ps.setDouble(4, v.getPrixCatalogue());
            ps.setInt(5, v.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erreur updateVehicule: " + e.getMessage(), e);
        }
    }

    public static void deleteVehicule(int id) {
        String sql = "DELETE FROM " + Schema.tVehicule() + " WHERE " + Schema.cVehiculeId() + "=?";
        try (Connection cnx = bdd.getConnection();
             PreparedStatement ps = cnx.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erreur deleteVehicule: " + e.getMessage(), e);
        }
    }

    // === VENTES (hebdo) ===
    public static Object[][] ventesGlobalHebdo() {
        String sql = "SELECT YEARWEEK(" + Schema.cVenteDate() + ", 1) AS semaine,"
                + " MIN(" + Schema.cVenteDate() + ") AS debut,"
                + " SUM(" + Schema.cVenteTotal() + ") AS total,"
                + " COUNT(*) AS nb"
                + " FROM " + Schema.tVente()
                + " GROUP BY YEARWEEK(" + Schema.cVenteDate() + ", 1)"
                + " ORDER BY semaine DESC";

        List<Object[]> rows = new ArrayList<>();
        try (Connection cnx = bdd.getConnection();
             PreparedStatement ps = cnx.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                rows.add(new Object[]{
                        rs.getInt("semaine"),
                        rs.getTimestamp("debut"),
                        rs.getDouble("total"),
                        rs.getInt("nb")
                });
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur ventesGlobalHebdo: " + e.getMessage(), e);
        }
        return rows.toArray(new Object[0][]);
    }

    public static Object[][] ventesParEmployeHebdo() {
        String sql = "SELECT u." + Schema.cUserId() + " AS idemp,"
                + " CONCAT(u." + Schema.cUserNom() + ",' ',u." + Schema.cUserPrenom() + ") AS employe,"
                + " YEARWEEK(v." + Schema.cVenteDate() + ", 1) AS semaine,"
                + " SUM(v." + Schema.cVenteTotal() + ") AS total,"
                + " COUNT(*) AS nb"
                + " FROM " + Schema.tVente() + " v"
                + " JOIN " + Schema.tUser() + " u ON u." + Schema.cUserId() + " = v." + Schema.cVenteIdEmploye()
                + " GROUP BY idemp, employe, semaine"
                + " ORDER BY semaine DESC, employe ASC";

        List<Object[]> rows = new ArrayList<>();
        try (Connection cnx = bdd.getConnection();
             PreparedStatement ps = cnx.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                rows.add(new Object[]{
                        rs.getInt("idemp"),
                        rs.getString("employe"),
                        rs.getInt("semaine"),
                        rs.getDouble("total"),
                        rs.getInt("nb")
                });
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur ventesParEmployeHebdo: " + e.getMessage(), e);
        }
        return rows.toArray(new Object[0][]);
    }

    // === CONFIG (marge globale %) ===
    public static double getMargePct() {
        String sql = "SELECT " + Schema.cConfigValeur()
                + " FROM " + Schema.tConfig()
                + " WHERE " + Schema.cConfigId() + "=? LIMIT 1";

        try (Connection cnx = bdd.getConnection();
             PreparedStatement ps = cnx.prepareStatement(sql)) {
            ps.setInt(1, Schema.configRowId());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getDouble(1);
            }
        } catch (SQLException ignored) {
        }
        return Schema.margeDefault();
    }

    public static void setMargePct(double pct) {
        if (pct < 0) pct = 0;
        if (pct > 500) pct = 500;

        String sql = "UPDATE " + Schema.tConfig() + " SET " + Schema.cConfigValeur() + "=? WHERE " + Schema.cConfigId() + "=?";
        try (Connection cnx = bdd.getConnection();
             PreparedStatement ps = cnx.prepareStatement(sql)) {
            ps.setDouble(1, pct);
            ps.setInt(2, Schema.configRowId());

            int updated = ps.executeUpdate();
            if (updated == 0) {
                String ins = "INSERT INTO " + Schema.tConfig() + "(" + Schema.cConfigId() + "," + Schema.cConfigValeur() + ") VALUES (?,?)";
                try (PreparedStatement psi = cnx.prepareStatement(ins)) {
                    psi.setInt(1, Schema.configRowId());
                    psi.setDouble(2, pct);
                    psi.executeUpdate();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur setMargePct: " + e.getMessage(), e);
        }
    }
}
