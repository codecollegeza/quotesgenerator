package solar.data;

import solar.Measurements;

public interface MeasurementsRepository {

  Iterable<Measurements> findAll();
  
  Measurements findById(String id);
  
  Measurements save(Measurements measurement);
  
}
