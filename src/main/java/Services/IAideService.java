package Services;

import Entities.Aide;

import java.sql.SQLException;
import java.util.List;

public interface IAideService extends IService<Aide> {
    List<Aide> searchAideByQuestion(String keyword) throws SQLException;
}

