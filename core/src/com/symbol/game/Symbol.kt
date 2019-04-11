package com.symbol.game

import com.badlogic.gdx.Game
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.profiling.GLProfiler
import com.symbol.game.input.MouseCursor
import com.symbol.game.screen.AbstractScreen
import com.symbol.game.screen.GameScreen
import com.symbol.game.screen.MenuScreen
import com.symbol.game.util.Resources
import kotlin.math.min

class Symbol : Game() {

    lateinit var batch: Batch private set
    lateinit var res: Resources private set
    private var mouseCursor: MouseCursor? = null
    private lateinit var profiler: GLProfiler

    private var currentScreen: AbstractScreen? = null

    lateinit var menuScreen: MenuScreen private set
    lateinit var gameScreen: GameScreen private set

    override fun create() {
        batch = SpriteBatch()
        res = Resources()

        if (!Config.onAndroid()) {
            mouseCursor = MouseCursor()
            mouseCursor?.createCursor()
        }

        profiler = GLProfiler(Gdx.graphics)
        profiler.enable()

        menuScreen = MenuScreen(this)
        gameScreen = GameScreen(this)

        this.setScreen(menuScreen)
    }

    fun profile(className: String) {
        if (Config.onAndroid()) return

        val vertexCount = "{\n" +
                "\t\ttotal: ${profiler.vertexCount.total}\n" +
                "\t\taverage: ${profiler.vertexCount.average}\n" +
                "\t\tmin: ${profiler.vertexCount.min}\n" +
                "\t\tmax: ${profiler.vertexCount.max}\n" +
                "\t\tcount: ${profiler.vertexCount.count}\n" +
                "\t}"
        println("Profiling $className ...\n" +
                "\tTOTAL CALLS: ${profiler.calls}, \n" +
                "\tDRAW CALLS: ${profiler.drawCalls}, \n" +
                "\tSHADER SWITCHES: ${profiler.shaderSwitches}, \n" +
                "\tTEXTURE BINDINGS: ${profiler.textureBindings}, \n" +
                "\tVERTEX COUNT: $vertexCount \n")
        profiler.reset()
    }

    fun setScreen(screen: AbstractScreen?) {
        super.setScreen(screen)
        currentScreen = screen
    }

    override fun pause() {
        currentScreen?.notifyPause()
    }

    override fun render() {
        screen?.render(min(Config.DELTA_TIME_BOUND, Gdx.graphics.deltaTime))
        mouseCursor?.update(Gdx.graphics.deltaTime)
    }

    override fun dispose() {
        batch.dispose()
        res.dispose()
        mouseCursor?.dispose()
        gameScreen.dispose()
    }

}
