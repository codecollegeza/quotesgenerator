package solar.data;

import solar.Quote;

public interface QuoteRepository {

  Quote save(Quote order);
  
}
