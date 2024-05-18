/* KOTLIN FOR THE CURIOUS - Faisal Islam */
/* Chapter 07: Projects 31 */

// Breadth-first search

import java.util.ArrayDeque

fun main() {
    // define the graph to be searched
    val graph = mapOf(
        "0" to setOf("1", "2", "3"),
        "1" to setOf("0", "2"),
        "2" to setOf("0", "1", "4"),
        "3" to setOf("0"),
        "4" to setOf("2")
    )
    println("\n*** Breadth-First Search of a Graph ***\n")
    println("Graph to search:")
    for ((key,value) in graph)
        println("Node: $key,  Neighbors: $value")

    val visited = bfsQueue(graph, "0")
    println("\nVisited nodes:\n$visited")
}

// ---------------------------------------------------------------------

fun bfsQueue(graph: Map<String, Set<String>>, start: String): Set<String> {
    val visited = mutableSetOf<String>()
    visited.add(start)
val queue = ArrayDeque<String>()
    queue.offer(start)
    
    while (queue.isNotEmpty()) {
        val node = queue.poll()
        for (next in graph[node]!!) {
            if (next !in visited) {
                queue.offer(next)
                visited.add(next)
            }
        }
    }
    return visited
}
