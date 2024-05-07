/* KOTLIN FOR THE CURIOUS - Faisal Islam */
/* Chapter 02: full examples */


// -------------------- Listing 01 --------------------

class Person(val name: String = "", val age: Int = -99) {
    override fun toString(): String {
       return "Person(name=$name, age=$age)"
    }
}	

fun main() {
    val person1 = Person("John", 25)
    val person2 = Person("Irina", 21)
    println(person1)
    println(person2)
}

// -------------------- Listing 02 --------------------

class Car(val make: String, val model: String, val year: Int) {
    // property initialization inside class body
    var color: String = "Unknown"

    // 1st secondary constructor (no args)
    constructor() : this("Unknown", "Unknown", 0)

    // 2nd secondary constructor (1 arg)
    constructor(make: String) : this(make, "Unknown", 0)

    // 3rd secondary constructor (2 args)
    constructor(make: String, model: String) : this(make, model, 0)

    override fun toString(): String = 
        "Make: ${make}, Model: ${model}, Year: ${year}, Color: ${color}"
}

fun main() {
    val c1 = Car()
    val c2 = Car("Nissan")
    val c3 = Car("Toyota", "Prius")
    val c4 = Car("Ford", "Mustang", 2024)

    c1.color = "Blue"
    c2.color = "Red"
    c3.color = "Black"
    c4.color = "Yellow"

    println(c1)
    println(c2)
    println(c3)
    println(c4)
}

// -------------------- Listing 03 --------------------

class Person(private var name: String, private var age: Int) {
    fun introduce() {
        println("Hi, I'm $name, and I'm $age years old.")
    }

    fun haveBirthday() {
        age++
    }
}

fun main() {
    val person = Person("Alice", 30)

    // access and modify properties using public methods
    person.introduce()
    person.haveBirthday()
    person.introduce()

    // trying to access private properties directly 
    // will result in a compilation error
    // println(person.name)
    // person.age++
}

// -------------------- Listing 04 --------------------

class Book(var title: String, var author: String) {
    fun displayInfo() {
        println("Title: $title")
        println("Author: $author")
    }

    fun updateInfo(title: String, author: String) {
        this.title = title
        this.author = author
    }
}

fun main() {

    val book1 = Book("The Great Gatsby", "F. Scott Fitzgerald")

    // display book information
    book1.displayInfo()

    // update book information
    book1.updateInfo("To Kill a Mockingbird", "Harper Lee")
    
    println("\nUpdated book information:")
    book1.displayInfo()
}

// -------------------- Listing 05 --------------------

open class ParentClass(val name: String, val age: Int) {
    init {
        println()
        println("Hello, I am $name, and I am $age years old.")
    }
}

class ChildClass(name: String, age: Int, val occupation: String)
    : ParentClass(name, age) {

    init {
        println("My occupation is $occupation.")
    }
}

fun main() {
    // create instances of parent and child classes
    val person1 = ParentClass("John", 33)
    val person2 = ChildClass("Sarah", 24, "acounting")
}

// -------------------- Listing 06 --------------------

// parent class
open class Vehicle {
    open fun startEngine() {
        println("Vehicle engine started")
    }
}

// child class 
class Car : Vehicle() {
    override fun startEngine() {
        println("Car engine started")
    }
}

fun main() {
    val myCar = Car()
    myCar.startEngine() 
}

// -------------------- Listing 07 --------------------

// declare a data class
data class Person(val name: String, val age: Int)

fun main() {
    // create an instance
    val person = Person("Steve", 40)
    println(person)
}

// -------------------- Listing 08 --------------------

abstract class Shape {
    abstract fun area(): Double  // abstract method
    val name: String = "Shape"   // concrete property
    fun describe() {
        println("This is a $name")
    }
}

class Circle(val radius: Double): Shape() {
    override fun area(): Double {
        return Math.PI * radius * radius
    }
}

class Square(val side: Double): Shape() {
    override fun area(): Double {
        return side * side
    }
}

fun main() {
    val circle = Circle(5.0)
    val square = Square(4.0)

    circle.describe()
    println("Area of the circle: ${circle.area()}")

    square.describe()
    println("Area of the square: ${square.area()}")
}

// -------------------- Listing 09 --------------------

Import kotlin.math.PI

interface Properties {
    fun area(): Double
    fun perimeter(): Double
}

class Circle(val radius: Double): Properties {
    override fun area() = PI * radius * radius
    override fun perimeter() = 2 * PI * radius
}

fun main() {
    val circle = Circle(4.0)
    val area = circle.area()
    val perimeter = circle.perimeter()

    println("Properties of the circle:")
    println(" radius = ${circle.radius}\n area = $area\n" +
            " perimeter = $perimeter")
}

// -------------------- Listing 10 --------------------

// define an enum class for days of the week
enum class DayOfWeek {
    MONDAY, TUESDAY, WEDNESDAY, THURSDAY, 
    FRIDAY, SATURDAY, SUNDAY
}

fun main() {
    // using the enum values
    val today = DayOfWeek.MONDAY

    when (today) {
        DayOfWeek.MONDAY -> println("It's a manic Monday!")
        else -> println("It's some other day.")
    }
}

// -------------------- Listing 11 --------------------

data class Person(val name: String,
                  val hobbies: MutableList<String>)  
fun main() {
    
    val person1 = Person("Bob", mutableListOf("Reading", "Gaming"))
    val person2 = person1.copy()

    // print both objects
    println(person1)
    println(person2)

    // add a new element to the mutable list of person1
    person1.hobbies.add("Coding")

    // print both objects again
    println(person1)
    println(person2)
}

// -------------------- Listing 12 --------------------

data class Address(var street: String, val city: String)
data class Person(val name: String, val address: Address)

fun deepCopyPerson(person: Person): Person {
    val clonedAddress = Address(person.address.street,
                                person.address.city)
    return Person(person.name, clonedAddress)
}

fun main() {
    val originalPerson = Person("Alice", Address("123 Main St", "Cityville"))
    val copiedPerson = deepCopyPerson(originalPerson)

    // modify the original address
    originalPerson.address.street = "456 Elm St"

    // check if the copied address remains unchanged
    println(originalPerson.address.street) //output: 456 Elm St
    println(copiedPerson.address.street)  // output: 123 Main St
}

// -------------------- Listing 13 --------------------

data class Person(var name: String, var age: Int)

fun main() {
    // original mutable list
    val originalList =
        mutableListOf(Person("Alice", 30), Person("Bob", 25))

    // deep copy the list using map() and copy()
    val deepCopyList = 
        originalList.map{ it.copy() }.toMutableList()
}


