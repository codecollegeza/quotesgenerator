package solar.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import solar.Measurements;
import solar.data.MeasurementsRepository;

@Component
public class MeasurementsByIdConverter implements Converter<String, Measurements> {

  private MeasurementsRepository measurementRepo;

  @Autowired
  public MeasurementsByIdConverter(MeasurementsRepository measurementRepo) {
    this.measurementRepo = measurementRepo;
  }
  
  @Override
  public Measurements convert(String id) {
    return measurementRepo.findById(id);
  }

}
