package Services;

import Entities.Agent;

import javax.mail.MessagingException;
import java.sql.SQLException;

public interface IAgentService extends IService<Agent>{
    public void sendEmailToAgent(Agent agent) throws MessagingException;
    public boolean Login(String login,String password) throws SQLException;
    public int StatsBookingToDay() throws SQLException;
    public int StatsBookingGeneral() throws SQLException;




}
