package com.eteks.sweethome3d.adaptive.treetobuilding;

public enum Direction { left , down, up, right ;

public static Direction opposite(Direction dir) {
    switch (dir)
    {
      case down:
        return up;
      case up:
         return down;
      case left:
         return right;
      case right:
          return left;
      default:
           return null;
    }
}

public static Direction ortogonal(Direction dir) {
  switch (dir)
  {
    case down:
      return left;
    case up:
       return left;
    case left:
       return up;
    case right:
        return up;
    default:
         return null;
  }
}
}