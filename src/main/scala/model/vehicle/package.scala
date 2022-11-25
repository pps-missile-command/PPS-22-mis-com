package model

import model.collisions.LifePoint


package object vehicle:
    val planeVelocity: Int = 5
    val planeInitialLife: LifePoint = 3

    enum vehicleTypes:
        case Left_To_Right, Right_To_Left