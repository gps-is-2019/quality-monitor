package it.unisa.Amigo;

import it.unisa.Amigo.autenticazione.dao.UserDAO;
import it.unisa.Amigo.autenticazione.domanin.Role;
import it.unisa.Amigo.autenticazione.domanin.User;
import it.unisa.Amigo.gruppo.dao.ConsiglioDidatticoDAO;
import it.unisa.Amigo.gruppo.dao.PersonaDAO;
import it.unisa.Amigo.gruppo.dao.SupergruppoDAO;
import it.unisa.Amigo.gruppo.domain.Commissione;
import it.unisa.Amigo.gruppo.domain.ConsiglioDidattico;
import it.unisa.Amigo.gruppo.domain.Gruppo;
import it.unisa.Amigo.gruppo.domain.Persona;
import it.unisa.Amigo.task.dao.TaskDAO;
import it.unisa.Amigo.task.domain.Task;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;


@SpringBootApplication
public class AmigoApplication {
	public static void main(String[] args) {
		SpringApplication.run(AmigoApplication.class, args);
	}

		@Bean
		public CommandLineRunner demo(
				UserDAO userDAO
				, PersonaDAO personaDAO
				, ConsiglioDidatticoDAO consiglioDidatticoDAO
				, SupergruppoDAO supergruppoDAO
				, PasswordEncoder encoder
				, TaskDAO taskDAO
		){
			return args -> {

				Role userRole = new Role(Role.USER_ROLE);
				Role pqaRole = new Role(Role.PQA_ROLE);
				Role capogruppoRole = new Role(Role.CAPOGRUPPO_ROLE);
				Role ndvRole = new Role(Role.NDV_ROLE);
				Role cpdsRole = new Role(Role.CPDS_ROLE);

				User userFerrucci = new User("ferrucci@unisa.it",encoder.encode("ferrucci"));
				userFerrucci.addRole(userRole);
				userFerrucci.addRole(capogruppoRole);

				User userScarano = new User("vitsca@unisa.it",encoder.encode("scarano"));
				userScarano.addRole(userRole);

				User userMalandrino = new User("dmalandrino@unisa.it",encoder.encode("malandrino"));
				userMalandrino.addRole(userRole);
				userMalandrino.addRole(ndvRole);

				User userDePrisco= new User("robdep@unisa.it",encoder.encode("dePrisco"));
				userDePrisco.addRole(userRole);
				userDePrisco.addRole(pqaRole);
				userDePrisco.addRole(capogruppoRole);
				userDePrisco.addRole(cpdsRole);

				User userPolese = new User("gpolese@unisa.it",encoder.encode("polese"));
				userPolese.addRole(userRole);

				User userGravino = new User("gravino@unisa.it",encoder.encode("gravino"));
				userGravino.addRole(userRole);

				User userVincenzi = new User("vincenzi@unisa.it",encoder.encode("vincenzi"));
				userVincenzi.addRole(userRole);
				userVincenzi.addRole(ndvRole);

				User userRossi = new User("mariorossi@unisa.it",encoder.encode("mario"));
				userRossi.addRole(userRole);
				userRossi.addRole(cpdsRole);

				Persona ferrucci = new Persona("Filomena","Ferrucci","Professore Ordinario");
				Persona scarano = new Persona("Vittorio","Scarano","Professore Ordinario");
				Persona malandrino = new Persona("Delfina","Malandrino","Professore Associato");
				Persona dePrisco = new Persona("Roberto","De Prisco","Professore Ordinario");
				Persona polese = new Persona("Giuseppe","Polese","Professore Associato");
				Persona gravino = new Persona("Carmine","Gravino","Professore Associato");
				Persona rossi = new Persona("Mario","Rossi", "Professore Associato");
				Persona vincenzi = new Persona("Giovanni","Vincenzi", "Professore Associato");

				ferrucci.setUser(userFerrucci);
				scarano.setUser(userScarano);
				malandrino.setUser(userMalandrino);
				dePrisco.setUser(userDePrisco);
				polese.setUser(userPolese);
				gravino.setUser(userGravino);
				rossi.setUser(userRossi);
				vincenzi.setUser(userVincenzi);

				Commissione commissioneAAL = new Commissione("Accompagnamento al lavoro", "Commissione", true, "");
				Commissione commissioneEL = new Commissione("Piattaforme EL", "Commissione", true, "");

				Gruppo GAQD = new Gruppo( "GAQD-Informatica","Gruppo",true );
				GAQD.addPersona(scarano);
				GAQD.addPersona(ferrucci);
				GAQD.addPersona(dePrisco);
				GAQD.addPersona(malandrino);
				GAQD.addPersona(gravino);
				GAQD.addPersona(rossi);
				GAQD.addPersona(vincenzi);
				GAQD.setResponsabile(ferrucci);
				GAQD.addCommissione(commissioneAAL);
				GAQD.addCommissione(commissioneEL);

				commissioneAAL.addPersona(scarano);
				commissioneAAL.addPersona(malandrino);
				commissioneAAL.setResponsabile(scarano);
				commissioneAAL.addPersona(dePrisco);
				commissioneEL.addPersona(dePrisco);
				commissioneEL.setResponsabile(dePrisco);

				ConsiglioDidattico cd = new ConsiglioDidattico("Informatica");
				cd.setSupergruppo(GAQD);
				GAQD.setConsiglio(cd);
				cd.addPersona(rossi);
				cd.addPersona(ferrucci);
				cd.addPersona(scarano);
				cd.addPersona(malandrino);
				cd.addPersona(dePrisco);
				cd.addPersona(polese);
				cd.addPersona(gravino);
				cd.addPersona(vincenzi);

				//TODO add task
				Date tmpDate;
				SimpleDateFormat formatter=new SimpleDateFormat("dd-MM-yyyy");
				tmpDate = formatter.parse("1-1-2019");

				Task taskprova = new Task("t1" , tmpDate, "task1" , "in valutazione");

				taskprova.setPersona(ferrucci);
				ferrucci.addTask(taskprova);

				taskprova.setSupergruppo(GAQD);
				GAQD.addTask(taskprova);

				Task taskprova2 = new Task("t2" , tmpDate , "task2" , "approvato");
				taskprova2.setPersona(scarano);
				scarano.addTask(taskprova2);

				taskprova2.setSupergruppo(GAQD);
				GAQD.addTask(taskprova2);

				supergruppoDAO.save(GAQD);
				supergruppoDAO.save(commissioneAAL);
				supergruppoDAO.save(commissioneEL);
				consiglioDidatticoDAO.save(cd);
				personaDAO.saveAll(Arrays.asList(ferrucci, scarano, malandrino, dePrisco, polese, gravino, rossi, vincenzi));
				userDAO.saveAll(Arrays.asList(userFerrucci, userScarano, userMalandrino, userDePrisco, userPolese, userGravino, userRossi, userVincenzi));
				taskDAO.saveAll(Arrays.asList(taskprova, taskprova2));
			};
		}
	}