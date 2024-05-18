/* KOTLIN FOR THE CURIOUS - Faisal Islam */
/* Chapter 07: Projects 26 */

// Insertion sort

fun main() {
    // define an array to be sorted
    val arr = intArrayOf(8, 3, 4, 5, 1, 2)

    println("\n*** Sorting an Array Using Insertion Sort ***\n")
    println("original array:\n${arr.contentToString()}")
    
    // call the insertion sort function
    insertionSort(arr)
    
    println("sorted array:\n${arr.contentToString()}")
}

// ---------------------------------------------------------------------

fun insertionSort(A: IntArray) {
    // sorting happens in-place
    for (i in 1 until A.size) {
        val key = A[i]
        var j = i
        while( j > 0 && A[j-1] > key ) {
            A[j] = A[j-1]
            j -= 1
        }
        A[j] = key
    }
}
