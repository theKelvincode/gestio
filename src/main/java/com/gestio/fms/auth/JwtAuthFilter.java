package com.gestio.fms.auth;

import com.gestio.fms.auth.service.JwtService;
import com.gestio.fms.auth.service.UserService;
import com.gestio.fms.user.User;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

// this will be the filter that intercepts the request, but we actually have to make it a filter,
// we also want it to be active everytime we get a request, which is why we extend the OPRF interface.

@Component // using the component annotation to tell spring that we want this class to become a managed bean.
 // will create a constructor using any final field that is declared in the class.
public class JwtAuthFilter extends OncePerRequestFilter {

    // this is a class that can manipulate the JWT token, so we can get the userEmail from the token.

    private final JwtService jwtService;

    private final UserService userService;

    public JwtAuthFilter(JwtService jwtService, UserService userService) {
        this.jwtService = jwtService;
        this.userService = userService;
    }


    @Override
    // we can intercept all the objects, extract data from (request) and provide new data within the (response headers)
    protected void doFilterInternal(
        @NonNull HttpServletRequest request,
        @NonNull HttpServletResponse response,

        // this is the chain of responsibility design pattern, it contains the list of
        // filters that we need to execute. so when we call it(doFilter),
        // it will call the next filter.
        @NonNull FilterChain filterChain) throws ServletException, IOException {

        // we call this field so, because when we make a call, we need to pass jwt auth token within the header,
        // so it should be within the header called authorization token.
        // so we will try to extract this header.
        final String authHeader = request.getHeader("Authorization"); // the header that contains the jwt token called Authorization.
        final String jwt;
        final String userEmail;
        // let us implement the jwt token check
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }
        // extract token from auth header
        jwt = authHeader.substring(7);
        userEmail = jwtService.extractUsername(jwt);


        // check if there is user email and user is not authenticated
        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null ) // means user isnt connected yet
            {
            // get details from database
            User user = (User) this.userService.loadUserByUsername(userEmail);

            // check for validity of user
            if (jwtService.isTokenValid(jwt, user)) {
                // if token is valid, you need to update the security context and send request to dispatcher servlet
                // this auth token object is needed to update d security context.
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        user,
                        null,
                        user.getAuthorities()
                );
                // enforce the token with details of request
                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );
                // update sec context holder. // update the authentication token
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        filterChain.doFilter(request, response);
    }
}