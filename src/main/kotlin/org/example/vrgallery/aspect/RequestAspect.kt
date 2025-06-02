package org.example.vrgallery.aspect

import jakarta.servlet.http.HttpServletRequest
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Before
import org.example.vrgallery.annotation.FillPebbleConst
import org.springframework.security.authentication.AnonymousAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.ui.Model
import org.springframework.ui.ModelMap

@Aspect
@Component
class RequestAspect {

    @Around("execution(* org.example.vrgallery.controller.*.*(..)) && @annotation(org.example.vrgallery.annotation.FillPebbleConst) ")
    fun aroundControllerMethods(
        joinPoint: ProceedingJoinPoint,
        fillPebbleConst: FillPebbleConst,
        modelMap: ModelMap,
        request: HttpServletRequest
    ): Any? {
        // Выполняем общую логику перед вызовом метода контроллера
        val lang = joinPoint.args.get(1)
        val session = request.session

        if (lang == "en") session.setAttribute("lang", lang)
        if (lang == "ru") session.setAttribute("lang", lang)
        val isEngLocale = when (session.getAttribute("lang")) {
            "en" -> {
                modelMap.addAttribute("lang", "en")
                true
            }
            else -> false
        }

        modelMap.addAttribute("location", fillPebbleConst.location)
        if (SecurityContextHolder.getContext().authentication !is AnonymousAuthenticationToken) modelMap.addAttribute("isAdmin", true)

        // Вызываем оригинальный метод контроллера
        return joinPoint.proceed(joinPoint.args)
    }
}