package model

import model.collisions.LifePoint


package object vehicle:
    val planeVelocity: Int = 5
    val planeInitialLife: LifePoint = 3
    val satelliteInitialLife: LifePoint = 3
    val satelliteHeight = 10
    val spawnInterval = 10

    val planeBaseSize = 50
    val planeHeightSize = 15
    val satelliteBaseSize = 30
    val satelliteHeightSize = 30

    enum vehicleTypes:
        case Left_To_Right, Right_To_Left