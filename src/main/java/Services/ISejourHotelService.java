package Services;

import Entities.Reservation;
import Entities.SejourHotel;

import java.sql.SQLException;
import java.util.List;

public interface ISejourHotelService extends IService<SejourHotel> {
    List<SejourHotel> getSejourHotelByCriteria(SejourHotel criteria) throws SQLException;
}
