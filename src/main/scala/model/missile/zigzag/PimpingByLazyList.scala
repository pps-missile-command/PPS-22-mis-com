package model.missile.zigzag

object PimpingByLazyList:
  /**
   * Extension method that adds the pop mechanism to a LazyList, return a pair composed by the first element
   * to pop out and the rest of the list without the popped element.
   */
  extension[A](list: LazyList[A])
    def pop(): (A, LazyList[A]) = (list.take(1).head, list.tail)