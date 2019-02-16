package com.symbol.ecs.component

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.utils.Pool

class ProjectileComponent : Component, Pool.Poolable {

    var unstoppable: Boolean = false
    var enemy: Boolean = false
    var damage: Int = 0
    var knockback: Float = 0f

    var detonateTime: Float = 0f

    override fun reset() {
        unstoppable = false
        enemy = false
        damage = 0
        knockback = 0f
        detonateTime = 0f
    }
}