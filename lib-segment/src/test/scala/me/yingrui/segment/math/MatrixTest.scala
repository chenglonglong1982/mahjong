package me.yingrui.segment.math

import me.yingrui.segment.Assertion._
import org.scalatest.{Matchers, FunSuite}

class MatrixTest extends FunSuite with Matchers {

  test("should_return_matrix_size") {
    val m = Matrix(2, 2)
    m.flatten.length shouldBe 4
    shouldBeEqual(0D, m.flatten)
  }

  test("should_return_right_value_when_add_number_to_matrix") {
    val m = Matrix(2, 2) + 1
    shouldBeEqual(1D, m.flatten)
  }

  test("should_return_right_value_when_add_other_matrix") {
    val m = Matrix(2, 2, Array(1D, 2D, 1D, 2D)) + Matrix(2, 2, Array(2D, 1D, 2D, 1D))
    shouldBeEqual(3D, m.flatten)
  }

  test("should_return_right_value_when_subtract_number_to_matrix") {
    val m = Matrix(2, 2) - 1
    shouldBeEqual(-1D, m.flatten)
  }

  test("should_return_right_value_when_subtract_other_matrix") {
    val m = Matrix(2, 2, Array(3D, 2D, 3D, 2D)) - Matrix(2, 2, Array(2D, 1D, 2D, 1D))
    shouldBeEqual(1D, m.flatten)
  }

  test("should_return_right_value_when_divide_number_to_matrix") {
    val m = Matrix(2, 2, Array(10D, 10D, 10D, 10D)) / 10
    shouldBeEqual(1D, m.flatten)
  }

  test("should_throw_exception_when_matrix_form_is_not_match") {
    try {
      val m = Matrix(2, 2, Array(1D, 2D, 1D, 2D)) + Matrix(3, 3, Array(2D, 1D, 2D, 1D))
      fail()
    }
    catch {
      case _: Throwable =>
    }
  }

  test("should_return_all_elements_in_specified_row") {
    val m = Matrix(2, 3, Array(1D, 1D, 1D, 2D, 2D, 2D))
    m.row(0).col shouldBe 3
    shouldBeEqual(1D, m.row(0).flatten)
  }

  test("should_return_all_elements_in_specified_col") {
    val m = Matrix(2, 3, Array(1D, 2D, 3D, 1D, 2D, 3D))
    m.col(0).col shouldBe 2
    shouldBeEqual(1D, m.col(0).flatten)
  }

  test("should_return_right_value_when_multiply_number") {
    val m = Matrix(2, 2, Array(1D, 1D, 1D, 1D)) x 4D
    shouldBeEqual(4D, m.flatten)
  }

  test("should_return_right_value_when_dot_product_two_matrix") {
    val dotProductResult = Matrix(1, 4, Array(1D, 1D, 1D, 1D)) * Matrix(1, 4, Array(1D, 1D, 1D, 1D))
    shouldBeEqual(4D, dotProductResult)
  }

  test("should_enable_apply_method_to_assign_value") {
    val m = Matrix(2, 2, Array(1D, 1D, 2D, 2D))
    shouldBeEqual(1D, m(0,0))
    shouldBeEqual(1D, m(0,1))
    m(0,1) = 2D
    shouldBeEqual(2D, m(0,1))
  }

  test("should_return_right_value_when_multiply_matrices") {
    val m = Matrix(2, 2, Array(1D, 1D, 2D, 2D))
    val n = Matrix(2, 2, Array(1D, 1D, 2D, 2D))
    val matrix = m x n
    shouldBeEqual(3D, matrix(0, 0))
    shouldBeEqual(3D, matrix(0, 1))
    shouldBeEqual(6D, matrix(1, 0))
    shouldBeEqual(6D, matrix(1, 1))
  }

  test("should_transpose_matrix") {
    val m = Matrix(2, 3, Array(1D, 2D, 3D, 4D, 5D, 6D))
    val n = m.T
    shouldBeEqual(1D, n(0, 0))
    shouldBeEqual(4D, n(0, 1))
    shouldBeEqual(2D, n(1, 0))
    shouldBeEqual(5D, n(1, 1))
    shouldBeEqual(3D, n(2, 0))
    shouldBeEqual(6D, n(2, 1))
  }

  test("should_return_true_if_this_matrix_is_a_vector") {
    val m = Matrix(1,2, Array(1D, 2D))
    m.isVector shouldBe true
    val n = Matrix(2,2, Array(1D, 2D, 1D, 2D))
    n.isVector shouldBe false
  }

  test("should_return_mapped_matrix") {
    val m = Matrix(1,2, Array(2D, 2D))
    val n = Matrix.map(m, d => d + 1D)
    shouldBeEqual(3D, n.flatten)
  }

  test("should_return_true_when_compare_two_matrix") {
    val m = Matrix(2, 2, Array(2D,2D,3D,3D))
    val n = Matrix(2, 2, Array(2D,2D,3D,3D))
    m shouldBe n
  }

  test("should_return_false_when_compare_two_matrix") {
    val m = Matrix(2, 2, Array(2D,2D,3D,3D))
    val n = Matrix(2, 2, Array(2D,2D,3D,4D))
    m == n shouldBe false
  }

  test("should_clear_matrix") {
    val m = Matrix(2, 2, Array(2D,2D,3D,3D))
    m.clear
    shouldBeEqual(0D, m.flatten)
  }

  test("should_return_sum_of_all_elements") {
    val m = Matrix(2, 2, Array(2D,2D,3D,3D))
    shouldBeEqual(10D, m.sum)
  }
}
