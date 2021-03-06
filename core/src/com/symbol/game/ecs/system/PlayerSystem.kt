package com.symbol.game.ecs.system

import com.badlogic.ashley.core.EntitySystem
import com.symbol.game.ecs.Mapper
import com.symbol.game.ecs.entity.Player
import com.symbol.game.screen.GameScreen
import com.symbol.game.util.Data

class PlayerSystem(private val player: Player,
                   private val data: Data,
                   private val gameScreen: GameScreen)
    : EntitySystem() {

    private var stateTime = 0f
    private var healTime = 0f
    private var totalHealTime = 0f

    override fun update(dt: Float) {
        val playerComp = Mapper.PLAYER_MAPPER[player]

        if (!playerComp.canShoot) {
            stateTime += dt
            if (stateTime >= data.getPlayerData("shootDelay").asFloat()) {
                playerComp.canShoot = true
                stateTime = 0f
            }
        }

        if (playerComp.healing != 0) {
            val oneHealTime = (1f / playerComp.healing.toFloat()) * playerComp.healTime
            if (playerComp.startHealing) {
                healTime += dt
                totalHealTime += dt

                if (totalHealTime >= playerComp.healTime) {
                    Mapper.HEALTH_MAPPER[player].heal(1)
                    healTime = 0f
                    totalHealTime = 0f
                    playerComp.startHealing = false
                }
                else if (healTime >= oneHealTime) {
                    Mapper.HEALTH_MAPPER[player].heal(1)
                    healTime = 0f
                }
            }
        }

        if (playerComp.dead && !gameScreen.deathDialog.isDisplayed) {
            gameScreen.showDeathDialog()
        }
    }

}