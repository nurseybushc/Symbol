package com.symbol.game.scene.page

import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.utils.Array

class PagedScrollPane(private val horizontal: Boolean = true,
                      style: ScrollPaneStyle, pageSpace: Float) : ScrollPane(null, style) {

    private var panDragOrFling = false
    private val container = Table()

    private var autoResetOnPageEnter = true
    private var snapToPage = true
    private var totalEndPadding = 0f

    init {
        container.defaults().space(pageSpace)
        actor = container
        setFadeScrollBars(true)
        setupFadeScrollBars(0f, 0f)
    }

    fun disableSnapToPage() {
        snapToPage = false
    }

    fun disableAutoReset() {
        autoResetOnPageEnter = false
    }

    fun addPages(pages: Array<Page>) = pages.forEach { addPage(it) }

    fun addPage(page: Page) {
        page.setPageIndex(container.children.size)
        if (horizontal) container.add(page.pageActor).expandY().fillY()
        else container.add(page.pageActor).expandX().fillX().row()
    }

    fun addPadding(offset: Float) {
        if (container.children.size > 0) totalEndPadding += offset

        if (horizontal) container.add().minWidth(offset)
        else container.add().minHeight(offset).row()
    }

    fun reset() {
        panDragOrFling = false
        if (horizontal) scrollX = 0f else scrollY = 0f
        updateVisualScroll()
        container.clearChildren()
    }

    override fun act(dt: Float) {
        super.act(dt)

        if (panDragOrFling && !isPanning && !isDragging && !isFlinging) {
            panDragOrFling = false
            scrollToPage()
        }
        else if (isPanning || isDragging || isFlinging) {
            panDragOrFling = true
            resetCurrentPage()
        }
    }

    private fun scrollToPage() {
        if (!snapToPage) return

        val pages = container.children

        if (horizontal && (scrollX >= maxX || scrollX <= 0f)) return
        else if (!horizontal && (scrollY >= maxY || scrollY <= 0f)) return

        var pageCoord = 0f
        var pageDimen = 0f
        if (pages.size > 0) {
            for (page in pages) {
                pageCoord = if (horizontal) page.x else maxY - page.y
                pageDimen = if (horizontal) page.width else page.height
                if ((if (horizontal) scrollX else scrollY) < pageCoord + pageDimen / 2f) break
            }
            if (horizontal) scrollX = MathUtils.clamp(pageCoord - (width - pageDimen) / 2f, 0f, maxX)
            else scrollY = MathUtils.clamp(pageCoord - (height - pageDimen) / 2f, 0f, maxY)
        }
    }

    fun scrollToPrevious() {
        getCurrentIndex().let {
            if (it == 0) return
            resetCurrentPage()
            if (horizontal) scrollX = container.children[it - 1].x
            else scrollY = maxY - container.children[it - 1].y
        }
    }

    fun scrollToNext() {
        getCurrentIndex().let {
            if (it == container.children.size - 1) return
            resetCurrentPage()
            if (horizontal) scrollX = container.children[it + 1].x
            else scrollY = maxY - container.children[it + 1].y
        }

    }

    fun scrollToIndex(index: Int) {
        container.children.let {
            if (horizontal) scrollX = it[index].x - totalEndPadding
            else scrollY = it[it.size - 1 - index].y - totalEndPadding
        }
    }

    fun resetCurrentPage() {
        if (!autoResetOnPageEnter) return

        (getCurrentPage() as Page).run {
            reset()
            if (!hasSeen()) notifySeen()
        }
    }

    fun resetAllPages() = container.children.forEach { page -> (page as Page).reset() }

    fun hasAllSeen() = container.children.find { !(it as Page).hasSeen() } == null

    fun isCurrentPageSeen() = (getCurrentPage() as Page).hasSeen()

    fun isNextPageSeen() : Boolean {
        getCurrentIndex().let {
            if (it == container.children.size - 1) return true
            return (container.children[it + 1] as Page).hasSeen()
        }
    }

    private fun getCurrentPage() : Actor? {
        container.children.forEach { page ->
            if (horizontal && scrollX <= page.x + page.width ||
                    !horizontal && scrollY <= maxY - page.y + page.height / 2) {
                return page
            }
        }
        return null
    }

    private fun getCurrentIndex() : Int {
        for ((index, page) in container.children.withIndex()) {
            if (horizontal && scrollX <= page.x + page.width) return index
            else if (!horizontal && scrollY <= maxY - page.y + page.height / 2) return index
        }
        return -1
    }

}