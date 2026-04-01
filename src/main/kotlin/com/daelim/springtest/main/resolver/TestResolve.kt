package com.daelim.springtest.main.resolver

import com.daelim.springtest.main.api.model.dto.TestDto
import net.datafaker.Faker
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.MutationMapping
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.stereotype.Controller
import java.util.*

@Controller // @Component 대신 @Controller 사용 권장
class PostResolver {
    private val tests = mutableListOf<TestDto>()
    private val faker = Faker(Locale.KOREA)

    @QueryMapping // 스키마의 Query { findAllTests }와 매핑
    fun findAllTests(): List<TestDto> {
        return tests
    }

    @QueryMapping // 스키마의 Query { findTestById(id: String) }와 매핑
    fun findTestById(@Argument id: String): TestDto? { // 파라미터는 @Argument 필수
        return tests.find { it.id == id }
    }

    @MutationMapping // 스키마의 Mutation { createTest(userId: String) }와 매핑
    fun createTest(@Argument userId: String): TestDto {
        val test = TestDto(
            id = userId,
            address = faker.address().fullAddress(),
            email = faker.internet().emailAddress(),
            tel = faker.phoneNumber().phoneNumber(),
            age = Random().nextInt(100)
        )
        tests.add(test)
        return test
    }
}