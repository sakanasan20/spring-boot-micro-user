package tw.niq.app.filter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import tw.niq.app.dto.UserDto;
import tw.niq.app.model.LoginRequestModel;
import tw.niq.app.service.UserService;

public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {
	
	private final Environment environment;
	private final UserService userService;

	public AuthenticationFilter(Environment environment, UserService userService,
			AuthenticationManager authenticationManager) {
		this.environment = environment;
		this.userService = userService;
		super.setAuthenticationManager(authenticationManager);
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {

		try {
			
			LoginRequestModel cred = new ObjectMapper()
					.readValue(request.getInputStream(), LoginRequestModel.class);
			
			return getAuthenticationManager().authenticate(
					new UsernamePasswordAuthenticationToken(
							cred.getEmail(), 
							cred.getPassword(), 
							new ArrayList<>()));
			
		} catch (IOException ex) {
			throw new RuntimeException();
		}
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {

		String userName = ((User) authResult.getPrincipal()).getUsername();
		
		UserDto userDto = userService.getUserByEmail(userName);
		
		String token = Jwts.builder()
				.setSubject(userDto.getUserId())
				.setExpiration(new Date(System.currentTimeMillis() + Long.parseLong(environment.getProperty("token.expiration_time"))))
				.signWith(SignatureAlgorithm.HS512, environment.getProperty("token.secret"))
				.compact();
				
		response.addHeader("token", token);
		response.addHeader("userId", userDto.getUserId());
	}

}
