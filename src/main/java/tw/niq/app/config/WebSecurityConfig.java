package tw.niq.app.config;

import javax.servlet.Filter;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import tw.niq.app.filter.AuthenticationFilter;
import tw.niq.app.service.UserService;


@EnableWebSecurity
@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	
	private final Environment environment;
	private final UserService userService;
	private final BCryptPasswordEncoder bCryptPasswordEncoder;

	public WebSecurityConfig(Environment environment, UserService userService,
			BCryptPasswordEncoder bCryptPasswordEncoder) {
		this.environment = environment;
		this.userService = userService;
		this.bCryptPasswordEncoder = bCryptPasswordEncoder;
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable();
		http.headers().frameOptions().disable();
		http.authorizeRequests().antMatchers("/**").hasIpAddress(environment.getProperty("gateway.ip"))
			.and()
			.addFilter(getAuthenticationFilter());
	}

	private Filter getAuthenticationFilter() throws Exception {

		AuthenticationFilter authenticationFilter = new AuthenticationFilter(environment, userService, authenticationManager());
		authenticationFilter.setFilterProcessesUrl(environment.getProperty("login.url.path"));
		
		return authenticationFilter;
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userService).passwordEncoder(bCryptPasswordEncoder);
	}

}
