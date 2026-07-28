package com.VastaImoveis.CRM.Requisitos.Service;

import com.VastaImoveis.CRM.Exception.ResourceNotFoundException;
import com.VastaImoveis.CRM.Requisitos.Entity.Domain.Requisito;
import com.VastaImoveis.CRM.Requisitos.Entity.Domain.StatusRequisito;
import com.VastaImoveis.CRM.Requisitos.Entity.dto.RequisitoRequestDTO;
import com.VastaImoveis.CRM.Requisitos.Entity.dto.RequisitoResponseDTO;
import com.VastaImoveis.CRM.Requisitos.Repository.RequisitosRepository;
import com.VastaImoveis.CRM.Requisitos.mapper.RequisitoMapper;
import com.VastaImoveis.CRM.UserBusinessRules.Users.Entity.Domain.User;
import com.VastaImoveis.CRM.UserBusinessRules.Users.Repository.UserRepository;
import com.VastaImoveis.CRM.shared.utils.SecurityUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class RequisitosService {
    private final RequisitosRepository repository;
    private final UserRepository userRepository;

    public RequisitosService(RequisitosRepository repository, UserRepository userRepository) {
        this.repository = repository;
        this.userRepository = userRepository;
    }

    public RequisitoResponseDTO create(RequisitoRequestDTO dto) {
        User user = SecurityUtils.getCurrentUser();
        System.out.println(dto.getCorretor());
        System.out.println(dto.getGerente());
        User corretor = userRepository.findById(dto.getCorretor())
                .orElseThrow(() -> new ResourceNotFoundException("Corretor não existe"));
        User gerente = userRepository.findById(dto.getGerente())
                .orElseThrow(() -> new ResourceNotFoundException("Gerente não existe"));

        Requisito requisito = RequisitoMapper.toEntity(dto);
        requisito.setCorretorId(corretor);
        requisito.setGerenteId(gerente);

        return RequisitoMapper.toDTO(repository.save(requisito));
    }

    public Page<RequisitoResponseDTO> findAll(Pageable pageable){
        User user = SecurityUtils.getCurrentUser();

        if(user.getRole().name().equals("GERENTE")){
            return repository.findByGerente(user, pageable)
                    .map(RequisitoMapper::toDTO);
        }

        return repository.findByCorretor(user,pageable)
                .map(RequisitoMapper::toDTO);
    }

    public RequisitoResponseDTO findById(UUID id){
        return RequisitoMapper.toDTO(repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Requisito não encontrado")));
    }

    public Page<RequisitoResponseDTO> findAllByUserId(UUID userId, Pageable pageable){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));

        if(user.getRole().name().equals("GERENTE")){
            return repository.findByGerente(user, pageable)
                    .map(RequisitoMapper::toDTO);
        }

        return repository.findByCorretor(user, pageable)
                .map(RequisitoMapper::toDTO);

    }

    public RequisitoResponseDTO update(UUID id, RequisitoRequestDTO dto){
        Requisito requisito = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Requisito não encontrado"));

        Requisito updated = repository.save(RequisitoMapper.updateEntity(requisito, dto));
        return RequisitoMapper.toDTO(updated);
    }


    public RequisitoResponseDTO updateStatus(UUID id, StatusRequisito status){
        Requisito requisito = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Requisito não encontrado"));

        Requisito updated = repository.save(RequisitoMapper.updateEntityStatus(requisito, status));
        return RequisitoMapper.toDTO(updated);
    }

    public void Delete(UUID id){
        Requisito requisito = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Requisito não encontrado"));
        repository.delete(requisito);
    }
}
