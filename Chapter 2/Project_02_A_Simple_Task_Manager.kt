/* KOTLIN FOR THE CURIOUS - Faisal Islam */
/* PROJECT 2: A Simple Task Manager */


data class Task(val title: String,
                val description: String,
                var status: String = "not done"
)

class TaskManager {
    val taskList = mutableListOf<Task>()

    fun addTask(task: Task) {
        taskList.add(task)
    }

    fun listTasks() {
        if (taskList.size > 0 ) {
            println("\nTasks:")
            for ((index, task) in taskList.withIndex()) {
                println("${index+1}. ${task.title} - " +
                        "${task.description} - ${task.status}")
            }
        } else
            println("Task list is empty.")

    }

    fun markTaskAsDone(taskIndex: Int) {
        if (taskIndex in taskList.indices) {
            taskList[taskIndex].status = "done"
        } else {
            println("Invalid task index. Task not found.")
        }
    }

    fun deleteTask(taskIndex: Int) {
        if (taskIndex in taskList.indices) {
            taskList.removeAt(taskIndex)
        } else {
            println("Invalid task index. Task not found.")
        }
    }
}
// -----------------------------------------------------------------------------------
fun main() {
    val taskManager = TaskManager()

    while (true) {

        printOptions()

        when (readln()) {
            "1" -> {
                print("\nEnter task title: ")
                val title = readln()
                print("Enter task description: ")
                val description = readln()
                val task = Task(title, description)
                taskManager.addTask(task)
            }
            "2" -> taskManager.listTasks()
            "3" -> {
                taskManager.listTasks()
                if (taskManager.taskList.size <= 0) {
                    continue
                } else {
                    print("\nEnter the task number to mark as done: ")
                    val taskNumber = readIndex(taskManager.taskList.size)
                    if (taskNumber != null) {
                        taskManager.markTaskAsDone(taskNumber -1)
                    }
                }
            }
            "4" -> {
                taskManager.listTasks()
                if (taskManager.taskList.size <= 0) {
                    continue
                } else {
                    print("\nEnter the task number to be deleted: ")
                    val taskNumber = readIndex(taskManager.taskList.size)
                    if (taskNumber != null) {
                        taskManager.deleteTask(taskNumber -1)
                    }
                }
            }
            "5" -> break   // breaks the while() loop
            else -> println("\nInvalid choice. Please try again.")
        }
    }
}
// -----------------------------------------------------------------------------------
fun readIndex(taskListSize: Int): Int? {
    val input = readln()
    if (input.isNullOrBlank()) {
        println("Invalid input. Please enter a task number.")
        return null
    }

    val taskNumber = input.toIntOrNull()
    if (taskNumber != null && taskNumber >= 1 && taskNumber <= taskListSize) {
        return taskNumber
    } else {
        println("Invalid task number. Please enter a valid task number.")
        return null
    }
}
// -----------------------------------------------------------------------------------
fun printOptions() {
    println("\nTask Manager Menu:")
    println("1. Add Task")
    println("2. List Tasks")
    println("3. Mark Task as done")
    println("4. Delete Task")
    println("5. Exit")
    print("Enter your choice (1-5): ")
}

