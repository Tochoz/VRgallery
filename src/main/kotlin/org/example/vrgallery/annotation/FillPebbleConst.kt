package org.example.vrgallery.annotation

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class FillPebbleConst(
    val location: String
)
