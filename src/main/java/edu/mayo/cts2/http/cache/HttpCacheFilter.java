package edu.mayo.cts2.http.cache;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.ehcache.constructs.blocking.LockTimeoutException;
import net.sf.ehcache.constructs.web.AlreadyCommittedException;
import net.sf.ehcache.constructs.web.AlreadyGzippedException;
import net.sf.ehcache.constructs.web.PageInfo;
import net.sf.ehcache.constructs.web.filter.FilterNonReentrantException;
import net.sf.ehcache.constructs.web.filter.SimplePageCachingFilter;

import org.springframework.security.web.util.AntPathRequestMatcher;

public class HttpCacheFilter extends SimplePageCachingFilter {

	private static final AntPathRequestMatcher ADMIN_PATTERN = new AntPathRequestMatcher(
			"/system/**");
	private static final AntPathRequestMatcher CACHE_PATTERN = new AntPathRequestMatcher(
			"/cache/**");

	public HttpCacheFilter() {
		super();
	}

	protected void doFilter(final HttpServletRequest request,
			final HttpServletResponse response, final FilterChain chain)
			throws AlreadyGzippedException, AlreadyCommittedException,
			FilterNonReentrantException, LockTimeoutException, Exception {
		if (ADMIN_PATTERN.matches(request) || CACHE_PATTERN.matches(request)) {
			chain.doFilter(request, response);
		} else {
			if (response.isCommitted()) {
				throw new AlreadyCommittedException(
						"Response already committed before doing buildPage.");
			}
			logRequestHeaders(request);
			PageInfo pageInfo = buildPageInfo(request, response, chain);

			if (response.isCommitted()) {
				throw new AlreadyCommittedException(
						"Response already committed after doing buildPage"
								+ " but before writing response from PageInfo.");
			}
			writeResponse(request, response, pageInfo);
		}
	}

}
