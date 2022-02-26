package me.zort.perworldactions.action

fun interface ActionFactory<T> {

    fun create(input: T): Action

}