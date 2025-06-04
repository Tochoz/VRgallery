package org.example.vrgallery.util

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpSession
import org.aspectj.lang.ProceedingJoinPoint
import org.example.vrgallery.annotation.FillPebbleConst
import org.springframework.security.authentication.AnonymousAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.ui.Model
import org.springframework.ui.ModelMap

fun <T> Boolean.map(a: T, b: T): T =
    if (this) a else b

fun fillPebbleConst(
    location: String,
    lang: String? = null,
    model: Model,
    session: HttpSession
): Unit {
    // Выполняем общую логику перед вызовом метода контроллера
    if (lang == "en") session.setAttribute("lang", lang)
    if (lang == "ru") session.setAttribute("lang", lang)
    when (session.getAttribute("lang")) {
        "en" -> {
            model.addAttribute("lang", "en")
        }
    }

    model.addAttribute("location", location)
    if (SecurityContextHolder.getContext().authentication !is AnonymousAuthenticationToken) model.addAttribute(
        "isAdmin",
        true
    )
}

fun processLikedCookie(cookie: String?): MutableSet<Int> =
    cookie
        ?.split("|")
        ?.filter { it.isNotBlank() }
        ?.map { it.toInt() }
        ?.toMutableSet()
        ?: mutableSetOf()
