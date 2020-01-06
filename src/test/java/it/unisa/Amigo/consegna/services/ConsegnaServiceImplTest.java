package it.unisa.Amigo.consegna.services;

import it.unisa.Amigo.autenticazione.configuration.UserDetailImpl;
import it.unisa.Amigo.autenticazione.domanin.Role;
import it.unisa.Amigo.autenticazione.domanin.User;
import it.unisa.Amigo.consegna.dao.ConsegnaDAO;
import it.unisa.Amigo.consegna.domain.Consegna;
import it.unisa.Amigo.documento.dao.DocumentoDAO;
import it.unisa.Amigo.documento.domain.Documento;
import it.unisa.Amigo.documento.service.DocumentoService;
import it.unisa.Amigo.documento.service.DocumentoServiceImpl;
import it.unisa.Amigo.gruppo.domain.Persona;
import it.unisa.Amigo.gruppo.services.GruppoServiceImpl;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.multipart.MultipartFile;

import javax.swing.text.Document;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;


@SpringBootTest

class ConsegnaServiceImplTest {

    @Mock
    private  DocumentoServiceImpl documentoService;

    @Mock
    private  GruppoServiceImpl gruppoService;

    @Mock
    private ConsegnaDAO consegnaDAO;

    @InjectMocks
    private  ConsegnaServiceImpl consegnaService;


    @ParameterizedTest
    @MethodSource("provideDocumento")
    void sendDocumento(int[] idDestinatari, String locazione, MultipartFile file) {

        Persona expectedPersona1 = new Persona("Admin", "123", "Administrator");
        Persona expectedPersona2 = new Persona("123", "null", "Administrator");

        Documento doc = new Documento();
        doc.setDataInvio(LocalDate.now());
        doc.setNome(file.getOriginalFilename());
        doc.setInRepository(false);
        doc.setFormat(file.getContentType());

        Consegna consegna1 = new Consegna();
        consegna1.setDataConsegna(LocalDate.now());
        consegna1.setStato("da valutare");
        consegna1.setDocumento(doc);

        when(documentoService.addDocumento(file)).thenReturn(doc);
        when(gruppoService.getAuthenticatedUser()).thenReturn(expectedPersona1);
        when(gruppoService.findPersona(expectedPersona1.getId())).thenReturn(expectedPersona1);
        when(gruppoService.findPersona(expectedPersona2.getId())).thenReturn(expectedPersona2);


        List<Consegna> expectedConsegne = new ArrayList<>();

        if (idDestinatari != null) {
            for (int id : idDestinatari) {
                Consegna consegna = new Consegna();
                consegna.setDataConsegna(LocalDate.now());
                consegna.setStato("da valutare");
                consegna.setDocumento(doc);
                consegna.setMittente(gruppoService.getAuthenticatedUser());
                consegna.setLocazione(Consegna.USER_LOCAZIONE);
                consegna.setDestinatario(gruppoService.findPersona(id));
                expectedConsegne.add(consegna);
            }
            assertEquals(expectedConsegne.size(), consegnaService.sendDocumento(idDestinatari, locazione, file).size());
        }
        else {
            Consegna consegna = new Consegna();
            consegna.setDataConsegna(LocalDate.now());
            consegna.setStato("da valutare");
            consegna.setDocumento(doc);
            consegna.setMittente(gruppoService.getAuthenticatedUser());
            if (locazione.equalsIgnoreCase(Consegna.PQA_LOCAZIONE))
                consegna.setLocazione(Consegna.PQA_LOCAZIONE);
            if (locazione.equalsIgnoreCase(Consegna.NDV_LOCAZIONE))
                consegna.setLocazione(Consegna.NDV_LOCAZIONE);
            expectedConsegne.add(consegna);
            assertEquals(expectedConsegne.size(), consegnaService.sendDocumento(idDestinatari, locazione, file).size());
        }
    }

    private static Stream<Arguments> provideDocumento() {

        Persona expectedPersona1 = new Persona("Admin", "123", "Administrator");
        Persona expectedPersona2 = new Persona("123", "112", "Administrator");
        Persona expectedPersona3 = new Persona("123", "Boh", "Administrator");

        String NDV_LOCAZIONE = "NDV";
        String USER_LOCAZIONE = "USER";

        int[] ids = {expectedPersona2.getId(), expectedPersona3.getId()};
        int[] ids2 = {expectedPersona3.getId()};

        MockMultipartFile file =
                new MockMultipartFile(
                        "file",
                        "test contract.pdf",
                        MediaType.APPLICATION_PDF_VALUE,
                        "<<pdf data>>".getBytes(StandardCharsets.UTF_8));

        return Stream.of(
                Arguments.of(ids, NDV_LOCAZIONE, file),
                Arguments.of(ids2, USER_LOCAZIONE, file)
        );
    }



    @Test
    void downloadDocumento() {
    }

    @Test
    void consegneInviate() {
        Consegna consegna = new Consegna();
        Consegna consegna1 = new Consegna();
        User user = new User("admin", "admin");
        Set<Role> ruoli = new HashSet<Role>();
        ruoli.add(new Role(Role.NDV_ROLE));
        user.setRoles(ruoli);
        UserDetailImpl userDetails = new UserDetailImpl(user);
        Persona persona = new Persona("persona", "persona", "PQA");
        consegna.setMittente(persona);
        consegna1.setMittente(persona);
        List<Consegna> expectedConsegne = new ArrayList<>();
        expectedConsegne.add(consegna);
        expectedConsegne.add(consegna1);

        when(consegnaDAO.findAllByMittente(gruppoService.getAuthenticatedUser())).thenReturn(expectedConsegne);

        assertEquals(expectedConsegne, consegnaDAO.findAllByMittente(gruppoService.getAuthenticatedUser()));
    }


    @Test
    void consegneRicevute() {
        Consegna consegna = new Consegna();
        Consegna consegna1 = new Consegna();
        User user = new User("admin", "admin");
        Set<Role> ruoli = new HashSet<Role>();
        ruoli.add(new Role(Role.PQA_ROLE));
        user.setRoles(ruoli);
        UserDetailImpl userDetails = new UserDetailImpl(user);
        Persona persona = new Persona("persona", "persona", "PQA");
        persona.setUser(user);
        consegna.setDestinatario(persona);
        consegna1.setDestinatario(persona);
        List<Consegna> expectedConsegne = new ArrayList<>();
        expectedConsegne.add(consegna);
        expectedConsegne.add(consegna1);

        when(gruppoService.getAuthenticatedUser()).thenReturn(persona);
        when(consegnaDAO.findAllByMittente(gruppoService.getAuthenticatedUser())).thenReturn(expectedConsegne);
        when(consegnaDAO.findAllByLocazione(Consegna.PQA_LOCAZIONE)).thenReturn(expectedConsegne);
        when(consegnaDAO.findAllByLocazione(Consegna.NDV_LOCAZIONE)).thenReturn(expectedConsegne);

        assertEquals(expectedConsegne, consegnaService.consegneRicevute());
    }

    @Test
    void findConsegnaByDocumento() {
        Documento doc = new Documento();
        doc.setDataInvio(LocalDate.now());


        Consegna expectedConsegna = new Consegna();
        expectedConsegna.setDataConsegna(LocalDate.now());
        expectedConsegna.setStato("da valutare");
        expectedConsegna.setDocumento(doc);

        when(consegnaDAO.findByDocumento_Id(doc.getId())). thenReturn(expectedConsegna);
        assertEquals(expectedConsegna, consegnaService.findConsegnaByDocumento(doc.getId()));
    }

    @Test
    void testSendDocumento() {
    }

    @Test
    void testDownloadDocumento() {
    }

    @Test
    void testConsegneInviate() {
    }

    @Test
    void testConsegneRicevute() {
    }

    @Test
    void testFindConsegnaByDocumento() {
    }

    @Test
    void possibiliDestinatari() {
    }
}