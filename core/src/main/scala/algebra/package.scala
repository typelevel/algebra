package object algebra {
  type Ring[A] = algebra.ring.Ring[A]
  type Field[A] = algebra.ring.Field[A]

  type sp = spec._specialized
  type mb = spec._miniboxed
}
