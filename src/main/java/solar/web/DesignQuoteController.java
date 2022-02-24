package solar.web;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

import solar.Measurements;
import solar.Quote;
import solar.User;
import solar.Measurements.Type;
import solar.data.MeasurementsRepository;
import solar.data.UserRepository;

@Controller
@RequestMapping("/design")
@SessionAttributes("order")
public class DesignQuoteController {

  private final MeasurementsRepository measurementRepo;

  private UserRepository designRepo;

  @Autowired
  public DesignQuoteController(
        MeasurementsRepository measurementRepo, 
        UserRepository designRepo) {
    this.measurementRepo = measurementRepo;
    this.designRepo = designRepo;
  }

  @ModelAttribute(name = "order")
  public Quote order() {
    return new Quote();
  }
  
  @ModelAttribute(name = "taco")
  public User taco() {
    return new User();
  }

  @GetMapping
  public String showDesignForm(Model model) {
    List<Measurements> measurements = new ArrayList<>();
    measurementRepo.findAll().forEach(i -> measurements.add(i));
    
    Type[] types = Measurements.Type.values();
    for (Type type : types) {
      model.addAttribute(type.toString().toLowerCase(), 
          filterByType(measurements, type));      
    }

    return "design";
  }

  @PostMapping
  public String processDesign(
      @Valid User design, Errors errors, 
      @ModelAttribute Quote order) {

    if (errors.hasErrors()) {
      return "design";
    }

    User saved = designRepo.save(design);
    order.addDesign(saved);

    return "redirect:/orders/current";
  }
 
  private List<Measurements> filterByType(
      List<Measurements> measurements, Type type) {
    return measurements
              .stream()
              .filter(x -> x.getType().equals(type))
              .collect(Collectors.toList());
  }
}
