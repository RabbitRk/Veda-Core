package com.veda.config;

import java.io.IOException;
import java.security.Principal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import com.veda.model.auth.User;
import com.veda.service.Jwt.IJwtTokenUtil;

import jakarta.inject.Inject;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.container.PreMatching;
import jakarta.ws.rs.core.MultivaluedMap;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import jakarta.ws.rs.core.UriInfo;
import jakarta.ws.rs.ext.Provider;

@Provider
@PreMatching
public class WebConfig implements ContainerRequestFilter {
    @Inject
    IJwtTokenUtil jwtTokenUtil;
    private static Logger LOG = LoggerFactory.getLogger(WebConfig.class);
    private static String AUTHORIZATION = "Authorization";

    private static final List<String> EXCLUDED_PATHS = Arrays.asList("/api/auth/sign-up", "/api/auth/forgot-password",
            "/api/auth/authenticate", "/api/auth/refresh-token", "/api/greeting");

    @Override
    public void filter(ContainerRequestContext containerRequestContext) throws IOException {
        UriInfo info = containerRequestContext.getUriInfo();
        LOG.info("Url path ===> ", info.getPath());
        if (!EXCLUDED_PATHS.contains(info.getPath())) {
            Optional<User> user = getAuthentication(containerRequestContext.getHeaders());
            if (user.isEmpty()) {
                Response response = Response.status(Response.Status.FORBIDDEN).build();
                containerRequestContext.abortWith(response);
                return;
            }
            containerRequestContext.setSecurityContext(new JwtSecurityContext(user.get(), true));
        }
    }

    private Optional<User> getAuthentication(MultivaluedMap<String, String> headers) {

        final List<String> authorization = headers.get(AUTHORIZATION);

        if (authorization == null || authorization.isEmpty()) {
            return Optional.empty();
        }
        final String token = new String(authorization.get(0).replaceFirst("Bearer ", ""));
        try {
            User user = jwtTokenUtil.verifyToken(token);
            if (user == null || StringUtils.isEmpty(user.getId())) {
                return Optional.empty();
            }
            return Optional.of(user);
        } catch (RuntimeException e) {
            LOG.error("Error on token validation ", e);
            return Optional.empty();
        }
    }

    public static class JwtSecurityContext implements SecurityContext {
        private final com.veda.model.auth.User user;
        private final boolean secured;

        public JwtSecurityContext(User user, boolean secured) {
            this.user = user;
            this.secured = secured;
        }

        @Override
        public Principal getUserPrincipal() {
            return this.user;
        }

        @Override
        public boolean isUserInRole(String s) {
            return Boolean.TRUE; // not checking the roles
        }

        @Override
        public boolean isSecure() {
            return this.secured;
        }

        @Override
        public String getAuthenticationScheme() {
            return SecurityContext.FORM_AUTH;
        }
    }
}
