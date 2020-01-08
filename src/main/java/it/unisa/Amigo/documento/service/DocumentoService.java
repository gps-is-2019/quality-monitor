package it.unisa.Amigo.documento.service;

import it.unisa.Amigo.documento.domain.Documento;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface DocumentoService {
    Documento addDocumento(MultipartFile file);
    Documento updateDocumento(Documento documento);
    Documento findDocumentoById(Integer idDocumento);
    List<Documento> searchDocumenti(Documento example);
    Resource loadAsResource(Documento documento);


    //TODO comunicare l'agginta della funzione per visualizzare lista documenti approvati
    List<Documento> approvedDocuments(int idSupergruppo);

}
