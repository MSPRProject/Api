package fr.mspr_api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import fr.mspr_api.component.*;
import fr.mspr_api.repository.*;
import java.sql.Timestamp;


@SpringBootApplication
public class MsprAPIApplication implements CommandLineRunner {
	@Autowired
	private CountryRepository countryRepository;
	@Autowired
	private InfectionRepository infectionRepository;
	@Autowired
	private PandemicRepository pandemicRepository;
	@Autowired
	private ReportRepository reportRepository;
	public static void main(String[] args) {
		SpringApplication.run(MsprAPIApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		try{
			Country country = new Country(Continent.EUROPE, "Spain", "ESP", 70300723 );
			Country country1 = new Country(Continent.AFRICA, "Morocco", "MA", 40000000);
			Country country2 = new Country(Continent.ASIA, "China", "CHN", 9597);
			Country country3 = new Country(Continent.NORTH_AMERICA, "United_State_Of_America", "USA", 340);
			Country country4 = new Country(Continent.OCEANIA, "Australia", "AUS", 2666 );
			Country country5 = new Country(Continent.SOUTH_AMERICA, "Mexico", "MEX", 10);
			Country country6 = new Country(Continent.EUROPE, "United Kingdom", "KW", 90);
			countryRepository.save(country);
			countryRepository.save(country1);
			countryRepository.save(country2);
			countryRepository.save(country3);
			countryRepository.save(country4);
			countryRepository.save(country5);
			countryRepository.save(country6);

			Pandemic pandemic = new Pandemic("Grippe Espagnol", "Grippe", "Maladie mortel", java.sql.Timestamp.valueOf("1918-01-02 10:25:30"), java.sql.Timestamp.valueOf("1921-01-01 06:30:00"), "A fait entre 30 et 50 Millions de mort soit entre 2,5 et 5% de la population Mondiale de l'époque ");
			Pandemic pandemic1 = new Pandemic("Peste Noir", "Yersinia pestis", "Maladie extrêmement Mortel",java.sql.Timestamp.valueOf("1346-01-03 03:12:25"), java.sql.Timestamp.valueOf("1351-01-01 04:25:36"), "Trop aproximatif pour donner un nombre de mort exact");
			pandemicRepository.save(pandemic);
			pandemicRepository.save(pandemic1);

			Infection infection = new Infection(country, pandemic,300000000, 52000000);
			Infection infection1 = new Infection(country1, pandemic1, 1000000, 500000);
			infectionRepository.save(infection);
			infectionRepository.save(infection1);

			Report report = new Report(infection, java.sql.Timestamp.valueOf("2023-03-01 10:05:30"), 1000, 500);
			Report report1 = new Report(infection1, java.sql.Timestamp.valueOf("2023-01-01 00:00:00"), 100, 52);
			reportRepository.save(report);
			reportRepository.save(report1);

		}catch (Exception e){
			System.out.println("KO");
			e.printStackTrace();
		}
	}

}
