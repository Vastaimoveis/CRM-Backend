package com.VastaImoveis.CRM.Users.Service;

import com.VastaImoveis.CRM.Exception.BusinessException;
import com.VastaImoveis.CRM.shared.utils.SecurityUtils;
import com.VastaImoveis.CRM.Users.Entity.Domain.RegiaoUsers;
import com.VastaImoveis.CRM.Users.Entity.Domain.RoleUsers;
import com.VastaImoveis.CRM.Users.Entity.Domain.User;
import com.VastaImoveis.CRM.Users.Entity.dto.UserRequestDTO;
import com.VastaImoveis.CRM.Users.Entity.dto.UserResponseDTO;
import com.VastaImoveis.CRM.Users.Repository.UserRepository;
import com.VastaImoveis.CRM.Users.mapper.UserMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static com.VastaImoveis.CRM.Users.mapper.UserMapper.toDTO;

@Service
public class UserService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository repository,
                       PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }


    public UserResponseDTO create(UserRequestDTO dto) {
        User userAtual = SecurityUtils.getCurrentUser();
        if(!userAtual.getRole().name().equals("GERENTE")){
            throw new BusinessException("Você não tem permissão para criar um usuário");
        }

        String email = dto.getEmail().toLowerCase().trim();
        if (repository.existsByEmail(email)) {
            throw new BusinessException("Email já cadastrado");
        }

        String telefone = dto.getTelefone().toLowerCase().trim();

        User user = new User();
        user.setNome(dto.getNome());
        user.setEmail(email);
        user.setTelefone(telefone);
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setRole(dto.getRole() != null ? dto.getRole() : RoleUsers.CORRETOR);

        return toDTO(repository.save(user));
    }

    public Page<UserResponseDTO> listUserByRegiao(RegiaoUsers regiaoUsers, Pageable pageable){
        User user = SecurityUtils.getCurrentUser();
        if(!user.getRole().name().equals("GERENTE")){
            throw new BusinessException("Você não tem acesso a essa chamada");
        }

        return repository.findByRegiao(regiaoUsers, pageable).map(UserMapper::toDTO);
    }

    public UserResponseDTO update(UUID id, UserRequestDTO dto) {
        User userAtual = SecurityUtils.getCurrentUser();
        if(!userAtual.getRole().name().equals("GERENTE")){
            throw new BusinessException("Você não tem acesso a essa chamada");
        }
        User user = repository.findById(id)
                .orElseThrow(() -> new BusinessException("Usuário não encontrado"));

        // 🔥 Normalização
        String email = dto.getEmail().toLowerCase().trim();

        // 🔥 Verifica duplicidade (se mudou o email)
        if (!user.getEmail().equals(email) &&
                repository.existsByEmail(email)) {
            throw new BusinessException("Email já cadastrado");
        }

        // 🔥 Atualizações seguras
        user.setNome(dto.getNome());
        user.setEmail(email);

        // ⚠️ Senha: só atualiza se vier preenchida
        if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(dto.getPassword()));
        }

        // ⚠️ Role: opcional (depende da sua regra de negócio)
        if (dto.getRole() != null) {
            user.setRole(dto.getRole());
        }

        return toDTO(repository.save(user));
    }

    public UserResponseDTO findById(UUID id){
        return  UserMapper.toDTO(repository.findById(id).orElseThrow(() -> new BusinessException("Erro ao buscar usuário")));
    }

    public Page<UserResponseDTO> findAll(Pageable pageable){


        return repository.findAll(pageable).map(UserMapper::toDTO);
    }

    public void delete(UUID id){
        try{
            User user = repository.findById(id).orElseThrow(() -> new BusinessException("Erro ao buscar usuário"));
            repository.delete(user);
        } catch(BusinessException e) {
            throw new BusinessException("Erro ao deletar usuário");
        }
    }

}