package com.notzed.springsecurity.filters;

import com.notzed.springsecurity.entity.SessionEntity;
import com.notzed.springsecurity.repository.SessionRepository;
import com.notzed.springsecurity.service.JwtService;
import com.notzed.springsecurity.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;
import java.io.IOException;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    private final SessionRepository sessionRepository;

    private final UserService userService;

    @Autowired
    @Qualifier("handlerExceptionResolver")
    private HandlerExceptionResolver handlerExceptionResolver;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7);
            Optional<SessionEntity> session = sessionRepository.findByToken(token);
            if(session.isEmpty()) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }

        }


        filterChain.doFilter(request, response);


//            String token = requestTokenHeader.split("Bearer ")[1];
//            Long userId = jwtService.getUserIdFromToken(token);
//            if (userId != null && SecurityContextHolder.getContext().getAuthentication() == null) {
//                User user = userService.getUserById(userId);
//                UsernamePasswordAuthenticationToken authenticationToken =
//                        new UsernamePasswordAuthenticationToken(user, null, null);
//                authenticationToken.setDetails(
//                        new WebAuthenticationDetailsSource().buildDetails(request)
//                );
//                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
//            }
//            filterChain.doFilter(request, response);
//        }catch (Exception ex){
//            handlerExceptionResolver.resolveException(request, response, null, ex);
//        }
    }


}
