package cn.micro.trace.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.web.context.request.RequestContextHolder;

import java.util.Optional;

import static cn.micro.trace.constant.TraceConstant.REQUEST_SERVICE_METHOD;
import static org.springframework.web.context.request.RequestAttributes.SCOPE_REQUEST;

@Aspect
@Order(-2)
public class RequestAspect {

    @Pointcut("@annotation(org.springframework.web.bind.annotation.GetMapping) ")
    public void getMappingPoint() {
    }

    @Pointcut("@annotation(org.springframework.web.bind.annotation.PostMapping) ")
    public void postMappingPoint() {
    }

    @Pointcut("@annotation(org.springframework.web.bind.annotation.PutMapping) ")
    public void putMappingPoint() {
    }

    @Pointcut("@annotation(org.springframework.web.bind.annotation.DeleteMapping) ")
    public void deleteMappingPoint() {
    }

    @Pointcut("@annotation(org.springframework.web.bind.annotation.RequestMapping) " + "&& !execution(* org.springframework.boot.autoconfigure.web.servlet.error.BasicErrorController.*(..)) ")
    public void requestMappingPoint() {
    }

    @Around("getMappingPoint() || postMappingPoint() || putMappingPoint() || deleteMappingPoint() || requestMappingPoint()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        Class<?> target = point.getTarget().getClass();
        MethodSignature methodSignature = (MethodSignature) point.getSignature();
        String serviceMethod = target.getName() + "." + methodSignature.getName();
        Optional.ofNullable(RequestContextHolder.getRequestAttributes())
                .ifPresent(requestAttributes -> requestAttributes.setAttribute(REQUEST_SERVICE_METHOD, serviceMethod, SCOPE_REQUEST));
        return point.proceed();
    }

}
