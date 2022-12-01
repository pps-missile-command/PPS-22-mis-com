package model.missile.zigzag

object PimpingByLazyList:
  
  extension[A](list: LazyList[A])
    
    /**
    * Extension method that adds the pop mechanism to a LazyList, return a pair composed by the first element
    * to pop out and the rest of the list without the popped element.
    * @return a tuple composed by the element popped out and the new LazyList updated with the head element removed
    */
    def pop(): (A, LazyList[A]) = (list.take(1).head, list.tail)