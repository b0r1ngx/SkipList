import kotlin.math.E
import kotlin.random.Random

//SkiplistList = listOf(1,2,3,...,n)
/**
//Выполнение по 4.3 я так понял должен иметь обязательно такой вид изначальго листа от 1,2,3 до Н
//четко и последоватеьно, чтобы length, на которых основаны основные функции правильно работали
*/
class SkipList(): MutableList<Any> {

    override var size: Int = 0
        get() = this.size
    var n = 0
    val p = 1/E //more efficient probability for list of elements comes to +infinity
    val p1 = 5/16//maybe take 5/16 for probability property and lists size < 5000
    val MAX_HEIGHT = 15 // for example?
    var H = 15 //MAX_HEIGHT
    val sentinel = Node(first(), H) // И он не понимает что я хочу от него здесь, да я если честно не пойму)
    val tail = Node(sentinel.next, H)//NIH - нужен ли тейл?

    class Node(var element: Any?, H: Int) {
        //Node[] next
        //next = (Node[])Array.newInstance(Node.class, h+1)
        //var next: Array<Node> = Array.newInstance(Node::class.java, h + 1) as Array<Node>
        //var nex = Array<Node>(H + 1)
        // var nex = Array<Node> = arrayOf(null, H + 1)
        var next: Array<Node> = arrayOf(Node::class.java, H + 1) as Array<Node>
        var length = IntArray(H + 1)

        fun height(): Int {
            return next.size - 1
        }
    }

//    internal class Node<T>(var element: T, h: Int) {
//        var next: Array<Node<T>> = Array<Node<T>>(h + 1)
//        fun level(): Int {
//            return next.size - 1
//        }
//    }

    //determine the height - k, of a new node
    fun pickHeight(): Int {
        val z: Int = Random.nextInt() //rand.nextInt()
        var k = 0
        var m = 1
        while (z and m != 0) {
            k++
            m = m shl 1
        }
        return k
    }

    fun findPred(index: Int): Node? {
        var r = H //Int
        var u = sentinel //Node(u)
        var j = -1 // the index of u
        while (r >= 0) {
            while (u.next[r] != null && j + u.length[r] < index) {// -1 + (sentinel).length[H] < index
                j += u.length[r]
                u = u.next[r]
            }
            r--
        }
        return u
    }



    override fun get(index: Int): Any {
        if (index < 0 || index > n - 1) throw IndexOutOfBoundsException()
        return findPred(index)!!.next[0].element!!
    }

    override fun set(index: Int, element: Any): Any {
        if (index < 0 || index > n - 1) throw IndexOutOfBoundsException()
        val u = findPred(index)!!.next[0]
        val y: Any? = u.element
        u.element = element
        return y!!
    }

    override fun add(index: Int, element: Any) {
        if (index < 0 || index > n) throw IndexOutOfBoundsException()
        val w = Node(element, pickHeight())
        if (w.height() > H) H = w.height()
        add(index, w)
    }

    fun add(i: Int, w: Node): Node? {
        val height = w.height()
        var u = sentinel
        var j = -1 // index of u
        var r: Int = H
        while (r >= 0) {
            while (u.next[r] != null && j + u.length[r] < i) {
                j += u.length[r]
                u = u.next[r]
            }
            u.length[r]++ // to account for new node in list 0
            if (r <= height) {
                w.next[r] = u.next[r]
                u.next[r] = w
                w.length[r] = u.length[r] - (i - j)
                u.length[r] = i - j
            }
            r--
        }
        n++
        return u
    }

    override fun removeAt(index: Int): Boolean { //:Any
        if (index < 0 || index > n - 1) throw IndexOutOfBoundsException()
        var x: Any? = null
        var u: Node = sentinel
        var j = -1 // index of node u
        var r: Int = H
        while (r >= 0) {
            while (u.next[r] != null && j + u.length[r] < index) {//-1 + sentinel.length[H] < index of remove element
                j += u.length[r]
                u = u.next[r]
            }
            u.length[r]-- // for the node we are removing
            if (j + u.length[r] + 1 == index && u.next[r] != null) {
                x = u.next[r].element
                u.length[r] += u.next[r].length[r]
                u.next[r] = u.next[r].next[r]
                if (u == sentinel && u.next[r] == null) H--
            }
            r--
        }
        n--
        return true //return x if fun (): Any<--
    }
    //Returns true if element is found in the collection.
    override fun contains(element: Any): Boolean {
        TODO()
    }
    //Checks if all elements in the specified collection are contained in this collection.
    override fun containsAll(elements: Collection<Any>): Boolean {
        TODO("not implemented")
    }

    override fun isEmpty(): Boolean {
        TODO("not implemented")
    }

    override fun iterator(): MutableIterator<Any> {
        TODO("not implemented")
    }
    //Adds the specified element to the end of this list.
    override fun add(element: Any): Boolean {
        if (size < 2) throw IndexOutOfBoundsException()
        add(n - 1, element)
        return get(n-1) == element
    }
    //Inserts all of the elements of the specified collection elements into this list at the specified index.
    override fun addAll(index: Int, elements: Collection<Any>): Boolean {
        TODO("not implemented")
    }
    //Adds all of the elements of the specified collection to the end of this list.
    override fun addAll(elements: Collection<Any>): Boolean {
        if (size < 2) throw IndexOutOfBoundsException()
        for (element in elements)
            add(n - 1, element)
        return containsAll(elements)
    }
    //Removes all elements from this collection.
    override fun clear() {
        TODO("not implemented")
    }
    //Returns a list iterator over the elements in this list (in proper sequence).
    override fun listIterator(): MutableListIterator<Any> {
        TODO("not implemented")
    }
    //Returns a list iterator over the elements in this list (in proper sequence), starting at the specified index.
    override fun listIterator(index: Int): MutableListIterator<Any> {
        TODO("not implemented")
    }
    //Removes a single instance of the specified element from this collection, if it is present.
    override fun remove(element: Any): Boolean {
        TODO("not implemented")
    }
    //Removes all of this collection's elements that are also contained in the specified collection.
    override fun removeAll(elements: Collection<Any>): Boolean {
        TODO("not implemented")
    }
    //Retains only the elements in this collection that are contained in the specified collection.
    override fun retainAll(elements: Collection<Any>): Boolean {
        TODO("not implemented")
    }
    //Returns a view of the portion of this list between the specified fromIndex (inclusive) and
    // toIndex (exclusive). The returned list is backed by this list,
    // so non-structural changes in the returned list are reflected in this list, and vice-versa.
    override fun subList(fromIndex: Int, toIndex: Int): MutableList<Any> {
        if (size < 2) throw IndexOutOfBoundsException()
        return mutableListOf() //just for no warning
    }
    //get index of element, which in list
    override fun indexOf(element: Any): Int {
        if (n < 0 || size < 2) throw IndexOutOfBoundsException()
        var x: Any? = null
        var u: Node = sentinel //head(u)
        var j = -1 // index of node u
        var r: Int = H // visota levela
        val count = 0
        while (r >= 0) {

        }
        return -1
    }
//the same as indexOf() cos skip list ain't have same elements, same only at levels, so that means different
    override fun lastIndexOf(element: Any): Int = indexOf(element)
}

fun main() {
    println(SkipList().p)
    //println(SkipList().pickHeight())
}