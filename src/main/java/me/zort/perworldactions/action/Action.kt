package me.zort.perworldactions.action

interface Action {

    fun verify(data: InvokationData): Boolean
    fun invoke(data: InvokationData)

}