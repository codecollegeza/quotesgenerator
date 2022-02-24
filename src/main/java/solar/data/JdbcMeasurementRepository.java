package solar.data;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import solar.Measurements;

@Repository
public class JdbcMeasurementRepository
    implements MeasurementsRepository {

  private JdbcTemplate jdbc;

  @Autowired
  public JdbcMeasurementRepository(JdbcTemplate jdbc) {
    this.jdbc = jdbc;
  }

  @Override
  public Iterable<Measurements> findAll() {
    return jdbc.query("select id, name, type from Ingredient",
        this::mapRowToMeasurement);
  }

  @Override
  public Measurements findById(String id) {
    return jdbc.queryForObject(
        "select id, name, type from Ingredient where id=?",
        this::mapRowToMeasurement, id);
  }

  @Override
  public Measurements save(Measurements measurement) {
    jdbc.update(
        "insert into Measurement (id, name, type) values (?, ?, ?)",
        measurement.getId(), 
        measurement.getName(),
        measurement.getType().toString());
    return measurement;
  }

  private Measurements mapRowToMeasurement(ResultSet rs, int rowNum)
      throws SQLException {
    return new Measurements(
        rs.getString("id"), 
        rs.getString("name"),
        Measurements.Type.valueOf(rs.getString("type")));
  }
}

