package Services;

import Entities.Vol;

import java.sql.SQLException;

public interface IVolServices extends IService<Vol>{
    public int StatsFlightToDay() throws SQLException;
    public int StatsFlightGneral() throws SQLException;
    public float SumFlights() throws SQLException;
}
