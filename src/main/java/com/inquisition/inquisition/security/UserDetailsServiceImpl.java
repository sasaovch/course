package com.inquisition.inquisition.security;

import java.util.Optional;

import com.inquisition.inquisition.jwt.JwtUtils;
import com.inquisition.inquisition.model.official.Official;
import com.inquisition.inquisition.model.user.User;
import com.inquisition.inquisition.model.user.UserRole;
import com.inquisition.inquisition.repository.OfficialRepository;
import com.inquisition.inquisition.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.inquisition.inquisition.utils.UserConverter.getRoleByOfficial;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;
    private final OfficialRepository officialRepository;

    @Autowired
    public UserDetailsServiceImpl(UserRepository userRepository, OfficialRepository officialRepository) {
        this.userRepository = userRepository;
        this.officialRepository = officialRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User Not Found with username: " + username);
        }

        Official official = officialRepository.getCurrentByPersonId(user.getPersonId());
        user.setRole(getRoleByOfficial(official));
        return UserDetailsImpl.build(user);
    }

    public User getUser() {
        String username =
                ((UsernamePasswordAuthenticationToken) SecurityContextHolder
                        .getContext()
                        .getAuthentication()).getName();
        return userRepository.findByUsername(username);
    }
}
