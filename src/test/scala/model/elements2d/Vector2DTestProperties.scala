package model.elements2d

import org.scalacheck.{Prop, Properties, Gen}
import org.scalacheck.Prop.{forAll, exists}

object Vector2DTestProperties:
  private val vector2DGen: Gen[Vector2D] = for
    x <- Gen.choose(Double.MinValue, Double.MaxValue)
    y <- Gen.choose(Double.MinValue, Double.MaxValue)
  yield
    Vector2D(x, y)

  private val vectors2DGen: Gen[(Vector2D, Vector2D)] = for
    v1 <- vector2DGen
    v2 <- vector2DGen
  yield
    (v1, v2)

class Vector2DTestProperties extends Properties("Vector") :

  import Vector2DTestProperties._

  property("Sum is commutative") = forAll(vectors2DGen) { (v1: Vector2D, v2: Vector2D) =>
    v1 + v2 == v2 + v1
  }
  property("Sum has vector zero as identity") = forAll(vector2DGen) { (v: Vector2D) =>
    v + Vector2D.Zero == v && Vector2D.Zero + v == v
  }

