/* KOTLIN FOR THE CURIOUS - Faisal Islam */
/* Chapter 07: Projects 28 */

// Merge sort

fun main() {
    // define an array to be sorted
    val arr = intArrayOf(8, 3, 4, 5, 1, 2)

    println("\n*** Sorting an Array Using Merge Sort ***\n")
    println("original array:\n${arr.contentToString()}")
   
    // call the recursive function
    mergeSort(arr)

    println("\nsorted array:\n${arr.contentToString()}")
}

// ---------------------------------------------------------------------

fun mergeSort(arr: IntArray) {
    val length = arr.size
    if (length < 2) return // done splitting subarrays

    val middle = length / 2
    val leftArray = arr.copyOfRange(0, middle)
    val rightArray = arr.copyOfRange(middle, length)

    mergeSort(leftArray)
    mergeSort(rightArray)
    merge(leftArray, rightArray, arr)
}

// ---------------------------------------------------------------------

fun merge(leftArray: IntArray, rightArray: IntArray,
     arr: IntArray) {

    val leftSize = arr.size / 2
    val rightSize = arr.size - leftSize
    var i = 0   // for original array
    var l = 0   // for left array
    var r = 0   // for right array

    // compare, sort and merge
    while(l < leftSize && r < rightSize) {
        if (leftArray[l] < rightArray[r]) {
            arr[i] = leftArray[l]
            l++
        } else {
            arr[i] = rightArray[r]
            r++
        }
        i++
    }
    
    // if all elements of a subarray are assigned, assign the
    // remaining elements of the nonempty array to "arr"
    while (l < leftSize) {
        arr[i] = leftArray[l]
        l++
        i++
    }
    
    while (r < rightSize) {
        arr[i] = rightArray[r]
        r++
        i++
    }
}
