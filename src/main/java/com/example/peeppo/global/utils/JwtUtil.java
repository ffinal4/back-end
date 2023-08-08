package com.example.peeppo.global.utils;

import com.example.peeppo.domain.user.entity.UserRoleEnum;
import com.example.peeppo.domain.user.repository.UserRepository;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Slf4j(topic = "JwtUtil")
@Component
public class JwtUtil {
    // Header KEY 값
    public static final String ACCESS_TOKEN = "AccessToken";
    public static final String REFRESH_TOKEN = "RefreshToken";
    // 사용자 권한 값의 KEY
    public static final String AUTHORIZATION_KEY = "auth";
    // Token 식별자
    public static final String BEARER_PREFIX = "Bearer ";

    // accessToken 만료시간
    private final long TOKEN_TIME = 60 * 60 * 1000L; // 한시간

    // refreshToken 만료시간
    private final long REFRESH_TOKEN_TIME = 14 * 24 * 60 * 60 * 1000L; //2주

    @Value("${jwt.secret.key}") // Base64 Encode 한 SecretKey
    private String secretKey;
    private Key key;
    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
    private final UserRepository userRepository;
    public JwtUtil(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // 로그 설정
    public static final Logger logger = LoggerFactory.getLogger("JWT 관련 로그");

    @PostConstruct
    public void init() {
        byte[] bytes = Base64.getDecoder().decode(secretKey);
        key = Keys.hmacShaKeyFor(bytes);
    }

    public String createAccessToken(String username, UserRoleEnum role) {
        Date date = new Date();

        return BEARER_PREFIX +
                Jwts.builder()
                        .setSubject(username) // 사용자 식별자값(ID)
                        .claim(AUTHORIZATION_KEY, role) // 사용자 권한
                        .setExpiration(new Date(date.getTime() + TOKEN_TIME)) // 만료 시간
                        .setIssuedAt(date) // 발급일
                        .signWith(key, signatureAlgorithm) // 암호화 알고리즘
                        .compact();
    }

    public String createRefreshToken(String username){
        Date now = new Date();

        return BEARER_PREFIX +
                Jwts.builder()
                        .setSubject(username) // 사용자 식별자값(ID)
                        .setExpiration(new Date(now.getTime() + REFRESH_TOKEN_TIME)) // 만료 시간
                        .setIssuedAt(now) // 발급일
                        .signWith(key, signatureAlgorithm) // 암호화 알고리즘
                        .compact();
    }

    // JWT Cookie 에 저장
    public void addJwtToCookie(String token, HttpServletResponse res) {
        try {
            token = URLEncoder.encode(token, "utf-8").replaceAll("\\+", "%20"); // Cookie Value 에는 공백이 불가능해서 encoding 진행

            Cookie cookie = new Cookie(ACCESS_TOKEN, token); // Name-Value
            cookie.setMaxAge(60 * 60); // 60초 60분 1시간
            cookie.setPath("/");
            // ResponseHeader에 token 추가
            res.addHeader(ACCESS_TOKEN, token);
            // Response 객체에 Cookie 추가
            res.addCookie(cookie);
        } catch (UnsupportedEncodingException e) {
            logger.error(e.getMessage());
        }
    }

    // header 에서 ACCESS TOKEN 가져오기
    public String getJwtFromHeader(HttpServletRequest request) {
        String accessToken = request.getHeader(ACCESS_TOKEN);
        if (StringUtils.hasText(accessToken) && accessToken.startsWith(BEARER_PREFIX)) {
            return accessToken.substring(7);
        }
        return null;
    }

    // 토큰 검증
    public boolean validateToken(String token, HttpServletRequest req, HttpServletResponse res) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException | SignatureException e) {
            log.error("Invalid JWT signature, 유효하지 않는 JWT 서명 입니다.");
        } catch (ExpiredJwtException e) {
            if(req.getHeader(REFRESH_TOKEN).isEmpty()) {
                log.error("Expired JWT token, 만료된 JWT token 입니다.");
                throw new RuntimeException();
            } else {
                String RefreshToken = req.getHeader(REFRESH_TOKEN);
                // 리프레시 토큰 체크 후 존재하면 Accesstoken 발급
//                String newAccessToken = regenerateAccessToken(RefreshToken);
//                res.addHeader(JwtUtil.ACCESS_TOKEN, newAccessToken);
//                res.addHeader(JwtUtil.REFRESH_TOKEN, RefreshToken);
//                log.info("토큰재발급 성공: {}", newAccessToken);
            }
        } catch (UnsupportedJwtException e) {
            log.error("Unsupported JWT token, 지원되지 않는 JWT 토큰 입니다.");
        } catch (IllegalArgumentException e) {
            log.error("JWT claims is empty, 잘못된 JWT 토큰 입니다.");
        }
        return false;
    }



    // 토큰에서 사용자 정보 가져오기
    public Claims getUserInfoFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }

    // HttpServletRequest 에서 Cookie Value : JWT 가져오기
    public String getTokenFromRequest(HttpServletRequest req) {
        //쿠키의 경우 모든 쿠기에서 필요로 하는 값을 찾아서
        Cookie[] cookies = req.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(ACCESS_TOKEN)) {
                    try {
                        System.out.println(URLDecoder.decode(cookie.getValue(), "UTF-8"));
                        return URLDecoder.decode(cookie.getValue(), "UTF-8"); // Encode 되어 넘어간 Value(공백 인코딩) 다시 Decode
                    } catch (UnsupportedEncodingException e) {
                        return null;
                    }
                }
            }
        }
        // 쿠키 없는 경우 헤더에서 값 가져오기
//        String header = null;
        String header = req.getHeader(ACCESS_TOKEN);
        if (header != null) {
            try {
                return URLDecoder.decode(header, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                throw null;
            }
        }
//        return null;
        throw new NullPointerException("토큰이 존재하지 않습니다. 로그인 해주세요.");
    }

    public String substringToken(String tokenValue) {
        if (StringUtils.hasText(tokenValue) && tokenValue.startsWith(BEARER_PREFIX)) {
            return tokenValue.substring(7);
        }
        logger.error("Not Found Token");
        throw new NullPointerException("토큰이 존재하지 않습니다. 로그인 해주세요.");
    }
}