package controleur;

import modele.Modele;

public class Controleur {
    public static User login(String email, String mdp)
    {
        return Modele.selectWhereUser(email, mdp);
    }
}
