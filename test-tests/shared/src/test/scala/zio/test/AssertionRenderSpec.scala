package zio.test

import zio.test.Assertion._

import scala.concurrent.duration.Duration

object AssertionRenderSpec extends ZIOBaseSpec {

  def spec = suite("Assertion Render Spec")(
    test("renders ops")({
      assertionShouldRenderTo(equalTo(30) || equalTo(40))("equalTo(30) or equalTo(40)") &&
      assertionShouldRenderTo(equalTo(30) && equalTo(40))("equalTo(30) and equalTo(40)")
    }),
    test("embedded assertions")({
      assertionShouldRenderTo(isSome(equalTo(40)))("isSome(equalTo(40))") &&
      assertionShouldRenderTo(isSome(equalTo(40) && contains("a")))("isSome(equalTo(40) and contains(a))")
    }),
    test("class names render")({
      assertionShouldRenderTo(isSubtype[Duration.Infinite](Assertion.anything))("isSubtype(Infinite)(anything)")
    }),
    test("list render")({
      assertionShouldRenderTo(equalTo(List.fill(9)(10.0)))(
        "equalTo(List(10.0, 10.0, 10.0, 10.0, 10.0, 10.0, 10.0, 10.0, 10.0))"
      )
    }),
    test("fields render")({
      case class Person(age: Int)
      assertionShouldRenderTo(hasField("age", (p: Person) => p.age, Assertion.equalTo(10)))(
        "hasField(_.age)(equalTo(10))"
      )
    }),
    test("map keys render")({
      assertionShouldRenderTo(hasKey(equalTo("bar")))("hasKey(equalTo(bar))")
    }),
    test("map has keys render")({
      assertionShouldRenderTo(hasKeys[String, Int](equalTo(List("key1", "key2"))))("hasKeys(equalTo(List(key1, key2)))")
    }),
    test("has none of")({
      assertionShouldRenderTo(hasNoneOf(List(123, 234, 32)))("hasNoneOf(List(123, 234, 32))")
    }),
    test("isUnit")({
      assertionShouldRenderTo(isUnit)("isUnit")
    }),
    test("hasSameElementsAs")({
      assertionShouldRenderTo(hasSameElements(List(1, 2, 3)))("hasSameElements(List(1, 2, 3))")
    }),
    test("approximately equals")({
      assertionShouldRenderTo(approximatelyEquals(10, 0.1))("approximatelyEquals(10.0, tolerance=0.1)")
    }),
    test("hasAt")({
      assertionShouldRenderTo(hasAt(1)(equalTo(40)))("hasAt(1)(equalTo(40))")
    }),
    test("failsWith")({
      assertionShouldRenderTo(failsWithA[RuntimeException])("failsWithA(RuntimeException)")
    }),
    test("isWithin")({
      assertionShouldRenderTo(Assertion.isWithin(10, 100))("isWithin(min=10, max=100)")
    }),
    test("diesWithA")({
      assertionShouldRenderTo(diesWithA[RuntimeException])("diesWithA(RuntimeException)")
    })
  )

  private def assertionShouldRenderTo[A](assertion: Assertion[A])(expected: String) =
    assert(assertion.render)(equalTo(expected))
}
