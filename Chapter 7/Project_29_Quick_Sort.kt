/* KOTLIN FOR THE CURIOUS - Faisal Islam */
/* Chapter 07: Projects 29 */

// Quick sort

fun main() {
    // define an array to be sorted
    val arr = intArrayOf(8, 3, 4, 5, 1, 2)

    println("\n*** Sorting an Array Using Quick Sort ***\n")
    println("original array:\n${arr.contentToString()}")

    // call the recursive function
    quickSort(arr, start = 0, end = arr.size -1)

    println("\nsorted array:\n${arr.contentToString()}")
}

// ---------------------------------------------------------------------

fun quickSort(arr: IntArray, start: Int, end: Int) {
    // check the termination condition for recursion
    // base case is when start = end
    if (start < end) {
        val pivotIndex = partition(arr, start, end)
        quickSort(arr = arr, start = start, end = pivotIndex - 1)
        quickSort(arr = arr, start = pivotIndex + 1, end = end)
    }
}

// ---------------------------------------------------------------------

fun partition(arr: IntArray, start: Int, end: Int): Int {
    val pivot = arr[end]
    var i = start

    for (j in start until end) {
        if (arr[j] < pivot) {
            swap(arr, i, j)
            i++
        }
    }
    swap(arr, i, end)
    return i
}

// ---------------------------------------------------------------------

fun swap(arr: IntArray, i: Int, j: Int) {
    val temp = arr[i]
    arr[i] = arr[j]
    arr[j] = temp
}  
