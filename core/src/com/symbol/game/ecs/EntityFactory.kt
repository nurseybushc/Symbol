package com.symbol.game.ecs

import com.badlogic.ashley.core.PooledEngine
import com.badlogic.gdx.maps.MapProperties
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Rectangle
import com.symbol.game.ecs.entity.*
import com.symbol.game.util.*

object EntityFactory {

    fun createEnemy(engine: PooledEngine, res: Resources, data: Data,
                    type: EnemyType, rect: Rectangle, facingRight: Boolean) {
        val textureStr = if (type == EnemyType.Because) "e_${type.typeStr}0" else "e_${type.typeStr}"
        val texture = res.getTexture(textureStr)!!
        val color = data.getColor("e_${type.typeStr}")!!
        when (type) {
            EnemyType.EConstant -> {
                EntityBuilder.instance(engine)
                        .enemy(enemyType = type)
                        .statusEffect()
                        .activation(150f)
                        .attack(damage = 1, projectileSpeed = 45f, attackTexture = "p_dot", attackRate = 1.5f)
                        .color(color)
                        .health(1000)
                        .boundingBox(7f, 7f)
                        .position(rect.x, rect.y)
                        .velocity(speed = 25f)
                        .direction(facingRight = facingRight)
                        .texture(texture, textureStr)
                        .gravity().remove().build()
            }
            EnemyType.SquareRoot -> {
                EntityBuilder.instance(engine)
                        .enemy(enemyType = type, movementType = EnemyMovementType.Charge)
                        .statusEffect()
                        .activation(75f)
                        .attack(damage = 3)
                        .color(color)
                        .health(3)
                        .boundingBox(10f, 8f)
                        .position(rect.x, rect.y)
                        .velocity(speed = 60f)
                        .direction(facingRight = facingRight)
                        .texture(texture, textureStr)
                        .gravity().remove().build()
            }
            EnemyType.Exists -> {
                EntityBuilder.instance(engine)
                        .enemy(enemyType = type, movementType = EnemyMovementType.Charge)
                        .statusEffect()
                        .activation(90f)
                        .attack(damage = data.getPlayerData("hp").asInt())
                        .color(color)
                        .health(2)
                        .boundingBox(9f, 13f)
                        .position(rect.x, rect.y)
                        .velocity(speed = 75f)
                        .direction(facingRight = facingRight)
                        .texture(texture, textureStr)
                        .gravity().remove().build()
            }
            EnemyType.Summation -> {
                EntityBuilder.instance(engine)
                        .enemy(enemyType = type, attackType = EnemyAttackType.ShootOne)
                        .statusEffect()
                        .activation(120f)
                        .attack(damage = 2, attackTexture = "p_dot4", attackRate = 2.5f, projectileSpeed = 45f)
                        .color(color)
                        .health(2)
                        .boundingBox(10f, 13f)
                        .position(rect.x, rect.y)
                        .velocity()
                        .direction(facingRight = facingRight)
                        .texture(texture, textureStr)
                        .gravity().remove().build()
            }
            EnemyType.BigPi -> {
                EntityBuilder.instance(engine)
                        .enemy(enemyType = type, attackType = EnemyAttackType.ShootOne)
                        .statusEffect()
                        .activation(120f)
                        .attack(damage = 4, attackTexture = "p_big_ll", attackRate = 1.4f, projectileSpeed = 45f)
                        .color(color)
                        .health(4)
                        .boundingBox(11f, 13f)
                        .position(rect.x, rect.y)
                        .velocity()
                        .direction(facingRight = facingRight)
                        .texture(texture, textureStr)
                        .gravity().remove().build()
            }
            EnemyType.In -> {
                EntityBuilder.instance(engine)
                        .enemy(enemyType = type, attackType = EnemyAttackType.ShootOne)
                        .statusEffect()
                        .activation(100f)
                        .attack(damage = 1, attackTexture = "p_xor", attackRate = 2f, projectileSpeed = 45f, attackDetonateTime = 2f)
                        .color(color)
                        .health(3)
                        .boundingBox(11f, 11f)
                        .position(rect.x, rect.y)
                        .velocity()
                        .direction(facingRight = facingRight)
                        .texture(texture, textureStr)
                        .gravity().remove().build()
            }
            EnemyType.BigOmega -> {
                EntityBuilder.instance(engine)
                        .enemy(enemyType = type, attackType = EnemyAttackType.SprayThree)
                        .statusEffect()
                        .activation(150f)
                        .attack(damage = 2, attackTexture = "p_cup", attackRate = 2.5f, projectileSpeed = 200f)
                        .color(color)
                        .health(3)
                        .boundingBox(12f, 13f)
                        .position(rect.x, rect.y)
                        .velocity()
                        .texture(texture, textureStr)
                        .gravity().remove().build()
            }
            EnemyType.NaturalJoin -> {
                EntityBuilder.instance(engine)
                        .enemy(enemyType = type, movementType = EnemyMovementType.BackAndForth)
                        .statusEffect()
                        .activation(100f)
                        .attack(damage = 2, attackTexture = "p_ltimes", projectileSpeed = 45f)
                        .explode()
                        .color(color)
                        .health(4)
                        .boundingBox(9f, 7f)
                        .position(rect.x, rect.y)
                        .velocity(speed = 30f)
                        .texture(texture, textureStr)
                        .direction(facingRight = facingRight)
                        .knockback().gravity().remove().build()
            }
            EnemyType.BigPhi -> {
                EntityBuilder.instance(engine)
                        .enemy(enemyType = type, attackType = EnemyAttackType.ShootAndQuake)
                        .statusEffect()
                        .activation(200f)
                        .attack(damage = 4, attackTexture = "p_alpha", attackRate = 1.5f, projectileSpeed = 60f)
                        .explode()
                        .color(color)
                        .health(10)
                        .boundingBox(14f, 16f)
                        .position(rect.x, rect.y)
                        .velocity()
                        .jump(150f)
                        .texture(texture, textureStr)
                        .direction(facingRight = facingRight)
                        .gravity().remove().build()
            }
            EnemyType.Percent -> {
                val parent = EntityBuilder.instance(engine)
                        .enemy(enemyType = type, movementType = EnemyMovementType.BackAndForth, visible = false)
                        .statusEffect()
                        .activation(120f)
                        .attack(damage = 1)
                        .color(color)
                        .health(20)
                        .boundingBox(10f, 10f)
                        .position(rect.x, rect.y)
                        .velocity(speed = 20f)
                        .jump(120f)
                        .texture(texture, textureStr)
                        .direction(facingRight = facingRight)
                        .gravity(gravity = -480f, terminalVelocity = -240f).knockback().remove().build()

                val angles = listOf(MathUtils.PI2 / 5f, MathUtils.PI2 * 2f / 5f, MathUtils.PI2 * 3 / 5f, MathUtils.PI2 * 4 / 5f, 0f)
                for (angle in angles) {
                    EntityBuilder.instance(engine)
                            .enemy(enemyType = type, movementType = EnemyMovementType.Orbit, parent = parent, visible = false)
                            .activation(150f)
                            .attack(damage = 1)
                            .color(color)
                            .health(1)
                            .boundingBox(6f, 6f)
                            .position(rect.x, rect.y)
                            .velocity()
                            .texture(res.getTexture("e_${type.typeStr}$ORBIT")!!, "e_${type.typeStr}$ORBIT")
                            .orbit(angle = angle, speed = 2f, radius = 15f)
                            .remove().build()
                }
            }
            EnemyType.Nabla -> {
                EntityBuilder.instance(engine)
                        .enemy(enemyType = type)
                        .activation(140f)
                        .attack(damage = 4)
                        .color(color)
                        .health(1)
                        .gravity(gravity = -1200f, terminalVelocity = -160f, collidable = false)
                        .boundingBox(texture.regionWidth.toFloat() - 4, texture.regionHeight.toFloat())
                        .position(rect.x, rect.y)
                        .velocity()
                        .texture(texture, textureStr)
                        .remove().build()
            }
            EnemyType.CIntegral -> {
                EntityBuilder.instance(engine)
                        .enemy(enemyType = type, attackType = EnemyAttackType.ArcTwo)
                        .statusEffect()
                        .activation(120f)
                        .attack(damage = 4, attackTexture = "p_succ", attackRate = 2f,
                                projectileSpeed = 80f, projectileAcceleration = 80f)
                        .corporeal(incorporealTime = 2f)
                        .color(color)
                        .health(5)
                        .boundingBox(8f, 16f)
                        .position(rect.x, rect.y)
                        .direction(facingRight)
                        .velocity()
                        .texture(texture, textureStr)
                        .gravity().remove().build()
            }
            EnemyType.Because -> {
                EntityBuilder.instance(engine)
                        .enemy(enemyType = type, movementType = EnemyMovementType.BackAndForth)
                        .statusEffect()
                        .activation(120f)
                        .attack(damage = 2, attackTexture = "p_because", projectileSpeed = 60f)
                        .trap()
                        .color(color)
                        .health(100)
                        .boundingBox(14f, 12f)
                        .position(rect.x, rect.y)
                        .direction(facingRight)
                        .velocity(speed = 20f)
                        .texture(texture, "e_because")
                        .gravity().remove().build()
            }
            EnemyType.Block -> {
                EntityBuilder.instance(engine)
                        .enemy(enemyType = type)
                        .statusEffect()
                        .attack()
                        .activation()
                        .block()
                        .color(color)
                        .health(15)
                        .boundingBox(8f, 8f)
                        .position(rect.x, rect.y)
                        .direction(facingRight)
                        .velocity()
                        .texture(texture, "e_block")
                        .remove().build()
            }
            else -> {}
        }
    }

    fun createMapEntity(engine: PooledEngine, res: Resources, props: MapProperties, type: MapEntityType, rect: Rectangle) {
        val dx = (props["dx"] ?: 0f) as Float
        val dy = (props["dy"] ?: 0f) as Float
        val dist = (props["dist"] ?: 0f) as Float

        when (type) {
            MapEntityType.MovingPlatform -> {
                val textureStr = "${type.typeStr}${MathUtils.ceil(rect.width / 8)}"
                val texture = res.getTexture(textureStr)!!

                EntityBuilder.instance(engine)
                        .mapEntity(type = type, projectileCollidable = true)
                        .movingPlatform()
                        .backAndForth(dist = dist, positive = dx > 0)
                        .boundingBox(texture.regionWidth.toFloat(), texture.regionHeight.toFloat())
                        .position(rect.x, rect.y)
                        .velocity(dx = dx)
                        .texture(texture, textureStr)
                        .build()
            }
            MapEntityType.TemporaryPlatform -> {
                val texture = res.getTexture("approx")!!
                EntityBuilder.instance(engine)
                        .mapEntity(type = type)
                        .boundingBox(texture.regionWidth.toFloat(), texture.regionHeight.toFloat(), x = rect.x, y = rect.y)
                        .position(rect.x, rect.y)
                        .texture(texture, "approx")
                        .remove().build()
            }
            MapEntityType.Portal -> {
                val texture = res.getTexture("curly_brace_portal")!!
                val bw = texture.regionWidth.toFloat() - 4f
                val bh = texture.regionHeight.toFloat() - 4f
                val id = props["id"]!! as Int
                val target = props["target"]!! as Int

                EntityBuilder.instance(engine)
                        .mapEntity(type = type)
                        .portal(id, target)
                        .boundingBox(bw, bh)
                        .position(rect.x, rect.y)
                        .velocity()
                        .texture(texture, "curly_brace_portal")
                        .build()
            }
            MapEntityType.Clamp -> {
                val textureKey = (props["texture"] ?: "square_bracket") as String
                val acceleration = (props["accel"] ?: 144f) as Float
                val backVelocity = (props["backVel"] ?: 10f) as Float
                val textureLeft = res.getTexture(textureKey + BRACKET_LEFT)!!
                val textureRight = res.getTexture(textureKey + BRACKET_RIGHT)!!

                EntityBuilder.instance(engine)
                        .mapEntity(type = type, projectileCollidable = true)
                        .clamp(false, rect, acceleration, backVelocity)
                        .boundingBox(textureLeft.regionWidth.toFloat(), textureLeft.regionHeight.toFloat())
                        .position(rect.x, rect.y)
                        .velocity()
                        .texture(textureLeft)
                        .build()

                EntityBuilder.instance(engine)
                        .mapEntity(type = type, projectileCollidable = true)
                        .clamp(true, rect, acceleration, backVelocity)
                        .boundingBox(textureRight.regionWidth.toFloat(), textureRight.regionHeight.toFloat())
                        .position(rect.x + rect.width - textureRight.regionWidth, rect.y)
                        .velocity()
                        .texture(textureRight)
                        .build()
            }
            MapEntityType.HealthPack -> {
                val texture = res.getTexture("health_pack")!!
                val regen = (props["regen"] ?: 0) as Int
                val regenTime = (props["time"] ?: 0f) as Float

                EntityBuilder.instance(engine)
                        .mapEntity(type = type)
                        .healthPack(regen, regenTime)
                        .boundingBox(texture.regionWidth.toFloat(), texture.regionHeight.toFloat())
                        .position(rect.x, rect.y)
                        .velocity()
                        .texture(texture, "health_pack")
                        .remove().build()
            }
            MapEntityType.Mirror -> {
                val orientation = (props["or"] ?: "v") as String
                val textureStr = "between_$orientation"
                val texture = res.getTexture(textureStr)!!

                EntityBuilder.instance(engine)
                        .mapEntity(type = type)
                        .mirror(orientation)
                        .boundingBox(texture.regionWidth.toFloat(), texture.regionHeight.toFloat())
                        .position(rect.x, rect.y)
                        .velocity()
                        .texture(texture, textureStr)
                        .build()
            }
            MapEntityType.GravitySwitch -> {
                val textureStr = "updownarrow"
                val texture = res.getTexture(textureStr + TOGGLE_OFF)!!

                EntityBuilder.instance(engine)
                        .mapEntity(type = type, projectileCollidable = true)
                        .boundingBox(texture.regionWidth.toFloat(), texture.regionHeight.toFloat())
                        .position(rect.x, rect.y)
                        .velocity()
                        .texture(texture, textureStr)
                        .build()
            }
            MapEntityType.SquareSwitch -> {
                val textureStr = "square_switch"
                val texture = res.getTexture(textureStr + TOGGLE_ON)!!
                val targetId = props["targetId"]!! as Int

                EntityBuilder.instance(engine)
                        .mapEntity(type = type, mapCollidable = true, projectileCollidable = true)
                        .squareSwitch(targetId)
                        .boundingBox(texture.regionWidth.toFloat(), texture.regionHeight.toFloat())
                        .position(rect.x, rect.y)
                        .velocity()
                        .texture(texture, textureStr)
                        .build()
            }
            MapEntityType.ToggleTile -> {
                val texture = res.getTexture("toggle_square")!!
                val id = props["id"]!! as Int
                val width = texture.regionWidth.toFloat()
                val height = texture.regionHeight.toFloat()

                EntityBuilder.instance(engine)
                        .mapEntity(type = type, mapCollidable = true, projectileCollidable = true)
                        .toggleTile(id, rect.x + 0.5f, rect.y + 0.5f, width - 1.5f, height - 1.5f)
                        .boundingBox(width, height)
                        .position(rect.x, rect.y)
                        .velocity()
                        .texture(texture, "toggle_square")
                        .build()
            }
            MapEntityType.ForceField -> {
                val duration = (props["duration"] ?: 0f) as Float
                val textureStr = "forcefield${MathUtils.floor((rect.width - 32f) / 16f) + 1}"
                val texture = res.getTexture(textureStr)!!

                EntityBuilder.instance(engine)
                        .mapEntity(type = type)
                        .forceField(duration = duration)
                        .boundingCircle(rect.x + rect.width / 2, rect.y + rect.height / 2, rect.width / 2)
                        .position(rect.x, rect.y)
                        .texture(texture, textureStr)
                        .build()
            }
            MapEntityType.DamageBoost -> {
                val duration = (props["duration"] ?: 5f) as Float
                val damageBoost = (props["damage"] ?: 0) as Int
                val textureStr = "damage_boost"
                val texture = res.getTexture(textureStr)!!

                EntityBuilder.instance(engine)
                        .mapEntity(type = type)
                        .damageBoost(damageBoost = damageBoost, duration = duration)
                        .boundingBox(texture.regionWidth.toFloat(), texture.regionHeight.toFloat())
                        .position(rect.x, rect.y)
                        .velocity()
                        .texture(texture, textureStr)
                        .build()
            }
            MapEntityType.InvertSwitch -> {
                val textureStr = "invert_switch"
                val texture = res.getTexture(textureStr)!!

                EntityBuilder.instance(engine)
                        .mapEntity(type = type, mapCollidable = true, projectileCollidable = true)
                        .invertSwitch()
                        .boundingBox(texture.regionWidth.toFloat(), texture.regionHeight.toFloat())
                        .position(rect.x, rect.y)
                        .velocity()
                        .texture(texture, textureStr)
                        .build()
            }
            MapEntityType.AccelerationGate -> {
                val boost = (props["boost"] ?: 0f) as Float
                val orientation = (props["or"] ?: "v") as String
                val textureStr = "agate_$orientation"
                val texture = res.getTexture(textureStr)!!

                EntityBuilder.instance(engine)
                        .mapEntity(type = type)
                        .accelerationGate(boost = boost)
                        .boundingBox(texture.regionWidth.toFloat(), texture.regionHeight.toFloat())
                        .position(rect.x, rect.y)
                        .velocity()
                        .texture(texture, textureStr)
                        .build()
            }
            else -> {}
        }
    }

}