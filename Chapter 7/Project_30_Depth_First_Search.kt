/* KOTLIN FOR THE CURIOUS - Faisal Islam */
/* Chapter 07: Projects 30 */

// Depth-First Search

import java.util.ArrayDeque

fun main() {
    val graph = mapOf(
        "0" to setOf("1", "2", "3"),
        "1" to setOf("0", "2"),
        "2" to setOf("0", "1", "4"),
        "3" to setOf("0"),
        "4" to setOf("2")
    )
    println("\n*** Depth-First Search of a Graph ***\n")
    println("Graph to search:")

    for ((key,value) in graph)
        println("Node: $key,  Neighbors: $value")

    val visited = dfsStack(graph, "0")
    println("\nVisited nodes:\n$visited")
}

// ---------------------------------------------------------------------

fun dfsStack(graph: Map<String, Set<String>>, start: String):
               Set<String> { 

    val visited = mutableSetOf<String>()
    val stack = ArrayDeque<String>()
    stack.push(start)

    while (stack.isNotEmpty()) {
        val node = stack.pop()
        if (node !in visited) {
            // do something as needed
            visited.add(node)
            for (next in graph[node]!!) {
            	stack.push(next)
            }
        }
    }
    return visited
}
