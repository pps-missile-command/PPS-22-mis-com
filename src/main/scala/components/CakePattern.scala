package components

/**
 * Object that implements all the cake pattern's interfaces and the relatives dependencies
 * implicitly
 */
object MVC
  extends ModelModule.Interface
  with ViewModule.Interface
  with ControllerModule.Interface:

  override val model: ModelModule.Model = new ModelImpl()
  override val view: ViewModule.View = new ViewImpl()
  override val controller: ControllerModule.Controller = new ControllerImpl()
