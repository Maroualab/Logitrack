package com.logitrack.logitrack.mapper;


import com.logitrack.logitrack.dto.client.ClientDTO;
import com.logitrack.logitrack.model.Client;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ClientMapper {

    ClientDTO toDto(Client client);

    Client toEntity(ClientDTO clientDTO);

    List<ClientDTO> toDtoList(List<Client> clients);
}