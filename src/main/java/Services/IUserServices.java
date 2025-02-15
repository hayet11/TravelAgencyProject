package Services;

import Entities.Utilisateur;

import javax.mail.MessagingException;
import java.sql.SQLException;
import java.util.List;

public interface IUserServices{
    public boolean ajouterUtilisateur(Utilisateur utilisateur);
    public void supprimerUtilisateur(int id);
    public Utilisateur getUtilisateurById(int id);
    public List<Utilisateur> getAllUtilisateurs() ;
    public Utilisateur seConnecter(String email, String motDePasse);
    public  boolean emailExiste(String email);
    public boolean sInscrire(Utilisateur utilisateur);
//    public void sendEmailToAgent(Utilisateur agent) throws MessagingException;
    public int StatsBookingToDay() throws SQLException;
    public int StatsBookingGeneral() throws SQLException;
    public int StatsSumStay() throws SQLException;
    }
