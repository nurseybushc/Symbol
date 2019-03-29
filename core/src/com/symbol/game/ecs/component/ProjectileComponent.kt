package com.symbol.game.ecs.component

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.utils.Pool

class ProjectileComponent : Component, Pool.Poolable {

    var lifeTime = 0f

    var parentFacingRight = false
    var sub = false

    var originX = 0f
    var originY = 0f

    var textureStr: String? = null
    var collidesWithTerrain = true
    var collidesWithProjectiles = false
    var enemy = false
    var damage = 0
    var knockback = 0f
    var playerType = 0

    var detonateTime = 0f
    var acceleration = 0f

    var movementType = ProjectileMovementType.Normal

    var arcHalf = false
    var waveDir = Direction.Left
    var waveTimer = 0f

    override fun reset() {
        lifeTime = 0f
        parentFacingRight = false
        sub = false
        originX = 0f
        originY = 0f
        textureStr = null
        collidesWithTerrain = true
        collidesWithProjectiles = false
        enemy = false
        damage = 0
        knockback = 0f
        playerType = 0
        detonateTime = 0f
        acceleration = 0f
        movementType = ProjectileMovementType.Normal

        arcHalf = false
        waveDir = Direction.Left
        waveTimer = 0f
    }
}

enum class ProjectileMovementType {

    Normal,
    Arc,
    Wave

}