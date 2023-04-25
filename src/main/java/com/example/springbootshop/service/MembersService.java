package com.example.springbootshop.service;

import com.example.springbootshop.dto.MembersUpdateDto;
import com.example.springbootshop.entity.Members;
import com.example.springbootshop.repository.MembersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class MembersService implements UserDetailsService {

    private final MembersRepository membersRepository;

    private final PasswordEncoder passwordEncoder;

    public Members getMembers(String email) {
        return membersRepository.findByEmail(email);
    }

    public Members saveMembers(Members members) {
        validateDuplicateMembers(members);
        return membersRepository.save(members);
    }

    private void validateDuplicateMembers(Members members) {
        Members findMembers = membersRepository.findByEmail(members.getEmail());
        if (findMembers != null) {
            throw new IllegalStateException("이미 가입된 회원입니다.");
        }
    }

    public void updateMembers(MembersUpdateDto membersUpdateDto, String email) {
        Members members = membersRepository.findByEmail(email);
        String encPassword = passwordEncoder.encode(membersUpdateDto.getPassword());
        members.update(membersUpdateDto.getName(), encPassword);
    }

    @Override//스프링 시큐리티에서 유저의 정보를 가져오기 위함
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Members members = membersRepository.findByEmail(email);

        if (email == null) {
            throw new UsernameNotFoundException(email);
        }
        return User.builder()
                .username(members.getEmail())
                .password(members.getPassword())
                .roles(members.getRole().toString())
                .build()
                ;
    }


    public Members findMembersByEmail(String email) {
        return membersRepository.findByEmail(email);
    }
}
