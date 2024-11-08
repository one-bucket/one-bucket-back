package com.onebucket.global.auth.jwtAuth.component;

import com.onebucket.domain.memberManage.dao.MemberRepository;
import com.onebucket.domain.memberManage.domain.Member;
import com.onebucket.global.auth.jwtAuth.domain.JwtToken;
import com.onebucket.global.exceptionManage.customException.memberManageExceptoin.AuthenticationException;
import com.onebucket.global.exceptionManage.errorCode.AuthenticationErrorCode;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Calendar;
import java.util.Date;
import java.util.stream.Collectors;

/**
 * <br>package name   : com.onebucket.gloabal.auth.jwtAuth.component
 * <br>file name      : JwtProvider
 * <br>date           : 2024-06-20
 * <pre>
 * <span style="color: white;">[description]</span>
 * This component is responsible for generating JWT tokens, including both access and refresh tokens.
 * It utilizes the io.jsonwebtoken library to create tokens signed with a provided secret key.
 * </pre>
 * <pre>
 * <span style="color: white;">usage:</span>
 * {@code
 * Authentication authentication = ...;
 * JwtProvider jwtProvider = new JwtProvider("secretKey", 3600000, 7200000);
 * JwtToken jwtToken = jwtProvider.generateToken(authentication);
 * System.out.println("Access Token: " + jwtToken.getAccessToken());
 * System.out.println("Refresh Token: " + jwtToken.getRefreshToken());
 * } </pre>
 * <pre>
 * modified log :
 * =======================================================
 * DATE           AUTHOR               NOTE
 * -------------------------------------------------------
 * 2024-06-20        jack8              init create
 * </pre>
 */

@Component
public class JwtProvider {

    private final Key key;
    private final long expireDateAccessToken;
    private final long expireDateRefreshToken;

    private final MemberRepository memberRepository;

    /**
     * Constructor of JwtProvider class.  Parameters of constructor are from {@code application.properties}.
     * Make secret key to key byte with Base64 Decoder and Sha algorithm.
     * @param secretKey from application.properties / to decode token
     * @param expireDateAccessToken from application.properties
     * @param expireDateRefreshToken from application.properties
     */
    @Autowired
    public JwtProvider(@Value("${jwt.secret}") String secretKey,
                       @Value("${jwt.expireDate.accessToken}") long expireDateAccessToken,
                       @Value("${jwt.expireDate.refreshToken}") long expireDateRefreshToken,
                       MemberRepository memberRepository) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);

        this.expireDateAccessToken = expireDateAccessToken;
        this.expireDateRefreshToken = expireDateRefreshToken;
        this.memberRepository = memberRepository;
    }

    /**
     * <p>
     *     public method of JwtProvide class to generate access token and refresh token wrapped in {@link JwtToken}
     *     which type is {@code Bearer}. Get authentication from security context holder.
     * </p>
     * @param authentication given in {@link org.springframework.security.core.context.SecurityContextHolder SecurityContextHolder}
     * @return JwtToken - contain token and type.
     * @see JwtToken
     */
    public JwtToken generateToken(Authentication authentication) {

        if (authentication == null
                || authentication.getPrincipal() == null
                || !(authentication.getPrincipal() instanceof UserDetails)) {

            throw new IllegalArgumentException("Invalid authentication object");
        }
        long nowDate = (new Date()).getTime();
        String accessToken = generateAccessToken(authentication, nowDate);
        String refreshToken = generateRefreshToken(nowDate);
        //implement this
        return JwtToken.builder()
                .grantType("Bearer")
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }


    /**
     * <p>
     *     Generate Access Token by given Authentication with now-date.
     *     Sign with given key in {@code application.properties}, with HS256.
     *     Token also contains expire date and subject which from user name in authentication.
     * </p>
     * @param authentication from generateToken
     * @param date String with java Date format
     * @return jwt string contain username, expire, authorities, sign key
     */
    private String generateAccessToken(Authentication authentication, long date) {
        Date tokenExpiration = new Date(date + expireDateAccessToken);
        String authorities = getAuthoritiesFromAuthentication(authentication);
        Long univId = getUnivId(authentication);
        Long userId = getUserId(authentication);
        return Jwts.builder()
                .setSubject(authentication.getName())
                .setIssuedAt(Calendar.getInstance().getTime())
                .setExpiration(tokenExpiration)
                .claim("auth", authorities)
                .claim("univId",univId)
                .claim("userId",userId)
                .signWith(key, SignatureAlgorithm.HS256).compact();
    }

    private Long getUnivId(Authentication authentication) {
        String username = authentication.getName();
        Member member = memberRepository.findByUsername(username).orElseThrow(() -> new AuthenticationException(AuthenticationErrorCode.UNKNOWN_USER));
        return member.getUniversity().getId();
    }

    private Long getUserId(Authentication authentication) {
        String username = authentication.getName();
        return memberRepository.findIdByUsername(username).orElseThrow(() -> new AuthenticationException(AuthenticationErrorCode.UNKNOWN_USER));
    }
    /**
     * <p>
     *     Generate refresh Token by given now date. Sign with given key in {@code application.properties} with HS256.
     * </p>
     * @param date String with java Date format
     * @return jwt string contain expire, sign key
     */
    private String generateRefreshToken(long date) {
        Date tokenExpiration = new Date(date + expireDateRefreshToken);

        return Jwts.builder()
                .setIssuedAt(Calendar.getInstance().getTime())
                .setExpiration(tokenExpiration)
                .signWith(key, SignatureAlgorithm.HS256).compact();
    }

    /**
     * <p>
     *     Get authorities from given authentication , private method.
     * </p>
     * @param authentication to get authorities
     * @return authorities in authentication
     */
    private String getAuthoritiesFromAuthentication(Authentication authentication) {
        return authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining("."));
    }
}
