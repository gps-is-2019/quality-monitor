package it.unisa.Amigo;

import it.unisa.Amigo.autenticazione.dao.UserDAO;
import it.unisa.Amigo.autenticazione.domanin.Role;
import it.unisa.Amigo.autenticazione.domanin.User;
import it.unisa.Amigo.gruppo.dao.ConsiglioDidatticoDAO;
import it.unisa.Amigo.gruppo.dao.DipartimentoDAO;
import it.unisa.Amigo.gruppo.dao.PersonaDAO;
import it.unisa.Amigo.gruppo.dao.SupergruppoDAO;
import it.unisa.Amigo.gruppo.domain.ConsiglioDidattico;
import it.unisa.Amigo.gruppo.domain.Dipartimento;
import it.unisa.Amigo.gruppo.domain.Persona;
import it.unisa.Amigo.gruppo.domain.Supergruppo;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.Arrays;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.Arrays;

@SpringBootApplication
public class AmigoApplication {

	private static final Logger log = LoggerFactory.getLogger(AmigoApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(AmigoApplication.class, args);
	}

	@Bean
	public CommandLineRunner demo(DipartimentoDAO dipartimentoDAO, UserDAO userDAO , PersonaDAO personaDAO, ConsiglioDidatticoDAO consiglioDidatticoDAO, SupergruppoDAO supergruppoDAO, PasswordEncoder encoder){
		return args -> {

			log.info("Creating two admin and user");

			Role adminRole = new Role(Role.ADMIN_ROLE);
			Role userRole = new Role(Role.USER_ROLE);


			User admin = new User("admin@c9.it",encoder.encode("admin123"));
			admin.addRole(adminRole);

			User userArmando = new User("armando@gmail.it",encoder.encode("magi123"));
			userArmando.addRole(userRole);

			User userMario = new User("mario@gmail.it.it",encoder.encode("magi123"));
			userMario.addRole(userRole);


			Persona mario = new Persona("Mario","Inglese","ciao");
			Persona armando = new Persona("Armando","Conte","ciao");
			Persona administrator = new Persona("Admin", "Admin", "ciao");

			mario.setUser(userMario);
			armando.setUser(userArmando);
			administrator.setUser(admin);

			Dipartimento dipartimento = new Dipartimento("Informatica");
			dipartimento.addPersona(armando);

			Dipartimento dipartimento1 = new Dipartimento("Ingegneria Meccanica");
			dipartimento1.addPersona(armando);


			ConsiglioDidattico cd = new ConsiglioDidattico("Informatica");
			cd.addPersona(mario);
			cd.addPersona(armando);

			Supergruppo GAQD = new Supergruppo( "GAQD-Informatica","gruppo",true );
			cd.setSupergruppo(GAQD);
			GAQD.addPersona(armando);
			GAQD.addPersona(mario);


			dipartimentoDAO.save(dipartimento);
			dipartimentoDAO.save(dipartimento1);
			consiglioDidatticoDAO.save(cd);
			supergruppoDAO.save(GAQD);
			personaDAO.save(mario);
			personaDAO.save(armando);
			personaDAO.save(administrator);
			userDAO.saveAll(Arrays.asList(userArmando,userMario,admin));

			log.info("Saved {} persona",mario);
			log.info("Saved {} persona",armando);
			log.info("Saved {} consiglioDidattico",cd);
			log.info("Saved {} supergruppo",GAQD);
			log.info("Saved {} user",userArmando);
			log.info("Saved {} user",userMario);
			log.info("Saved {} user",admin);

			log.info("----------");
		};
	}



}
