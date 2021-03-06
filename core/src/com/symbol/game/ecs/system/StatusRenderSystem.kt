package com.symbol.game.ecs.system

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.Batch
import com.symbol.game.ecs.Mapper
import com.symbol.game.ecs.component.HealthComponent
import com.symbol.game.ecs.component.ProjectileComponent
import com.symbol.game.ecs.component.StatusEffect
import com.symbol.game.ecs.component.StatusEffectComponent
import com.symbol.game.map.camera.CameraUtil
import com.symbol.game.util.Resources
import com.symbol.game.util.STATUS_EFFECT

private const val HP_BAR_VISIBLE_DURATION = 2f
private const val HP_BAR_VISIBLE_DURATION_WITH_SE = 0.4f
private const val HEALTH_BAR_HEIGHT = 1
private const val HP_BAR_X_OFFSET = 2
private const val HP_BAR_Y_OFFSET = 3

private const val SE_SIZE = 5f
private const val SE_Y_OFFSET = 2

class StatusRenderSystem(private val batch: Batch,
                         private val res: Resources,
                         private val cam: OrthographicCamera)
    : IteratingSystem(Family.one(StatusEffectComponent::class.java,
        HealthComponent::class.java).exclude(ProjectileComponent::class.java).get()) {

    override fun processEntity(entity: Entity, dt: Float) {
        val pos = Mapper.POS_MAPPER[entity]
        val texture = Mapper.TEXTURE_MAPPER[entity]
        val se = Mapper.STATUS_EFFECT_MAPPER[entity]

        val hasStatusEffect = se != null && se.type !== StatusEffect.None
        val width = texture.texture!!.regionWidth.toFloat()
        val height = texture.texture!!.regionHeight.toFloat()

        val health = Mapper.HEALTH_MAPPER[entity]
        if (health.hpChange && health.hp > 0 && Mapper.PLAYER_MAPPER[entity] == null) {
            se.startHealthBar = true
            se.renderTimer = 0f
            health.hpChange = false
        }

        if (se != null && se.startHealthBar) {
            se.renderTimer += dt

            val maxHpBarWidth = width + HP_BAR_X_OFFSET * 2 - 2
            val hpBarWidth = maxHpBarWidth * (health.hp.toFloat() / health.maxHp)

            if (CameraUtil.withinCamera(pos.x - HP_BAR_X_OFFSET, pos.y + height + HP_BAR_Y_OFFSET.toFloat(), cam)) {
                batch.draw(res.getTexture("black"), pos.x - HP_BAR_X_OFFSET, pos.y + height + HP_BAR_Y_OFFSET.toFloat(),
                        width + HP_BAR_X_OFFSET * 2, (HEALTH_BAR_HEIGHT + 2).toFloat())
                batch.draw(res.getTexture("hp_bar_bg_color"), pos.x - HP_BAR_X_OFFSET + 1, pos.y + height + HP_BAR_Y_OFFSET.toFloat() + 1f,
                        maxHpBarWidth, HEALTH_BAR_HEIGHT.toFloat())
                batch.draw(res.getTexture("hp_bar_color"), pos.x - HP_BAR_X_OFFSET + 1, pos.y + height + HP_BAR_Y_OFFSET.toFloat() + 1f,
                        hpBarWidth, HEALTH_BAR_HEIGHT.toFloat())
            }

            val duration = if (hasStatusEffect) HP_BAR_VISIBLE_DURATION_WITH_SE else HP_BAR_VISIBLE_DURATION

            if (se.renderTimer >= duration) {
                se.startHealthBar = false
                se.renderTimer = 0f
            }
        }
        else if (hasStatusEffect) {
            if (CameraUtil.withinCamera(pos.x + (width - SE_SIZE) / 2, pos.y + height + SE_Y_OFFSET, cam)) {
                batch.draw(res.getTexture(STATUS_EFFECT + se.type.typeStr),
                        pos.x + (width - SE_SIZE) / 2,
                        pos.y + height + SE_Y_OFFSET)
            }
        }
    }

}