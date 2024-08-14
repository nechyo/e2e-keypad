package bob.e2e.presentation.dto

import bob.e2e.domain.model.E2eKeypad
import bob.e2e.domain.model.E2eKeypadHash

data class E2eKeypadRequestDto (
    val keys: E2eKeypadHash,
    val image: String
) {
    fun toEntity(keys: E2eKeypadHash, image: String) =
        E2eKeypad(
            keys = keys,
            image = image
        )
}