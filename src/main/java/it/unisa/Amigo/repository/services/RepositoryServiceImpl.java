package it.unisa.Amigo.repository.services;

import it.unisa.Amigo.documento.domain.Documento;
import it.unisa.Amigo.documento.service.DocumentoService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.net.MalformedURLException;
import java.util.List;

/**
 * Questa classe implementa i metodi per la logica di Business del sottosistema "Repository"
 */
@Service
@RequiredArgsConstructor
@Transactional
public class RepositoryServiceImpl implements RepositoryService {

    private final DocumentoService documentoService;

    /**
     * Aggiunge un documento @{@link Documento} alla repository.
     *
     * @param fileName da aggiungere alla repository.
     * @param bytes
     * @param mimeType
     * @return true se il documento è stato aggiunto alla repository.
     */
    @Override
    public boolean addDocumentoInRepository(final String fileName, final byte[] bytes, final String mimeType) {
        Documento documento = documentoService.addDocumento(fileName, bytes, mimeType);
        documento.setInRepository(true);
        documentoService.updateDocumento(documento);
        return true;
    }

    /**
     * Permette il download di un documento.
     *
     * @param documento @{@Link Documento}da scaricare.
     * @return Resource del documento associato.
     */
    @Override
    public Resource downloadDocumento(final Documento documento) throws MalformedURLException {
        return documentoService.loadAsResource(documento);
    }

    /**
     * Permette la ricerca di un documento @{@Link Documento}.
     *
     * @param idDocumento id del documento @{@Link Documento} da cercare.
     * @return Documento corrispondente all'id.
     */
    @Override
    public Documento findDocumentoById(final int idDocumento) {
        return documentoService.findDocumentoById(idDocumento);
    }

    /**
     * Permette la ricerca di un documento @{@Link Documento} nella repository.
     *
     * @param nameDocumento nome del documento da ricercare.
     * @return lista di documenti il cui nome contiene il nome passato come parametro.
     */

    @Override
    public List<Documento> searchDocumentInRepository(final String nameDocumento) {
        Documento documentoExample = new Documento();
        documentoExample.setInRepository(true);
        if (nameDocumento != null) {
            documentoExample.setNome(nameDocumento);
        }
        List<Documento> documenti = documentoService.searchDocumenti(documentoExample);
        return documenti;
    }
}
