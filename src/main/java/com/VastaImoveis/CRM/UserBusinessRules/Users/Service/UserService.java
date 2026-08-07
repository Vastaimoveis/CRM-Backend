package com.VastaImoveis.CRM.UserBusinessRules.Users.Service;

import com.VastaImoveis.CRM.Exception.BusinessException;
import com.VastaImoveis.CRM.UserBusinessRules.Permissions.Entity.Domain.PermissionName;
import com.VastaImoveis.CRM.UserBusinessRules.Roles.Entity.Domain.Role;
import com.VastaImoveis.CRM.UserBusinessRules.Roles.Repository.RoleRepository;
import com.VastaImoveis.CRM.UserBusinessRules.Users.Entity.dto.updateUserRoleDto;
import com.VastaImoveis.CRM.shared.utils.SecurityUtils;
import com.VastaImoveis.CRM.UserBusinessRules.Users.Entity.Domain.RegiaoUsers;
import com.VastaImoveis.CRM.UserBusinessRules.Users.Entity.Domain.User;
import com.VastaImoveis.CRM.UserBusinessRules.Users.Entity.dto.UserRequestDTO;
import com.VastaImoveis.CRM.UserBusinessRules.Users.Entity.dto.UserResponseDTO;
import com.VastaImoveis.CRM.UserBusinessRules.Users.Repository.UserRepository;
import com.VastaImoveis.CRM.UserBusinessRules.Users.mapper.UserMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static com.VastaImoveis.CRM.UserBusinessRules.Users.mapper.UserMapper.toDTO;

@Service
public class UserService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    public UserService(UserRepository repository,
                       PasswordEncoder passwordEncoder,
                       RoleRepository roleRepository) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }


    public UserResponseDTO create(UserRequestDTO dto) {
        User userAtual = SecurityUtils.getCurrentUser();
        assert userAtual != null;
        if (!userAtual.hasPermission(PermissionName.USER_CREATE)) {
            throw new BusinessException("Você não tem permissão para criar um usuário");
        }

        String email = dto.getEmail().toLowerCase().trim();
        if (repository.existsByEmail(email)) {
            throw new BusinessException("Email já cadastrado");
        }

        String telefone = dto.getTelefone().toLowerCase().trim();
        Role role = roleRepository.findById(dto.getRole())
                .orElseThrow(() -> new BusinessException("Role não encontrada"));
        User user = new User();
        user.setNome(dto.getNome());
        user.setEmail(email);
        user.setTelefone(telefone);
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setRole(role);
        return toDTO(repository.save(user));
    }

    public Page<UserResponseDTO> listUserByRegiao(RegiaoUsers regiaoUsers, Pageable pageable) {
        User user = SecurityUtils.getCurrentUser();
        assert user != null;
        if (user.hasPermission(PermissionName.USER_VIEW)) {
            throw new BusinessException("Você não tem acesso a essa chamada");
        }

        return repository.findByRegiao(regiaoUsers, pageable).map(UserMapper::toDTO);
    }

    public UserResponseDTO update(UUID id, UserRequestDTO dto) {
        User userAtual = SecurityUtils.getCurrentUser();
        assert userAtual != null;
        if (userAtual.hasPermission(PermissionName.USER_EDIT)) {
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
            Role role = roleRepository.findById(dto.getRole())
                    .orElseThrow(() -> new BusinessException("Cargo não encontrado ao atualizar um usuário"));
            user.setRole(role);
        }

        return toDTO(repository.save(user));
    }

    public UserResponseDTO patchRole(UUID id, updateUserRoleDto roleId) {
        Role role = roleRepository.findById(roleId.roleId())
                .orElseThrow(() -> new BusinessException("Role não encontrada ao atualizar a role do user"));

        User user = repository.findById(id)
                .orElseThrow(() -> new BusinessException("Erro ao buscar usuário"));

        if (user.getRole().getName().equalsIgnoreCase(role.getName())) {
            throw new BusinessException("A role do user já é a mesma indicada");
        }
        user.setRole(role);
        return UserMapper.toDTO(repository.save(user));
    }

    public UserResponseDTO findById(UUID id) {
        return UserMapper.toDTO(repository.findById(id).orElseThrow(() -> new BusinessException("Erro ao buscar usuário")));
    }

    public Page<UserResponseDTO> findAll(Pageable pageable) {


        return repository.findAll(pageable).map(UserMapper::toDTO);
    }

    public void delete(UUID id) {
        try {
            User user = repository.findById(id).orElseThrow(() -> new BusinessException("Erro ao buscar usuário"));
            repository.delete(user);
        } catch (BusinessException e) {
            throw new BusinessException("Erro ao deletar usuário");
        }
    }

}