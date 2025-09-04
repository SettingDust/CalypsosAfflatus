package settingdust.calypsos_afflatus.util

import net.minecraft.core.Direction

enum class Side(val direction: Direction) {
    BOTTOM(Direction.DOWN),
    TOP(Direction.UP),
    BACK(Direction.NORTH),
    FRONT(Direction.SOUTH),
    LEFT(Direction.WEST),
    RIGHT(Direction.EAST)
}