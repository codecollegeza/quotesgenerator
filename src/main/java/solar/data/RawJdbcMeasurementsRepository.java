package solar.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;

import solar.Measurements;


public class RawJdbcMeasurementsRepository implements MeasurementsRepository {

  private DataSource dataSource;

  public RawJdbcMeasurementsRepository(DataSource dataSource) {
    this.dataSource = dataSource;
  }
  
  @Override
  public Iterable<Measurements> findAll() {
    List<Measurements> measurements = new ArrayList<>();
    Connection connection = null;
    PreparedStatement statement = null;
    ResultSet resultSet = null;
    try {
      connection = dataSource.getConnection();
      statement = connection.prepareStatement(
          "select id, name, type from Ingredient");
      resultSet = statement.executeQuery();
      while(resultSet.next()) {
    	  Measurements measurement = new Measurements(
            resultSet.getString("id"),
            resultSet.getString("name"),
            Measurements.Type.valueOf(resultSet.getString("type")));
    	  measurements.add(measurement);
      }      
    } catch (SQLException e) {
      // ??? What should be done here ???
    } finally {
      if (resultSet != null) {
        try {
          resultSet.close();
        } catch (SQLException e) {}
      }
      if (statement != null) {
        try {
          statement.close();
        } catch (SQLException e) {}
      }
      if (connection != null) {
        try {
          connection.close();
        } catch (SQLException e) {}
      }
    }
    return measurements;
  }
 
  @Override
  public Measurements findById(String id) {
    Connection connection = null;
    PreparedStatement statement = null;
    ResultSet resultSet = null;
    try {
      connection = dataSource.getConnection();
      statement = connection.prepareStatement(
          "select id, name, type from Ingredient");
      statement.setString(1, id);
      resultSet = statement.executeQuery();
      Measurements measurement = null;
      if(resultSet.next()) {
    	  measurement = new Measurements(
            resultSet.getString("id"),
            resultSet.getString("name"),
            Measurements.Type.valueOf(resultSet.getString("type")));
      } 
      return measurement;
    } catch (SQLException e) {
      // ??? What should be done here ???
    } finally {
      if (resultSet != null) {
        try {
          resultSet.close();
        } catch (SQLException e) {}
      }
      if (statement != null) {
        try {
          statement.close();
        } catch (SQLException e) {}
      }
      if (connection != null) {
        try {
          connection.close();
        } catch (SQLException e) {}
      }
    }
    return null;
  }
  // end::rawfindOne[]
  
  @Override
  public Measurements save(Measurements measurement) {
    // TODO: I only needed one method for comparison purposes, so
    //       I've not bothered implementing this one (yet).
    return null;
  }
  
}
