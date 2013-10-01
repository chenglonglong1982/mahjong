package websiteschema.mpsegment.util

import java.io._

import scala.collection.mutable.Map
import scala.collection.mutable.OpenHashMap
import websiteschema.mpsegment.math.Matrix

object SerializeHandler {
  val MODE_READ_ONLY = 0
  val MODE_WRITE_ONLY = 1

  def apply(f: File, mode: Int): SerializeHandler = {
    if (!f.exists()) f.createNewFile()

    if (mode == MODE_WRITE_ONLY) {
      return new SerializeHandler(null, new DataOutputStream(new FileOutputStream(f)))
    }
    if (mode == MODE_READ_ONLY) {
      return new SerializeHandler(new DataInputStream(new FileInputStream(f)), null)
    }

    return null
  }

  def apply(input: DataInputStream) = new SerializeHandler(input, null)

  def apply(input: InputStream) = new SerializeHandler(new DataInputStream(input), null)

  def apply(output: DataOutputStream) = new SerializeHandler(null, output)

  def apply(output: OutputStream) = new SerializeHandler(null, new DataOutputStream(output))
}

class SerializeHandler(input: DataInputStream, output: DataOutputStream) {

  def close() {
    if (null != input) {
      input.close()
    }
    if (null != output) {
      output.close()
    }
  }

  def serializeMapStringInt(map: java.util.Map[String, Int]) {
    if (null != map) {
      output.writeInt(map.size)
      if (!map.isEmpty) {
        val keys = new java.util.ArrayList[String](map.keySet())
        var i = 0
        while (i < keys.size()) {
          val key = keys.get(i)
          val value = map.get(key)
          output.writeUTF(key)
          output.writeInt(value)
          i += 1
        }
      }
      output.flush()
    }
  }

  def serializeMapIntDouble(map: java.util.Map[Int, Double]) {
    if (null != map) {
      output.writeInt(map.size)
      if (!map.isEmpty) {
        val keys = new java.util.ArrayList[Int](map.keySet())
        var i = 0
        while (i < keys.size()) {
          val key = keys.get(i)
          val value = map.get(key)
          output.writeInt(key)
          output.writeDouble(value)
          i += 1
        }
      }
      output.flush()
    }
  }

  def deserializeMapStringInt(): java.util.Map[String, Int] = {
    val size = input.readInt()
    if (size > 0) {
      val map = new java.util.HashMap[String, Int]()
      for (i <- 0 until size) {
        val key = input.readUTF()
        val value = input.readInt()
        map.put(key, value)
      }
      map
    } else {
      new java.util.HashMap[String, Int]()
    }
  }

  def deserializeMapIntDouble(): java.util.Map[Int, Double] = {
    val size = input.readInt()
    if (size > 0) {
      val map = new java.util.HashMap[Int, Double]()
      for (i <- 0 until size) {
        val key = input.readInt()
        val value = input.readDouble()
        map.put(key, value)
      }
      map
    } else {
      new java.util.HashMap[Int, Double]()
    }
  }

  def serializeArrayString(array: Array[String]) {
    if (null != array) {
      output.writeInt(array.length)
      for (str <- array) {
        serializeString(str)
      }
      output.flush()
    }
  }

  def deserializeArrayString(): Array[String] = {
    val size = input.readInt()
    val array = new Array[String](size)
    if (size > 0) {
      for (i <- 0 until size) {
        val value = deserializeString()
        array(i) = value
      }
    }
    return array
  }

  def serializeArrayInt(array: Array[Int]) {
    if (null != array) {
      output.writeInt(array.length)
      for (i <- array) {
        output.writeInt(i)
      }
      output.flush()
    }
  }

  def deserializeArrayInt(): Array[Int] = {
    val size = input.readInt()
    val array = new Array[Int](size)
    if (size > 0) {
      for (i <- 0 until size) {
        val value = input.readInt()
        array(i) = value
      }
    }
    return array
  }

  def serializeArrayDouble(array: Array[Double]) {
    if (null != array) {
      output.writeInt(array.length)
      for (i <- array) {
        output.writeDouble(i)
      }
      output.flush()
    }
  }

  def deserializeArrayDouble(): Array[Double] = {
    val size = input.readInt()
    val array = new Array[Double](size)
    if (size > 0) {
      for (i <- 0 until size) {
        val value = input.readDouble()
        array(i) = value
      }
    }
    return array
  }

  def deserializeMatrix(): Matrix = {
    val row = deserializeInt()
    val col = deserializeInt()
    val data = deserializeArrayDouble()
    Matrix(row, col, data)
  }

  def serializeMatrix(matrix: Matrix) {
    serializeInt(matrix.row)
    serializeInt(matrix.col)
    serializeArrayDouble(matrix.flatten)
  }

  def serialize2DArrayInt(array: Array[Array[Int]]) {
    if (null != array) {
      output.writeInt(array.length)
      for (a <- array) {
        output.writeInt(a.length)
        for (i <- a) {
          output.writeInt(i)
        }
      }
      output.flush()
    }
  }

  def deserialize2DArrayInt(): Array[Array[Int]] = {
    var size = input.readInt()
    var array = new Array[Array[Int]](size)
    if (size > 0) {
      for (i <- 0 until size) {
        var length = input.readInt()
        array(i) = new Array[Int](length)
        for (j <- 0 until length) {
          array(i)(j) = input.readInt()
        }
      }
    }
    return array
  }

  def serializeDouble(d: Double) {
    output.writeDouble(d)
    output.flush()
  }

  def deserializeDouble(): Double = {
    return input.readDouble()
  }

  def serializeInt(i: Int) {
    output.writeInt(i)
    output.flush()
  }

  def deserializeInt(): Int = {
    return input.readInt()
  }

  def serializeString(str: String) {
    output.writeUTF(str)
    output.flush()
  }

  def deserializeString(): String = {
    return input.readUTF()
  }
}
