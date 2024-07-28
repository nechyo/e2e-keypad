package bob.e2e.presentation.dto

import bob.e2e.domain.model.E2eKeypad

data class E2eKeypadRequestDto (
    val id: String
) {
    fun toEntity(hash: String, image: String) =
        E2eKeypad(
            id = id,
            hash = hash,
            image = image,
        )
}