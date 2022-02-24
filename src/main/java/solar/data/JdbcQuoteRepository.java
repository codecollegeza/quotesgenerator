package solar.data;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.databind.ObjectMapper;

import solar.Quote;
import solar.User;

@Repository
public class JdbcQuoteRepository implements QuoteRepository {

  private SimpleJdbcInsert quoteInserter;
  private SimpleJdbcInsert quoteUserInserter;
  private ObjectMapper objectMapper;

  @Autowired
  public JdbcQuoteRepository(JdbcTemplate jdbc) {
    this.quoteInserter = new SimpleJdbcInsert(jdbc)
        .withTableName("User_Quote")
        .usingGeneratedKeyColumns("id");

    this.quoteUserInserter = new SimpleJdbcInsert(jdbc)
        .withTableName("User_Quote_Users");

    this.objectMapper = new ObjectMapper();
  }

  @Override
  public Quote save(Quote quote) {
	  quote.setPlacedAt(new Date());
    long quoteId = saveQuoteDetails(quote);
    quote.setId(quoteId);
    List<User> users = quote.getUsers();
    for (User user : users) {
      saveUserToQuote(user, quoteId);
    }

    return quote;
  }

  private long saveQuoteDetails(Quote quote) {
    @SuppressWarnings("unchecked")
    Map<String, Object> values =
        objectMapper.convertValue(quote, Map.class);
    values.put("placedAt", quote.getPlacedAt());

    long quoteId =
    		quoteInserter
            .executeAndReturnKey(values)
            .longValue();
    return quoteId;
  }

  private void saveUserToQuote(User user, long quoteId) {
    Map<String, Object> values = new HashMap<>();
    values.put("UserQuote", quoteId);
    values.put("user", user.getId());
    quoteUserInserter.execute(values);
  }
}
