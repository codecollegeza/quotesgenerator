package solar.data;

import java.sql.Timestamp;
import java.sql.Types;
import java.util.Arrays;
import java.util.Date;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementCreatorFactory;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import solar.Measurements;
import solar.User;

@Repository
public class JdbcUserRepository implements UserRepository {

  private JdbcTemplate jdbc;

  public JdbcUserRepository(JdbcTemplate jdbc) {
    this.jdbc = jdbc;
  }

  @Override
  public User save(User user) {
    long userId = saveUserInfo(user);
    user.setId(userId);
    for (Measurements measurement : user.getMeasurements()) {
      saveMeasurementsToUser(measurement, userId);
    }

    return user;
  }

  private long saveUserInfo(User user) {
    user.setCreatedAt(new Date());
    PreparedStatementCreator psc =
        new PreparedStatementCreatorFactory(
            "insert into User (name, createdAt) values (?, ?)",
            Types.VARCHAR, Types.TIMESTAMP
        ).newPreparedStatementCreator(
            Arrays.asList(
                user.getName(),
                new Timestamp(user.getCreatedAt().getTime())));

    KeyHolder keyHolder = new GeneratedKeyHolder();
    jdbc.update(psc, keyHolder);

    return keyHolder.getKey().longValue();
  }

  private void saveMeasurementsToUser(
		  Measurements measurement, long userId) {
    jdbc.update(
        "insert into User_Measurements (user, measurement) " +
        "values (?, ?)",
        userId, measurement.getId());
  }

}
