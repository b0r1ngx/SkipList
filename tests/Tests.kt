fun main() {
    Tests().testAdd()
    Tests().testFewOperations()
}

class Tests {

    val skipList = SkipList<Int>()

    fun testContains() {

    }

    fun testAdd() {
        val testList = SkipList<Int>()
        testList.add(1)
        testList.add(2)
        testList.add(3)
        testList.add(4)
        testList.add(5)
        testList.add(4)
        println(testList)
        println(testList.toString(testList))
    }

    fun testRemove() {

    }

    fun testIndexOf() {

    }

    fun testIterator() {

    }

    fun testFewOperations() {
        println(skipList.MAX_HEIGHT)
        println(skipList.size)
        println(skipList.add(1))
        println(skipList.add(5))
        println(skipList.size)
        println(skipList.add(1))
        println(skipList.size)
    }
}

//fun main() {
//    println(SkipList<Int>())
//    val list = mutableListOf(1, 3)
//    println(list)
//    list[1] = 5
//    list.add(4) // so now 1, 5, 4
//    println(list)
//    list.sort()
//    println(list)
//    list.sortDescending()
//    println(list)
//    val linkedList = LinkedList<Int>()
//    linkedList.add(1)
//    linkedList.add(4)
//    linkedList.add(7)
//    linkedList.add(5)
//    println(linkedList)
//    linkedList.sort()
//    println(linkedList)
////    var sum = 0
////    for (i in 0..5000) {
////        sum += SkipList<Int>().jumpToLevel()
////    }
////    println(sum)
////    println("${sum.toDouble()/5000.0}")
////
////    for (i in 0..100000)
////        if (SkipList<Int>().jumpToLevel() >= 11) println("good!")
//}