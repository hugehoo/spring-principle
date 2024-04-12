package com.spring.principle.examples.roulette

import jakarta.persistence.*
import lombok.AllArgsConstructor
import lombok.NoArgsConstructor

@Entity
@Table(name = "user")
@NoArgsConstructor
@AllArgsConstructor
class User(
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    var grade: GradeStatus
) {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null
}

enum class GradeStatus(description: String) {
    BASIC("기본 등급"),
    PREMIUM("프리미엄 등급"),
    VIP("VIP 등급")
}