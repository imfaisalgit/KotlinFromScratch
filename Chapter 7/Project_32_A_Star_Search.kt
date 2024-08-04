/* KOTLIN FOR THE CURIOUS - Faisal Islam */
/* Chapter 07: Projects 32 */

// A-Star Search

// no import block

data class Node(
    var gScore: Int,
    var fScore: Int,
    var previousNode: String
)

fun main() {
    // define the graph to be searched
    val graph = mapOf(
        "A" to mapOf("B" to 4, "C" to 6, "D" to 5),
        "B" to mapOf("A" to 4, "C" to 2, "E" to 4, "F" to 4),
        "C" to mapOf("A" to 6, "B" to 2, "D" to 3, "F" to 3),
        "D" to mapOf("A" to 5, "C" to 3, "G" to 6, "H" to 4),
        "E" to mapOf("B" to 4, "I" to 6),
        "F" to mapOf("B" to 4, "C" to 3, "G" to 4, "I" to 5),
        "G" to mapOf("D" to 6, "F" to 4, "I" to 6, "K" to 3),
        "H" to mapOf("D" to 4, "K" to 3),
        "I" to mapOf("E" to 6, "F" to 5, "G" to 6, "J" to 6),
        "J" to mapOf("I" to 6, "K" to 5),
        "K" to mapOf("G" to 3, "H" to 3, "J" to 5, "L" to 3),
        "L" to mapOf("K" to 3, "M" to 5),
        "M" to mapOf("L" to 5)
    )

    println("### A* algorithm ###")
    println("\nDisplay graph:")
    displayGraph(graph)

    val startNode  = "A"
    val targetNode = "J"    // also ensure its h-score is 0
    val visitedList = aStar(graph, startNode, targetNode)

    println("\n--- Final Visited List ---")
    displayList(visitedList)
    displayShortestPath(visitedList, startNode, targetNode)
}

// ---------------------------------------------------------------------

fun displayGraph(graph: Map<String, Map<String, Int>>) {
    for ((node, neighbors) in graph) {
        println("Node: $node")
        print("Neighbors: ")

        for ((nNode, cost) in neighbors) {
            print("$nNode:$cost ")
        }
        println()
    }
    println()
}

// ---------------------------------------------------------------------

fun displayList(mapList: Map<String, Node>) {
    println("   (g-score, f-score, previous)")

    for ((node, attributes) in mapList) {
        println("$node: $attributes")
    }
    println()
}

// ---------------------------------------------------------------------

fun displayShortestPath(visited: Map<String, Node>,
                        startNode: String, targetNode: String) {

    var currentNode = targetNode
    var path = targetNode
    println("path initialized from target: $path")

    while (currentNode != startNode) {
        val previousNode = visited[currentNode]!!.previousNode
        // previousNode is placed before "path" so no need to reorder
        path = previousNode + path
        println("previousNode: $previousNode")
        println("path updated: $path")
        currentNode = previousNode
    }

    val cost = visited[targetNode]!!.gScore
    println("\nThe shortest path from $startNode to $targetNode is:")
    println("Path: $path")
    println("Cost: $cost")
}

// ---------------------------------------------------------------------

fun aStar(graph: Map<String, Map<String, Int>>,
          startNode: String, targetNode: String):
        Map<String, Node> {

    // define two mutable maps
    val visited = mutableMapOf<String, Node>()
    val unvisited = mutableMapOf<String, Node>()

    // initialize all unvisited nodes
    for (node in graph.keys) {
        // the list is made of g-score, f-score, and previous node
        unvisited[node] = Node(Int.MAX_VALUE, Int.MAX_VALUE, "none")
    }

    // update the start node attributes in the unvisited list
    val hScore = getHScore(startNode)

    // for startNode: g-score=0, f-score=10, previous node=none
    unvisited[startNode] = Node(0, hScore, "none")

    println("--- Initialized state of unvisited list ---")
    displayList(unvisited)

    while (unvisited.isNotEmpty()) {
        // set the node with minimum f-score to current node
        val currentNode = getCurrentNode(unvisited)

        if (currentNode == targetNode) {
            // add the targetNode to visited
            visited[currentNode] = unvisited[currentNode]!!
            println("--- Target node:$currentNode reached ---")
            break
        }

        val neighbors = graph[currentNode]!!

        for (node in neighbors.keys) {
            if (node !in visited) {
                val newGScore =
                    unvisited[currentNode]!!.gScore + neighbors[node]!!

                if (newGScore < unvisited[node]!!.gScore) {
                    unvisited[node] = Node(
                        newGScore,
                        newGScore + getHScore(node),
                        currentNode)
                }
            }
        }

        // add currentNode to visited
        visited[currentNode] = unvisited[currentNode]!!

        // remove currentNode from unvisited
        unvisited.remove(currentNode)
    }
    return visited
}

// ---------------------------------------------------------------------

fun getHScore(node: String) = when (node) {
    "A" -> 8   // start node
    "B" -> 6
    "C" -> 6
    "D" -> 6
    "E" -> 4
    "F" -> 4
    "G" -> 4
    "H" -> 4
    "I" -> 2
    "J" -> 0    // target node
    "K" -> 2
    "L" -> 4
    "M" -> 6
    else -> 0
}

// ---------------------------------------------------------------------

fun getCurrentNode(unvisited: Map<String, Node>) =
    unvisited.minByOrNull { it.value.fScore }!!.key