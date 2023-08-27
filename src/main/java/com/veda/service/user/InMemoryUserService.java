package com.veda.service.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.veda.exception.UserNotFoundException;
import com.veda.model.User;
import com.veda.model.Users;

import io.quarkus.runtime.Startup;
import jakarta.enterprise.context.ApplicationScoped;

import org.apache.commons.lang3.StringUtils; 
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
 
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@ApplicationScoped
@Startup
public class InMemoryUserService  implements  IUserService{
    private static Logger LOG = LoggerFactory.getLogger(InMemoryUserService.class);
    Map<String, User> userMap = new ConcurrentHashMap<>();

    public InMemoryUserService(){
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        File file = new File(classLoader.getResource("users.yaml").getFile());
        ObjectMapper objectMapper = new ObjectMapper(new YAMLFactory());
        try {
            Users users = objectMapper.readValue(file, Users.class);
            users.getUserList().stream().forEach(user -> {
                userMap.putIfAbsent(user.getId(), user);
            });
        } catch (IOException e) {
            LOG.error("error on reading file", e);
        }
    }

    @Override
    public User findUser(String id, String password) throws UserNotFoundException {
        if (!userMap.containsKey(id)) throw new UserNotFoundException();
       Optional<User> user = Optional.of(userMap.get(id));
        if (user.isEmpty() || (!StringUtils.equals(password, user.get().getPassword()))) throw new UserNotFoundException();
        return  user.get();
    }

    @Override
    public void registerUser(User user) {
        userMap.putIfAbsent(user.getId(), user);
    }
 
}
