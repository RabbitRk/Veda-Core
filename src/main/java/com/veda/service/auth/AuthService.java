package com.veda.service.auth;

import java.sql.Timestamp;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import com.veda.entity.master.Profile;
import com.veda.entity.master.RefreshToken;
import com.veda.entity.master.Roles;
import com.veda.entity.master.Users;
import com.veda.exception.UserNotFoundException;
import com.veda.model.auth.LoginRequest;
import com.veda.model.auth.LoginResponse;
import com.veda.model.auth.RefreshTokenRequest;
import com.veda.model.auth.SignupRequest;
import com.veda.model.auth.User;
import com.veda.repository.master.ProfileRepository;
import com.veda.repository.master.RefreshTokenRepository;
import com.veda.repository.master.RoleRepository;
import com.veda.repository.master.UserRepository;
import com.veda.service.Jwt.IJwtTokenUtil;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class AuthService {

    @ConfigProperty(name = "jwt.refreshToken.expiration-time")
    int refreshTokenExpiryTime;

    @Inject
    RoleRepository roleRepository;

    @Inject
    UserRepository userRepository;

    @Inject
    ProfileRepository profileRepository;

    @Inject
    RefreshTokenRepository refreshTokenRepository;

    @Inject
    IJwtTokenUtil jwtTokenUtil;

    LocalDateTime now = LocalDateTime.now();

    public Profile createProfile(Users users) {

        Profile profile = new Profile();
        profile.setProfileName(users.getUserName());
        profile.setUser(users);

        profile = profileRepository.save(profile);

        return profile;
    }

    public Users createUser(SignupRequest signupRequest) {

        Users users = new Users();
        users.setUserName(signupRequest.getUserName());

        // TODO: Encypt the password
        users.setPassword(signupRequest.getPassWord());
        Set<Roles> updateRole = new HashSet<>();

        Optional<Roles> roles = roleRepository.findByRoleName(signupRequest.getType());

        if (roles.isPresent()) {
            updateRole.add(roles.get());
        } else {
            // TODO: Implement Exception
        }

        users.setRoles(updateRole);
        users = userRepository.save(users);

        return users;
    }

    public LoginResponse authenticate(LoginRequest login) throws UserNotFoundException {

        User user = verifyCredential(login);

        final String token = jwtTokenUtil.generateToken(user);

        RefreshToken refreshToken = generateRefreshToken(user.getId());

        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setToken(token);
        loginResponse.setRefreshToken(refreshToken.getRefreshToken());

        return loginResponse;
    }

    private RefreshToken generateRefreshToken(String id) {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUserID(id);
        refreshToken.setRefreshToken(UUID.randomUUID().toString());
        refreshToken.setExpiryDate(Timestamp.valueOf(now.plusDays(refreshTokenExpiryTime)));
        refreshToken = refreshTokenRepository.save(refreshToken);

        return refreshToken;
    }

    public User verifyCredential(LoginRequest login) throws UserNotFoundException {
        Optional<Users> users = userRepository.findByUserName(login.getUsername());
        User user = new User();

        if (users.isPresent()) {
            if (users.get().getPassword().equals(login.getPassword())) {
                user.setName(users.get().getUserName());
                user.setId(users.get().getId().toString());

                List<String> roles = users.get().getRoles().stream()
                        .map(Roles::getRoleName).collect(Collectors.toList());

                user.setRoles(roles);
            } else {
                throw new UserNotFoundException();
            }
        } else {
            throw new UserNotFoundException();
        }
        return user;
    }

    public LoginResponse refreshToken(RefreshTokenRequest refreshTokenRequest) throws UserNotFoundException {

        User user = new User();

        // check id
        Optional<Users> users = userRepository.findById(refreshTokenRequest.getUserID());
        Optional<RefreshToken> refreshToken = refreshTokenRepository
                .findByRefreshToken(refreshTokenRequest.getRefreshToken());

        if (!users.isPresent()) {
            throw new UserNotFoundException();
        }

        if (!refreshToken.isPresent()) {
            throw new UserNotFoundException();
        }

        Instant expiryDate = refreshToken.get().getExpiryDate().toInstant();
        Instant now = Instant.now();

        Duration difference = Duration.between(expiryDate, now);
        long daysDifference = difference.toDays();
        if (daysDifference > 7) {
            throw new UserNotFoundException();
        }

        user.setName(users.get().getUserName());
        user.setId(users.get().getId().toString());

        List<String> roles = users.get().getRoles().stream()
                .map(Roles::getRoleName).collect(Collectors.toList());

        user.setRoles(roles);

        final String token = jwtTokenUtil.generateToken(user);

        RefreshToken newRefreshToken = generateRefreshToken(user.getId());

        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setToken(token);
        loginResponse.setRefreshToken(newRefreshToken.getRefreshToken());

        return loginResponse;
    }

}
