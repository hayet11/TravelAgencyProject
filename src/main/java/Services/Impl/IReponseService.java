package Services;
import Entities.Reponse;

import java.sql.SQLException;
import java.util.List;
public interface IReponseService extends IService<Reponse> {
    Reponse readByReclamationId(int reclamationId) throws SQLException;
}
