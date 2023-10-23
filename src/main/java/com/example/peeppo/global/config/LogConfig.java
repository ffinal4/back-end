package com.example.peeppo.global.config;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

import java.util.Arrays;


// Spring AOP에서 사용되는 조인 포인트는 항상 메소드 호출을 의미
/*
Pointcut 지정자의 종류 -> advice를 어디에 적용 시킬지 , AOP에 알려주는 키워드

1. execution 규칙

@Pointcut(
          "execution("    // PCD execution 지정
        + "[접근제한자 패턴] "  // public
        + "리턴타입 패턴"       // long
        + "[패키지명, 클래스 경로 패턴]"          // com.moong.ahea.UserService
        + "메소드명 패턴(파라미터 타입 패턴|..)"  // .findUserId(String)
        + "[throws 예외 타입 패턴]"             // throws RuntimeException
        +")"
          )
각 패턴은 * 표현으로 가능, .. 0개 이상의 의미, []은 생략이 가능한 옵션을 의미하고 | 는 OR 조건을 의미

2. within - 패키지, 클래스 제한 -> 메소드가 아닌 특정 타입에 속한 메소드를 포인트 컷으로 설정 시 사용
3. 등등 https://gmoon92.github.io/spring/aop/2019/05/06/pointcut.html 참고하기

애노테이션과 PCD
1. @target : 애노테이션이 부착된 클래스 제한
@Pointcut("@target(org.springframework.stereotype.Service)") -> @Service 애노테이션이 부착된 모든 클래스의 메소드에 어드바이스를 부여할 수 있습니다.
2. @args : 특정 클래스를 사용되고 있는 파라미터를 추적하기
3. @within : 특정 애노테이션을 통합 관리
4. @annotation : 어노테이션을 생성하고 특정 메소드에 애노테이션 부착 후 @annotation PCD 지정하면된다.
 */

@Aspect
@Component
@Slf4j
public class LogConfig {

    @Pointcut("@within(org.springframework.stereotype.Service)") // 특정 기능을 적용시킬 그룹 설정
    public void controller(){

    }

    //JoinPoint : advice 적용할 위치
    @Before("controller()") // 메서드가 실행되기 전에 동작
    public void beforeRequest(JoinPoint joinPoint) {
        log.info("###Start request {}", joinPoint.getSignature().toShortString());
        Arrays.stream(joinPoint.getArgs())
                .map(Object::toString)
                .map(str -> "\t" + str)
                .forEach(log::info);
    }
    //JoinPoint::getSigniture : 호출된 메서드 정보를 담은 객체를 호출
    //JoinPoint::getArgs : 호출된 메서드의 Arguments들을 호출

    @AfterReturning(pointcut = "controller()", returning = "returnValue")
    public void afterReturningLogging(JoinPoint joinPoint, Object returnValue) {
        log.info("###End request {}", joinPoint.getSignature().toShortString());

        if (returnValue == null) return;

        log.info("\t{}", returnValue.toString());
    }

    @AfterThrowing(pointcut = "controller()", throwing = "e")
            public void afterThrowingLogging(JoinPoint joinPoint, Exception e) {
        log.error("###Occured error in request {}", joinPoint.getSignature().toShortString());
        log.error("\t{}", e.getMessage());
    }
}
