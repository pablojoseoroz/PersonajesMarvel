package com.pablojoseoroz.uibase

/**
 *
 * Interface correspondiente a los métodos de [org.greenrobot.eventbus.EventBus]
 */
interface BusInterface {
    /**
     * Sobreescribir este método si queremos registrar/desregistrar el bus automaticamente
     *
     * @return true para registrarlo, false por defecto
     */
    fun shouldRegisterBus(): Boolean {
        return false
    }

    /**
     * @return true si [org.greenrobot.eventbus.EventBus] está registrado
     */
    val isBusRegistered: Boolean

    /**
     * Método donde registrar [org.greenrobot.eventbus.EventBus]
     */
    fun registerBus()

    /**
     * Método donde desregistrar [org.greenrobot.eventbus.EventBus]
     */
    fun unregisterBus()
}