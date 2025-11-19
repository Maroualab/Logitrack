package com.logitrack.logitrack.service;

import com.logitrack.logitrack.exception.BusinessException;
import com.logitrack.logitrack.exception.ResourceNotFoundException;
import com.logitrack.logitrack.mapper.ClientMapper;
import com.logitrack.logitrack.dto.client.ClientDTO;
import com.logitrack.logitrack.model.Client;
import com.logitrack.logitrack.repository.ClientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClientServiceTest {

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private ClientMapper clientMapper;

    @InjectMocks
    private ClientService clientService;

    // Données de test réutilisables
    private Client client;
    private ClientDTO clientDTO;

    @BeforeEach
    void setUp() {
        // "Arrange" (Arrangement) commun
        client = new Client();
        client.setId(1L);
        client.setName("Test Client");
        client.setContactEmail("test@client.com");

        clientDTO = new ClientDTO();
        clientDTO.setId(1L);
        clientDTO.setName("Test Client");
        clientDTO.setContactEmail("test@client.com");
    }

    /**
     * Teste le cas de succès de la création.
     * CORRIGÉ : Utilise un DTO d'entrée spécifique et des stubs d'arguments exacts.
     */
    @Test
    void testCreateClient_Success() {
        // --- Arrange (Arrangement) ---

        // 1. Définir l'objet DTO que l'on envoie (sans ID)
        ClientDTO dtoToCreate = new ClientDTO();
        dtoToCreate.setName("Test Client");
        dtoToCreate.setContactEmail("test@client.com");

        // 2. Définir l'objet Entité que le mapper est censé créer
        Client clientToSave = new Client();
        clientToSave.setName("Test Client");
        clientToSave.setContactEmail("test@client.com");

        // 3. Configurer les Mocks avec des arguments PRÉCIS

        // Simuler le mapper (DTO -> Entité)
        when(clientMapper.toEntity(dtoToCreate)).thenReturn(clientToSave);

        // Simuler le repository (vérification de l'email)
        // C'est la correction clé : on attend "test@client.com", pas "null" ou ""
        when(clientRepository.findByContactEmail("test@client.com")).thenReturn(Optional.empty());

        // Simuler le repository (sauvegarde)
        // Le 'client' (avec ID) vient de notre méthode setUp()
        when(clientRepository.save(clientToSave)).thenReturn(client);

        // Simuler le mapper (Entité -> DTO)
        when(clientMapper.toDto(client)).thenReturn(clientDTO);

        // --- Act (Action) ---
        // On appelle la vraie méthode de service avec le DTO préparé
        ClientDTO result = clientService.createClient(dtoToCreate);

        // --- Assert (Vérification) ---
        // Le résultat doit être le DTO complet (avec ID)
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("test@client.com", result.getContactEmail());

        // Vérifier que les mocks ont été appelés comme prévu
        verify(clientRepository, times(1)).findByContactEmail("test@client.com");
        verify(clientRepository, times(1)).save(clientToSave);
        verify(clientMapper, times(1)).toEntity(dtoToCreate);
        verify(clientMapper, times(1)).toDto(client);
    }

    /**
     * Teste l'échec de création si l'email existe déjà.
     * (US1)
     */
    @Test
    void testCreateClient_Fails_WhenEmailExists() {
        // --- Arrange (Arrangement) ---
        // Le 'clientDTO' vient de setUp()

        // Simuler le Repository pour qu'il TROUVE un client
        when(clientRepository.findByContactEmail("test@client.com")).thenReturn(Optional.of(client));

        // --- Act & Assert (Action & Vérification) ---
        // On vérifie que l'appel lève bien une BusinessException
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            // On appelle le service avec le DTO de setUp
            clientService.createClient(clientDTO);
        });

        // On vérifie que le message d'erreur est correct
        assertTrue(exception.getMessage().contains("already exists"));

        // On vérifie que la méthode 'save' n'a JAMAIS été appelée
        verify(clientRepository, never()).save(any(Client.class));
    }

    /**
     * Teste l'échec de getById si le client n'est pas trouvé.
     */
    @Test
    void testGetClientById_Fails_NotFound() {
        // --- Arrange (Arrangement) ---
        // Simuler le Repository pour qu'il ne trouve rien
        when(clientRepository.findById(99L)).thenReturn(Optional.empty());

        // --- Act & Assert (Action & Vérification) ---
        // On vérifie que l'appel lève bien une ResourceNotFoundException
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            clientService.getClientById(99L); // 99L est un ID inexistant
        });

        assertEquals("Client not found with id: 99", exception.getMessage());
    }
}