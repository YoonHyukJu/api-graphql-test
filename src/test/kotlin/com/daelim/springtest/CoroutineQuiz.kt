package com.daelim.springtest

import kotlinx.coroutines.*
import net.datafaker.Faker
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import java.util.*

@SpringBootTest
class CoroutineQuiz {

    private val faker = Faker(Locale.KOREA)

    @Test
    fun testDataFaker() {
        val faker = Faker(Locale.KOREA)
        println(faker.name().name())
    }

    /**
     * 1. 100명의 가상 사용자 이름을 로그로 출력하세요
     */
    @Test
    fun quiz1Test() = runBlocking {
        repeat(100) {
            launch {
                println("사용자 이름: ${faker.name().name()}")
            }
        }
    }

    /**
     * 2. 50명의 가상 사용자의 이름, 이메일, 주소를 로그 출력하세요
     */
    @Test
    fun quiz2Test() = runBlocking {
        repeat(50) {
            launch {
                val name = faker.name().name()
                val email = faker.internet().emailAddress()
                val address = faker.address().fullAddress()
                println("이름: $name | 이메일: $email | 주소: $address")
            }
        }
    }

    /**
     * 3. 30명의 가상 사용자의 이름과 나이 생성를 데이터클래스로 생성하고,
     * 어린 나이 순으로 정렬 후 출력하세요
     */
    data class User(val name: String, val age: Int)

    @Test
    fun generateSortAndPrintUserAges() = runBlocking {
        // 30명의 사용자 데이터를 비동기적으로 생성
        val userDeferredList = List(30) {
            async {
                User(
                    name = faker.name().name(),
                    age = faker.number().numberBetween(10, 80) // 10세~80세 사이
                )
            }
        }

        // 결과 수집 및 정렬
        val users = userDeferredList.awaitAll()
        val sortedUsers = users.sortedBy { it.age }

        // 출력
        println("--- 어린 나이 순 정렬 결과 ---")
        sortedUsers.forEachIndexed { index, user ->
            println("${index + 1}. [${user.age}세] ${user.name}")
        }
    }
}