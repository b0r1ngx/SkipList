import java.util.*
import kotlin.math.E

//need sort!
class SkipList<T>: MutableList<T> {

    override var size: Int = 0
    /**
     * include 0 (0,1,2..11) my predict its good for 100k elements (in 8, 16, 32)
     */
    val MAX_HEIGHT = 12

    private val head: Node<T>? = null
    /**
     * One of my thing in mind, for practical realisation best chance is 1/2,
     * use 5/16 if u have less than 5k elements, using 1/E for 1kk elements and more(best for +unlimited)
     */
    private val CHANCE = 1/E
    /**
     * finally ain't needed, cos tail in realisation is just checked is next(end/NIL) is null
     *
     * @thanks Petrov M.A.
     */
    private val tail = head?.next?.add(null)
    /**
     * init the tower of first nulls elements of MAX_HEIGHT
     */
    var heads = ArrayList<Node<T>?>(MAX_HEIGHT)
    init {//size of heads is 12, so its a list on a level 0 with all elements
        //and a heads+1 list with fewer elements
        for (level in 0 until MAX_HEIGHT) {
            heads.add(level, null)
        }
    }

    inner class Node<T>(private var element: T) {
        //val nextAsQuestion = LinkedList<Node<T>?>()
        var next = ArrayList<Node<T>?>() //link to next tower of Nodes
        fun level(): Int = next.size - 1
        fun next(lvl: Int): Node<T>? = next[lvl] //link to next on some lvl
        fun value(): T = element
        fun setValue(value: T) {
            element = value
        }
        /**
         * Prepare the tower for added Node(i.e. element)
         */
        init {
            var lvl = 1
            while (Math.random() < CHANCE && lvl < MAX_HEIGHT)
                lvl++
            next = ArrayList(lvl)
            for (indexOfElement in 0 until lvl)
                next.add(indexOfElement, null)
        }
    }
    //easy realisation its just a brute force elements in a loop to index what we need
    //and optional for get, its a use lengths(we know about in a list on height 0 - we have all elements,
    //and the length of line from element to element is equals to 0, so with this info,
    // we can calcute upper lists length of distance between elements
    override fun get(index: Int): T {
        checkOfBound(index)
            var thisIndex = heads[0]
            for (i in 0 until index)
                thisIndex = thisIndex!!.next[0]
            return thisIndex!!.value()
    }

    //Replace the element at the specified position in this list with the specified element
    override fun set(index: Int, element: T): T {
//        if (contains(element))
//        var temp: Node<T>? = null
        checkOfBound(index)
            var thisIndex = heads[0]
            for (i in 0 until index)
                thisIndex = thisIndex!!.next[0]
            thisIndex!!.setValue(element) //cannot be null, cos null in our realisation is mean we at NIL elements, so its not exist for SkipList iterator
            return thisIndex.value()
    }

    //find the element in list or not in list
    override fun contains(element: T): Boolean {
        @Suppress("UNCHECKED_CAST")
        val contains = element as Comparable<T>
        var lvl = heads.size - 1 //heads.size i.e. tower height
        //check how biggest tower is, for a little get more speed of a search
        //but in book, really found this doesn't helps a much, or a little bit
        //so it was really expected:)
        var temp = heads[lvl]
        while (temp?.next == null) lvl-- //its mean if we have a null element
        while (lvl >= 0) {
            if (temp != null) {
                if (contains == temp.value())
                    return true
                if (lvl == 0 && temp.next(0) == null || contains < temp.value())
                    return false
            }
            when {
                temp?.next(lvl) == null -> lvl--
                contains < temp.next(lvl)!!.value() -> lvl--
                contains >= temp.next(lvl)!!.value() -> temp = temp.next(lvl)
            }
        }
        return false
    }

    override fun containsAll(elements: Collection<T>): Boolean {
        for (element in elements)
            if (!contains(element))
                return false
        return true
    }

    override fun add(element: T): Boolean {
        @Suppress("UNCHECKED_CAST")
        val add = element as Comparable<T> //for comparing!)
        val addThis: Node<T> = Node(element)
        val atLevel = addThis.level()
//potential high of new added node
        val path = ArrayList<Node<T>?>(MAX_HEIGHT)
        for (each in 0 until MAX_HEIGHT)
            path.add(each, null)

        var thisNode: Node<T>? = head
        for (lvl in MAX_HEIGHT - 1 downTo 0) {
            if (thisNode == null) {
                if (heads[lvl] == null || add < heads[lvl]!!.value())
                    path[lvl] = null
                else {
                    thisNode = heads[lvl]
                    while (thisNode?.next(lvl) != null && add > thisNode.next(lvl)!!.value())
                        thisNode = thisNode.next(lvl)!! //not null already so then add
                    path[lvl] = thisNode
                }
            } else {
                while ( thisNode?.next(lvl) != null && add > thisNode.next(lvl)!!.value())
                    thisNode = thisNode.next(lvl)!!
                path[lvl] = thisNode
            }
        }
//add new element to structure
        for (lvl in 0 until atLevel) {
            if (path[lvl] == null) {
                addThis.next[lvl] = heads[lvl]
                heads[lvl] = addThis
            } else {
                addThis.next[lvl] = path[lvl]!!.next(lvl)
                path[lvl]!!.next[lvl] = addThis
            }
        }
        size++
        return true
    }

    override fun add(index: Int, element: T) {
        TODO("not implemented")
    }

    override fun addAll(index: Int, elements: Collection<T>): Boolean {
        TODO("not implemented")
    }
    //i bet this doesn't works properly, but for first time it good
    override fun addAll(elements: Collection<T>): Boolean {
        var default = false
        for (element in elements)
            if (add(element))
                default = true
        return default
    }

    //this realisation is need to compare the value of next cell of index in which level list we in
    override fun remove(element: T): Boolean {
        size--
        return true
    }

    /**
     * Removes all of this collection's elements that are also contained in the specified collection.
     * Return true if any of the specified elements was removed from the collection,
     * false if the collection was not modified.
     */
    //for example need to work on this
    //my SkipList(1,3,5,6,8,9)
    //collection: (4,5)
    //after removeAll(collection), we need see (1,3,6,8,9)
    override fun removeAll(elements: Collection<T>): Boolean {
        //("for (i in elements) if (SkipList().contains(i)) remove(i)")
        var modified = false
        val collection = ArrayList<T>(elements)
        for (i in collection.indices) {
            modified = true
        }
        return modified
    }

    //remove at index
    override fun removeAt(index: Int): T {
        checkOfBound(index)
        if (index == 0) {
            val special = heads[0]!!
            val element = special.value()
            for(i in 0 until special.level())
                heads[i] = special.next(i)
            size--
            return element!!
        } else {
            val element = get(index)
            @Suppress("UNCHECKED_CAST")
            val removeAt = element as Comparable<T>

            //potential high of tower was deleted
            val path = ArrayList<Node<T>?>(MAX_HEIGHT)
            for (each in 0 until MAX_HEIGHT)
                path.add(each, null)

            var thisNode: Node<T>? = head
            for (lvl in MAX_HEIGHT - 1 downTo 0)
                if (thisNode == null) {
                    if (heads[lvl] == null || removeAt < heads[lvl]!!.value())
                        path[lvl] = null
                    else {
                        thisNode = heads[lvl]
                        while (thisNode!!.next(lvl) != null && removeAt > thisNode.next(lvl)!!.value())
                            thisNode = thisNode.next(lvl)
                        path[lvl] = thisNode
                    }
                } else {
                    while(thisNode!!.next(lvl) != null && removeAt > thisNode.next(lvl)!!.value())
                        thisNode = thisNode.next(lvl)
                    path[lvl] = thisNode
                }
            //deleting all the nodes in the tower
            val current = path[0]!!
            for (i in 0 until current.level())
                path[i]!!.next[i] = current.next(i)

            size--
            return current.value()
        }
    }

    override fun isEmpty(): Boolean = size == 0

    //just make operation like when start work with SkipList
    override fun clear() {
        size = 0
        heads = ArrayList(MAX_HEIGHT)
        for (i in 0 until MAX_HEIGHT) {
            heads.add(i, null)
        }
    }

    override fun retainAll(elements: Collection<T>): Boolean {
        TODO("not implemented")
    }

    override fun subList(fromIndex: Int, toIndex: Int): MutableList<T> {
        TODO("not implemented")
    }

    override fun indexOf(element: T): Int {
        TODO("not implemented")
    }

    override fun lastIndexOf(element: T): Int {
        TODO("not implemented")
    }

    override fun iterator(): MutableIterator<T> = SkipListIterator()

    override fun listIterator(): MutableListIterator<T> {
        TODO("not implemented")
    }

    override fun listIterator(index: Int): MutableListIterator<T> {
        TODO("not implemented")
    }

    inner class SkipListIterator: MutableIterator<T> {

        override fun hasNext(): Boolean {
            TODO("not implemented")
        }

        override fun next(): T {
            TODO("not implemented")
        }

        override fun remove() {
            TODO("not implemented")
        }
    }

    override fun toString(): String {
        return super.toString()
    }

    fun toString(a: SkipList<T>): String {
        if (a == null) return "null"
        val iMax: Int = a.size - 1
        if (iMax == -1) return "[]"

        return buildString {
            append("[")
            for (i in 0 until a.size) {
                append(a[i])
                if (i == iMax) return append("]").toString()
                append(", ")
            }
        }
    }

    override fun equals(other: Any?): Boolean {
        return super.equals(other)
    }

    override fun hashCode(): Int {
        return super.hashCode()
    }

    //Optional functions
    private fun checkOfBound(index: Int) {
        if (index < 0 || index >= size)
            throw IndexOutOfBoundsException("List properly not contains index like this!")
    }
}