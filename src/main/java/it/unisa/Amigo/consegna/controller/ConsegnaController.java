package it.unisa.Amigo.consegna.controller;

import it.unisa.Amigo.autenticazione.domanin.Role;
import it.unisa.Amigo.consegna.domain.Consegna;
import it.unisa.Amigo.consegna.services.ConsegnaService;
import it.unisa.Amigo.gruppo.domain.Persona;
import it.unisa.Amigo.gruppo.services.GruppoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Controller
public class ConsegnaController {
    @Autowired
    private ConsegnaService consegnaService;

    @Autowired
    private GruppoService gruppoService;

    @GetMapping("/consegna")
    public String viewConsegna(Model model) {
        Persona personaLoggata = gruppoService.getAuthenticatedUser();
        Set<Role> ruoli = personaLoggata.getUser().getRoles();
        List<String> ruoliString = new ArrayList<>();
        for (Role r : ruoli) {
            if (r.getName().equalsIgnoreCase(Role.PQA_ROLE)) {
                ruoliString.add(Role.CAPOGRUPPO_ROLE);
                ruoliString.add(Role.NDV_ROLE);
            }
            if (r.getName().equalsIgnoreCase(Role.CPDS_ROLE)) {
                ruoliString.add(Role.NDV_ROLE);
                ruoliString.add(Role.PQA_ROLE);
            }
            if (r.getName().equalsIgnoreCase(Role.CAPOGRUPPO_ROLE)) {
                ruoliString.add(Role.PQA_ROLE);
            }
        }
        model.addAttribute("personaLoggata", personaLoggata);
        model.addAttribute("ruoliString", ruoliString);
        return "consegna/destinatari";
    }

    @GetMapping("/consegna/{ruolo}")
    public String sendDocumento(Model model, @PathVariable("ruolo") String ruolo) {
        List<Persona> destinatari = gruppoService.findAllByRuolo(ruolo);
        model.addAttribute("destinatari", destinatari);
        return "consegna/invio-consegna";
    }

    @PostMapping("/consegna")
    public String sendDocumento(Model model, @RequestParam MultipartFile file, @RequestParam String destinatariPost) {
        String[] destinatariString = destinatariPost.split(",");
        int[] destinatariInt = new int[destinatariString.length];
        int i = 0;

        for (String p : destinatariString)
            destinatariInt[i++] = Integer.parseInt(p);

        consegnaService.sendDocumento(destinatariInt, file);

        return "redirect:/consegna/inviati?name=";
    }

    @GetMapping("/consegna/inviati")
    public String findConsegneInviate(Model model, @RequestParam("name") String name) {
        List<Consegna> consegne = consegnaService.consegneInviate(gruppoService.getAuthenticatedUser());
        List<Consegna> consegneReturn = new ArrayList<>();

        for (Consegna c : consegne)
            if (c.getDocumento().getNome().toLowerCase().contains(name.toLowerCase()) || name.equals(""))
                consegneReturn.add(c);

        model.addAttribute("consegne", consegneReturn);
        return "consegna/documenti-inviati";
    }

    @GetMapping("/consegna/ricevuti")
    public String findConsegneRicevute(Model model, @RequestParam("name") String name) {
        List<Consegna> consegne = consegnaService.consegneRicevute(gruppoService.getAuthenticatedUser());
        List<Consegna> consegneReturn = new ArrayList<>();

        for (Consegna c : consegne)
            if (c.getDocumento().getNome().toLowerCase().contains(name.toLowerCase()) || name.equals(""))
                consegneReturn.add(c);

        model.addAttribute("consegne", consegneReturn);
        return "consegna/documenti-ricevuti";
    }

    @GetMapping("/consegna/miei-documenti/{idDocumento}")
    public ResponseEntity<Resource> downloadDocumento(Model model, @PathVariable("idDocumento") int idDocumento) {
        return consegnaService.downloadDocumento(idDocumento);
    }

    //TODO Aggiungere gente al NdV, PQA, CPDS. Controllo permessi di invio. Filtro documenti ricevuti/inviati.
    //TODO Sicurezza sui controller.
}