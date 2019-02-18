package com.symbol.map

import com.badlogic.ashley.core.PooledEngine
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.maps.MapLayer
import com.badlogic.gdx.maps.objects.RectangleMapObject
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer
import com.badlogic.gdx.maps.tiled.TmxMapLoader
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.Array
import com.badlogic.gdx.utils.Disposable
import com.symbol.ecs.entity.EnemyFactory
import com.symbol.ecs.entity.EnemyType
import com.symbol.util.Resources

private const val DIR = "map/"

private const val PLAYER_SPAWN_LAYER = "player"
private const val TILE_LAYER = "tile"
private const val COLLISION_LAYER = "collision"
private const val ENEMY_LAYER = "enemy"

private const val MAP_OBJECT_TYPE = "type"
private const val MAP_OBJECT_DAMAGE = "damage"

private const val ENEMY_TYPE = "type"
private const val ENEMY_FACING_RIGHT = "facingRight"

class MapManager(batch: Batch, private val cam: OrthographicCamera,
                 private val engine: PooledEngine, private val res: Resources) : Disposable {

    private val mapLoader: TmxMapLoader = TmxMapLoader()
    private lateinit var tiledMap: TiledMap
    private val renderer: OrthogonalTiledMapRenderer = OrthogonalTiledMapRenderer(null, 1f, batch)

    private lateinit var tileLayer: TiledMapTileLayer
    private lateinit var collisionLayer: MapLayer
    private lateinit var playerSpawnLayer: MapLayer
    private var enemyLayer: MapLayer? = null

    var tileSize: Int = 0
        private set

    var mapWidth: Int = 0
        private set
    var mapHeight: Int = 0
        private set

    var playerSpawnPosition: Vector2 = Vector2()

    val mapObjects: Array<MapObject> = Array()

    fun load(mapName: String) {
        tiledMap = mapLoader.load("$DIR$mapName.tmx")

        tileLayer = tiledMap.layers.get(TILE_LAYER) as TiledMapTileLayer
        collisionLayer = tiledMap.layers.get(COLLISION_LAYER)
        playerSpawnLayer = tiledMap.layers.get(PLAYER_SPAWN_LAYER)
        enemyLayer = tiledMap.layers.get(ENEMY_LAYER)

        tileSize = tileLayer.tileWidth.toInt()
        mapWidth = tileLayer.width
        mapHeight = tileLayer.height

        val spawn = playerSpawnLayer.objects.getByType(RectangleMapObject::class.java)[0].rectangle
        playerSpawnPosition.set(spawn.x, spawn.y)

        loadMapObjects()

        if (enemyLayer != null) {
            loadEnemies()
        }

        renderer.map = tiledMap
    }

    private fun loadMapObjects() {
        mapObjects.clear()
        val objects = collisionLayer.objects
        for (rectangleMapObject in objects.getByType(RectangleMapObject::class.java)) {
            val mapObjectRect = rectangleMapObject.rectangle
            val typeProp = rectangleMapObject.properties[MAP_OBJECT_TYPE]
            val damageProp = rectangleMapObject.properties[MAP_OBJECT_DAMAGE]

            val mapObjectType = if (typeProp == null) MapObjectType.Ground else MapObjectType.getType(typeProp.toString())!!
            val mapObjectDamage = if (damageProp == null) 0 else damageProp as Int

            mapObjects.add(MapObject(mapObjectRect, mapObjectType, mapObjectDamage))
        }
    }

    private fun loadEnemies() {
        val enemyObjects = enemyLayer?.objects
        for (enemyMapObject in enemyObjects!!.getByType(RectangleMapObject::class.java)) {
            val enemyObjectRect = enemyMapObject.rectangle
            val typeProp = enemyMapObject.properties[ENEMY_TYPE]
            val facingRightProp = enemyMapObject.properties[ENEMY_FACING_RIGHT]

            val enemyObjectType = if (typeProp == null) EnemyType.None else EnemyType.getType(typeProp.toString())!!
            val facingRight = if (facingRightProp == null) true else facingRightProp as Boolean

            EnemyFactory.createEnemy(engine, res, enemyObjectType, enemyObjectRect, facingRight)
        }
    }

    fun update() {
        renderer.setView(cam)
    }

    fun render() {
        renderer.renderTileLayer(tileLayer)
    }

    override fun dispose() {
        renderer.dispose()
        tiledMap.dispose()
    }

}