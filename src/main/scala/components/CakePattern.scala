package components

object MVC
  extends ModelModule.Interface
  with ViewModule.Interface
  with ControllerModule.Interface:

  override val model: ModelModule.Model = new ModelImpl()
  override val view: ViewModule.View = new ViewImpl()
  override val controller: ControllerModule.Controller = new ControllerImpl()
